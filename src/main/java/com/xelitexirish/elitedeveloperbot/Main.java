package com.xelitexirish.elitedeveloperbot;

import com.xelitexirish.elitedeveloperbot.commands.*;
import com.xelitexirish.elitedeveloperbot.handlers.ConfigHandler;
import com.xelitexirish.elitedeveloperbot.handlers.TwitterHandler;
import com.xelitexirish.elitedeveloperbot.listeners.BadUsernameListener;
import com.xelitexirish.elitedeveloperbot.listeners.BotListener;
import com.xelitexirish.elitedeveloperbot.listeners.SpellCheckerListener;
import com.xelitexirish.elitedeveloperbot.utils.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import java.util.HashMap;

// https://discordapp.com/oauth2/authorize?client_id=207593082328186880&scope=bot&permissions=0
public class Main {

    public static JDA jda;
    public static final CommandParser parser = new CommandParser();
    public static HashMap<String, ICommand> commands = new HashMap<>();

    public static String DISCORD_TOKEN;
    public static boolean enableAutoMessages = true;
    public static boolean enableSpellChecker = true;
    public static boolean enableUsernameChecker = true;
    public static String CONSUMER_KEY;
    public static String CONSUMER_SECRET;
    public static String ACCESS_TOKEN;
    public static String ACCESS_TOKEN_SECRET;

    // Unused
    public static boolean enableTimerMessages = true;

    /**
     * Pass true to disable the config file and use options from script.
     * @param args
     */
    public static void main(String[] args) {

        if (args.length > 0 && Boolean.parseBoolean(args[0])) {
            DISCORD_TOKEN = args[1];
        } else {
            ConfigHandler.init();
        }

        if (DISCORD_TOKEN == null || DISCORD_TOKEN.equals("")) {
            BotLogger.error("Please enter a valid discord token and try again.");
            return;
        }
        if (CONSUMER_KEY == null || CONSUMER_KEY.equals("")) {
            BotLogger.error("Please enter valid twitter credentials to use this feature.");
        }
        BotLogger.initLogger();
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(DISCORD_TOKEN).setAudioEnabled(false).addListener(new BotListener()).buildBlocking();
            jda.setAutoReconnect(true);
            jda.getPresence().setGame(Game.of("Use '" + Constants.COMMAND_PREFIX + "help' to view the bot info!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        SpellCheckerListener.init();
        registerCommands();
        UserPrivs.setupUsers();
        TwitterHandler.init(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
        BadUsernameListener.init();
        //MessageTimer.init();
        //WarningHandler.setup();
    }

    private static void registerCommands(){
        commands.put("help", new HelpCommand());
        commands.put("admin", new AdminCommand());
        commands.put("userid", new PlayerIdCommand());
        commands.put("correction", new SpellCheckerCommand());
        //commands.put("warn", new WarningCommand());
    }

    public static void handleCommand(CommandParser.CommandContainer cmd) {
        if (commands.containsKey(cmd.invoke)) {
            commands.get(cmd.invoke).action(cmd.args, cmd.event);
            commands.get(cmd.invoke).executed(true, cmd.event);
        }
    }
}
