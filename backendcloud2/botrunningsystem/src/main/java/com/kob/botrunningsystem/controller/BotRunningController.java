package com.kob.botrunningsystem.controller;

import com.kob.botrunningsystem.service.BotRunningService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class BotRunningController {
    private final BotRunningService botRunningService;

    @PostMapping("/bot/add/")
    public String addBot (@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        String botCode = data.getFirst("bot_code");
        String input = data.getFirst("input");
        System.out.println(data);

        return botRunningService.addBot(userId, botCode, input);
    }
}
