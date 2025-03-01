package com.DormitoryBack.domain.auth.email.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.DormitoryBack.domain.auth.email.domain.dto.EmailRequestDTO;
import com.DormitoryBack.domain.auth.email.domain.dto.EmailResponseDTO;
import com.DormitoryBack.domain.auth.email.domain.enums.CodeStateType;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionResponseDTO;
import com.DormitoryBack.module.crypt.PIEncryptor;

import lombok.extern.slf4j.Slf4j;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class EmailService {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.email}")
    private String email;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttls;

    @Value("${spring.mail.duration}")
    private long codeDuration;

    @Autowired
    private PIEncryptor encryptor;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public CompletableFuture<EmailResponseDTO> sendVerifyMail(EmailRequestDTO request) {
        EmailResponseDTO response = EmailResponseDTO.builder()
            .stateType(CodeStateType.SENDING)
            .build();

        CompletableFuture.runAsync(() -> {
            try {
                String to = request.getEmail();
                if(to==null){
                    throw new RuntimeException("NoEmail");
                }
                Properties props = new Properties();
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", port);
                props.put("mail.smtp.auth", auth);
                props.put("mail.smtp.starttls.enable", starttls);

                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(email, password);
                    }
                });

                String key = createKey();
                String subject = "DeliveryBox 인증 코드";
                String msg = "";


                msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
                msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
                msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
                msg += key;
                msg += "</td></tr></tbody></table></div>";
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(email));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject(subject);
                message.setContent(msg, "text/html; charset=utf-8");

                Transport.send(message);

                restoreExpirableKeyAfterSend(to,key);


            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }, executorService);

        return CompletableFuture.completedFuture(response);
    }

    public void sendRestrictionConfirmMail(String to, RestrictionResponseDTO restriction){
        CompletableFuture.runAsync(()->{
            try{
                if(to==null){
                    throw new RuntimeException("NoEmail");
                }
                if(restriction==null){
                    throw new RuntimeException("NoRestriction");
                }
                Properties props = new Properties();
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", port);
                props.put("mail.smtp.auth", auth);
                props.put("mail.smtp.starttls.enable", starttls);

                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(email, password);
                    }
                });

                String subject = "DeliveryBox 계정 이용 제한 안내";
                String msg = "";

                msg+="<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">사용자님의 계정이 아래와 같은 사유로 "+restriction.getExpireTime()+"까지 로그인이 제한되었습니다.</p>";
                msg+="";
                msg+="<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">사유 : "+restriction.getReason()+"</p>";
                msg+="<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">문의 사항은 "+email+" 로 남겨주세요.</p>";

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(email));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                message.setSubject(subject);
                message.setContent(msg, "text/html; charset=utf-8");

                Transport.send(message);

            }catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        },executorService);
        
        return ;
    }

    private void restoreExpirableKeyAfterSend(String key, String value) {
        ValueOperations<String, String> valueOperations=redisTemplate.opsForValue();
        Duration expireDuration=Duration.ofSeconds(codeDuration);
        String encryptedKey;
        try{
            encryptedKey=encryptor.hashify(key); //이메일 hashify
        }catch(NoSuchAlgorithmException e){
            return ;
        }
        String encryptedValue=encryptor.encrypt_AES256(value); //인증 코드 aes256으로 암호화
        valueOperations.set(encryptedKey, encryptedValue, expireDuration);;
    }

    public EmailResponseDTO authenticateCode(EmailRequestDTO request){
        String email=request.getEmail();
        String code=request.getCode();
        Boolean isCorrect=false;
        if(email==null){
            throw new RuntimeException("NoEmail");
        }
        if(code==null){
            throw new RuntimeException("NoVerifyCode");
        }
        String encryptedKey;
        try{
            encryptedKey=encryptor.hashify(email); //이메일 hashify
        }catch(NoSuchAlgorithmException e){
            return null;
        }
        ValueOperations<String, String> valueOperations=redisTemplate.opsForValue();
        String encryptedValue=valueOperations.get(encryptedKey);
        String originValue=encryptor.decrypt_AES256(encryptedValue);
        if(originValue.equals(code)){
            isCorrect=true;
            valueOperations.getAndDelete(encryptedKey);
        }

        EmailResponseDTO response=EmailResponseDTO.builder()
            .stateType(isCorrect ? CodeStateType.MATCH : CodeStateType.MISMATCH)
            .build();
                    
        return response;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int r = random.nextInt(10);
            key.append(r);
        }

        return key.toString();
    }
}
