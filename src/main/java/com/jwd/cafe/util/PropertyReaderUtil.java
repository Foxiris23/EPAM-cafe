package com.jwd.cafe.util;

import com.jwd.cafe.exception.ApplicationStartException;

import java.io.IOException;
import java.util.Properties;

public class PropertyReaderUtil {

    public static Properties readProperties(String filename) {
        Properties properties = new Properties();
        try {
            properties.load(PropertyReaderUtil.class.getClassLoader().getResourceAsStream(filename + ".properties"));
        } catch (IOException e) {
            throw new ApplicationStartException("Failed to load properties file with filename: " + filename);
        }
        return properties;
    }
}