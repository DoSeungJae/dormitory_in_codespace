package com.DormitoryBack.module.sequence;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "counters")
@Getter
@Setter
public class DatabaseSequence {

    @Id
    private String id;

    private long seq;
    
}
