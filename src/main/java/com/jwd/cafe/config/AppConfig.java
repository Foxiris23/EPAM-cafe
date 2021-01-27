package com.jwd.cafe.config;

import com.jwd.cafe.util.PropertyReaderUtil;
import lombok.Getter;

import java.util.Properties;

@Getter
public class AppConfig {
    private static volatile AppConfig instance;

    public static AppConfig getInstance() {
        AppConfig localInstance = instance;
        if (localInstance == null) {
            synchronized (AppConfig.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new AppConfig();
                }
            }
        }
        return localInstance;
    }

    private Integer pointsPerDollar;
    private Integer minusToBlock;
    private String serverHost;
    private String serverPort;

    private AppConfig() {
        init();
    }

    private void init() {
        Properties properties = PropertyReaderUtil.readProperties("application");
        pointsPerDollar = Integer.parseInt(properties.getProperty("app.pointsPerDollar"));
        minusToBlock = Integer.parseInt(properties.getProperty("app.minusToBlock"));
        serverHost = properties.getProperty("server.host");
        serverPort = properties.getProperty("server.port");
    }


}
