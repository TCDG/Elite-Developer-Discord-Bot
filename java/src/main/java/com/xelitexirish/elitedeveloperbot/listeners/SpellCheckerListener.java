package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.Main;
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

    private static final List<String> baseWords = new ArrayList<>();
    private static final List<String> replaceWords = new ArrayList<>();

    public static void init(){
        fillLists();
    }

    public static void handleMessage(MessageReceivedEvent event) {
        User sender = event.getAuthor();
        String[] messageSplit = event.getMessage().toString().split(" ");

        for (int x = 0; x < messageSplit.length; x++) {
            String word = baseWords.get(x);
            if (baseWords.contains(word.toLowerCase())) {
                // Tell user
                notifyUser(sender, word, replaceWords.get(x));
            }else if (baseWords.contains(word.toUpperCase())){
                notifyUser(sender, word, replaceWords.get(x));
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

                    String baseWord = jsonItem.getString("baseWord");
                    String dixordWord = String.valueOf(jsonItem.get("dixordWord"));

                    baseWords.add(baseWord);
                    replaceWords.add(dixordWord);
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
