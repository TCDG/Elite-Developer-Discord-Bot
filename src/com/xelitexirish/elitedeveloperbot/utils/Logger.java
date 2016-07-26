package com.xelitexirish.elitedeveloperbot.utils;

public class Logger {

    public static void log(String heading, String message){
        System.out.println("[" + heading.toUpperCase() + "] " + message);
    }

    public static void info(String message){
        log("info", message);
    }

    public static void command(String title, String username){
        log("COMAND: " + title, "Issued by player: " + username);
    }
}
