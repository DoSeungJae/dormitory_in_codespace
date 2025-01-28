package com.DormitoryBack.domain.auth.oauth.domain.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.DormitoryBack.domain.auth.oauth.domain.dto.GoogleRequestDTO;
import com.DormitoryBack.domain.auth.oauth.domain.dto.GoogleResponseDTO;
import com.DormitoryBack.domain.auth.oauth.domain.enums.ProviderType;
import com.DormitoryBack.domain.auth.oauth.domain.enums.StateType;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OAuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    private NetHttpTransport transport=new NetHttpTransport();

    private GsonFactory gsonFactory=GsonFactory.getDefaultInstance();

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    public GoogleResponseDTO googleLogin(GoogleRequestDTO requestDTO){
        String email;
        Boolean emailVerified;
        GoogleResponseDTO responseDTO;
        GoogleIdTokenVerifier verifier=new GoogleIdTokenVerifier.Builder(transport, gsonFactory)
            .setAudience(Arrays.asList(clientId))
            .build();
        String accessToken=requestDTO.getToken();

        try{
            GoogleIdToken idToken=verifier.verify(accessToken);
            if(idToken!=null){
                Payload payload=idToken.getPayload();
                email=payload.getEmail();
                emailVerified=payload.getEmailVerified();
                
            }else{
                throw new RuntimeException("GoogleAccessTokenNotValid");
            }
            if(!emailVerified){
                throw new RuntimeException("GoogleEmailNotVerified");
            }
        }catch(GeneralSecurityException generalSecurityException){
            throw new RuntimeException("GeneralSecurityException");
        }catch(IOException ioException){
            throw new RuntimeException("IOException");
        }

        User socialAccount=userService.getSocialAccount(ProviderType.GOOGLE, email); //메서드 테스트 필요 <- 현재는 저장된 소셜 계정이 없어서 안됨, 지금은 return값 항상 null 
        if(socialAccount!=null){
            String token=tokenProvider.createToken(socialAccount);
            responseDTO=GoogleResponseDTO.builder()
                .state(StateType.LOGINED)
                .token(token)
                .build();
        }else{
            responseDTO=GoogleResponseDTO.builder()
                .state(StateType.SIGNUP_NEEDED)
                .email(email)
                .provider(ProviderType.GOOGLE)
                .build();
        }

        //"google provider"에 해당 계정이 있는지 확인
        //계정이 없다면 회원가입 필요 -> 추가 정보 요청을 필요로 하는 헤더(?) 반환
        //계정이 있다면 로그인 처리 -> jwt 토큰 반환
        return responseDTO;
    }


    public String logInWithGoogle(GoogleRequestDTO dto){
        return  "";
    }

    

    
    



}
