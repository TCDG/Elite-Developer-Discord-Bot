package com.xelitexirish.elitedeveloperbot;

import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.JsonReader;
import net.dv8tion.jda.entities.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class UserPrivs {

    private static ArrayList<User> adminUsers = new ArrayList<>();

    public static void setupUsers(){
        addDefaultUsers();
    }

    public static void addUserToAdmin(User user){
        adminUsers.add(user);
    }

    public static boolean isUserAdmin(User user){
        return adminUsers.contains(user);
    }

    public static void addDefaultUsers(){

        try {
            JSONObject jsonObject = JsonReader.readJsonFromUrl(Constants.ADMIN_USERS_URL);
            JSONArray jsonArray = jsonObject.getJSONArray("adminUsers");
            if(jsonArray != null){
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(x);

                    String username = jsonItem.getString("username");
                    String id = String.valueOf(jsonItem.get("id"));

                    adminUsers.add(Main.jda.getUserById(id));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
