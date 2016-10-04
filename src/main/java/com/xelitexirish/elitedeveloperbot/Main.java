package com.xelitexirish.elitedeveloperbot;

import com.xelitexirish.elitedeveloperbot.commands.*;
import com.xelitexirish.elitedeveloperbot.handlers.ConfigHandler;
import com.xelitexirish.elitedeveloperbot.handlers.TwitterHandler;
import com.xelitexirish.elitedeveloperbot.listeners.BadUsernameListener;
import com.xelitexirish.elitedeveloperbot.listeners.BotListener;
import com.xelitexirish.elitedeveloperbot.listeners.SpellCheckerListener;
import com.xelitexirish.elitedeveloperbot.utils.*;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.OnlineStatus;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

import java.util.HashMap;
import java.util.Scanner;

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

    // https://discordapp.com/oauth2/authorize?client_id=207593082328186880&scope=bot&permissions=0

    public static void main(String[] args) {

        ConfigHandler.init();

        if(DISCORD_TOKEN.equals("")){
            System.out.println("Please enter a valid discord token and try again.");
            return;
        }
        if(CONSUMER_KEY.equals("")){
            System.out.println("Please enter valid twitter credentials to use this feature.");
        }

        BotLogger.initLogger();

        try {
            jda = new JDABuilder().setBotToken(DISCORD_TOKEN).setAudioEnabled(false).addListener(new BotListener()).buildBlocking();
            jda.setAutoReconnect(true);
            jda.getAccountManager().setGame("Use '" + Constants.COMMAND_PREFIX + "help' to view the bot information!");
        }catch (Exception e){
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

    public static void handleCommand(CommandParser.CommandContainer cmd){
        if(commands.containsKey(cmd.invoke)){
            boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);

            if(safe){
                commands.get(cmd.invoke).action(cmd.args, cmd.event);
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }else {
                commands.get(cmd.invoke).executed(safe, cmd.event);
            }
        }
    }
}
