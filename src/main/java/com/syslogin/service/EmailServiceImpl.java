package com.syslogin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;

    public void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("no-reply@scsp.com", "SCSP suporte");
        helper.setTo(recipientEmail);

        String subject = "Aqui está o link para redefinir sua senha";

        String content = "<p>Olá,</p>"
                + "<p>Você solicitou a redefinição da sua senha.</p>"
                + "<p>Clique no link abaixo para alterar sua senha:</p>"
                + "<p><a href=\"" + link + "\">Alterar minha senha</a></p>"
                + "<br>"
                + "<p>Ignore este e-mail se você se lembra da sua senha, "
                + "ou se você não fez essa solicitação.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

}
