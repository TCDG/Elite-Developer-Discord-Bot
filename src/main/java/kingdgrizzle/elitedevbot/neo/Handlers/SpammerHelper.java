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
 * File Created @ [ 29.12.2016, 18:39 (GMT +02) ]
 */
package kingdgrizzle.elitedevbot.neo.Handlers;

import kingdgrizzle.elitedevbot.neo.Utils.BotLogger;
import kingdgrizzle.elitedevbot.neo.Utils.Reference;
import kingdgrizzle.elitedevbot.neo.Utils.UserPrivs;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpammerHelper {

    private static List<User> spammerUsers = new ArrayList<>();
    private static File spammerUsersFile = new File("spammers.json");

    public static void onUserJoin(GuildMemberJoinEvent event){
        if (spammerUsers.contains(event.getMember().getUser())) {
            // Is a spammer
            for (Role role : event.getGuild().getRoles()) {
                if (role.getId().equalsIgnoreCase(Reference.ROLE_MUTED_ID)) {
                    if (UserPrivs.hasPermission(event.getJDA().getSelfUser(), Permission.MANAGE_ROLES)) {
                        event.getJDA().getGuildById(Reference.DISCORD_SERVER_ID).getController().addRolesToMember(event.getMember(), event.getGuild().getRoleById(Reference.ROLE_MUTED_ID));
                    }
                    event.getGuild().getPublicChannel().sendMessage("Oi " + event.getMember().getAsMention() + " your still in spammers! Don't try to evade punishments");
                }
            }
        }
    }

    public static void onRoleAdded(GuildMemberRoleAddEvent event) {
        if (!spammerUsers.contains(event.getMember().getUser())) {
            for (Role role : event.getGuild().getMember(event.getMember().getUser()).getRoles()) {
                if (role.getId().equalsIgnoreCase(Reference.ROLE_MUTED_ID)) {
                    spammerUsers.add(event.getMember().getUser());
                    BotLogger.info("Put in spammers: " + event.getMember().getEffectiveName() + " has been put in spammers");
                }
            }
        }
    }

    public static void onRoleRemoved(GuildMemberRoleRemoveEvent event){
        for (Role role : event.getGuild().getMember(event.getMember().getUser()).getRoles()){
            if(role.getId().equalsIgnoreCase(Reference.ROLE_MUTED_ID)){
                spammerUsers.remove(event.getMember().getUser());
                BotLogger.info("Removed from spammers: " + event.getMember().getEffectiveName() + " has been removed from spammers");
            }
        }
    }

    public static void reloadSpammerData() {
        spammerUsers.clear();
        loadSpammerData();
    }

    private static void loadSpammerData() {

    }

    private static void writeSpammerData() {
        try {
            if (!spammerUsersFile.exists()) {
                spammerUsersFile.createNewFile();
            }
            JSONObject jsonObject = new JSONObject();
            JSONArray arraySpammmerID = new JSONArray();
            jsonObject.put("spammerID", arraySpammmerID);
            for (int x = 0; x < spammerUsers.size(); x++) {
                User line = spammerUsers.get(x);
                arraySpammmerID.put(line.getId());
            }
            FileWriter fileWriter = new FileWriter(spammerUsersFile);
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            BotLogger.debug("Error writing to JSON!", e);
        }
    }
}
