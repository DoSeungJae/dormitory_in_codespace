package com.DormitoryBack.domain.notification.dto;

import com.DormitoryBack.domain.notification.enums.EntityType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Notifiable {
    EntityType entityType;
    Long entityId;
    String stringifiedEntity;


}
