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
 * File Created @ [ 26.12.2016, 10:40 (GMT +02) ]
 */
package TCDG.EliteDevBot.Neo.Utils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class MessageUtils {

    public static MessageEmbed wrapMessageInEmbed(Color color, String message) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
        //eb.setFooter(Reference.EMBED_FOOTER_NAME, Reference.EMBED_FOOTER_IMAGE);
        eb.setColor(color);
        eb.setDescription(message);
        eb.setTitle("Notification");
        return eb.build();
    }

    public static void sendNoPermissionMessage(TextChannel channel) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
        //eb.setFooter(Reference.EMBED_FOOTER_NAME, Reference.EMBED_FOOTER_IMAGE);
        eb.setColor(Color.red);
        eb.setTitle("Permission Error!");
        eb.setDescription("Sorry, but you don't have the required permission to use this command!");
        MessageEmbed embed = eb.build();
        channel.sendMessage(embed).queue();
    }

    public static void sendMessageToStaffDebugChat(JDA jda, String message) {
        for (Guild guild : jda.getGuilds()) {
            if (guild.getId().equals(Reference.DISCORD_SERVER_ID)) {
                for (TextChannel channel : guild.getTextChannels()) {
                    if (channel.getId().equals(Reference.STAFF_LOG_CHANNEL_ID)) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                        eb.setColor(Color.gray);
                        eb.setTitle("Debug Message");
                        eb.setDescription(message);
                        MessageEmbed embed = eb.build();
                        channel.sendMessage(embed).queue();
                    }
                }
            }
        }
    }

    public static void sendMessageToStaffInfoChat(JDA jda, String message) {
        Guild guild = jda.getGuildById(Reference.DISCORD_SERVER_ID);
        for (TextChannel channel : guild.getTextChannels()) {
            if (channel.getId().equals(Reference.STAFF_CHAT_CHANNEL_ID)) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                eb.setColor(Color.green);
                eb.setDescription(message);
                eb.setTitle("Notification");
                MessageEmbed embed = eb.build();
                channel.sendMessage(embed).queue();
            }
        }
    }

    public static void sendMessageToNickChat(JDA jda, String message) {
        Guild guild = jda.getGuildById(Reference.DISCORD_SERVER_ID);
        for (TextChannel channel : guild.getTextChannels()) {
            if (channel.getId().equals(Reference.STAFF_NICK_CHAT_CHANNEL_ID)) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                eb.setColor(Color.green);
                eb.setDescription(message);
                eb.setTitle("Nickname changed!");
                MessageEmbed embed = eb.build();
                channel.sendMessage(embed).queue();
            }
        }
    }
}
