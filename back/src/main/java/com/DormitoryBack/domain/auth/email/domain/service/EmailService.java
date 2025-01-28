package com.DormitoryBack.domain.auth.email.domain.service;

import org.springframework.stereotype.Service;

import com.DormitoryBack.domain.auth.email.domain.dto.EmailRequestDTO;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    public String sendVerifyMail(EmailRequestDTO dto) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.naver.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication("이메일", "비밀번호");
            }
        });
        String key="03931192";
        String subject="DeliveryBox 인증 코드";
        String msg="";
        String to=dto.getEmail();
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += key;
        msg += "</td></tr></tbody></table></div>";
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("dsj5508@naver.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(msg, "text/html; charset=utf-8");

        Transport.send(message);
        return key;
    }
}
