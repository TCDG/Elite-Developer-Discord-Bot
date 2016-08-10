package com.xelitexirish.elitedeveloperbot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WarningHandler {

    private static final File userList = new File("UserWarningList.json");
    private static JSONObject jsonObject = new JSONObject();
    private static JSONArray jsonArrayUsers = new JSONArray();

    public static void setup() {

        if (doesJsonExist()) {
            readJsonArray();
        } else {
            // Lets set it up
            jsonObject.put(jsonArrayUsers, "userWarnings");
        }
    }

    public static int getUserWarnings(String username){
        for(int x = 0; x < jsonArrayUsers.size(); x++){
            String text = jsonArrayUsers.get(x).toString();
            String[] textSplit = text.split("-");

            if(textSplit[0].equalsIgnoreCase(username)){
                return Integer.parseInt(textSplit[1]);
            }
        }
        return 0;
    }

    public static void addWarning(String username){
        readJsonArray();
        if(!jsonArrayUsers.isEmpty()) {
            for (int x = 0; x < jsonArrayUsers.size(); x++) {
                String text = jsonArrayUsers.get(x).toString();
                String[] textSplit = text.split("-");

                // Has the user been warned
                if (getUserWarnings(username) == 0) {
                    jsonArrayUsers.add(username + "-" + "1");

                } else if (getUserWarnings(username) >= 0) {
                    int warnings = Integer.parseInt(textSplit[1]);
                    jsonArrayUsers.remove(x);
                    int newWarnings = warnings + 1;
                    jsonArrayUsers.add(username + "-" + newWarnings++);
                }
            }
        }else {
            jsonArrayUsers.add(username + "-" + "1");
        }
        writeJsonToFile();
    }

    private static void readJsonArray() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(userList));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray users = (JSONArray) jsonObject.get("userWarnings");
            jsonArrayUsers = users;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void writeJsonToFile() {
        try {
            FileWriter fileWriter = new FileWriter(userList);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean doesJsonExist() {
        return userList.exists();
    }
}
