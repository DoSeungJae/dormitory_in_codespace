package com.DormitoryBack.domain.auth.email.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.DormitoryBack.domain.auth.email.domain.dto.EmailRequestDTO;
import com.DormitoryBack.domain.auth.email.domain.dto.EmailResponseDTO;
import com.DormitoryBack.domain.auth.email.domain.enums.CodeStateType;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
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

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public CompletableFuture<EmailResponseDTO> sendVerifyMail(EmailRequestDTO request) {
        EmailResponseDTO response = EmailResponseDTO.builder()
            .stateType(CodeStateType.SENDING)
            .build();

        CompletableFuture.runAsync(() -> {
            try {
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
                String to = request.getEmail();

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

                // Transport.send()가 끝난 후 실행할 작업
                postSendAction();


            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }, executorService);

        return CompletableFuture.completedFuture(response);
    }

    private void postSendAction() {
        // Transport.send()가 끝난 후 실행할 작업을 여기에 작성합니다.
        System.out.println("Email sent successfully!");
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
