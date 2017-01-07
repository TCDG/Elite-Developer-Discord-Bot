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
 * File Created @ [ 28.12.2016, 10:58 (GMT +02) ]
 */
package TCDG.EliteDevBot.Neo.Commands.User;

import TCDG.EliteDevBot.Neo.API.ShardingManager;
import TCDG.EliteDevBot.Neo.Commands.ICommand;
import TCDG.EliteDevBot.Neo.Main;
import TCDG.EliteDevBot.Neo.Utils.Reference;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ProfileCommand implements ICommand {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (args.length == 0) {
            User user = event.getAuthor();
            String roleNames = "";
            for (Role role : event.getMember().getRoles()) {
                roleNames += role.getName() + ", ";
            }
            EmbedBuilder eb = new EmbedBuilder();
            String status = "";
            switch (event.getAuthor().getJDA().getPresence().getStatus()) {
                case DO_NOT_DISTURB:
                    status = "Do Not Disturb";
                    break;
                case IDLE:
                    status = "Idle";
                    break;
                case ONLINE:
                    status = "Online";
                    break;
                case OFFLINE:
                case INVISIBLE:
                    status = "Offline or Invisible";
                    break;
            }
            eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
            eb.setColor(Color.green);
            eb.setTitle("Profile Information for: **" + user.getName() + "**");
            eb.addField("__**Username**__", user.getAsMention(), true);
            eb.addField("__**User ID**__", user.getId(), true);
            eb.addField("__**Status**__", status, true);
            eb.addField("__**Discriminator**__", user.getDiscriminator(), true);
            try {
                eb.addField("__**Ranks**__", roleNames.substring(0, roleNames.length() - 2), true);
            } catch (NullPointerException e) {
                eb.addField("__**Ranks**__", "Error wile getting role names!", true);
            }
            eb.addField("__**Playing**__", (event.getMember().getGame() != null ? event.getMember().getGame().getName() : "Nothing"), true);
            eb.addField("__**Created on**__", user.getCreationTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy 'at' HH:mm:ss")), true);
            eb.addField("__**Joined on**__", event.getMember().getJoinDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy 'at' HH:mm:ss")), true);
            eb.setThumbnail(user.getEffectiveAvatarUrl());
            MessageEmbed embed = eb.build();
            event.getChannel().sendMessage(embed).queue();
        } else {
            User mentionedUser = event.getMessage().getMentionedUsers().get(0);
            String roleNames = "";
            for (Role role : event.getGuild().getMember(mentionedUser).getRoles() ) {
                roleNames += role.getName() + ", ";
            }
            EmbedBuilder eb = new EmbedBuilder();
            String status = "";
            switch (event.getGuild().getMember(mentionedUser).getOnlineStatus()) {
                case DO_NOT_DISTURB:
                    status = "Do Not Disturb";
                    break;
                case IDLE:
                    status = "Idle";
                    break;
                case ONLINE:
                    status = "Online";
                    break;
                case OFFLINE:
                case INVISIBLE:
                    status = "Offline or Invisible";
                    break;
            }
            eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
            eb.setColor(Color.green);
            eb.setTitle("Profile Information for: **" + mentionedUser.getName() + "**");
            eb.addField("__**Username**__", mentionedUser.getAsMention(), true);
            eb.addField("__**User ID**__", mentionedUser.getId(), true);
            eb.addField("__**Status**__", status, true);
            eb.addField("__**Discriminator**__", mentionedUser.getDiscriminator(), true);
            try {
                eb.addField("__**Ranks**__", roleNames.substring(0, roleNames.length() - 2), true);
            } catch (NullPointerException e) {
                eb.addField("__**Ranks**__", "Error wile getting role names!", true);
            }
            eb.addField("__**Playing**__", (event.getGuild().getMember(mentionedUser).getGame() != null ? event.getGuild().getMember(mentionedUser).getGame().getName() : "Nothing"), true);
            eb.addField("__**Created on**__", mentionedUser.getCreationTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy 'at' HH:mm:ss")), true);
            eb.addField("__**Joined on**__", event.getGuild().getMember(mentionedUser).getJoinDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy 'at' HH:mm:ss")), true);
            if (mentionedUser.isBot() || mentionedUser.isFake()) {
                eb.addField("__**Fake / Bot**__", "Yes", true);
            }
            if (Main.sharding) {
                if (mentionedUser.getId().equals(ShardingManager.getBotID())) {
                    eb.addField("__**Uptime**__", new SimpleDateFormat("HH:mm:ss").format(new Date(new Date().getTime() - Main.uptime - 7200000)), true);
                }
            } else {
                if (mentionedUser.getId().equals(Main.jda.getSelfUser().getId())) {
                    eb.addField("__**Uptime**__", new SimpleDateFormat("HH:mm:ss").format(new Date(new Date().getTime() - Main.uptime - 7200000)), true);
                }
            }
            eb.setThumbnail(mentionedUser.getEffectiveAvatarUrl());
            MessageEmbed embed = eb.build();
            event.getChannel().sendMessage(embed).queue();
        }
    }

    @Override
    public String help() {
        return "Use `" + Reference.COMMAND_PREFIX + "profile` to see your profile info, or mention a user to see their info!";
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String getTag() {
        return "profile";
    }
}
