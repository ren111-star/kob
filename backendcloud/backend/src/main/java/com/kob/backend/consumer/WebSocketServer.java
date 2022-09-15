package com.kob.backend.consumer;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    private Session session = null;  // 每个链接使用session维护

    public static UserMapper userMapper;  // 特殊的注入

    private static BotMapper botMapper;

    public static RecordMapper recordMapper;
    public static RestTemplate restTemplate;

    private final static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";
    private Game game = null;

    public Game getGame() {
        return game;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRecordMapper (RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired  // 作用：发送请求
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }

    @Autowired
    public void setBotMapper (BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }

    private User user;  // 用来描述创建的session是为了谁

    // 需要根据用户的ID找到他所对应的链接是谁，才可以根据这个链接向前端发送请求
    // 需要一个对于所有的websocket可见的变量来存储每个ID对应的链接
    public final static ConcurrentHashMap<Integer, WebSocketServer> users
            = new ConcurrentHashMap<>();  // 这是一个线程安全的哈希表

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);

        if (this.user != null) {
            users.put(userId, this);
        } else {
            this.session.close();
        }
        System.out.println(users);

    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected!");
        if (this.user != null) {
            users.remove(this.user.getId());
        }
    }

    public static void startGame (Integer aId, Integer aBotId, Integer bId, Integer bBotId) {  // api
        User a = userMapper.selectById(aId);
        Bot botA = botMapper.selectById(aBotId), botB = botMapper.selectById(bBotId);
        User b = userMapper.selectById(bId);
        Game game = new Game(
                20,
                21,
                40,
                a.getId(),
                botA,
                b.getId(),
                botB
        );
        game.createGameMap();
        if (users.get(a.getId()) != null)
            users.get(a.getId()).game = game;
        if (users.get(b.getId()) != null)
            users.get(b.getId()).game = game;
        game.start();

        JSONObject respGame = new JSONObject();
        respGame.put("map", game.getG());
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());

        JSONObject respA = new JSONObject();
        respA.put("event", "start-matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);
        if (users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString());

        JSONObject respB = new JSONObject();
        respB.put("event", "start-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);
        if (users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toJSONString());
    }

    private void startMatching(Integer botId) {
        System.out.println("start matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        data.add("bot_id", botId.toString());
        restTemplate.postForObject(addPlayerUrl, data, String.class);
    }

    private void stopMatching() {
        System.out.println("stop matching");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl, data, String.class);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message!");
        JSONObject data = JSONObject.parseObject(message);  // 将接收到的字符串解析成JSON
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            startMatching(data.getInteger("bot_id"));
        }else if ("stop-matching".equals(event)) {
            stopMatching();
        } else if ("move".equals(event)) {
            move(data.getInteger("direction"));
        }

    }

    private void move (int direction) {
        if (game.getPlayerA().getId().equals(user.getId())) {
            if (game.getPlayerA().getBotId().equals(-1))
                game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(user.getId())) {
            if (game.getPlayerB().getBotId().equals(-1))
                game.setNextStepB(direction);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        // 由后端向前端发送信息
        synchronized (this.session) {  // 异步发送，使用排他锁
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}