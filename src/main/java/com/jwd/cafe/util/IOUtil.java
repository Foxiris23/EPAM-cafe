package com.jwd.cafe.util;

import lombok.extern.log4j.Log4j2;

import java.io.File;

@Log4j2
public class IOUtil {
    private static final String UPLOAD_DIR = "D:\\Epam projects\\cafe\\target\\cafe\\uploads\\";

    public static void deleteUpload(String filename) {
        File file = new File(UPLOAD_DIR + filename);
        if (!file.delete()) {
            log.error("Failed to delete upload with filename: " + filename);
        }
    }
}
