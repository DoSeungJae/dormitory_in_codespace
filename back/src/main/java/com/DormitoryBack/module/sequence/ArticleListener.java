package com.DormitoryBack.module.sequence;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import com.DormitoryBack.domain.article.domain.entity.Article;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class ArticleListener extends AbstractMongoEventListener<Article> {

    private final SequenceGeneratorService generatorService;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Article> event){
        event.getSource().setId(generatorService.generateSequence(Article.SEQUENCE_NAME));
        
    }
    
}
