package com.xelitexirish.elitedeveloperbot.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class BotLogger {

    static Logger logger = Logger.getLogger("logs.txt");
    static Logger messageLogger = Logger.getLogger("messageLogger.txt");

    static FileHandler fileHandler;
    static FileHandler fileHandlerMessage;

    public static void initLogger(){
        try {
            fileHandler = new FileHandler("logs.log", true);
            fileHandlerMessage = new FileHandler("messageLogger.log", true);

            logger.addHandler(fileHandler);
            messageLogger.addHandler(fileHandlerMessage);

            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            fileHandlerMessage.setFormatter(formatter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String heading, String message){
        logger.info("[" + heading.toUpperCase() + "]: " + message);
    }

    public static void command(String title, String username){
        messageLog("command: " + title, "Issued by player: " + username);
    }

    public static void messageLog(String heading, String message){
        messageLogger.info("[" + heading.toUpperCase() + "]: " + message);
    }

    private static void log(String LogMessage) {
        System.out.println(LogMessage);
    }

    public static void info(String log) {
        log("[Elite Developer Bot] " + log);
        log("info", log);
    }

    public static void error(String log) {
        log("[Elite Developer Bot: ERROR] " + log);
    }

    public static void debug(String log) {
        log("[Elite Developer Bot: DEBUG] " + log);
    }

    public static void debug(String log, Exception exception) {
        debug(log);
        exception.printStackTrace();
    }
}
