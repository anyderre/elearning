package com.sorbSoft.CabAcademie.Services.email;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import com.sorbSoft.CabAcademie.Entities.Attendee;
import com.sorbSoft.CabAcademie.Entities.TimeSlot;
import com.sorbSoft.CabAcademie.Entities.User;
import com.sorbSoft.CabAcademie.Services.TimeZoneConverter;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Value("${spring.mail.from}")
    private String MAIL_FROM;

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

    @Value("${email.confirmation.url}")
    private String emailConfirmationEndpoint;

    @Value("${email.date.time.format}")
    private String emailDateTimeFormat;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${reset.password.url}")
    private String resetPasswordUrl;

    private SimpleDateFormat F;

    @Autowired
    private TimeZoneConverter tzConverter;

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
        this.emailConfirmationEndpoint = appUrl + this.emailConfirmationEndpoint;
        this.resetPasswordUrl = frontendUrl + this.resetPasswordUrl;

        this.F = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

        log.debug("Simple date format output: "+emailDateTimeFormat);

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


        messageHelper.setFrom(mail.getMailFrom(), MAIL_FROM);
        messageHelper.setTo(mail.getMailTo());
        messageHelper.setSubject(mail.getMailSubject());

        if(mail.getICal() != null)
            messageHelper.addAttachment("appointment.ics", mail.getICal());

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
    @Async
    public void sendAppointmentRequestToTeacherMail(final TimeSlot timeSlot, final Attendee attendee) {

        User teacher = timeSlot.getTeacher();
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

        parameters.put("appointmentType", timeSlot.getType());


        Date dateFrom = new Date();
        Date dateTo = new Date();
        dateFrom.setTime(timeSlot.getDateFrom().getTime());
        dateTo.setTime(timeSlot.getDateTo().getTime());
        String teacherTZ = teacher.getTimeZone();
        tzConverter.convertFromUtcToTimeZoned(teacherTZ, dateFrom, dateTo);

        parameters.put("dateFrom", F.format(dateFrom));
        parameters.put("dateTo", F.format(dateTo));

        parameters.put("utc", teacherTZ);

        parameters.put("approveLink", approveLink);
        parameters.put("declineLink", declineLink);

        sendMail(mail, parameters);
    }

    @Async
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

        Date dateFrom = new Date();
        Date dateTo = new Date();
        dateFrom.setTime(booked.getDateFrom().getTime());
        dateTo.setTime(booked.getDateTo().getTime());
        String studentTZ = student.getTimeZone();
        tzConverter.convertFromUtcToTimeZoned(studentTZ, dateFrom, dateTo);

        parameters.put("dateFrom", F.format(dateFrom));
        parameters.put("dateTo", F.format(dateTo));

        parameters.put("utc", studentTZ);

        parameters.put("declineLink", declineLink);

        sendMail(mail, parameters);
    }

    @Async
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

        Date dateFrom = new Date();
        Date dateTo = new Date();
        dateFrom.setTime(timeSlot.getDateFrom().getTime());
        dateTo.setTime(timeSlot.getDateTo().getTime());
        String teacherTZ = teacher.getTimeZone();
        tzConverter.convertFromUtcToTimeZoned(teacherTZ, dateFrom, dateTo);

        parameters.put("dateFrom", F.format(dateFrom));
        parameters.put("dateTo", F.format(dateTo));
        parameters.put("utc", teacherTZ);

        parameters.put("videoConferenceLink", timeSlot.getVideoConferenceLink());

        parameters.put("declineLink", declineLink);

        iCal ical = makeICalModel4TeacherApprove(student, teacher, timeSlot);

        sendMailWithICal(mail, parameters, ical);
    }

    @Async
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


        Date dateFrom = new Date();
        Date dateTo = new Date();
        dateFrom.setTime(timeSlot.getDateFrom().getTime());
        dateTo.setTime(timeSlot.getDateTo().getTime());
        String studentTZ = student.getTimeZone();
        tzConverter.convertFromUtcToTimeZoned(studentTZ, dateFrom, dateTo);

        parameters.put("dateFrom", F.format(dateFrom));
        parameters.put("dateTo", F.format(dateTo));
        parameters.put("utc", studentTZ);

        parameters.put("videoConferenceLink", timeSlot.getVideoConferenceLink());

        parameters.put("declineLink", declineLink);

        iCal ical = makeICalModel4StudentApprove(student, teacher, timeSlot);

        sendMailWithICal(mail, parameters, ical);

    }

    private void sendMailWithICal(Mail mail, Map<String, Object> parameters, iCal ical) {

        try {
            File file = createICalFile(ical);
            mail.setICal(file);

            sendMail(mail, parameters);

            Files.deleteIfExists(file.toPath());

            log.debug("File has been removed: "+file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private iCal makeICalModel4StudentApprove(User student, User teacher, TimeSlot timeSlot) {

        String teacherName = teacher.getFirstName() +" "+teacher.getLastName();

        iCal ical = new iCal();
        ical.setStart(timeSlot.getDateFrom());
        ical.setEnd(timeSlot.getDateTo());
        ical.setSummary("Meeting with teacher "+teacherName);
        ical.setDescription("Teacher will be waiting for you by following link "+timeSlot.getVideoConferenceLink());
        ical.setUrl(""); //to avoid null in email
        ical.setOrganizer(USER_NAME);
        ical.setAttendee(student.getEmail());

        return ical;
    }

    private iCal makeICalModel4StudentCancel(User student, User teacher, TimeSlot timeSlot) {

        String teacherName = teacher.getFirstName() +" "+teacher.getLastName();

        iCal ical = new iCal();
        ical.setStart(timeSlot.getDateFrom());
        ical.setEnd(timeSlot.getDateTo());
        ical.setSummary("Cancelled: Meeting with teacher "+teacherName);
        ical.setDescription("Teacher will be waiting for you by following link "+timeSlot.getVideoConferenceLink());

        ical.setOrganizer(USER_NAME);
        ical.setAttendee(student.getEmail());

        return ical;
    }

    private iCal makeICalModel4TeacherApprove(User student, User teacher, TimeSlot timeSlot) {

        String studentName = student.getFirstName() +" "+student.getLastName();

        iCal ical = new iCal();
        ical.setStart(timeSlot.getDateFrom());
        ical.setEnd(timeSlot.getDateTo());
        ical.setSummary("Meeting with student "+studentName);
        ical.setDescription("Student will be waiting for you by following link "+timeSlot.getVideoConferenceLink());
        ical.setUrl(""); //to avoid null in email
        ical.setOrganizer(USER_NAME);
        ical.setAttendee(teacher.getEmail());

        return ical;
    }

    private iCal makeICalModel4TeacherCancel(User student, User teacher, TimeSlot timeSlot) {

        String studentName = student.getFirstName() +" "+student.getLastName();

        iCal ical = new iCal();
        ical.setStart(timeSlot.getDateFrom());
        ical.setEnd(timeSlot.getDateTo());
        ical.setSummary("Cancelled: Meeting with student "+studentName);
        ical.setDescription("Student will be waiting for you by following link "+timeSlot.getVideoConferenceLink());

        ical.setOrganizer(USER_NAME);
        ical.setAttendee(teacher.getEmail());

        return ical;
    }

    @Async
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

        Date dateFrom = new Date();
        Date dateTo = new Date();
        dateFrom.setTime(timeSlot.getDateFrom().getTime());
        dateTo.setTime(timeSlot.getDateTo().getTime());
        String teacherTZ = teacher.getTimeZone();
        tzConverter.convertFromUtcToTimeZoned(teacherTZ, dateFrom, dateTo);

        parameters.put("dateFrom", F.format(dateFrom));
        parameters.put("dateTo", F.format(dateTo));
        parameters.put("utc", teacherTZ);

        iCal ical = makeICalModel4TeacherCancel(student, teacher, timeSlot);

        sendMailWithICal(mail, parameters, ical);
    }

    @Async
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


        Date dateFrom = new Date();
        Date dateTo = new Date();
        dateFrom.setTime(timeSlot.getDateFrom().getTime());
        dateTo.setTime(timeSlot.getDateTo().getTime());
        String studentTZ = student.getTimeZone();
        tzConverter.convertFromUtcToTimeZoned(studentTZ, dateFrom, dateTo);


        parameters.put("dateFrom", F.format(dateFrom));
        parameters.put("dateTo", F.format(dateTo));
        parameters.put("utc", studentTZ);

        iCal ical = makeICalModel4StudentCancel(student, teacher, timeSlot);

        sendMailWithICal(mail, parameters, ical);
    }

    private File createICalFile(iCal ical) throws IOException {
        ICalendar iCalendar = new ICalendar();
        VEvent event = new VEvent();

        event.setSummary(ical.getSummary());
        event.setDateStart(ical.getStart());
        event.setDateEnd(ical.getEnd());
        event.setDescription(ical.getDescription() + ical.getUrl());
        event.setUrl(ical.getUrl());
        event.setOrganizer(ical.getOrganizer());
        event.addAttendee(ical.getAttendee());


        iCalendar.addEvent(event);
        iCalendar.setMethod(ical.getMethod());
        //iCalendar.getTimezoneInfo().setDefaultTimezone(utc);

        File file = new File("my.ics");
        Biweekly.write(iCalendar).go(file);

        return file;
    }

    @Async
    public void sendUserRegistrationMail(User user) {
        log.debug("Sending email confirmation link to user: {}", user);

        String emailConfirmationLink = this.emailConfirmationEndpoint.replace("{emailConfirmationUid}", user.getEmailConfirmationUID());

        final Mail mail = new Mail();
        String title;
        mail.setMailFrom(USER_NAME);
        mail.setMailTo(user.getEmail());

        title = "Email Confirmation";
        mail.setMailSubject(title);
        mail.setTemplateName("confirm_registration.vm");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", title);
        parameters.put("firstName", user.getFirstName());
        parameters.put("lastName", user.getLastName());
        parameters.put("emailConfirmationLink", emailConfirmationLink);

        sendMail(mail, parameters);
    }

    @Async
    public void sendResetPasswordEmail(User user) {
        log.debug("Sending reset password link to user: {}", user);

        String passwordResetToken = this.resetPasswordUrl.replace("{code}", user.getPasswordResetToken());

        final Mail mail = new Mail();
        String title;
        mail.setMailFrom(USER_NAME);
        mail.setMailTo(user.getEmail());

        title = "Reset Password Notification";
        mail.setMailSubject(title);
        mail.setTemplateName("reset_password.vm");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", title);
        parameters.put("firstName", user.getFirstName());
        parameters.put("lastName", user.getLastName());
        parameters.put("passwordResetToken", passwordResetToken);

        sendMail(mail, parameters);
    }
}
