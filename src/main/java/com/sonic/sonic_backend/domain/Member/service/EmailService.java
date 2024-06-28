package com.sonic.sonic_backend.domain.Member.service;

import com.sonic.sonic_backend.domain.Member.repository.AuthCodeRepository;
import com.sonic.sonic_backend.exception.WrongEmailAddress;
import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.eclipse.angus.mail.smtp.SMTPAddressFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private int authCode;
    @Value("${SMTP_EMAIL}") private String username;
    private final AuthCodeRepository authCodeRepository;

    public void joinEmail(String email) {
        //1. 랜덤코드 생성
        createRandomCode();
        //2. 메일내용 작성
        String mailAddress = email;
        String title = "SONIC 회원가입을 위한 이메일입니다";
        String content =
                "안녕하세요 SONIC 입니다" +
                        "<br><br>" +
                        "인증번호를 입력해 인증을 완료해주세요. "+
                        "인증 번호는 " + authCode + "입니다." +
                        "<br>";
        //3. 메일 발송
        sendMail(mailAddress, title, content);
        //4. redis에 저장
        authCodeRepository.save(authCode, email);
    }

    private void sendMail(String mailAddress, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(username); // service name
            helper.setTo(mailAddress); // customer email
            helper.setSubject(title); // email title
            helper.setText(content, true); // content, html: true
            javaMailSender.send(message);
        } catch (SMTPAddressFailedException e) {
            throw new WrongEmailAddress();
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송에 실패했습니다");
        }
    }

    private void createRandomCode() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }
        authCode = Integer.parseInt(randomNumber);
    }
}
