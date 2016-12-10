package com.xelitexirish.elitedeveloperbot.handlers;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.utils.Constants;

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

                properties.setProperty("SERVER_ID", "");
                properties.setProperty("BOT_OWNER_ID", "");
                properties.setProperty("STAFF_CHAT_LOG_CHANNEL_ID", "");
                properties.setProperty("STAFF_CHAT_CHANNEL_ID", "");
                properties.setProperty("STAFF_CHAT_NICK_CHANNEL_ID", "");

                properties.setProperty("ROLE_STAFF_ID", "");
                properties.setProperty("ROLE_ADMIN_ID", "");
                properties.setProperty("ROLE_MOD_ID", "");
                properties.setProperty("ROLE_MUTED_ID", "");

                properties.setProperty("DEFAULT_USERS_URL", "https://raw.githubusercontent.com/TCDG/Elite-Developer-Discord-Bot/master/src/main/resources/data/admin_users.json");
                properties.setProperty("DIXORD_WORDS_URL", "https://raw.githubusercontent.com/TCDG/Elite-Developer-Discord-Bot/master/src/main/resources/data/dixord_words.json");

                properties.store(fileWriter, "Discord Bot Settings (I'll make a better config soon)");
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

            Constants.DISCORD_SERVER_ID = properties.getProperty("SERVER_ID");
            Constants.USER_ID_BOT_OWNER = properties.getProperty("BOT_OWNER_ID");
            Constants.STAFF_LOG_CHANNEL_ID = properties.getProperty("STAFF_CHAT_LOG_CHANNEL_ID");
            Constants.STAFF_CHAT_CHANNEL_ID = properties.getProperty("STAFF_CHAT_CHANNEL_ID");
            Constants.STAFF_NICK_CHAT_CHANNEL_ID = properties.getProperty("STAFF_CHAT_NICK_CHANNEL_ID");

            Constants.ROLE_STAFF_ID = properties.getProperty("ROLE_STAFF_ID");
            Constants.ROLE_ADMIN_ID = properties.getProperty("ROLE_ADMIN_ID");
            Constants.ROLE_MOD_ID = properties.getProperty("ROLE_MOD_ID");
            Constants.ROLE_MUTED_ID = properties.getProperty("ROLE_MUTED_ID");

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
