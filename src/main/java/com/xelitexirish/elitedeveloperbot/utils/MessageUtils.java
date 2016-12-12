package com.xelitexirish.elitedeveloperbot.utils;


import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;

import java.awt.*;

public class MessageUtils {

    public static MessageEmbed wrapMessageInEmbed(String message) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(Constants.EMBED_AUTHOR, Constants.EMBED_AUTHOR_URL, Constants.EMBED_AUTHOR_IMAGE);
        eb.setFooter(Constants.EMBED_FOOTER_NAME, Constants.EMBED_FOOTER_IMAGE);
        eb.setColor(Color.cyan);
        eb.setDescription(message);
        eb.setTitle("Notification");
        return eb.build();
    }

    public static void sendNoPermissionMessage(TextChannel channel) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(Constants.EMBED_AUTHOR, Constants.EMBED_AUTHOR_URL, Constants.EMBED_AUTHOR_IMAGE);
        eb.setFooter(Constants.EMBED_FOOTER_NAME, Constants.EMBED_FOOTER_IMAGE);
        eb.setColor(Color.red);
        eb.setTitle("Permission Error!");
        eb.setDescription("Sorry, but you don't have the required permission to use this command!");
        MessageEmbed embed = eb.build();
        channel.sendMessage(embed).queue();
    }

    public static void sendMessageToStaffDebugChat(JDA jda, String message) {
        for (Guild guild : jda.getGuilds()) {
            if (guild.getId().equals(Constants.DISCORD_SERVER_ID)) {
                for (TextChannel channel : guild.getTextChannels()) {
                    if (channel.getId().equals(Constants.STAFF_LOG_CHANNEL_ID)) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor(Constants.EMBED_AUTHOR, Constants.EMBED_AUTHOR_URL, Constants.EMBED_AUTHOR_IMAGE);
                        eb.setFooter(Constants.EMBED_FOOTER_NAME, Constants.EMBED_FOOTER_IMAGE);
                        eb.setColor(Color.cyan);
                        eb.setTitle("Debug Message");
                        eb.setDescription(message);
                        MessageEmbed embed = eb.build();
                        channel.sendMessage(embed).queue();
                    }
                }
            }
        }
    }

    public static void sendMessageToStaffChat(JDA jda, String message) {
        Guild guild = jda.getGuildById(Constants.DISCORD_SERVER_ID);
        for (TextChannel channel : guild.getTextChannels()) {
            if (channel.getId().equals(Constants.STAFF_CHAT_CHANNEL_ID)) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(Constants.EMBED_AUTHOR, Constants.EMBED_AUTHOR_URL, Constants.EMBED_AUTHOR_IMAGE);
                eb.setColor(Color.green);
                eb.setDescription(message);
                eb.setTitle("Notification");
                eb.setFooter(Constants.EMBED_FOOTER_NAME, Constants.EMBED_FOOTER_IMAGE);
                MessageEmbed embed = eb.build();
                channel.sendMessage(embed).queue();
            }
        }
    }

    public static void sendMessageToNickChat(JDA jda, String message) {
        Guild guild = jda.getGuildById(Constants.DISCORD_SERVER_ID);
        for (TextChannel channel : guild.getTextChannels()) {
            if (channel.getId().equals(Constants.STAFF_NICK_CHAT_CHANNEL_ID)) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(Constants.EMBED_AUTHOR, Constants.EMBED_AUTHOR_URL, Constants.EMBED_AUTHOR_IMAGE);
                eb.setColor(Color.green);
                eb.setFooter(Constants.EMBED_FOOTER_NAME, Constants.EMBED_FOOTER_IMAGE);
                eb.setDescription(message);
                eb.setTitle("Nickname changed!");
                MessageEmbed embed = eb.build();
                channel.sendMessage(embed).queue();
            }
        }
    }
}
