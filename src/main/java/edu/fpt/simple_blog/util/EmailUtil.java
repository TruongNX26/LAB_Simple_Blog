package edu.fpt.simple_blog.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    private static final Session SESSION;
    private static final Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(EmailUtil.class.getResourceAsStream("/email.properties"));
        } catch (IOException e) { logger.error("Loading email properties failed", e); }

        SESSION = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String sender = properties.getProperty("mail.sender");
                String password = properties.getProperty("mail.password");
                return new PasswordAuthentication(sender, password);
            }
        });
    }

    public static void sendMail(String recipient, String content) throws MessagingException {
        Message message = prepareMessage(recipient, content);
        Transport.send(message);
    }

    private static Message prepareMessage(String recipient, String content) throws MessagingException {
        Message message = new MimeMessage(SESSION);
        message.setFrom(new InternetAddress(properties.getProperty("mail.sender")));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject("Confirm hotel booking action");
        message.setText(content);
        return message;
    }
}
