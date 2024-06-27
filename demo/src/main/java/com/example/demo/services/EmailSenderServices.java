package com.example.demo.services;

import com.example.demo.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@AllArgsConstructor
public class EmailSenderServices{
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private final UserRepository userRepository;
    public void sendRegisterEmail(String to, String subject, String url) throws MessagingException, UnsupportedEncodingException {

        String content = "<h1>UBER<h1>,<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Uber_Clone.";
        content = content.replace("[[URL]]", url);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message) ;
        helper.setFrom("phuongtrinhhoaian@gmail.com", "Phương Trịnh Coder");
        helper.setTo(to);
        helper.setSubject(subject);
        message.setContent(content, "text/html; charset=utf-8");
        mailSender.send(message);

    }

    public void sendFogetPassEmail(String to, String subject, String url) throws MessagingException, UnsupportedEncodingException {

        String content = "<h1>UBER<h1>,<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Uber_Clone.";
        content = content.replace("[[URL]]", url);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message) ;
        helper.setFrom("phuongtrinhhoaian@gmail.com", "Phương Trịnh Coder");
        helper.setTo(to);
        helper.setSubject(subject);
        message.setContent(content, "text/html; charset=utf-8");
        mailSender.send(message);

    }



}
