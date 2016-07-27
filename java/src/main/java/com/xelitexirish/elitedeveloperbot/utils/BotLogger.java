package com.xelitexirish.elitedeveloperbot.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class BotLogger {

    static Logger logger = Logger.getLogger("logs.txt");
    static FileHandler fileHandler;

    public static void initLogger(){
        try {
            fileHandler = new FileHandler("logs.log", true);
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String heading, String message){
        logger.info("[" + heading.toUpperCase() + "] " + message);
    }

    public static void info(String message){
        log("info", message);
    }

    public static void command(String title, String username){
        log("COMMAND: " + title, "Issued by player: " + username);
    }
}
