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
 * File Created @ [ 27.12.2016, 18:49 (GMT +02) ]
 */
package com.kingdgrizzle.elitedevbot.neo.Utils;

import com.kingdgrizzle.elitedevbot.neo.Main;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class UserPrivs {

    private static ArrayList<User> staffUsers = new ArrayList<>();

    public static void setupUsers() {
        addDefaultUsers();
    }

    static void addDefaultUsers() {
        try {
            JSONObject jsonObject = JSONReader.readJSONFromURL(Reference.ADMIN_USERS_URL);
            JSONArray jsonArray = jsonObject.getJSONArray("adminUsers");
            if (jsonArray != null) {
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(x);
                    String id = String.valueOf(jsonItem.get("id"));
                    staffUsers.add(Main.jda.getUserById(id));
                }
            }
        } catch (IOException e) {
            BotLogger.debug("Error reading the default file!", e);
        }
    }

    public static boolean hasPermission(User par1User, Permission par2Permission) {
        for (Role role : par1User.getJDA().getGuildById(Reference.DISCORD_SERVER_ID).getMember(par1User).getRoles()) {
            return role.hasPermission(par2Permission);
        }
        return false;
    }
}
