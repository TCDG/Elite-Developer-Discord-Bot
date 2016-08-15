package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.utils.Constants;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

public class PrivateMessageListener {

    public static void privateMessageReciever(PrivateMessageReceivedEvent event){
        if (event.getAuthor().getId().equals(Constants.USER_ID_XELITEXIRISH)) {
            String message = event.getMessage().getContent();

            if (message.startsWith("playing")) {
                String[] messageSplit = message.split(" ");
                StringBuilder playingBuilder = new StringBuilder();
                for (int x = 1; x < messageSplit.length; x++) {
                    playingBuilder.append(messageSplit[x] + " ");
                }
                event.getJDA().getAccountManager().setGame(playingBuilder.toString());
            }else {

                for (Guild guild : event.getJDA().getGuilds()) {
                    if (guild.getId().equals(Constants.SCAMMER_SUB_LOUNGE_ID)) {
                        guild.getPublicChannel().sendMessage(message);
                    }
                }
            }
        }
    }
}
