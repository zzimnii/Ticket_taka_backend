package umc.tickettaka.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import umc.tickettaka.web.dto.request.FCMRequestDto;

public interface FCMService {

    void sendNotification(FCMRequestDto request) throws FirebaseMessagingException;
}
