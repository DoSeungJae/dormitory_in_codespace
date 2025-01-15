package com.DormitoryBack.domain.article.domain.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.DormitoryBack.domain.article.comment.domain.service.CommentService;
import com.DormitoryBack.domain.article.domain.dto.ArticleDTO;
import com.DormitoryBack.domain.article.domain.dto.ArticlePreviewDTO;
import com.DormitoryBack.domain.article.domain.dto.NewArticleDTO;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.article.domain.repository.ArticleRepository;
import com.DormitoryBack.domain.group.domain.service.GroupService;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.dto.UserResponseDTO;
import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.repository.UserRepository;
import com.DormitoryBack.domain.member.restriction.domain.enums.Function;
import com.DormitoryBack.domain.member.restriction.domain.service.RestrictionService;
import com.DormitoryBack.module.TimeOptimizer;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CommentService commentService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private RestrictionService restrictionService;

    private final RedisTemplate<String,Long> redisTemplate;

    @Transactional
    public Article saveNewArticle(NewArticleDTO dto, String token) {

        if(!tokenProvider.validateToken(token)){
            throw new JwtException("유효하지 않은 토큰입니다.");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        User user=userRepository.findById(userId).orElse(null);
        checkRestricted(userId);

        Article newArticle = Article.builder()
                .dormId(dto.getDormId())
                .title(dto.getTitle())
                .contentHTML(dto.getContentHTML())
                .category(dto.getCategory())
                .createdTime(TimeOptimizer.now())
                .usrId(user)
                .userId(user.getId())
                .build();

        Article saved = articleRepository.save(newArticle);
        return saved;
    }

    public ArticleDTO getArticle(Long articleId){
        Article article=articleRepository.findById(articleId).orElse(null);
        if(article==null){
            throw new IllegalArgumentException("존재하지 않는 글 번호입니다.");
        }

        User user=userRepository.findById(article.getUserId()).orElse(null);
        UserResponseDTO userDTO=UserResponseDTO.builder()
            .id(user.getId())
            .nickName(user.getNickName())
            .build();

        ArticleDTO articleDTO=ArticleDTO.builder()
            .id(article.getId())
            .title(article.getTitle())
            .contentHTML(article.getContentHTML())
            .dormId(article.getDormId())
            .category(article.getCategory())
            .user(userDTO)
            .createdTime(article.getCreatedTime())
            .build();
        
        return articleDTO;
    }
    
    public Boolean checkArticleExist(Long articleId) {
        Article article=articleRepository.findById(articleId).orElse(null);
        if(article==null){
            return false;
        }
        return true;
    }

    public List<ArticlePreviewDTO> getAllArticlesWithinPage(int page, int size){
        Pageable pageable= PageRequest.of(page,size, Sort.by("createdTime").descending());
        Page<Article> articlePage=articleRepository.findAll(pageable);
        if(articlePage.isEmpty() && page==0){
            throw new RuntimeException("ArticleNotFound");
        }
        else if(articlePage.isEmpty()){
            throw new RuntimeException("NoMoreArticlePage");
        }
        List<ArticlePreviewDTO> articlePreviewList=this.makeArticlePreviewDTOList(articlePage);
        return articlePreviewList;
    }

    public List<ArticlePreviewDTO> getDormArticlesWithinPage(Long dorId,int page,int size){
        Pageable pageable=PageRequest.of(page,size,Sort.by("createdTime").descending());
        Page<Article> articlePage=articleRepository.findAllByDormId(dorId,pageable);
        if(articlePage.isEmpty() && page==0){
            throw new RuntimeException("ArticleNotFound");
        }
        else if(articlePage.isEmpty()){
            throw new RuntimeException("NoMoreArticlePage");
        }
        List<ArticlePreviewDTO> articlePreviewListGroupByDorm=this.makeArticlePreviewDTOList(articlePage);
        return articlePreviewListGroupByDorm;
    }

    public List<ArticlePreviewDTO> getUserArticlesWithinPage(int page, int size, String token) {
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("유효하지 않은 토큰입니다.");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        Pageable pageable=PageRequest.of(page,size,Sort.by("createdTime").descending());
        Page<Article> userArticlePage=articleRepository.findAllByUserId(userId,pageable);
        if(userArticlePage.isEmpty() && page==0){
            throw new RuntimeException("ArticleNotFound");
        }
        else if(userArticlePage.isEmpty()){
            throw new RuntimeException("ExceededPage");
        }
        List<ArticlePreviewDTO> userArticlePreviewList=this.makeArticlePreviewDTOList(userArticlePage);
        return userArticlePreviewList;
    }

    public List<ArticlePreviewDTO> getUserCommentedArticlesWithinPage(int page, int size, String token) {
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("유효하지 않은 토큰입니다.");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        List<Long> idList=commentService.getUserCommentedArticleIds(userId);
        Pageable pageable=PageRequest.of(page,size,Sort.by("createdTime").descending());
        List<Article> articleList=articleRepository.findAllById(idList);

        int start=(int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), articleList.size());
        if(start>end){ //start와 end가 같은 경우?
            throw new RuntimeException("ExceededPage");
        }
        List<Article> pagedArticleList = articleList.subList(start,end);
        if(pagedArticleList.isEmpty() && page==0){
            throw new RuntimeException("ArticleNotFound");
        }
        Page<Article> userCommentedArticlePage=new PageImpl<>(pagedArticleList, pageable, pagedArticleList.size()); 
        List<ArticlePreviewDTO> userCommentedArticlePreviewList=this.makeArticlePreviewDTOList(userCommentedArticlePage);
        return userCommentedArticlePreviewList;
    }


    public String getWriterNickName(Long articleId){
        Article article=articleRepository.findById(articleId).orElse(null);
        if(article==null){
            throw new RuntimeException("ArticleNotFound");
        }
        String nickName=article.getUsrId().getNickName();
        //userService에서 만약 exception이 throw된다면 어떻게 될까?
        return nickName;
    }

    @Transactional
    public Article updateArticle(NewArticleDTO dto,Long articleId,String token){
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("유효하지 않은 토큰입니다.");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        checkRestricted(userId);

        Article article=articleRepository.findById(articleId).orElse(null);
        article.update(dto);
        Article saved=articleRepository.save(article);

        if(article==null){
            throw new IllegalArgumentException("존재하지 않는 글입니다.");
        }
        return saved;
    }
    //필수적이진 않으나 수정 필요 

    @Transactional
    public void deleteArticle(Long articleId,String token){
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("유효하지 않은 토큰입니다.");
        }

        Article target=articleRepository.findById(articleId).orElse(null);
        if(target==null){
            throw new IllegalArgumentException("존재하지 않는 글입니다.");
        }

        SetOperations<String,Long> setOperations=redisTemplate.opsForSet();
        if(setOperations.size(String.valueOf(articleId))>0){
            throw new RuntimeException("ArticleWithAProceedingGroupCannotBeDeleted");
        } 
        articleRepository.delete(target);
    }

    private List<ArticlePreviewDTO> makeArticlePreviewDTOList(Page<Article> articlePage){

        List<ArticlePreviewDTO> articleList= new ArrayList<>(); 
        for (Article article : articlePage.getContent()){
            ArticlePreviewDTO articlePreviewDTO=this.makeArticlePreviewDTO(article);
            articleList.add(articlePreviewDTO);
        }
        return articleList;
    }
    
    private ArticlePreviewDTO makeArticlePreviewDTO(Article article){
        Long articleId=article.getId();
        Long numComments=commentService.getNumberOfComments(articleId);
        Long groupNumMembers=groupService.getNumberOfMembers(articleId);
        Long groupMaxCapacity;
        try{
            groupMaxCapacity=groupService.getMaxCapacity(articleId);
        }catch(RuntimeException e){
            if("GroupNotFound".equals(e.getMessage())){
                groupMaxCapacity=0L;
            }else{
                throw e;
            }
        }

        ArticlePreviewDTO articlePreviewDTO=ArticlePreviewDTO.builder()
            .id(articleId)
            .title(article.getTitle())
            .contentText(article.getContentHTML()) 
            //contentHTML을 contentText로 변환하는 메서드 필요
            //현재는 contentHTML도 모두 text로 이뤄져있기 때문에 그냥 사용
            .userNickName(article.getUsrId().getNickName())
            .dormId(article.getDormId())
            .numComments(numComments)
            .groupNumMembers(groupNumMembers)
            .groupMaxCapacity(groupMaxCapacity)
            .createdTime(article.getCreatedTime())
            .build();

        return articlePreviewDTO;
    }

    public void checkRestricted(Long userId){
        if((Boolean)restrictionService.getIsRestricted(Function.ARTICLE, userId)){
            throw new RuntimeException("ArticleFunctionRestricted");
        }
    }
    
}
