package com.DormitoryBack.domain.member.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity(name="encrypted_email_map")
@AllArgsConstructor
@NoArgsConstructor
public class EncryptedEmailMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "email_hash") 
    private String emailHash;

    @Column(name = "email_aes256")
    private String emailAES256;
       
}
