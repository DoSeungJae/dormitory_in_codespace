package com.DormitoryBack.domain.group.chat.domain.constant;

public class Constants {
    public static final String WELCOME_MESSAGE = "participated:%s"; // %s <- username
    public static final String DISCONNECT_MESSAGE="disconnected:%s"; // %s <- username
    public static final String MEMBER_ENTER_GROUP="participatedInGroup:%s"; //%s <- username
    public static final String MEMBER_ENTER_GROUP_KOR="%s님이 그룹에 참여했어요."; //%s <- username
    public static final String MEMBER_ENTER_CHAT="participatedInChat:%s"; // %s <- username
    public static final String MEMBER_EXPELLED="expelledFromGroup:%s"; // %s <- username
    public static final String MEMBER_EXPELLED_KOR="%S님이 그룹에서 추방됐어요."; // %s <- username
    public static final String MEMBER_LEFT="leftGroup:%s"; // %s <- username
    public static final String MEMBER_LEFT_KOR="%s님이 그룹을 떠났어요."; // %s <- username
    public static final String GROUP_CLOSED="groupClosed:%s"; // %s <- room
    public static final String GROUP_CLOSED_KOR="참여하신 그룹(%s)이 마감되었어요. "; // %s <- room
    public static final String GROUP_FINISHED="groupFinished:%s"; // %s <- room
    public static final String GROUP_FINISHED_KOR="참여하신 그룹(%s)이 종료되었어요."; // %s <- room

}
