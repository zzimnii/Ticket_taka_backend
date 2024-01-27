package umc.tickettaka.web.dto.request;

import com.google.firebase.messaging.Notification;
import lombok.Builder;

@Builder
public record FCMRequestDto (String deviceToken, String title, String body) {

    //todo client한테 기기 식별 token 전달받아 Member entity에 저장
    public Notification toNotification() {
        return Notification.builder()
            .setTitle(title)
            .setBody(body)
            .build();
    }
}
