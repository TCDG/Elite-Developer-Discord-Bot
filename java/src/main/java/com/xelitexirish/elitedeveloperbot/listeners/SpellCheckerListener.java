package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.JsonReader;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpellCheckerListener {

    private static final List<String> replaceWords = new ArrayList<>();

    public static void init() {
        fillLists();
    }

    public static void handleMessage(MessageReceivedEvent event) {
        fillLists();
        User sender = event.getAuthor();
        String[] messageSplit = event.getMessage().getContent().split(" ");

        if (!replaceWords.isEmpty()) {
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

    private static void fillLists() {
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
        String message = "Oi you spelt " + baseWord + " wrong, it's spelt " + dixordWord;

        user.getPrivateChannel().sendMessage(message);
    }
}
