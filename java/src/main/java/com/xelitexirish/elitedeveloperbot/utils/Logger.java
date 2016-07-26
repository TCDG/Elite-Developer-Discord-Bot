package com.xelitexirish.elitedeveloperbot.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Logger {

    public static PrintWriter writer;

    public void initLogger(){
        try {
            writer = new PrintWriter("logger.txt", "UTF-8");
            writer.println("#Start of log file");
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void log(String heading, String message){
        System.out.println("[" + heading.toUpperCase() + "] " + message);
        writer.println("[" + heading.toUpperCase() + "] " + message);
    }

    public static void info(String message){
        log("info", message);
    }

    public static void command(String title, String username){
        log("COMMAND: " + title, "Issued by player: " + username);
    }
}
