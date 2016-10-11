package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.JsonReader;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpellCheckerListener {

    private static File userBlacklist = new File("user_blacklist.json");
    private static final List<String> blackListUsers = new ArrayList<>();
    private static final List<String> replaceWords = new ArrayList<>();

    public static void init() {
        fillWordLists();
        blackListUsers.clear();
        loadBlackListData();
    }

    public static void handleMessage(MessageReceivedEvent event) {
        User sender = event.getAuthor();
        String[] messageSplit = event.getMessage().getContent().split(" ");

        if (!replaceWords.isEmpty()) {
            if (!blackListUsers.contains(event.getAuthor().getId())) {
                for (int x = 0; x < messageSplit.length; x++) {
                    String word = messageSplit[x];

                    for (int y = 0; y < replaceWords.size(); y++) {
                        String[] lineParts = replaceWords.get(y).split("-");
                        if (lineParts[0].equalsIgnoreCase(word)) {
                            notifyUser(sender, lineParts[0], lineParts[1]);
                        }
                    }
                }
            }
        }
    }

    private static void fillWordLists() {
        try {
            JSONObject jsonObject = JsonReader.readJsonFromUrl(Constants.DIXCORD_WORDS_URL);

            JSONArray jsonArray = jsonObject.getJSONArray("dixordWords");
            if (jsonArray != null) {
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(x);

                    String replaceWord = jsonItem.getString("dixcordWord");

                    replaceWords.add(replaceWord);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void notifyUser(User user, String baseWord, String dixordWord) {
        try {
            String message = "Oi you spelt " + baseWord + " wrong, it's spelt " + dixordWord;
            String blockMessage = "\n You can blacklist yourself from these messages by entering '" + Constants.COMMAND_PREFIX + "correction false' in chat";

            user.getPrivateChannel().sendMessage(message + blockMessage);
        } catch (Exception e) {
        }
    }

    public static void reloadLists() {
        replaceWords.clear();
        blackListUsers.clear();
        fillWordLists();
        loadBlackListData();
    }

    public static void blockUser(Guild guild, User user, Boolean privateMsg) {
        String userId = user.getId();

        blackListUsers.clear();
        loadBlackListData();

        if (blackListUsers.contains(userId)) {
            user.getPrivateChannel().sendMessage("You are already blacklisted to receive spell check messages.  Use '" + Constants.COMMAND_PREFIX + "correction true' to unblock yourself");

        } else {
            blackListUsers.add(userId);
            user.getPrivateChannel().sendMessage("You are now on the bot blacklist. Use '" + Constants.COMMAND_PREFIX + "correction false' to unblock yourself.");
            if(privateMsg){
                user.getPrivateChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("User setting updated"));
            }else{
                guild.getPublicChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("User setting updated"));
            }

        }

        writeBlacklist();
    }

    public static void unblockUser(Guild guild, User user, Boolean privateMsg) {
        String userId = user.getId();

        blackListUsers.clear();
        loadBlackListData();

        if (blackListUsers.contains(userId)) {
            blackListUsers.remove(userId);
            user.getPrivateChannel().sendMessage("You are now removed from the bot blacklist. Use '" + Constants.COMMAND_PREFIX + "correction false' to block yourself.");
            if(privateMsg){
                user.getPrivateChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("User setting updated"));
            }else{
                guild.getPublicChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("User setting updated"));
            }
        } else {
            user.getPrivateChannel().sendMessage("You are currently not on the blacklist. Use '" + Constants.COMMAND_PREFIX + "correction false' to block yourself.");
        }
        writeBlacklist();
    }

    private static void loadBlackListData() {
        if (userBlacklist.exists()) {
            JSONParser parser = new JSONParser();

            try {
                Object obj = parser.parse(new FileReader(userBlacklist));
                org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;

                org.json.simple.JSONArray arrayBlacklist = (org.json.simple.JSONArray) jsonObject.get("arrayBlacklist");
                if (arrayBlacklist != null) {
                    Iterator<String> iterator = arrayBlacklist.iterator();
                    while (iterator.hasNext()){
                        blackListUsers.add(iterator.next());
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            writeBlacklist();
        }
    }

    private static void writeBlacklist() {
        try {

            if (!userBlacklist.exists()) {
                userBlacklist.createNewFile();
            }
            JSONObject jsonObject = new JSONObject();
            JSONArray arrayBlacklist = new JSONArray();
            jsonObject.put("arrayBlacklist", arrayBlacklist);
            for (int x = 0; x < blackListUsers.size(); x++) {
                String line = blackListUsers.get(x);

                arrayBlacklist.put(line);
            }

            FileWriter fileWriter = new FileWriter(userBlacklist);
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
