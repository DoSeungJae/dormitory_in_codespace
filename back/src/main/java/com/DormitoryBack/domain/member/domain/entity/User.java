package com.DormitoryBack.domain.member.domain.entity;
import com.DormitoryBack.domain.auth.oauth.domain.enums.ProviderType;
import com.DormitoryBack.domain.member.domain.dto.UserRequestDTO;
import com.DormitoryBack.domain.member.domain.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

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

    @Column(nullable=false, unique = false, name="email")
    private String encryptedEmail;

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
    private String imageName;

    @Column
    @Enumerated(EnumType.STRING)
    private RoleType role;

    public void update(UserRequestDTO dto){
        if(dto.getNickName()!=null){this.nickName=dto.getNickName();} 
        if(dto.getDormId()!=null){this.dormId=dto.getDormId();}
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
