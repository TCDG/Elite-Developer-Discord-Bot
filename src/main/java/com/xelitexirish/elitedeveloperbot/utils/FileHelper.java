package com.xelitexirish.elitedeveloperbot.utils;

import java.io.File;

public class FileHelper {

    public File getResourceFile(String fileName){

        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }
}
