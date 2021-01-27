package com.jwd.cafe.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationHelper {
    public static String localize(String locale, String content) {
        String[] parsedLocale = locale.split("_");
        ResourceBundle resourceBundle = ResourceBundle.getBundle("content", new Locale(parsedLocale[0], parsedLocale[1]));
        return resourceBundle.getString(content);
    }
}