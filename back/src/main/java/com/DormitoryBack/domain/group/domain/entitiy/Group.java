package com.DormitoryBack.domain.group.domain.entitiy;


import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.member.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false,name="dorm_id")
    private Long dormId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="host_id")
    private User host;

    @JsonProperty("hostId")
    public Long getHostId(){return host.getId();}

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="article_id")
    private Article article;
    @JsonProperty("articleId")
    public Long getArticleId(){return article.getId();}

    @CreatedDate
    @Column(nullable = false,name="created_time")
    private LocalDateTime createdTime;

    @Column(name="category")
    private String category;

    @JsonIgnore
    @OneToMany(mappedBy = "group") //이게 무슨 기능?
    private Set<User> members=new HashSet<>();

    public String toJsonString(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}
