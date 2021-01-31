package com.jwd.cafe.config;

import com.jwd.cafe.util.PropertyReaderUtil;

import java.util.Properties;


/**
 * The class encapsulates in itself all the data coming from the mail properties file
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public class MailSenderConfig {
    private static final String USERNAME_PROP = "mail.username";
    private static final String PASSWORD_PROP = "mail.password";

    private static volatile MailSenderConfig instance;

    private Properties properties;
    private String username;
    private String password;

    public static MailSenderConfig getInstance() {
        MailSenderConfig localInstance = instance;
        if (localInstance == null) {
            synchronized (MailSenderConfig.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MailSenderConfig();
                }
            }
        }
        return localInstance;
    }

    private MailSenderConfig() {
        init();
    }

    private void init() {
        properties = PropertyReaderUtil.readProperties("mail");
        username = properties.getProperty(USERNAME_PROP);
        password = properties.getProperty(PASSWORD_PROP);
    }

    public Properties getProperties() {
        return properties;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
