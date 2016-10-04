package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.user.UserNameUpdateEvent;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class BadUsernameListener {

    private static ArrayList<String> blockedWords = new ArrayList<>();
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
        String username = event.getUser().getUsername();

        Iterator<String> iterator = blockedWords.iterator();
        while (iterator.hasNext()) {
            String word = iterator.next();

            if (StringUtils.containsIgnoreCase(username, word)) {
                preformAction(event.getUser(), event.getGuild());
            }
        }
    }

    public static void onUsernameChange(UserNameUpdateEvent event) {

        String username = event.getUser().getUsername();

        Iterator<String> iterator = blockedWords.iterator();
        while (iterator.hasNext()) {
            String word = iterator.next();

            if (StringUtils.containsIgnoreCase(username, word)) {

            }
        }
    }

    private static void preformAction(User user, Guild guild) {
        user.getPrivateChannel().sendMessage("Your username was deemed inappropriate by staff, please change it and re-join");
        guild.getManager().ban(user, 1);
        guild.getManager().update();

        MessageUtils.sendMessageToStaffChat(Main.jda, "``" + user.getUsername() + "``" + " had a bad username and has been banned.");
        BotLogger.log("Bad Username", user.getUsername() + " had a bad username and has been banned.");
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
}
