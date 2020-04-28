package com.sorbSoft.CabAcademie.Services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Service
public class EmailApiService {

    @Value("${spring.mail.host}")
    private String HOST;

    @Value("${spring.mail.port}")
    private Integer PORT;

    @Value("${spring.mail.username}")
    private String USER_NAME;

    @Value("${spring.mail.password}")
    private String PASSWORD;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String SMTP_AUTH;

    @Value("${spring.mail.transport.protocol}")
    private String PROTOCOL;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String START_TTLS_ENABLE;

    @Value("${spring.mail.debug}")
    private String MAIL_DEBUG;

    @Autowired
    public JavaMailSender emailSender;

    @PostConstruct
    public void init() {
        String to = "w.volodymyr.bondarchuk@gmail.com";
        String subject = "Hello world";
        String text = "Test message...";
        sendSimpleMessage(to, subject, text);
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(HOST);
        mailSender.setPort(PORT);

        mailSender.setUsername(USER_NAME);
        mailSender.setPassword(PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", PROTOCOL);

        props.put("mail.smtp.auth", SMTP_AUTH);
        props.put("mail.smtp.starttls.enable", START_TTLS_ENABLE);
        props.put("mail.debug", MAIL_DEBUG);

        return mailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
        System.out.println("************Email sent***********");

    }
}
