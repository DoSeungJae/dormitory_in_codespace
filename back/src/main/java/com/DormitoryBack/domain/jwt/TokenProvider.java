package com.DormitoryBack.domain.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.restriction.domain.service.RestrictionServiceExternal;

import java.security.Key;
import java.util.Date;


@Slf4j
@Service
public class TokenProvider  {

    private static final String USER_ID_KEY = "usrId";

    private final Logger logger=LoggerFactory.getLogger(TokenProvider.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token-validity-in-seconds}")
    private Long tokenValidityMilliseconds;

    private Key key;

    @Autowired
    private RestrictionServiceExternal restrictionService;


    @PostConstruct
    public void init() {
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

        
            Long userId=getUserIdFromToken(token);
            Object restricted=restrictionService.getIsRestricted(userId);
            if(restricted instanceof String){ 
                String message="ForcedLogout";
                throw new RuntimeException(message);
            }
            
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
    }

}
