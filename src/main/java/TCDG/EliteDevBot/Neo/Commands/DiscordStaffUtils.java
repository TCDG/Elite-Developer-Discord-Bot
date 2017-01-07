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
package TCDG.EliteDevBot.Neo.Commands;

import TCDG.EliteDevBot.Neo.Utils.Reference;
import TCDG.EliteDevBot.Neo.Main;
import TCDG.EliteDevBot.Neo.Utils.BotLogger;
import TCDG.EliteDevBot.Neo.Utils.MessageUtils;
import TCDG.EliteDevBot.Neo.Utils.UserPrivs;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DiscordStaffUtils {

    private static String[] commands = {"ban", "help", "kick", "purge", "rm"};
    private static String[] commandsHelp = {
            "Bans the mentioned user (or users)!",
            "Displays help information for the Staff Commands!",
            "Kicks the mentioned user (or users)!",
            "Looks in the past number of messages you mentioned, and deletes the ones from the mentioned user (or users)!",
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
                        try {
                            for (User user : mentionedUsers) {
                                event.getGuild().getController().ban(user, 1).queue();
                                String logmessage = event.getAuthor().getName() + " has banned the following user: " + user.getAsMention();
                                event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed(Color.red, "User " + user.getAsMention() + " got **banned** from the server!")).queue();
                                MessageUtils.sendMessageToStaffInfoChat(event.getJDA(), logmessage);
                                if (Main.debugMode) {
                                    MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logmessage);
                                }
                            }
                        } catch (Exception e) {
                            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), e.getCause().toString());
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                            eb.setColor(Color.red);
                            eb.setDescription("I do not have permission to ban on this server!\nMake sure you give me __**Ban**__ and __**Kick**__ permission to use these commands");
                            eb.setTitle("Error!");
                            MessageEmbed embed = eb.build();
                            event.getTextChannel().sendMessage(embed).queue();
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
                } else if (lineSplit[0].substring(Reference.DISCORD_COMMAND_PREFIX.length()).equals(commands[2])) {
                    // Kick Handler
                    List<User> mentionedUsers = event.getMessage().getMentionedUsers();
                    if (!mentionedUsers.isEmpty()) {
                        try {
                            for (User user : mentionedUsers) {
                                event.getGuild().getController().kick(user.getName()).queue();
                                String logMessage = event.getAuthor().getName() + " has kicked the following user: " + user.getAsMention();
                                event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed(Color.red, "User " + user.getAsMention() + " got **kicked** from the server!")).queue();
                                MessageUtils.sendMessageToStaffInfoChat(event.getJDA(), logMessage);
                                if (Main.debugMode) {
                                    MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
                                }
                            }
                        } catch (Exception e) {
                            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), e.getCause().toString());
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                            eb.setColor(Color.red);
                            eb.setDescription("I do not have permission to kick on this server!\nMake sure you give me __**Ban**__ and __**Kick**__ permission to use these commands");
                            eb.setTitle("Error!");
                            MessageEmbed embed = eb.build();
                            event.getTextChannel().sendMessage(embed).queue();
                        }
                    } else {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                        eb.setColor(Color.red);
                        eb.setDescription("You must mention at least one person to kick!");
                        eb.setTitle("Error!");
                        MessageEmbed embed = eb.build();
                        event.getTextChannel().sendMessage(embed).queue();
                    }
                } else if (lineSplit[0].substring(Reference.DISCORD_COMMAND_PREFIX.length()).equals(commands[3])) {
                    //Purge Handler
                    try {
                        int deletedMsgs = 0;
                        if (lineSplit.length >= 2) {
                            List<User> mentionedUsers = event.getMessage().getMentionedUsers();
                            int historyLookup = Integer.parseInt(lineSplit[(mentionedUsers.size() + 1)]);
                            if (!mentionedUsers.isEmpty()) {
                                TextChannel channel = event.getTextChannel();
                                List<Message> deletedMessages = new ArrayList<>();
                                CompletableFuture<List<Message>> task = new CompletableFuture<>();
                                channel.getHistory().retrievePast(historyLookup).queue(task::complete, task::completeExceptionally);
                                List<Message> list = task.get();
                                for (Message msg : list) {
                                    for (User usr : mentionedUsers) {
                                        if (msg.getAuthor().getId().equals(usr.getId())) {
                                            deletedMessages.add(msg);
                                            deletedMsgs++;
                                        }
                                    }
                                }
                                channel.deleteMessages(deletedMessages).queue();
                                deletedMessages.clear();
                                event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed(Color.green, "Cleared the last " + deletedMsgs + " messages from the mentioned users from chat.")).queue();
                            } else {
                                event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed(Color.red, "You didn't mention anyone! Please use the syntax `" + Reference.DISCORD_COMMAND_PREFIX + commands[3] + " @user number`. You can mention multiple users!")).queue();
                            }
                        }
                    } catch (Exception e) {
                        MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), e.getCause().toString());
                        event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed(Color.red, "Invalid Parameters!\nYou can use a number between 2 and 100! (Inclusive)\nThe bot might not have **Manage Messages** Permission. Without that permission, the bot can't delete any messages!")).queue();
                    }
                } else if (lineSplit[0].substring(Reference.DISCORD_COMMAND_PREFIX.length()).equals(commands[4])) {
                    //RM Handler
                    if (lineSplit.length >= 1) {
                        String messageId = lineSplit[1];
                        try {
                            Message msg = event.getChannel().getMessageById(messageId).complete();
                            String logMessage = event.getAuthor().getAsMention() + " has removed a message in " + event.getTextChannel().getName() + ", the message said: `" + msg.getRawContent() + "`";
                            event.getChannel().deleteMessageById(messageId).queue();
                            event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed(Color.green, "Message Deleted")).queue();
                            if (Main.debugMode) {
                                MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
                            }
                            BotLogger.info("Message Removed:" + logMessage);
                        } catch (Exception e) {
                            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), e.getCause().toString());
                            event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed(Color.red, "Invalid Parameters")).queue();
                        }
                    }
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
