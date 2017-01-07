package TCDG.EliteDevBot.Neo.Listeners;

import TCDG.EliteDevBot.Neo.Utils.JSONReader;
import TCDG.EliteDevBot.Neo.Utils.Reference;
import TCDG.EliteDevBot.Neo.Utils.MessageUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
 * File Created @ [ 29.12.2016, 10:15 (GMT +02) ]
 */
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
            JSONObject jsonObject = JSONReader.readJSONFromURL(Reference.DIXCORD_WORDS_URL);
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
        String message = "Oi you spelt " + baseWord + " wrong, it's spelt " + dixordWord;
        String blockMessage = "\n\tYou can blacklist yourself from these messages by entering `" + Reference.COMMAND_PREFIX + "correction false` in chat";
        user.openPrivateChannel().queue(c -> c.sendMessage(MessageUtils.wrapMessageInEmbed(Color.gray, message + blockMessage)).queue());
    }

    public static void reloadLists() {
        replaceWords.clear();
        blackListUsers.clear();
        fillWordLists();
        loadBlackListData();
    }

    public static void blockUser(Guild guild, User user) {
        String userId = user.getId();
        blackListUsers.clear();
        loadBlackListData();
        if (blackListUsers.contains(userId)) {
            user.openPrivateChannel().queue(c -> c.sendMessage("You are already blacklisted to receive spell check messages.  Use '" + Reference.COMMAND_PREFIX + "correction true' to unblock yourself").queue());
        } else {
            blackListUsers.add(userId);
            user.openPrivateChannel().queue(c -> c.sendMessage("You are now on the bot blacklist. Use '" + Reference.COMMAND_PREFIX + "correction false' to unblock yourself.").queue());
            MessageUtils.sendMessageToStaffInfoChat(guild.getJDA(), "User setting updated for " + user.getAsMention() + "\n\tBlocked " + user.getAsMention() + " from the correction");
        }
        writeBlacklist();
    }

    public static void unblockUser(Guild guild, User user) {
        String userId = user.getId();
        blackListUsers.clear();
        loadBlackListData();
        if (blackListUsers.contains(userId)) {
            blackListUsers.remove(userId);
            user.openPrivateChannel().queue(c -> c.sendMessage("You are now removed from the bot blacklist. Use '" + Reference.COMMAND_PREFIX + "correction false' to block yourself.").queue());
            MessageUtils.sendMessageToStaffInfoChat(guild.getJDA(), "User setting updated for " + user.getName() + "\nUnblocked " + user.getAsMention() + " from the correction blacklist");
        } else {
            user.openPrivateChannel().queue(c -> c.sendMessage("You are currently not on the blacklist. Use '" + Reference.COMMAND_PREFIX + "correction false' to block yourself.").queue());
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
            } catch (ParseException | IOException e) {
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