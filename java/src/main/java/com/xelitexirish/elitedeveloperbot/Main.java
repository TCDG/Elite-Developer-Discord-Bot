package com.xelitexirish.elitedeveloperbot;

import com.xelitexirish.elitedeveloperbot.commands.ICommand;
import com.xelitexirish.elitedeveloperbot.commands.ProjectsCommands;
import com.xelitexirish.elitedeveloperbot.commands.UsageCommand;
import com.xelitexirish.elitedeveloperbot.listeners.BotListener;
import com.xelitexirish.elitedeveloperbot.utils.CommandParser;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;

import java.util.HashMap;

public class Main {

    private static JDA jda;
    public static final CommandParser parser = new CommandParser();
    public static HashMap<String, ICommand> commands = new HashMap<>();

    public static String DISCORD_TOKEN;
    public static boolean enableMessages = true;

    /**
     * 1: Discord Token
     * 2: Display messages when a user joins/bans/unbans
     * @param args
     */
    public static void main(String[] args) {

        if(args.length >= 2){
            DISCORD_TOKEN = args[0];
            enableMessages = Boolean.parseBoolean(args[1]);
        }else{
            System.out.println("Please enter a valid Discord Token!");
            return;
        }

        BotLogger.initLogger();

        try {
            jda = new JDABuilder().setBotToken(DISCORD_TOKEN).setAudioEnabled(false).addListener(new BotListener()).buildBlocking();
            jda.setAutoReconnect(true);
        }catch (Exception e){
            e.printStackTrace();
        }

        registerCommands();
    }

    private static void registerCommands(){
        commands.put("usage", new UsageCommand());
        commands.put("projects", new ProjectsCommands());
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
