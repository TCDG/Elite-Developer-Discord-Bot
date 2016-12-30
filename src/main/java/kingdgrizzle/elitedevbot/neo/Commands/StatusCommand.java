package kingdgrizzle.elitedevbot.neo.Commands;

import kingdgrizzle.elitedevbot.neo.API.ShardingManager;
import kingdgrizzle.elitedevbot.neo.Main;
import kingdgrizzle.elitedevbot.neo.Utils.Reference;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
 * File Created @ [ 30.12.2016, 12:16 (GMT +02) ]
 */
public class StatusCommand implements ICommand {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String status = "";
        if (Main.sharding) {
            switch (ShardingManager.shards.get(0).getPresence().getStatus()) {
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
        } else {
            switch (Main.jda.getPresence().getStatus()) {
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
        }
        String roleNames = "";
        if (Main.sharding) {
            for (Role role : event.getGuild().getMemberById(ShardingManager.getBotID()).getRoles()) {
                roleNames += role.getName() + ", ";
            }
        } else {
            for (Role role : event.getGuild().getMemberById(Main.jda.getSelfUser().getId()).getRoles()) {
                roleNames += role.getName() + ", ";
            }
        }
        String shardId = "";
        String shardTotal = "";
        if (Main.sharding) {
            shardId = "" + (event.getGuild().getMemberById(ShardingManager.getBotID()).getJDA().getShardInfo().getShardId() + 1);
            shardTotal = "" + event.getGuild().getMemberById(ShardingManager.getBotID()).getJDA().getShardInfo().getShardTotal();
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
        eb.setColor(Color.green);
        eb.setTitle("Bot Informations");
        if (Main.sharding) {
            eb.addField("__**Bots Name**__", ShardingManager.getBotMention(), true);
            eb.addField("__**Bots ID**__", ShardingManager.getBotID(), true);
            eb.addField("__**Bots Status**__", status, true);
            eb.addField("__**Bots Discriminator**__", ShardingManager.getBotDiscriminator(), true);
            try {
                eb.addField("__**Ranks**__", roleNames.substring(0, roleNames.length() - 2), true);
            } catch (NullPointerException e) {
                eb.addField("__**Ranks**__", "Error wile getting role names!", true);
            }
            eb.addField("__**Playing**__", (event.getGuild().getMemberById(ShardingManager.getBotID()).getGame() != null ? event.getGuild().getMemberById(ShardingManager.getBotID()).getGame().getName() : "Nothing"), true);
            eb.addField("__**Created on**__", ShardingManager.shards.get(0).getSelfUser().getCreationTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy 'at' HH:mm:ss")), true);
            eb.addField("__**Joined on**__", event.getGuild().getMemberById(ShardingManager.getBotID()).getJoinDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy 'at' HH:mm:ss")), true);
            eb.addField("__**Uptime**__", new SimpleDateFormat("HH:mm:ss").format(new Date(new Date().getTime() - Main.uptime - 7200000)), true);
            eb.addField("__**Using Shards**__", "Yes", true);
            eb.addField("__**Shard ID**__", shardId, true);
            eb.addField("__**Total Shards**__", shardTotal, true);
            eb.setThumbnail(ShardingManager.getBotAvatar());
        } else {
            eb.addField("__**Bots Name**__", Main.jda.getSelfUser().getAsMention(), true);
            eb.addField("__**Bots ID**__", Main.jda.getSelfUser().getId(), true);
            eb.addField("__**Bots Status**__", status, true);
            eb.addField("__**Bots Discriminator**__", Main.jda.getSelfUser().getDiscriminator(), true);
            try {
                eb.addField("__**Ranks**__", roleNames.substring(0, roleNames.length() - 2), true);
            } catch (NullPointerException e) {
                eb.addField("__**Ranks**__", "Error wile getting role names!", true);
            }
            eb.addField("__**Playing**__", (Main.jda.getPresence().getGame() != null ? Main.jda.getPresence().getGame().getName() : "Nothing"), true);
            eb.addField("__**Created on**__", Main.jda.getSelfUser().getCreationTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy 'at' HH:mm:ss")), true);
            eb.addField("__**Joined on**__", event.getGuild().getMemberById(Main.jda.getSelfUser().getId()).getJoinDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy 'at' HH:mm:ss")), true);
            eb.addField("__**Uptime**__", new SimpleDateFormat("HH:mm:ss").format(new Date(new Date().getTime() - Main.uptime - 7200000)), true);
            eb.setThumbnail(Main.jda.getSelfUser().getEffectiveAvatarUrl());
        }
        MessageEmbed embed = eb.build();
        event.getTextChannel().sendMessage(embed).queue();
    }

    @Override
    public String help() {
        return "Shows some information about the bot!";
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {}

    @Override
    public String getTag() {
        return "status";
    }
}
