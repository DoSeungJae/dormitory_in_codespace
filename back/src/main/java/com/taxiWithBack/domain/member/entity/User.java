package com.taxiWithBack.domain.member.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@Builder
@ToString
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique = true)
    private String eMail;
    @Column(nullable = false)
    private String passWord;
    @Column
    private String nickName;

    private PasswordEncoder passwordEncoder;


    public User(String eMail, String passWord, String nickName){
        this.eMail=eMail;
        this.passWord=passWord;
        this.nickName=nickName;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

}
