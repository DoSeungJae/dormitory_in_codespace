package com.DormitoryBack.domain.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.DormitoryBack.domain.member.domain.entity.User;

import java.security.Key;
import java.util.Date;


@Slf4j
public class  TokenProvider implements InitializingBean {

    private static final String USER_ID_KEY = "usrId";

    private final Logger logger=LoggerFactory.getLogger(TokenProvider.class);

    private String secret;

    private Long tokenValidityMilliseconds;

    private Key key;

    public TokenProvider(String secret, Long tokenValidityMilliseconds){
        this.secret=secret;
        this.tokenValidityMilliseconds=tokenValidityMilliseconds;
        if (this.secret == null || this.secret.isEmpty()) {
            throw new IllegalArgumentException("NoSecretKey");
        }
        if (this.tokenValidityMilliseconds==null) {
            throw new IllegalArgumentException("NoSecrettokenValidityMilliseconds");
        }
        afterPropertiesSet();
    }


    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        // Key length check
        logger.info("Key bytes length: {}", keyBytes.length);

        // Check if key length is sufficient, if not, generate a new key
        if (keyBytes.length < 32) {
            this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            logger.info("Generated a new key with sufficient length.");
        } else {
            this.key = Keys.hmacShaKeyFor(keyBytes);
        }
    }

    public String createToken(User user){ //With no Spring Security

        Date now=new Date();
        Date validity=new Date(now.getTime()+this.tokenValidityMilliseconds);

        return Jwts.builder()
                .claim(USER_ID_KEY,user.getId())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getUserIdFromToken(String token){
        Claims claims=Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        return claims.get(USER_ID_KEY,Long.class);
    }
    public boolean validateToken(String token){
        if(token==null || token.isEmpty()){
            throw new JwtException("토큰이 없습니다.");

        }
        try{
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

            return true;

        }catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            logger.info("잘못된 JWT 서명입니다.");
            return false;
        }catch(ExpiredJwtException e){
            logger.info("만료된 JWT 토큰입니다.");
            return false;
        }catch(UnsupportedJwtException e){
            logger.info("지원되지 않는 JWT 토큰입니다");
            return false;
        }catch(IllegalArgumentException e){
            logger.info("JWT 토큰이 잘못되었습니다.");
            return false;
        }

        //사용자 제제 여부 확인
    }

}
