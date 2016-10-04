package com.xelitexirish.elitedeveloperbot.handlers;

import com.xelitexirish.elitedeveloperbot.Main;

import java.io.*;
import java.util.Properties;

public class ConfigHandler {

    static File configFile = new File("config.properties");

    public static void init(){
        if(!configFile.exists()) {
            // First time setup
            System.out.println("First time setup..creating config file. Then shutting down.");

            try {
                configFile.createNewFile();
                Properties properties = new Properties();
                FileWriter fileWriter = new FileWriter(configFile);

                // Properties
                properties.setProperty("DISCORD_TOKEN", "");
                properties.setProperty("AUTO_CHAT_MESSAGES", "");
                properties.setProperty("ENABLE_SPELL_CHECKER", "");
                properties.setProperty("ENABLE_USERNAME_CHECKER", "");

                properties.setProperty("CONSUMER_KEY", "");
                properties.setProperty("CONSUMER_SECRET", "");
                properties.setProperty("ACCESS_TOKEN", "");
                properties.setProperty("ACCESS_TOKEN_SECRET", "");

                properties.store(fileWriter, "Discord Bot Settings");
                fileWriter.close();
                System.exit(1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileReader reader = new FileReader(configFile);
            Properties properties = new Properties();
            properties.load(reader);

            // Read values
            Main.DISCORD_TOKEN = properties.getProperty("DISCORD_TOKEN");
            Main.enableAutoMessages = Boolean.parseBoolean(properties.getProperty("AUTO_CHAT_MESSAGES"));
            Main.enableSpellChecker = Boolean.parseBoolean(properties.getProperty("ENABLE_SPELL_CHECKER"));
            Main.enableUsernameChecker = Boolean.parseBoolean(properties.getProperty("ENABLE_USERNAME_CHECKER"));
            Main.CONSUMER_KEY = properties.getProperty("CONSUMER_KEY");
            Main.CONSUMER_SECRET = properties.getProperty("CONSUMER_SECRET");
            Main.ACCESS_TOKEN = properties.getProperty("ACCESS_TOKEN");
            Main.ACCESS_TOKEN_SECRET = properties.getProperty("ACCESS_TOKEN_SECRET");

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
