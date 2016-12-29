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
 * File Created @ [ 29.12.2016, 9:45 (GMT +02) ]
 */
package kingdgrizzle.elitedevbot.neo.Commands;

import kingdgrizzle.elitedevbot.neo.Main;
import kingdgrizzle.elitedevbot.neo.Utils.BotLogger;
import kingdgrizzle.elitedevbot.neo.Utils.MessageUtils;
import kingdgrizzle.elitedevbot.neo.Utils.Reference;
import kingdgrizzle.elitedevbot.neo.Utils.UserPrivs;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class DiscordStaffUtils {

    private static String[] commands = {"ban", "help", "kick", "purge", "rm"};
    private static String[] commandsHelp = {
            "Bans the mentioned user (or users)!",
            "Displays help information for the Staff Commands!",
            "Kicks the mentioned user (or users)!",
            "Looks in the past 100 messages, and deletes the ones from the mentioned user (or users)!",
            "Right click a message and choose `Copy ID`. Then insert that ID in here, and the bot will attempt to remove it!"
    };

    public static void handleMessage(MessageReceivedEvent event) {
        String[] lineSplit = event.getMessage().getRawContent().split(" ");
        if (event.getMessage().getRawContent().startsWith(Reference.DISCORD_COMMAND_PREFIX) && Arrays.asList(commands).contains(lineSplit[0].substring(Reference.DISCORD_COMMAND_PREFIX.length()))) {
            if (UserPrivs.isUserStaff(event.getAuthor()) || event.getAuthor().getId().equals(Reference.KING_ID)) {
                if (lineSplit[0].substring(Reference.DISCORD_COMMAND_PREFIX.length()).equalsIgnoreCase(commands[0])) {
                    // Ban command Handler
                    List<User> mentionedUsers = event.getMessage().getMentionedUsers();
                    if (!mentionedUsers.isEmpty()) {
                        for (User user : mentionedUsers) {
                            event.getGuild().getController().ban(user, 1).queue();
                            String logmessage = event.getAuthor().getName() + " has banned the following user: " + user.getAsMention();
                            event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed(Color.red, "User " + user.getAsMention() + "got **banned** from the server!")).queue();
                            MessageUtils.sendMessageToStaffInfoChat(event.getJDA(), logmessage);
                            if (Main.debugMode) {
                                MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logmessage);
                            }
                        }
                    } else {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                        eb.setColor(Color.red);
                        eb.setDescription("You must mention at least one person to ban!");
                        eb.setTitle("Error!");
                        MessageEmbed embed = eb.build();
                        event.getTextChannel().sendMessage(embed).queue();
                    }
                } else if (lineSplit[0].substring(Reference.DISCORD_COMMAND_PREFIX.length()).equalsIgnoreCase(commands[1])) {
                    // Help Command Handler
                    sendGeneralHelp(event);
                }
            }
        }
    }

    private static void sendGeneralHelp(MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
        eb.setColor(Color.green);
        eb.setDescription("Heres the help for the Staff Commands!");
        eb.setTitle("Help for the Staff Commands");
        for (int x = 0; x < commands.length; x++) {
            eb.addField("__**" + commands[x] + "**__", commandsHelp[x], true);
        }
        MessageEmbed embed = eb.build();
        event.getTextChannel().sendMessage(embed).queue();
    }
}
