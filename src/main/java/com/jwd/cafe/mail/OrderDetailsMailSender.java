package com.jwd.cafe.mail;

import com.jwd.cafe.config.MailSenderConfig;
import com.jwd.cafe.exception.EmailException;
import lombok.extern.log4j.Log4j2;

/**
 * Sends an email with the {@link com.jwd.cafe.domain.Order}'s review code to the given address
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
@Log4j2
public class OrderDetailsMailSender extends AbstractMailSender {
    private final MailSenderConfig mailSenderConfig = MailSenderConfig.getInstance();
    private static final String SUBJECT = "EPAM-cafe order details";
    private static final String TEXT = "Thank you for your order, your review code:";

    private final String reviewCode;

    public OrderDetailsMailSender(String userEmail, String reviewCode) {
        super(userEmail);
        this.reviewCode = reviewCode;
    }

    @Override
    public void run() {
        try {
            send(SUBJECT, TEXT + "\n" + reviewCode);
        } catch (EmailException e) {
            log.error("Failed to send order details");
        }
    }
}
