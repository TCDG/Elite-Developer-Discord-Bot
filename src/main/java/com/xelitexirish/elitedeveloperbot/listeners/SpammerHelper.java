package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.UserPrivs;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;

import java.util.ArrayList;
import java.util.List;

public class SpammerHelper {

    private static List<User> spammerUsers = new ArrayList<>();

    public static void onUserJoin(GuildMemberJoinEvent event){
        if (spammerUsers.contains(event.getMember().getUser())) {
            // Is a spammer
            for (Role role : event.getGuild().getRoles()) {
                if (role.getId().equalsIgnoreCase(Constants.ROLE_MUTED_ID)) {
                    if (UserPrivs.hasPermission(event.getJDA().getSelfUser(), Permission.MANAGE_ROLES)) {
                        event.getJDA().getGuildById(Constants.DISCORD_SERVER_ID).getController().addRolesToMember(event.getMember(), event.getGuild().getRoleById(Constants.ROLE_MUTED_ID));
                    }
                    event.getGuild().getPublicChannel().sendMessage("Oi " + event.getMember().getAsMention() + " your still in spammers! Don't try to evade punishments");
                }
            }
        }
    }

    public static void onRoleAdded(GuildMemberRoleAddEvent event) {
        if (!spammerUsers.contains(event.getMember().getUser())) {
            for (Role role : event.getGuild().getMember(event.getMember().getUser()).getRoles()) {
                if (role.getId().equalsIgnoreCase(Constants.ROLE_MUTED_ID)) {
                    spammerUsers.add(event.getMember().getUser());
                    BotLogger.log("Put in spammers", event.getMember().getEffectiveName() + " has been put in spammers");
                }
            }
        }
    }

    public static void onRoleRemoved(GuildMemberRoleRemoveEvent event){
        for (Role role : event.getGuild().getMember(event.getMember().getUser()).getRoles()){
            if(role.getId().equalsIgnoreCase(Constants.ROLE_MUTED_ID)){
                spammerUsers.remove(event.getMember().getUser());
                BotLogger.log("Removed from spammers", event.getMember().getEffectiveName() + " has been removed from spammers");
            }
        }
    }

}
