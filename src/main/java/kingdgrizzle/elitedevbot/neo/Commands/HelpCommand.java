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
 * File Created @ [ 27.12.2016, 14:15 (GMT +02) ]
 */
package kingdgrizzle.elitedevbot.neo.Commands;

import kingdgrizzle.elitedevbot.neo.Main;
import kingdgrizzle.elitedevbot.neo.Utils.Reference;
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
            String helpCommand = args[0];
            ICommand command = getCommandFromString(helpCommand);
            sendHelpMessage(event, command);
        }
    }

    @Override
    public String help() {
        return "Use `" + Reference.COMMAND_PREFIX + "help <command>` to view the help message of that command!";
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {}

    @Override
    public String getTag() {
        return "help";
    }

    private static void sendGeneralHelpMessage(MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
        eb.setColor(Color.green);
        eb.setTitle("General Help Message");
        eb.setDescription("Hey! I'm Elite Developer Neo, and my creator is KingDGrizzle (Vlad)!\nThis project is a continuation of Elite Developer Bot made by XeliteXirish!");
        Iterator entries = Main.commands.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry pair = (Map.Entry) entries.next();
            ICommand cmd = (ICommand) pair.getValue();
            eb.addField("__**" + cmd.getTag() + "**__", cmd.help(), true);
        }
        //eb.setFooter(Reference.EMBED_FOOTER_NAME, Reference.EMBED_FOOTER_IMAGE);
        String otherInfo = "";
        otherInfo += "The Bot Prefix is: `" + Reference.COMMAND_PREFIX + "`\n";
        otherInfo += "The Staff Commands can be seen by entering: `" + Reference.DISCORD_COMMAND_PREFIX + "help`\n";
        otherInfo += "The Music Part of the bot can be accessed by typing `.help`";
        otherInfo += "The current Bot Version is: `" + Reference.VERSION + "`\n";
        eb.addField("__**Other Info:**__", otherInfo, false);
        MessageEmbed embed = eb.build();
        event.getTextChannel().sendMessage(embed).queue();
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
            eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
            //eb.setFooter(Reference.EMBED_FOOTER_NAME, Reference.EMBED_FOOTER_IMAGE);
            eb.setColor(Color.green);
            eb.setTitle("Heres the command help for: " + command.getTag());
            eb.setDescription(command.help());
            MessageEmbed embed = eb.build();
            event.getTextChannel().sendMessage(embed).queue();
        } else {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
            //eb.setFooter(Reference.EMBED_FOOTER_NAME, Reference.EMBED_FOOTER_IMAGE);
            eb.setColor(Color.red);
            eb.setTitle("Error while searching info for that command!");
            eb.setDescription("Sorry there is no info available for this command, please contact a bot admin.");
            MessageEmbed embed = eb.build();
            event.getTextChannel().sendMessage(embed).queue();
        }
    }
}
