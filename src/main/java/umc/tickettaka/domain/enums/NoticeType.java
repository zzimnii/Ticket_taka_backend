package umc.tickettaka.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NoticeType {
    INVITATION("초대장 도착 알림","새로운 팀으로부터 초대장을 받았어요!"),
    NEW_TICKET("Ticket 도착 알림","새로운 Ticket이 도착했어요!"),
    FEEDBACK_REQUEST("피드백 도착 알림","피드백 요청이 도착했어요!"),
    FEEDBACK_ACCEPT("피드백 수락 알림","피드백 요청이 수락됐어요!"),
    FEEDBACK_REJECT("피드백 거절 알림","피드백 요청이 거절됐어요!");

    private final String title;
    private final String body;
}
