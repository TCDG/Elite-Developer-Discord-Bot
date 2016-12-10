package com.xelitexirish.elitedeveloperbot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WarningHandler {

    private static final File userWarningList = new File("UserWarningList.json");
    public static List<String> warnedUsers = new ArrayList<>();

    public static void init() {

    }

    public static void warnUser(String userId) {

        warnedUsers.clear();
        loadWarningData();

        for (int x = 0; x < warnedUsers.size(); x++) {
            String[] warnedParts = warnedUsers.get(x).split("-");

            if(warnedParts[0].equalsIgnoreCase(userId)){
                int currentWarnings = getUserWarnings(userId);
                int newWarning = currentWarnings + 1;

                warnedUsers.remove(x);
                warnedUsers.add(userId + "-" + newWarning);
            }
        }
        writeWarningData();
    }

    public static void unWarnUser(String userId){
        warnedUsers.clear();
        loadWarningData();

        for(int x = 0; x < warnedUsers.size(); x++){
            String[] lineParts = warnedUsers.get(x).split("-");

            if(lineParts[0].equalsIgnoreCase(userId)){
                int currentWarnings = getUserWarnings(userId);
                int newWarnings = currentWarnings - 1;

                warnedUsers.remove(x);
                warnedUsers.add(userId + "-" + newWarnings);
            }
        }
    }

    public static int getUserWarnings(String userId) {
        for (int x = 0; x < warnedUsers.size(); x++) {
            String[] lineSplit = warnedUsers.get(x).split("-");

            if (lineSplit[0].equalsIgnoreCase(userId)) {
                int warnings = Integer.parseInt(lineSplit[1]);
                return warnings;
            }
        }
        return 0;
    }

    private static void loadWarningData() {
        if (userWarningList.exists()) {
            JSONParser parser = new JSONParser();

            try {
                Object obj = parser.parse(new FileReader(userWarningList));
                JSONObject jsonObject = (JSONObject) obj;

                JSONArray jsonArray = (JSONArray) jsonObject.get("arrayWarnings");
                if (jsonArray != null) {
                    Iterator<String> iterator = jsonArray.iterator();
                    while (iterator.hasNext()) {
                        warnedUsers.add(iterator.next());
                    }
                }

            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
        } else {
            writeWarningData();
        }
    }

    private static void writeWarningData() {
        try {
            if (!userWarningList.exists()) {
                userWarningList.createNewFile();
            }
            JSONObject jsonObject = new JSONObject();
            JSONArray arrayWarnedUsers = new JSONArray();

            jsonObject.put("arrayWarnings", arrayWarnedUsers);
            for (int x = 0; x < warnedUsers.size(); x++) {
                String line = warnedUsers.get(x);
                arrayWarnedUsers.add(line);
            }

            FileWriter fileWriter = new FileWriter(userWarningList);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
