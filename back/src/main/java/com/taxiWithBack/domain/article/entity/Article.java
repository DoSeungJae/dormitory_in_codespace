package com.taxiWithBack.domain.article.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
@Data
@Builder
@ToString
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Table(name="article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="article_id")
    private Long articleId;
    @Column(nullable=false,name="dor_id")
    private Long dorId ;

    @Column(name="user_id") //테스트 이후 외래키 설정 필요. (외래키 설정 이전에 jwt 인증)
    private Long usrId;
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

}
