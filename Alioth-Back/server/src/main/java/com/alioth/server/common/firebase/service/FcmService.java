package com.alioth.server.common.firebase.service;

import com.alioth.server.common.firebase.domain.FcmSendDto;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public interface FcmService {
    int sendMessageTo(FcmSendDto fcmSendDto) throws IOException;
}