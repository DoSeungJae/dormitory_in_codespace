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
    private static final String AUTHORITIES_KEY="auth20220393";

    private final String secret;

    private final long tokenValidityMilliseconds;

    private Key key;


    public TokenProvider(
            //@Value("${jwt.secret}") String secret,
            //@Value("${jwt.token-validity-in-seconds}") long tokenValiditySeconds
    ) {
        this.secret = "mySecretKey20220393VlwEyVBsYt9V7zq57Te";
        this.tokenValidityMilliseconds = 600 * 1000 * 100;  // Convert seconds to milliseconds


        if (this.secret == null || this.secret.isEmpty()) {
            throw new IllegalArgumentException("시크릿 키값이 없습니다.");
        }

        afterPropertiesSet();


    }


    /*
    public TokenProvider(){
        this.secret=null;
        this.tokenValidityMilliseconds=0;
        afterPropertiesSet();

    }

     */

/*
    @Override
    public void afterPropertiesSet(){
        byte[] keyBytes=Decoders.BASE64.decode(secret);
        this.key=Keys.hmacShaKeyFor(keyBytes);
        this.key=Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }


 */

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
    }


        /*
    public String createToken(Authentication authentication1){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now=(new Date()).getTime();
        Date validity=new Date(now+this.tokenValidityMilliseconds);


        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key,SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .compact();

    }

     */



    /*
    public Authentication getAuthentication(String token){
        Claims claims=Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(),"",authorities);
        return new UsernamePasswordAuthenticationToken(principal,"",authorities);

    }

     */




}
