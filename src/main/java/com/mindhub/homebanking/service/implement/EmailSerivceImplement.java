package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSerivceImplement implements EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSerivceImplement.class);

    @Autowired
    private JavaMailSender sender;

    @Override
    public boolean sendEmailTool(String textMenssage, String email, String subjetc) {
        boolean send = false;
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(email);
            helper.setText(textMenssage, true);
            helper.setSubject(subjetc);
            sender.send(message);
            send = true;
            LOGGER.info("Mail Enviado!!");
        } catch (MessagingException e) {
            LOGGER.error("Hubo un error al enviar el mail:{}", e);
        }
        return send;
    }
}
