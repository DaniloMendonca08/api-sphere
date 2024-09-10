package br.com.danilo.apisphere.mail;

import br.com.danilo.apisphere.user.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSenderImpl mailSender;

    public MailService(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(User user) {

        var email = new SimpleMailMessage();

        email.setTo(user.getEmail());
        email.setSubject("Boas vindas");
        email.setText("""
                    Olá, %s
                    
                    Ficamos felizes por ter você na nossa rede. Prometo que não seremos banidos do Brasil.
                    
                    Att
                    Nois
                """.formatted(user.getName()));

        mailSender.send(email);
    }
}
