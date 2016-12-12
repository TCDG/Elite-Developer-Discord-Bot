package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberNickChangeEvent;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class BadUsernameListener {

    public static ArrayList<String> blockedWords = new ArrayList<>();
    private static File blockedNames = new File("blockedNames.txt");

    public static void init() {
        if (!blockedNames.exists()) {
            try {
                blockedNames.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fillList();
    }

    public static void onUserJoin(GuildMemberJoinEvent event) {
        String username = event.getMember().getEffectiveName();
        Iterator<String> iterator = blockedWords.iterator();
        while (iterator.hasNext()) {
            String word = iterator.next();
            if (StringUtils.containsIgnoreCase(username, word)) {
                preformAction(event.getMember().getUser(), event.getGuild());
            }
        }
    }

    public static void onUsernameChange(GuildMemberNickChangeEvent event) {
        String username = event.getMember().getEffectiveName();
        Iterator<String> iterator = blockedWords.iterator();
        while (iterator.hasNext()) {
            String word = iterator.next();
            if (StringUtils.containsIgnoreCase(username, word)) {
                preformAction(event.getMember().getUser(), event.getGuild());
            }
        }
    }

    private static void preformAction(User user, Guild guild) {
        if (!user.hasPrivateChannel()) {
            user.openPrivateChannel().queue(channel -> {
                channel.sendMessage("Your username was deemed inappropriate by staff, please contact staff via Twitter @ScammerSubSSL").queue();
            });
        } else {
            user.getPrivateChannel().sendMessage("Your username was deemed inappropriate by staff, please contact staff via Twitter @ScammerSubSSL").queue();
        }
        guild.getController().ban(user, 1).queue();
        MessageUtils.sendMessageToStaffChat(Main.jda, "``" + user.getName() + "``" + " had a bad username and has been banned.");
        BotLogger.log("Bad Username", user.getName() + " had a bad username and has been banned.");
    }

    private static void fillList() {
        if (blockedWords.isEmpty()) {
            try {
                Scanner scanner = new Scanner(blockedNames);
                while (scanner.hasNextLine()) {
                    blockedWords.add(scanner.nextLine());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isBadUsername(User user){
        String username = user.getName();
        Iterator<String> iterator = blockedWords.iterator();
        while (iterator.hasNext()) {
            String word = iterator.next();
            if (StringUtils.containsIgnoreCase(username, word)) {
                return true;
            }
        }
        return false;
    }
}
