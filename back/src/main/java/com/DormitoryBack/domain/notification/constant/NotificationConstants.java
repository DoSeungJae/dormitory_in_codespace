package com.DormitoryBack.domain.notification.constant;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationConstants {
    
    public static final String MEMBER_EXPELLED_KOR="%s님이 그룹에서 추방됐어요."; // %s <- username %s <- messageCode (expelled, userId)
    public static final String MEMBER_LEFT_KOR="%s님이 그룹을 떠났어요."; // %s <- username %s <- messageCode ()
    public static final String GROUP_CLOSED_KOR="참여하신 그룹(%s)이 마감되었어요."; // %s <- room <- 사용자 경험을 위해 %s는 삭제하는게 나아보임
    public static final String GROUP_FINISHED_KOR="참여하신 그룹(%s)이 종료되었어요."; // %s <- room <- 사용자 경험을 위해 %s는 삭제하는게 나아보임
    public static final String NEW_COMMENT="새로운 댓글이 달렸어요: %s";
    public static final String NEW_REPLY="새로운 대댓글이 달렸어요: %s";
    public static final String MEMBER_ENTER_GROUP_KOR="%s님이 그룹에 참여했어요."; //%s <- username %s <-messageCode (in,userId)


    public static String getConstantName(String message) {
        String[] words=message.split("\\s+");
        String lastWord=words[words.length-1];

        if(lastWord.equals("참여했어요.")){
            return "MEMBER_ENTER_GROUP_KOR";
        }
        else if(lastWord.equals("종료되었어요.")){
            return "GROUP_FINISHED_KOR";
        }
        else if(lastWord.equals("마감되었어요.")){
            return "GROUP_CLOSED_KOR";
        }
        else if(lastWord.equals("떠났어요.")){
            return "MEMBER_LEFT_KOR";
        }
        else if(lastWord.equals("추방됐어요.")){
            return "MEMBER_EXPELLED_KOR";
        }
        return null;
            
    }

}
