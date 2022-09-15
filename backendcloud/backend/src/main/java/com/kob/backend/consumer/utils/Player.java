package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer botId;  // -1 表示亲自出马，否则表示AI打
    private String botCode;
    private Integer sx;
    private Integer sy;
    private List<Integer> steps;  // steps for each snake

    private boolean check_tail_increasing(int steps) {  // 检验当前回合，蛇的长度是否会增加
        if (steps <= 5) return true;
        return steps % 3 == 1;
    }

    public List<Cell> getCells () {  // 创建蛇的身体
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for (int d : steps) {
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            step ++;
            if (!check_tail_increasing(++ step)) {
                res.remove(0);
            }
        }
        return res;
    }

    public String getStepsString () {
        StringBuilder res = new StringBuilder();
        for (int d : steps) {
            res.append(d);
        }
        return res.toString();
    }
}
