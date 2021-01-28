package com.jwd.cafe.mail;

import com.jwd.cafe.config.MailSenderConfig;
import com.jwd.cafe.exception.EmailException;
import lombok.extern.log4j.Log4j2;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Log4j2
public abstract class AbstractMailSender implements Runnable {
    private final MailSenderConfig mailSenderConfig = MailSenderConfig.getInstance();
    private final String userEmail;

    public AbstractMailSender(String userEmail) {
        this.userEmail = userEmail;
    }

    public void send(String subject, String text) throws EmailException {
        if (userEmail != null) {
            try {
                String username = mailSenderConfig.getUsername();
                String password = mailSenderConfig.getPassword();

                Session session = Session.getDefaultInstance(mailSenderConfig.getProperties(), new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(mailSenderConfig.getUsername()));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
                message.setSubject(subject);
                message.setText(text);
                Transport transport = session.getTransport();
                transport.connect(username, password);
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
                log.debug("Email was successfully sent");
            } catch (MessagingException e) {
                log.error("Could not sent email", e);
            }
        }
    }
}