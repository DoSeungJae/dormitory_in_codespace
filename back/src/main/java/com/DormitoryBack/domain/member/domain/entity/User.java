package com.DormitoryBack.domain.member.domain.entity;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.member.domain.dto.UserRequestDTO;
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

    @Column
    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    @Column
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @JsonIgnore
    @OneToMany(mappedBy = "usrId")
    private List<Article> articles;
    //DB에 반영되지 않음.

    public void update(UserRequestDTO dto){
        if(dto.getMail()!=null){this.eMail=dto.getMail();}  //사실상 필요 없는 코드 
        if(dto.getPassWord()!=null){this.passWord=dto.getPassWord();} //사실상 필요 없는 코드  
        if(dto.getNickName()!=null){this.nickName=dto.getNickName();} 
        if(dto.getDormId()!=null){this.dormId=dto.getDormId();}
    }

    //사실상 필요 없음
    public User update2(String nickname){
        this.nickName=nickname;
        //this.picture=picture;
        return this;
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

}
