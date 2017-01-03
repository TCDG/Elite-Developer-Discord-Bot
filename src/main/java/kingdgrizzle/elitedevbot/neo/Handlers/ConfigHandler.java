/**
 * This class was created by <KingDGrizzle>. It's distributed as
 * part of the Elite-Dev-Bot-Neo Project. Get the Source Code on GitHub:
 * https://github.com/TCDG and search for the Elite-Dev-Bot-Neo project
 * <p>
 * Copyright (c) 2016 The Collective Developer Group. All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that this copyright block is included!
 * <p>
 * File Created @ [ 26.12.2016, 10:35 (GMT +02) ]
 */
package kingdgrizzle.elitedevbot.neo.Handlers;

import kingdgrizzle.elitedevbot.neo.Main;
import kingdgrizzle.elitedevbot.neo.Utils.BotLogger;
import kingdgrizzle.elitedevbot.neo.Utils.Reference;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ConfigHandler {

    static File CONFIG_FILE = new File("config.properties");

    public static void init() {
        if(!CONFIG_FILE.exists()) {
            BotLogger.info("First time setup.. Creating the config file, then shutting down!");
            try {
                CONFIG_FILE.createNewFile();
                Properties properties = new Properties();
                FileWriter fileWriter = new FileWriter(CONFIG_FILE);

                // Properties
                properties.setProperty("DISCORD_TOKEN", "");
                properties.setProperty("AUTO_CHAT_MESSAGES", "");
                properties.setProperty("ENABLE_SPELL_CHECKER", "");
                properties.setProperty("ENABLE_USERNAME_CHECKER", "");


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


                properties.setProperty("DEBUG_MODE", "false");
                properties.store(fileWriter, "Discord Bot Settings | General Bot utilities");
                fileWriter.close();
                System.exit(1);
            } catch (IOException e) {
                BotLogger.debug("Error while creating the config file!", e);
            }
        }
        try {
            FileReader reader = new FileReader(CONFIG_FILE);
            Properties properties = new Properties();
            properties.load(reader);

            // Read values
            Main.DISCORD_TOKEN = properties.getProperty("DISCORD_TOKEN");
            Main.enableAutoMessages = Boolean.parseBoolean(properties.getProperty("AUTO_CHAT_MESSAGES"));
            Main.enableSpellChecker = Boolean.parseBoolean(properties.getProperty("ENABLE_SPELL_CHECKER"));
            Main.enableUsernameChecker = Boolean.parseBoolean(properties.getProperty("ENABLE_USERNAME_CHECKER"));

            Reference.DISCORD_SERVER_ID = properties.getProperty("SERVER_ID");
            Reference.USER_ID_BOT_OWNER = properties.getProperty("BOT_OWNER_ID");
            Reference.STAFF_LOG_CHANNEL_ID = properties.getProperty("STAFF_CHAT_LOG_CHANNEL_ID");
            Reference.STAFF_CHAT_CHANNEL_ID = properties.getProperty("STAFF_CHAT_CHANNEL_ID");
            Reference.STAFF_NICK_CHAT_CHANNEL_ID = properties.getProperty("STAFF_CHAT_NICK_CHANNEL_ID");

            Reference.ROLE_STAFF_ID = properties.getProperty("ROLE_STAFF_ID");
            Reference.ROLE_ADMIN_ID = properties.getProperty("ROLE_ADMIN_ID");
            Reference.ROLE_MOD_ID = properties.getProperty("ROLE_MOD_ID");
            Reference.ROLE_MUTED_ID = properties.getProperty("ROLE_MUTED_ID");

            Main.debugMode = Boolean.parseBoolean(properties.getProperty("DEBUG_MODE"));
            reader.close();
        } catch (IOException e) {
            BotLogger.debug("Error while loading the config file!", e);
        }
    }
}
