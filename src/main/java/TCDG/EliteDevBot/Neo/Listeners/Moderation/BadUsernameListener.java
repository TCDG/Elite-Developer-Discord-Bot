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
 * File Created @ [ 27.12.2016, 18:03 (GMT +02) ]
 */
package TCDG.EliteDevBot.Neo.Listeners.Moderation;

import TCDG.EliteDevBot.Neo.Utils.BotLogger;
import TCDG.EliteDevBot.Neo.Utils.Reference;
import TCDG.EliteDevBot.Neo.Main;
import TCDG.EliteDevBot.Neo.Utils.MessageUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberNickChangeEvent;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class BadUsernameListener {

    static ArrayList<String> blockedWords = new ArrayList<>();
    private static File blockedNamesFile = new File("blockedNames.txt");

    public static void init() {
        if (!blockedNamesFile.exists()) {
            try {
                blockedNamesFile.createNewFile();
            } catch (IOException e) {
                BotLogger.debug("Error creating the blocked name file", e);
            }
        }
        fillList();
    }

    static void onGuildMemberJoin(GuildMemberJoinEvent par1Event) {
        String effectiveName = par1Event.getMember().getEffectiveName();
        Iterator<String> iterator = blockedWords.iterator();
        while (iterator.hasNext()) {
            String blockedWordIteration = iterator.next();
            if (StringUtils.containsIgnoreCase(effectiveName, blockedWordIteration)) {
                performAction(par1Event.getMember().getUser(), par1Event.getGuild(), par1Event.getJDA());
            }
        }
    }

    static void onGuildMemberNickChange(GuildMemberNickChangeEvent par1Event) {
        String effectiveName = par1Event.getMember().getEffectiveName();
        Iterator<String> iterator = blockedWords.iterator();
        while (iterator.hasNext()) {
            String blockedWordIteration = iterator.next();
            if (StringUtils.containsIgnoreCase(effectiveName, blockedWordIteration)) {
                performAction(par1Event.getMember().getUser(), par1Event.getGuild(), par1Event.getJDA());
            }
        }
    }

    private static void performAction(User par1User, Guild par2Guild, JDA jda) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
        eb.setFooter(Reference.EMBED_FOOTER_NAME, Reference.EMBED_FOOTER_IMAGE);
        eb.setColor(Color.red);
        eb.setTitle("Warning");
        eb.setDescription("Your username has been deemed innapropriate by Staff, please change your name and join again!");
        MessageEmbed embed = eb.build();
        par1User.openPrivateChannel().queue(channel -> channel.sendMessage(embed).queue());
        par2Guild.getController().kick(par1User.getId()).queue();
        MessageUtils.sendMessageToStaffInfoChat(jda, "`" + par1User.getName() + "`" + " had a bad username and has been kicked.");
    }

    private static void fillList() {
        if (blockedWords.isEmpty()) {
            try {
                Scanner scanner = new Scanner(blockedNamesFile);
                while (scanner.hasNextLine()) {
                    blockedWords.add(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                BotLogger.debug("Couldn't find the file " + blockedNamesFile, e);
            }
        }
    }

    static boolean isBadUsername(User par1User) {
        String name = par1User.getName();
        Iterator<String> iterator = blockedWords.iterator();
        while (iterator.hasNext()) {
            String blockedWordIteration = iterator.next();
            if (StringUtils.containsIgnoreCase(name, blockedWordIteration)) {
                return true;
            }
        }
        return false;
    }
}
