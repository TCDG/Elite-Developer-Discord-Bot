package TCDG.EliteDevBot.Neo;

import TCDG.EliteDevBot.Neo.API.ShardingManager;
import TCDG.EliteDevBot.Neo.Commands.Guild.GuildCommand;
import TCDG.EliteDevBot.Neo.Commands.HelpCommand;
import TCDG.EliteDevBot.Neo.Commands.ICommand;
import TCDG.EliteDevBot.Neo.Commands.SpellCheckerCommand;
import TCDG.EliteDevBot.Neo.Commands.StatusCommand;
import TCDG.EliteDevBot.Neo.Commands.User.IDCommand;
import TCDG.EliteDevBot.Neo.Handlers.ConfigHandler;
import TCDG.EliteDevBot.Neo.Listeners.BotListener;
import TCDG.EliteDevBot.Neo.Listeners.Moderation.BadUsernameListener;
import TCDG.EliteDevBot.Neo.Listeners.SpellCheckerListener;
import TCDG.EliteDevBot.Neo.Music.MusicControl;
import TCDG.EliteDevBot.Neo.Utils.BotLogger;
import TCDG.EliteDevBot.Neo.Utils.CommandParser;
import TCDG.EliteDevBot.Neo.Utils.Reference;
import TCDG.EliteDevBot.Neo.Utils.UserPrivs;
import TCDG.EliteDevBot.Neo.Commands.User.ProfileCommand;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import java.util.Date;
import java.util.HashMap;

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
public class Main {
    public static JDA jda;
    public static final CommandParser parser = new CommandParser();
    public static HashMap<String, ICommand> commands = new HashMap<>();

    public static String DISCORD_TOKEN;
    public static boolean enableAutoMessages = true;
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
                jda.addEventListener(new MusicControl());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SpellCheckerListener.init();
        registerCommands();
        UserPrivs.setupUsers();
        BadUsernameListener.init();
        if (sharding) {
            Reference.EMBED_AUTHOR = ShardingManager.getBotName();
            Reference.EMBED_AUTHOR_IMAGE = ShardingManager.getBotAvatar();
        } else {
            Reference.EMBED_AUTHOR = jda.getSelfUser().getName();
            Reference.EMBED_AUTHOR_IMAGE = jda.getSelfUser().getEffectiveAvatarUrl();
        }
    }

    private static void registerCommands(){
        commands.put("help", new HelpCommand());
        commands.put("id", new IDCommand());
        commands.put("profile", new ProfileCommand());
        commands.put("guild", new GuildCommand());
        commands.put("status", new StatusCommand());
        commands.put("correction", new SpellCheckerCommand());
    }

    public static void handleCommand(CommandParser.CommandContainer cmd) {
        if (commands.containsKey(cmd.invoke)) {
            commands.get(cmd.invoke).action(cmd.args, cmd.event);
            commands.get(cmd.invoke).executed(true, cmd.event);
        }
    }
}
