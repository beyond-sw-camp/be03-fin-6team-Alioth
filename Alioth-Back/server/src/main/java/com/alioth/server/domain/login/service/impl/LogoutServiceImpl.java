package com.alioth.server.domain.login.service.impl;

import com.alioth.server.common.redis.RedisService;
import com.alioth.server.domain.login.service.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final RedisService redisService;


    @Override
    public void logout(Long memberCode) {
        redisService.deleteValues(memberCode + ":RefreshToken");
    }
}
