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
 * File Created @ [ 28.12.2016, 19:40 (GMT +02) ]
 */
package com.kingdgrizzle.elitedevbot.neo.Handlers;

import com.kingdgrizzle.elitedevbot.neo.Main;
import com.kingdgrizzle.elitedevbot.neo.Utils.BotLogger;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

public class ServerConfigHandler {

    private static File serverFolder = new File("serverData/");
    static File serverFile = null;

    public static void init(Guild guild) {
        if (!serverFile.exists()) {
            createConfigFile(guild);
        }
    }

    public static void createConfigFile(Guild guild) {
        try {
            serverFile = new File(serverFolder + guild.getId() + ".properties");
            Properties prop = new Properties();
            FileWriter writer = new FileWriter(serverFile);

            prop.setProperty("ENABLE_SPELL_CHECKER", "");

        } catch (IOException e) {
            BotLogger.debug("Error making config file?!", e);
        }
    }

    public static void changeOption(String[] args) {

    }
}
