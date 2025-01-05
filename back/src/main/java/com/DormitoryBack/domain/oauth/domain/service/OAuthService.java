package com.DormitoryBack.domain.oauth.domain.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.DormitoryBack.domain.oauth.domain.dto.GoogleRequestDTO;
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

    public String signUpWithGoogle(GoogleRequestDTO dto){
        String email;
        Boolean emailVerified;
        GoogleIdTokenVerifier verifier=new GoogleIdTokenVerifier.Builder(transport, gsonFactory)
            .setAudience(Arrays.asList(clientId))
            .build();
        String accessToken=dto.getToken();

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

        return email;

    }


    public String logInWithGoogle(GoogleRequestDTO dto){
        return  "";
    }

    

    
    



}
