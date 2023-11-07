package com.taxiWithBack.domain.article.entity;
import com.taxiWithBack.domain.article.dto.ArticleDTO;
import com.taxiWithBack.domain.member.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Table(name="article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable=false,name="dor_id")
    private Long dorId ;

    @ManyToOne
    @JoinColumn(name="usr_id") //테스트 이후 외래키 설정 필요. (외래키 설정 이전에 jwt 인증)
    //@Column(name="usr_id")
    private User usrId;
    @Column(nullable=false,name="title")
    private String title;
    @Column(nullable=false,name="content")
    private String content;

    @CreatedDate
    @Column(nullable=false,name="create_time")
    private LocalDateTime createTime;
    @Column(name="category")
    private String category;
    @Column(name="appointed_time") //추후에 설정.
    private LocalDateTime appointedTime;

    public void update(ArticleDTO dto){
        this.dorId=dto.getDorId();
        this.title=dto.getTitle();
        this.content=dto.getContent();
        this.category=dto.getCategory();
        this.createTime=dto.getCreateTime();
        //this.appointedTime=dto.getAppointedTime();
    }

    @Override
    public String toString(){
        return "article id: "+this.id+", dormitory id: "+this.dorId+", user(writer) id: "+this.usrId.getId()+", title: "+title+", content: "+content+", createTime: "+createTime+
                ", category: "+category+", appointed time: "+appointedTime;

    }

}
