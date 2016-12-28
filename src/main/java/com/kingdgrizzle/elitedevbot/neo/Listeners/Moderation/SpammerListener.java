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
 * File Created @ [ 27.12.2016, 18:43 (GMT +02) ]
 */
package com.kingdgrizzle.elitedevbot.neo.Listeners.Moderation;

import com.kingdgrizzle.elitedevbot.neo.Utils.BotLogger;
import com.kingdgrizzle.elitedevbot.neo.Utils.Reference;
import com.kingdgrizzle.elitedevbot.neo.Utils.UserPrivs;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;

import java.util.ArrayList;
import java.util.List;

public class SpammerListener {

    private static List<User> spammerUsers = new ArrayList<>();

    public static void init() {

    }

    public static void onUserJoin(GuildMemberJoinEvent event){
        if (spammerUsers.contains(event.getMember().getUser())) {
            for (Role role : event.getGuild().getRoles()) {
                if (role.getId().equalsIgnoreCase(Reference.ROLE_MUTED_ID)) {
                    if (UserPrivs.hasPermission(event.getJDA().getSelfUser(), Permission.MANAGE_ROLES)) {
                        event.getJDA().getGuildById(Reference.DISCORD_SERVER_ID).getController().addRolesToMember(event.getMember(), event.getGuild().getRoleById(Reference.ROLE_MUTED_ID)).queue();
                    }
                    event.getGuild().getPublicChannel().sendMessage("Oi " + event.getMember().getAsMention() + " your still in spammers! Don't try to evade punishments").queue();
                }
            }
        }
    }

    public static void onRoleAdded(GuildMemberRoleAddEvent event) {
        if (!spammerUsers.contains(event.getMember().getUser())) {
            for (Role role : event.getGuild().getMember(event.getMember().getUser()).getRoles()) {
                if (role.getId().equalsIgnoreCase(Reference.ROLE_MUTED_ID)) {
                    spammerUsers.add(event.getMember().getUser());
                    BotLogger.info("Put in spammers " + event.getMember().getEffectiveName() + " has been put in spammers");
                }
            }
        }
    }

    public static void onRoleRemoved(GuildMemberRoleRemoveEvent event){
        for (Role role : event.getGuild().getMember(event.getMember().getUser()).getRoles()){
            if (role.getId().equalsIgnoreCase(Reference.ROLE_MUTED_ID)){
                spammerUsers.remove(event.getMember().getUser());
                BotLogger.info("Removed from spammers " + event.getMember().getEffectiveName() + " has been removed from spammers");
            }
        }
    }
}
