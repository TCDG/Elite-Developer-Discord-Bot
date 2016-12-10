package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Iterator;
import java.util.Map;

public class HelpCommand implements ICommand {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (args.length == 0) {
            sendGeneralHelpMessage(event);
        } else if (args.length == 1) {
            // eb help (command)
            String helpCommand = args[0];
            ICommand command = getCommandFromString(helpCommand);
            sendHelpMessage(event, command);
        }
    }

    @Override
    public String help() {
        String message = "Use '" + Constants.COMMAND_PREFIX + "help <command name>' to view more information about that command!";
        return message;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {}

    @Override
    public String getTag() {
        return "help";
    }

    public static void sendGeneralHelpMessage(MessageReceivedEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hey I'm Elite Developer Bot, my master is XeliteXirish! Check his website out (www.xelitexirish.com)\n");
        stringBuilder.append("You can use the following commands with this bot: ");
        Iterator entries = Main.commands.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry pair = (Map.Entry) entries.next();
            ICommand command = (ICommand) pair.getValue();
            stringBuilder.append(command.getTag() + ", ");
        }
        stringBuilder.append("\nThe bot prefix is: " + Constants.COMMAND_PREFIX + "\n");
        stringBuilder.append("\nThe staff commands can be visible by entering " + Constants.DISCORD_COMMAND_PREFIX + " help\n");
        stringBuilder.append("The current bot version is: " + Constants.CURRENT_VERSION + "\n");
        event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed(stringBuilder.toString())).queue();
    }

    private static ICommand getCommandFromString(String commandName) {
        if (Main.commands.containsKey(commandName)) {
            return Main.commands.get(commandName);
        }
        return null;
    }

    private static void sendHelpMessage(MessageReceivedEvent event, ICommand command) {
        if (command.help() != null) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setAuthor(Constants.EMBED_AUTHOR, Constants.EMBED_AUTHOR_URL, Constants.EMBED_AUTHOR_IMAGE);
            eb.setFooter(Constants.EMBED_FOOTER_NAME, Constants.EMBED_FOOTER_IMAGE);
            eb.setColor(Color.green);
            eb.setTitle("Heres the command help for: " + command.getTag());
            eb.setDescription(command.help());
            MessageEmbed embed = eb.build();
            event.getTextChannel().sendMessage(embed).queue();
        } else {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setAuthor(Constants.EMBED_AUTHOR, Constants.EMBED_AUTHOR_URL, Constants.EMBED_AUTHOR_IMAGE);
            eb.setFooter(Constants.EMBED_FOOTER_NAME, Constants.EMBED_FOOTER_IMAGE);
            eb.setColor(Color.red);
            eb.setTitle("Error while searching info for that command!");
            eb.setDescription("Sorry there is no info available for this command, please contact a bot admin.");
            MessageEmbed embed = eb.build();
            event.getTextChannel().sendMessage(embed).queue();
        }
    }
}
