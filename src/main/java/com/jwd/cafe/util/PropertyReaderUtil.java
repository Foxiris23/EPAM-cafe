package com.jwd.cafe.util;

import com.jwd.cafe.exception.ApplicationStartException;
import com.jwd.cafe.exception.DaoException;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * The class is used to read properties files
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public class PropertyReaderUtil {

    /**
     * @param filename file directory without type
     * @throws ApplicationStartException if properties file was not found
     */
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