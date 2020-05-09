package com.sorbSoft.CabAcademie.Services.email;

import com.sorbSoft.CabAcademie.Entities.Attendee;
import com.sorbSoft.CabAcademie.Entities.TimeSlot;
import com.sorbSoft.CabAcademie.Entities.User;
import lombok.extern.log4j.Log4j2;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
@Log4j2
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

    @Value("${app.url}")
    private String appUrl;

    @Value("${approve.api.url}")
    private String approveEndpoint;

    @Value("${decline.api.url}")
    private String declineEndpoint;


    @Autowired
    public JavaMailSender emailSender;

    private Map<String, Template> templates = new HashMap<>();

    private VelocityEngine velocityEngine;

    public EmailApiService() {

        velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();
    }

    @PostConstruct
    public void init() {
        this.approveEndpoint = appUrl + this.approveEndpoint;
        this.declineEndpoint = appUrl + this.declineEndpoint;

        /*String to = "w.volodymyr.bondarchuk@gmail.com";
        String subject = "Hello world";
        String text = "Test message...";
        sendSimpleMessage(to, subject, text);*/
    }


    public void sendMail(final Mail mail, final Map<String, Object> contentParametersMap) {
        try {
            MimeMessage message = createMessage(mail, contentParametersMap);
            emailSender.send(message);
        } catch (Exception e) {
            log.error("Error during mail sending: " + e.getMessage(), e);
        }
    }

    private MimeMessage createMessage(Mail mail, Map<String, Object> contentParametersMap) throws MessagingException, UnsupportedEncodingException {
        contentParametersMap.put("appUrl", appUrl);
        //String unsubscribeLink = emailHelper.getUnsubscribeLink(mail.getMailTo());
        //contentParametersMap.put("unsubscribeLink", unsubscribeLink);

        log.debug("Sending mail: {}; parameters: {}", mail, contentParametersMap);

        MimeMessage message = emailSender.createMimeMessage();
        //message.addHeader("List-Unsubscribe", "<" + unsubscribeLink + ">");

        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");


        messageHelper.setFrom(mail.getMailFrom(), "CabAcademie");
        messageHelper.setTo(mail.getMailTo());
        messageHelper.setSubject(mail.getMailSubject());

        String name = mail.getTemplateName();

        Template template = templates.get(name);

        if (template == null) {
            template = velocityEngine.getTemplate("email/" + name, "UTF-8");
            templates.put(name, template);
        }

        VelocityContext velocityContext = new VelocityContext(contentParametersMap);

        StringWriter stringWriter = new StringWriter(6000);//approx size for email is 5500 bytes.

        template.merge(velocityContext, stringWriter);

        messageHelper.setText(stringWriter.toString(), true);
        return message;
    }

    public void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
        System.out.println("************Email sent***********");

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


    //from, to, approveUid, declineUid, appointment type, date from, date to
    public void sendAppointmentRequestToTeacherMail(final TimeSlot booked, final Attendee attendee) {

        User teacher = booked.getTeacher();
        User student = attendee.getUser();

        log.debug("Sending appointment request email to teacher: {}", teacher);

        String approveLink = this.approveEndpoint.replace("{approveUid}", attendee.getApprovalUid());
        String declineLink = this.declineEndpoint.replace("{declineUid}", attendee.getDeclineUid());

        final Mail mail = new Mail();
        String title;
        mail.setMailFrom(USER_NAME);
        mail.setMailTo(teacher.getEmail());

        title = "Appointment request";
        mail.setMailSubject(title);
        mail.setTemplateName("appointment_request_notification_teacher.vm");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", title);
        parameters.put("firstName", teacher.getFirstName());
        parameters.put("lastName", teacher.getLastName());

        parameters.put("requesterFName", student.getFirstName());
        parameters.put("requesterLName", student.getLastName());

        parameters.put("appointmentType", booked.getType());
        parameters.put("dateFrom", booked.getDateFrom());
        parameters.put("dateTo", booked.getDateTo());

        parameters.put("approveLink", approveLink);
        parameters.put("declineLink", declineLink);

        sendMail(mail, parameters);
    }

    public void sendAppointmentRequestNotificationToStudentMail(final TimeSlot booked, final Attendee attendee) {
        User teacher = booked.getTeacher();
        User student = attendee.getUser();

        log.debug("Sending appointment request notification email to student: {}", student);

        String declineLink = this.declineEndpoint.replace("{declineUid}", attendee.getDeclineUid());

        final Mail mail = new Mail();
        String title;
        mail.setMailFrom(USER_NAME);
        mail.setMailTo(student.getEmail());

        title = "Appointment request";
        mail.setMailSubject(title);
        mail.setTemplateName("appointment_request_notification_student.vm");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", title);
        parameters.put("firstName", student.getFirstName());
        parameters.put("lastName", student.getLastName());

        parameters.put("teacherFName", teacher.getFirstName());
        parameters.put("teacherLName", teacher.getLastName());

        parameters.put("appointmentType", booked.getType());
        parameters.put("dateFrom", booked.getDateFrom());
        parameters.put("dateTo", booked.getDateTo());

        parameters.put("declineLink", declineLink);

        sendMail(mail, parameters);
    }

    public void sendApproveNotificationToTeacher(final TimeSlot timeSlot, final Attendee attendee) {
        User teacher = timeSlot.getTeacher();
        User student = attendee.getUser();

        log.debug("Sending approved appointment email to teacher: {}", teacher);

        String approveLink = this.approveEndpoint.replace("{approveUid}", attendee.getApprovalUid());
        String declineLink = this.declineEndpoint.replace("{declineUid}", attendee.getDeclineUid());

        final Mail mail = new Mail();
        String title;
        mail.setMailFrom(USER_NAME);
        mail.setMailTo(teacher.getEmail());

        title = "Appointment approved";
        mail.setMailSubject(title);
        mail.setTemplateName("appointment_approve_notification_teacher.vm");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", title);
        parameters.put("firstName", teacher.getFirstName());
        parameters.put("lastName", teacher.getLastName());

        parameters.put("requesterFName", student.getFirstName());
        parameters.put("requesterLName", student.getLastName());

        parameters.put("appointmentType", timeSlot.getType());
        parameters.put("dateFrom", timeSlot.getDateFrom());
        parameters.put("dateTo", timeSlot.getDateTo());

        parameters.put("videoConferenceLink", "...will be added...");

        parameters.put("declineLink", declineLink);

        sendMail(mail, parameters);
    }

    public void sendApproveNotificationToStudent(TimeSlot timeSlot, Attendee attendee) {
        User teacher = timeSlot.getTeacher();
        User student = attendee.getUser();

        log.debug("Sending approved appointment notification email to student: {}", student);

        String declineLink = this.declineEndpoint.replace("{declineUid}", attendee.getDeclineUid());

        final Mail mail = new Mail();
        String title;
        mail.setMailFrom(USER_NAME);
        mail.setMailTo(student.getEmail());

        title = "Appointment approved";
        mail.setMailSubject(title);
        mail.setTemplateName("appointment_approve_notification_student.vm");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", title);
        parameters.put("firstName", student.getFirstName());
        parameters.put("lastName", student.getLastName());

        parameters.put("teacherFName", teacher.getFirstName());
        parameters.put("teacherLName", teacher.getLastName());

        parameters.put("appointmentType", timeSlot.getType());
        parameters.put("dateFrom", timeSlot.getDateFrom());
        parameters.put("dateTo", timeSlot.getDateTo());

        parameters.put("videoConferenceLink", "...will be added...");

        parameters.put("declineLink", declineLink);

        sendMail(mail, parameters);
    }

    public void sendDeclineNotificationToTeacher(TimeSlot timeSlot, Attendee attendee) {
        User teacher = timeSlot.getTeacher();
        User student = attendee.getUser();

        log.debug("Sending decline appointment email to teacher: {}", teacher);

        final Mail mail = new Mail();
        String title;
        mail.setMailFrom(USER_NAME);
        mail.setMailTo(teacher.getEmail());

        title = "Appointment declined/canceled";
        mail.setMailSubject(title);
        mail.setTemplateName("appointment_decline_notification_teacher.vm");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", title);
        parameters.put("firstName", teacher.getFirstName());
        parameters.put("lastName", teacher.getLastName());

        parameters.put("requesterFName", student.getFirstName());
        parameters.put("requesterLName", student.getLastName());

        parameters.put("appointmentType", timeSlot.getType());
        parameters.put("dateFrom", timeSlot.getDateFrom());
        parameters.put("dateTo", timeSlot.getDateTo());

        sendMail(mail, parameters);
    }

    public void sendDeclineNotificationToStudent(TimeSlot timeSlot, Attendee attendee) {
        User teacher = timeSlot.getTeacher();
        User student = attendee.getUser();

        log.debug("Sending decline appointment notification email to student: {}", student);

        final Mail mail = new Mail();
        String title;
        mail.setMailFrom(USER_NAME);
        mail.setMailTo(student.getEmail());

        title = "Appointment declined/canceled";
        mail.setMailSubject(title);
        mail.setTemplateName("appointment_decline_notification_student.vm");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", title);
        parameters.put("firstName", student.getFirstName());
        parameters.put("lastName", student.getLastName());

        parameters.put("teacherFName", teacher.getFirstName());
        parameters.put("teacherLName", teacher.getLastName());

        parameters.put("appointmentType", timeSlot.getType());
        parameters.put("dateFrom", timeSlot.getDateFrom());
        parameters.put("dateTo", timeSlot.getDateTo());

        sendMail(mail, parameters);
    }
}
