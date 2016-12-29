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
 * File Created @ [ 27.12.2016, 23:19 (GMT +02) ]
 */
package kingdgrizzle.elitedevbot.neo.Commands.User;

import kingdgrizzle.elitedevbot.neo.Commands.ICommand;
import kingdgrizzle.elitedevbot.neo.Main;
import kingdgrizzle.elitedevbot.neo.Utils.BotLogger;
import kingdgrizzle.elitedevbot.neo.Utils.Reference;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class IDCommand implements ICommand {

    private static String[] idCommands = {"channel", "role", "user"};
    private static String[] idCommandsHelp = {
            "Gets the mentioned channel (or channels) ID!",
            "Gets the mentioned role (or roles) ID!",
            "Gets the mentioned user (or users) ID!"
    };

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (args.length == 0) {
            sendIDHelp(event);
        } else {
            if (args[0].equalsIgnoreCase("channel")) {
                List<TextChannel> mentionedChannels = event.getMessage().getMentionedChannels();
                if (!mentionedChannels.isEmpty()) {
                    HashMap<String, String> channnelID = new HashMap<>();
                    HashMap<String, String> channelName = new HashMap<>();
                    for (TextChannel channel : mentionedChannels) {
                        channnelID.put(channel.getName(), channel.getId());
                        channelName.put(channel.getName(), channel.getName());
                    }
                    if (Main.debugMode = true) {
                        BotLogger.listHashMap(channnelID);
                        BotLogger.listHashMap(channelName);
                    }
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                    if (mentionedChannels.size() >= 24) {
                        eb.setFooter("You have mentioned too many channels! Please mention at-most 25", "https://cdn2.iconfinder.com/data/icons/freecns-cumulus/32/519791-101_Warning-128.png");
                    }
                    eb.setColor(Color.green);
                    eb.setTitle("Command Output");
                    eb.setDescription("Here's the channel ID" + (mentionedChannels.size() > 1 ? "s" : "") + " for the mentioned channel" + (mentionedChannels.size() > 1 ? "s" : "") + ":");
                    Iterator entries = channnelID.entrySet().iterator();
                    Iterator entries2 = channelName.entrySet().iterator();
                    while (entries.hasNext() && entries2.hasNext()) {
                        Map.Entry pair = (Map.Entry) entries.next();
                        Map.Entry pair2 = (Map.Entry) entries2.next();
                        String userID = (String) pair.getValue();
                        String userName = (String) pair2.getValue();
                        eb.addField("__**" + userName + "**__", userID, true);
                    }
                    MessageEmbed embed = eb.build();
                    event.getChannel().sendMessage(embed).queue();
                    channnelID.clear();
                    channelName.clear();
                } else {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                    eb.setColor(Color.red);
                    eb.setTitle("Error");
                    eb.setDescription("Please mention at least one channel!");
                    MessageEmbed embed = eb.build();
                    event.getTextChannel().sendMessage(embed).queue();
                }
            } else if (args[0].equalsIgnoreCase("role")) {
                List<Role> mentionedRoles = event.getMessage().getMentionedRoles();
                if (!mentionedRoles.isEmpty()) {
                    HashMap<String, String> roleID = new HashMap<>();
                    HashMap<String, String> roleName = new HashMap<>();
                    for (Role role : mentionedRoles) {
                        roleID.put(role.getName(), role.getId());
                        roleName.put(role.getName(), role.getName());
                    }
                    if (Main.debugMode = true) {
                        BotLogger.listHashMap(roleID);
                        BotLogger.listHashMap(roleName);
                    }
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                    if (mentionedRoles.size() >= 24) {
                        eb.setFooter("You have mentioned too many roles! Please mention at-most 25", "https://cdn2.iconfinder.com/data/icons/freecns-cumulus/32/519791-101_Warning-128.png");
                    }
                    eb.setColor(Color.green);
                    eb.setTitle("Command Output");
                    eb.setDescription("Here's the role ID" + (mentionedRoles.size() > 1 ? "s" : "") + " for the mentioned role" + (mentionedRoles.size() > 1 ? "s" : "") + ":");
                    Iterator entries = roleID.entrySet().iterator();
                    Iterator entries2 = roleName.entrySet().iterator();
                    while (entries.hasNext() && entries2.hasNext()) {
                        Map.Entry pair = (Map.Entry) entries.next();
                        Map.Entry pair2 = (Map.Entry) entries2.next();
                        String userID = (String) pair.getValue();
                        String userName = (String) pair2.getValue();
                        eb.addField("__**" + userName + "**__", userID, true);
                    }
                    MessageEmbed embed = eb.build();
                    event.getChannel().sendMessage(embed).queue();
                    roleID.clear();
                    roleName.clear();
                } else {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                    eb.setColor(Color.red);
                    eb.setTitle("Error");
                    eb.setDescription("Please mention at least one role!");
                    MessageEmbed embed = eb.build();
                    event.getTextChannel().sendMessage(embed).queue();
                }
            } else if (args[0].equalsIgnoreCase("user")) {
                List<User> mentionedUser = event.getMessage().getMentionedUsers();
                if (!mentionedUser.isEmpty()) {
                    HashMap<String, String> userNameIDCombo = new HashMap<>();
                    HashMap<String, String> userNames = new HashMap<>();
                    for (User usr : mentionedUser) {
                        userNameIDCombo.put(usr.getName(), usr.getId());
                        userNames.put(usr.getName(), usr.getName());
                    }
                    if (Main.debugMode = true) {
                        BotLogger.listHashMap(userNameIDCombo);
                        BotLogger.listHashMap(userNames);
                    }
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                    if (mentionedUser.size() >= 24) {
                        eb.setFooter("You have mentioned too many users! Please mention at-most 25", "https://cdn2.iconfinder.com/data/icons/freecns-cumulus/32/519791-101_Warning-128.png");
                    }
                    eb.setColor(Color.green);
                    eb.setTitle("Command Output");
                    eb.setDescription("Here's the user ID" + (mentionedUser.size() > 1 ? "s" : "") + " for the mentioned user" + (mentionedUser.size() > 1 ? "s" : "") + ":");
                    Iterator entries = userNameIDCombo.entrySet().iterator();
                    Iterator entries2 = userNames.entrySet().iterator();
                    while (entries.hasNext() && entries2.hasNext()) {
                        Map.Entry pair = (Map.Entry) entries.next();
                        Map.Entry pair2 = (Map.Entry) entries2.next();
                        String userID = (String) pair.getValue();
                        String userName = (String) pair2.getValue();
                        eb.addField("__**" + userName + "**__", userID, true);
                    }
                    MessageEmbed embed = eb.build();
                    event.getChannel().sendMessage(embed).queue();
                    userNameIDCombo.clear();
                    userNames.clear();
                } else {
                    String userId = event.getAuthor().getId();
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                    eb.setColor(Color.green);
                    eb.setTitle("Command Output");
                    eb.setDescription("Your user ID is: " + userId);
                    MessageEmbed embed = eb.build();
                    event.getTextChannel().sendMessage(embed).queue();
                }
            }
        }
    }

    @Override
    public String help() {
        return "Use `" + Reference.COMMAND_PREFIX + "id` for help about the ID commands!";
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {}

    @Override
    public String getTag() {
        return "id";
    }

    private static void sendIDHelp(MessageReceivedEvent par1Event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
        eb.setColor(Color.cyan);
        eb.setTitle("Help for the ID commands");
        eb.setDescription("The following ID commands can be used with the bot:");
        for (int x = 0; x < idCommands.length; x++) {
            eb.addField("__**" + idCommands[x] + "**__", idCommandsHelp[x], true);
        }
        MessageEmbed embed = eb.build();
        par1Event.getChannel().sendMessage(embed).queue();
    }
}
