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
 * File Created @ [ 27.12.2016, 13:03 (GMT +02) ]
 */
package com.kingdgrizzle.elitedevbot.neo.Listeners;

import com.kingdgrizzle.elitedevbot.neo.Main;
import com.kingdgrizzle.elitedevbot.neo.Utils.BotLogger;
import com.kingdgrizzle.elitedevbot.neo.Utils.MessageUtils;
import com.kingdgrizzle.elitedevbot.neo.Utils.Reference;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Icon;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.io.File;
import java.io.IOException;

public class PrivateMessageListener {

    public static void privateMessageReceived(PrivateMessageReceivedEvent par1Event) {
        if (par1Event.getAuthor().getId().equals(Reference.USER_ID_BOT_OWNER) || par1Event.getAuthor().getId().equals(Reference.KING_ID)) {
            String msg = par1Event.getMessage().getRawContent();
            if (msg.startsWith("playing")) {
                String[] msgSplit = msg.split(" ");
                StringBuilder sb = new StringBuilder();
                for (int x = 1; x < msgSplit.length; x++) {
                    sb.append(msgSplit[x] + " ");
                }
                if (Main.debugMode = true) {
                    BotLogger.pm(sb.toString());
                }
                par1Event.getJDA().getPresence().setGame(Game.of(sb.toString()));
            } else if(msg.startsWith("setname")) {
                String[] msgSplit = msg.split(" ");
                StringBuilder sb = new StringBuilder();
                for (int x = 1; x <msgSplit.length; x++) {
                    sb.append(msgSplit[x] + " ");
                }
                if (Main.debugMode = true) {
                    BotLogger.pm(sb.toString());
                }
                Main.jda.getSelfUser().getManager().setName(sb.toString()).queue();
            } else if (msg.startsWith("image")) {
                File icon = new File("icon.jpg");
                try {
                    Main.jda.getSelfUser().getManager().setAvatar(Icon.from(icon)).queue();
                } catch (IOException e) {
                    BotLogger.debug("Error while setting Icon", e);
                }
            } else if(msg.equals("servers")) {
                StringBuilder sb = new StringBuilder();
                sb.append("I'm currently on the following " + par1Event.getJDA().getGuilds().size() + " guilds:\n");
                for (Guild guild : par1Event.getJDA().getGuilds()) {
                    sb.append("\t-" + guild.getName() + "\n");
                }
                par1Event.getAuthor().openPrivateChannel().queue(channel -> channel.sendMessage(MessageUtils.wrapMessageInEmbed(sb.toString())).queue());
            } else if (msg.equals("reload")) {
                Main.commands.clear();
                Main.registerCommands();
                par1Event.getAuthor().openPrivateChannel().queue(channel -> channel.sendMessage("Reloaded Commands!").queue());
            } else {
                if (par1Event.getAuthor().getId().equals(Reference.KING_ID) || par1Event.getAuthor().getId().equals(Reference.USER_ID_BOT_OWNER)) {
                    for (Guild guild : par1Event.getJDA().getGuilds()) {
                        guild.getPublicChannel().sendMessage(msg).queue();
                    }
                }

            }
        } else {
            String msg = par1Event.getMessage().getRawContent();
            if (msg.startsWith("report")) {
                String[] msgSplit = msg.split(" ");
                StringBuilder sb = new StringBuilder();
                for (int x = 1; x < msgSplit.length; x++) {
                    sb.append(msgSplit[x] + " ");
                }
//                for (User staff : UserPrivs.getAllStaff()) {
//                    staff.openPrivateChannel().queue(channel -> channel.sendMessage(par1Event.getAuthor().getName() + " has sent the following report: " + sb.toString()).queue());
//                }
                par1Event.getAuthor().openPrivateChannel().queue(channel -> channel.sendMessage("Thank you for submitting your report. Staff will get back to you shortly!").queue());
            } else {
                if (!par1Event.getAuthor().getId().equals(Main.jda.getSelfUser().getId())) {
                    BotLogger.pm(par1Event.getAuthor().getName() + "sent me this PM: " + msg);
                }
            }
        }
    }
}
