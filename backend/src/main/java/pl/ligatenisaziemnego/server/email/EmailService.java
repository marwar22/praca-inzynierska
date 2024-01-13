package pl.ligatenisaziemnego.server.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(String[] to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@rozgrywkitenisa.pl");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            System.err.println(String.join(", ", to));
            System.err.println(subject);
            System.err.println(text);
//            emailSender.send(message);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}