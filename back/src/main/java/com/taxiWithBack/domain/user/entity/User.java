package com.taxiWithBack.domain.user.entity;

        import jakarta.persistence.*;
        import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Table(name = "app_user")
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

    public User(String eMail, String passWord, String nickName){
        this.eMail=eMail;
        this.passWord=passWord;
        this.nickName=nickName;
    }

}
