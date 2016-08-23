package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.UserPrivs;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberRoleRemoveEvent;

import java.util.ArrayList;
import java.util.List;

public class SpammerHelper {

    private static List<User> spammerUsers = new ArrayList<>();

    public static void onUserJoin(GuildMemberJoinEvent event){
        if(spammerUsers.contains(event.getUser())){
            // Is a spammer
            for(Role role : event.getGuild().getRoles()){
                if(role.getId().equalsIgnoreCase(Constants.ROLE_MUTED_ID)){
                    if(UserPrivs.hasPermission(event.getUser(), Permission.MANAGE_ROLES)) {
                        event.getGuild().getManager().addRoleToUser(event.getUser(), role);
                        event.getGuild().getManager().update();
                    }
                    event.getGuild().getPublicChannel().sendMessage("Oi " + event.getUser().getAsMention() + " your still in spammers! Don't try to evade punishments");

                }
            }
        }
    }

    public static void onRoleAdded(GuildMemberRoleAddEvent event) {
        if(!spammerUsers.contains(event.getUser())) {
            for (Role role : event.getGuild().getRolesForUser(event.getUser())) {
                if (role.getId().equalsIgnoreCase(Constants.ROLE_MUTED_ID)) {
                    spammerUsers.add(event.getUser());

                    BotLogger.log("Put in spammers", event.getUser().getUsername() + " has been put in spammers");
                }
            }
        }
    }

    public static void onRoleRemoved(GuildMemberRoleRemoveEvent event){
        for (Role role : event.getGuild().getRolesForUser(event.getUser())){
            if(role.getId().equalsIgnoreCase(Constants.ROLE_MUTED_ID)){
                spammerUsers.remove(event.getUser());

                BotLogger.log("Removed from spammers", event.getUser().getUsername() + " has been removed from spammers");
            }
        }
    }

}
