package com.alioth.server.domain.login.service;

import com.alioth.server.common.response.CommonResponse;
import com.alioth.server.domain.login.dto.req.LoginReqDto;
import com.alioth.server.domain.login.dto.res.LoginResDto;

public interface LoginService {
    LoginResDto memberLogin(LoginReqDto dto);
}
