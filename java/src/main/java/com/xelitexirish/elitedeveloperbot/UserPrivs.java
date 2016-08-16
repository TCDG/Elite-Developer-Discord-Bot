package com.xelitexirish.elitedeveloperbot;

import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.JsonReader;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class UserPrivs {

    private static ArrayList<User> adminUsers = new ArrayList<>();

    public static void setupUsers() {
        addDefaultUsers();
    }

    public static boolean isUserAdmin(User user) {
        if (adminUsers.contains(user)) {
            return true;
        } else {
            hasPermission(user, Permission.MANAGE_SERVER);
        }
        return false;
    }

    public static boolean isUserStaff(User user) {
        for (Role role : user.getJDA().getGuildById(Constants.SCAMMER_SUB_LOUNGE_ID).getRolesForUser(user)) {
            if(role.getId().equalsIgnoreCase(Constants.ROLE_STAFF_ID)) {
                return true;
            }
        }
        return false;
    }

    public static void addDefaultUsers() {

        try {
            JSONObject jsonObject = JsonReader.readJsonFromUrl(Constants.ADMIN_USERS_URL);
            JSONArray jsonArray = jsonObject.getJSONArray("adminUsers");
            if (jsonArray != null) {
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(x);

                    String id = String.valueOf(jsonItem.get("id"));

                    adminUsers.add(Main.jda.getUserById(id));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasPermission(User user, Permission permission) {
        for (Role role : user.getJDA().getGuildById(Constants.SCAMMER_SUB_LOUNGE_ID).getRolesForUser(user)) {
            return role.hasPermission(permission);
        }
        return false;
    }

    public static ArrayList<User> getAllStaff() {
        ArrayList<User> allStaff = new ArrayList<>();
        for (User user : Main.jda.getGuildById(Constants.SCAMMER_SUB_LOUNGE_ID).getUsers()) {
            if (isUserStaff(user)) {
                allStaff.add(user);
            }
        }
        return allStaff;
    }
}
