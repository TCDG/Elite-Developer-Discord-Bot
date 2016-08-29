package com.xelitexirish.elitedeveloperbot.listeners;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.user.UserNameUpdateEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class BadUsernameListener {

    private static ArrayList<String> blockedWords = new ArrayList<>();

    public static void onUserJoin(GuildMemberJoinEvent event){
        String username = event.getUser().getUsername();

        Iterator<String> iterator = blockedWords.iterator();
        while (iterator.hasNext()){
            String word = iterator.next();

            if(StringUtils.containsIgnoreCase(username, word)){
                // GO
            }
        }
    }

    public static void onUsernameChange(UserNameUpdateEvent event){

        String username = event.getUser().getUsername();

        Iterator<String> iterator = blockedWords.iterator();
        while (iterator.hasNext()){
            String word = iterator.next();

            if(StringUtils.containsIgnoreCase(username, word)){
                preformAction(event.getUser(), event.);
            }
        }
    }

    private static void preformAction(User user, Guild guild){

    }
}
