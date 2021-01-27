package com.jwd.cafe.mail;

import com.jwd.cafe.config.AppConfig;
import com.jwd.cafe.exception.EmailException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ActivationMailSender extends AbstractMailSender {
    private static final String SUBJECT = "EPAM-cafe account verification";
    private static final String TEXT = "Follow the link to verify your account:";
    private static final String VERIFICATION_LINK = "http://%s:%s/cafe?command=verification&verification-code=%s";

    private final String verificationCode;

    public ActivationMailSender(String userEmail, String verificationCode) {
        super(userEmail);
        this.verificationCode = verificationCode;
    }

    @Override
    public void run() {
        AppConfig appConfig = AppConfig.getInstance();
        try {
            send(SUBJECT, TEXT + "\n" + String.format(VERIFICATION_LINK,
                    appConfig.getServerHost(), appConfig.getServerPort(), verificationCode));
        } catch (EmailException e) {
            log.error("Failed to send verification link");
        }
    }
}
