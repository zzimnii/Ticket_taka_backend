package umc.tickettaka.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.tickettaka.service.FCMService;
import umc.tickettaka.web.dto.request.FCMRequestDto;

@Service
@RequiredArgsConstructor
public class FCMServiceImpl implements FCMService {
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void sendNotification(FCMRequestDto request) throws FirebaseMessagingException {
        Message message = Message.builder()
            .setToken(request.deviceToken())
            .setNotification(request.toNotification())
            .build();

        firebaseMessaging.send(message);
    }
}
