package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.Project;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HelpCommand implements ICommand{

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(event.getMessage().getContent().equalsIgnoreCase("eb")){
            sendGeneralHelpMessage(event);
        }

        if(args.length == 0){
            sendGeneralHelpMessage(event);

        }else if(args.length == 1){
            // eb help (command)
            String helpCommand = args[0];

            ICommand command = getCommandFromString(helpCommand);
            sendHelpMessage(event, command);
        }
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String getTag() {
        return "help";
    }

    public static void sendGeneralHelpMessage(MessageReceivedEvent event){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("You can use the following commands with this bot: ");
        Iterator entries = Main.commands.entrySet().iterator();
        while (entries.hasNext()){
            Map.Entry pair = (Map.Entry) entries.next();
            ICommand command = (ICommand) pair.getValue();
            stringBuilder.append(command.getTag() + ", ");
        }
        stringBuilder.append("\n  The bot prefix is: " + Constants.COMMAND_PREFIX + "\n");
        stringBuilder.append("The current bot version is: " + Constants.CURRENT_VERSION);
        event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock(stringBuilder.toString()));
    }

    private static ICommand getCommandFromString(String commandName){
        if(Main.commands.containsKey(commandName)){
            return Main.commands.get(commandName);
        }
        return null;
    }

    private static void sendHelpMessage(MessageReceivedEvent event, ICommand command){
        if(command.help() != null) {
            event.getTextChannel().sendMessage(command.help());
        }else {
            event.getTextChannel().sendMessage("Sorry there is no info available for this command, please contact a bot admin.");
        }
    }
}
