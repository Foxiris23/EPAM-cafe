package com.jwd.cafe.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The class used to localize content in the server
 *
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public class LocalizationHelper {

    /**
     * @param locale contains locale of current user
     * @param content a name of content property
     * @return localized content
     */
    public static String localize(String locale, String content) {
        String[] parsedLocale = locale.split("_");
        ResourceBundle resourceBundle = ResourceBundle.getBundle("content", new Locale(parsedLocale[0], parsedLocale[1]));
        return resourceBundle.getString(content);
    }
}