package com.DormitoryBack.domain.notification.constant;

import java.util.HashMap;

public class NotificationConstants {
    public static final String MEMBER_ENTER_GROUP_KOR="%s님이 그룹에 참여했어요.:%s"; //%s <- username %s <-messageCode (in,userId)
    public static final String MEMBER_EXPELLED_KOR="%S님이 그룹에서 추방됐어요."; // %s <- username %s <- messageCode (expelled, userId)
    public static final String MEMBER_LEFT_KOR="%s님이 그룹을 떠났어요."; // %s <- username %s <- messageCode ()
    public static final String GROUP_CLOSED_KOR="참여하신 그룹(%s)이 마감되었어요. "; // %s <- room
    public static final String GROUP_FINISHED_KOR="참여하신 그룹(%s)이 종료되었어요."; // %s <- room
    public static final String NEW_COMMENT="새로운 댓글이 달렸어요: %s";
    public static final String NEW_REPLY="새로운 대댓글이 달렸어요: %s";

    private static final HashMap<String, String> reverseLookupMap = new HashMap<>();

    static {
        // 기본 템플릿 문자열을 맵에 추가
        reverseLookupMap.put(cleanTemplate(MEMBER_ENTER_GROUP_KOR), "MEMBER_ENTER_GROUP_KOR");
        reverseLookupMap.put(cleanTemplate(MEMBER_EXPELLED_KOR), "MEMBER_EXPELLED_KOR");
        reverseLookupMap.put(cleanTemplate(MEMBER_LEFT_KOR), "MEMBER_LEFT_KOR");
        reverseLookupMap.put(cleanTemplate(GROUP_CLOSED_KOR), "GROUP_CLOSED_KOR");
        reverseLookupMap.put(cleanTemplate(GROUP_FINISHED_KOR), "GROUP_FINISHED_KOR");
    }
    private static String cleanTemplate(String template) {
        return template.replaceAll("%s", "");
    }
    public static String getConstantName(String value) {
        // 입력값에서도 포맷팅 플레이스홀더를 제거
        String cleanedValue = cleanTemplate(value);
        return reverseLookupMap.get(cleanedValue);
    }

}
