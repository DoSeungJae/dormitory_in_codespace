package com.DormitoryBack.domain.member.entity;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.member.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
@Data
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Table(name="user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique = true)
    private String eMail;

    @Column(nullable = false)
    @JsonIgnore
    private String passWord;

    @Column
    private String nickName;

    @Column
    private Long dormId;

    @JsonIgnore
    @OneToMany(mappedBy = "usrId")
    private List<Article> articles;
    //DB에 반영되지 않음.


    //private PasswordEncoder passwordEncoder;

    //public void setPassWord(String passWord){
        //this.passWord=new BCryptPasswordEncoder().encode(passWord);
    //}


    public User(String eMail, String passWord, String nickName){
        this.eMail=eMail;
        this.passWord=passWord;
        this.nickName=nickName;
    }

    public void update(UserDTO dto){
        this.eMail=dto.getEMail();
        this.nickName=dto.getNickName();
        this.passWord=dto.getPassWord();
    }

    @Override
    public String toString(){
        return this.id+" "+this.eMail+" "+this.nickName;
    }

    public String toJsonString(){
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String jsonString="";
        try{
            jsonString=objectMapper.writeValueAsString(this);
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return jsonString;
    }

    /*
    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

     */

}
