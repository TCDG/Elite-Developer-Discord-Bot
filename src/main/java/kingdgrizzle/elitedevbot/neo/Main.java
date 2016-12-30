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
 * File Created @ [ 24.12.2016, 10:11 (GMT +02) ]
 */
package kingdgrizzle.elitedevbot.neo;

import kingdgrizzle.elitedevbot.neo.API.ShardingManager;
import kingdgrizzle.elitedevbot.neo.Commands.Guild.GuildCommand;
import kingdgrizzle.elitedevbot.neo.Commands.HelpCommand;
import kingdgrizzle.elitedevbot.neo.Commands.ICommand;
import kingdgrizzle.elitedevbot.neo.Commands.StatusCommand;
import kingdgrizzle.elitedevbot.neo.Commands.User.ProfileCommand;
import kingdgrizzle.elitedevbot.neo.Handlers.ConfigHandler;
import kingdgrizzle.elitedevbot.neo.Listeners.BotListener;
import kingdgrizzle.elitedevbot.neo.Listeners.Moderation.BadUsernameListener;
import kingdgrizzle.elitedevbot.neo.Utils.BotLogger;
import kingdgrizzle.elitedevbot.neo.Utils.CommandParser;
import kingdgrizzle.elitedevbot.neo.Utils.Reference;
import kingdgrizzle.elitedevbot.neo.Utils.UserPrivs;
import kingdgrizzle.elitedevbot.neo.Commands.User.IDCommand;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import java.util.Date;
import java.util.HashMap;

public class Main {

    public static JDA jda;
    public static final CommandParser parser = new CommandParser();
    public static HashMap<String, ICommand> commands = new HashMap<>();

    public static String DISCORD_TOKEN;
    //public static boolean enableAutoMessages = true;
    public static boolean enableSpellChecker = false;
    public static boolean enableUsernameChecker = true;

    public static boolean debugMode = true;
    public static boolean sharding = false;

    public static long uptime = new Date().getTime();

    public static void main(String[] args) {

        if (args.length > 0 && Boolean.parseBoolean(args[0])) {
            DISCORD_TOKEN = args[1];
        } else {
            ConfigHandler.init();
        }
        if (DISCORD_TOKEN == null || DISCORD_TOKEN.equals("")) {
            BotLogger.error("Please enter a valid discord token and try again.");
            System.exit(1);
        }
        if (sharding) {
            ShardingManager.init();
        } else {
            try {
                jda = new JDABuilder(AccountType.BOT).setToken(DISCORD_TOKEN).addListener(new BotListener()).buildBlocking();
                jda.setAutoReconnect(true);
                jda.getPresence().setGame(Game.of("Use '" + Reference.COMMAND_PREFIX + "help' to view the bot info!"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        SpellCheckerListener.init();
        registerCommands();
        UserPrivs.setupUsers();
        BadUsernameListener.init();
        if (sharding) {
            Reference.EMBED_AUTHOR = ShardingManager.shards.get(0).getSelfUser().getName();
            Reference.EMBED_AUTHOR_IMAGE = ShardingManager.shards.get(0).getSelfUser().getEffectiveAvatarUrl();
        } else {
            Reference.EMBED_AUTHOR = jda.getSelfUser().getName();
            Reference.EMBED_AUTHOR_IMAGE = jda.getSelfUser().getEffectiveAvatarUrl();
        }
    }

    public static void registerCommands(){
        commands.put("help", new HelpCommand());
        commands.put("id", new IDCommand());
        commands.put("profile", new ProfileCommand());
        commands.put("guild", new GuildCommand());
        commands.put("status", new StatusCommand());
    }

    public static void handleCommand(CommandParser.CommandContainer cmd) {
        if (commands.containsKey(cmd.invoke)) {
            commands.get(cmd.invoke).action(cmd.args, cmd.event);
            commands.get(cmd.invoke).executed(true, cmd.event);
        }
    }
}
