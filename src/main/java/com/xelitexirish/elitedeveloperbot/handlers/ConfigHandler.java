package com.xelitexirish.elitedeveloperbot.handlers;

import com.xelitexirish.elitedeveloperbot.Main;
import org.json.JSONArray;
import org.json.JSONObject;

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

                String[] configOptions = {"DISCORD_TOKEN", "AUTO_CHAT_MESSAGES", "ENABLE_SPELL_CHECKER", "ENABLE_USERNAME_CHECKER", "CONSUMER_KEY", "CONSUMER_SECRET", "ACCESS_TOKEN",
                        "ACCESS_TOKEN_SECRET", "SERVER_ID", ""};


                properties.setProperty("", "");
                properties.setProperty("", "");
                properties.setProperty("", "");
                properties.setProperty("", "");

                properties.setProperty("", "");
                properties.setProperty("BOT_OWNER_ID", "");
                properties.setProperty("STAFF_CHAT_LOG_CHANNEL_ID", "");
                properties.setProperty("STAFF_CHAT_CHANNEL_ID", "");

                properties.setProperty("ROLE_STAFF_ID", "");
                properties.setProperty("ROLE_ADMIN_ID", "");
                properties.setProperty("ROLE_MOD_ID", "");
                properties.setProperty("ROLE_MUTED_ID", "");

                properties.setProperty("DEFAULT_USERS_URL", "https://raw.githubusercontent.com/TCDG/Elite-Developer-Discord-Bot/master/src/main/resources/data/admin_users.json");
                properties.setProperty("DIXORD_WORDS_URL", "https://raw.githubusercontent.com/TCDG/Elite-Developer-Discord-Bot/master/src/main/resources/data/dixord_words.json");

                JSONObject jsonObject = new JSONObject();
                JSONArray arrayConfigOptions = new JSONArray();
                jsonObject.put("arrayConfigOptions", arrayConfigOptions);
                for (int x = 0; x < blackListUsers.size(); x++) {
                    String line = blackListUsers.get(x);

                    arrayConfigOptions.put(line);
                }

                FileWriter fileWriter = new FileWriter(configFile);
                fileWriter.write(jsonObject.toString());
                fileWriter.flush();
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
