package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.UserPrivs;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

public class PrivateMessageListener {

    public static void privateMessageReciever(PrivateMessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(Constants.USER_ID_BOT_OWNER)) {
            String message = event.getMessage().getContent();

            if (message.startsWith("playing")) {
                String[] messageSplit = message.split(" ");
                StringBuilder playingBuilder = new StringBuilder();
                for (int x = 1; x < messageSplit.length; x++) {
                    playingBuilder.append(messageSplit[x] + " ");
                }
                event.getJDA().getAccountManager().setGame(playingBuilder.toString());
            } else {

                for (Guild guild : event.getJDA().getGuilds()) {
                    if (guild.getId().equals(Constants.DISCORD_SERVER_ID)) {
                        guild.getPublicChannel().sendMessage(message);
                    }
                }
            }
        } else {
            String message = event.getMessage().getContent();

            if (message.startsWith("report")) {
                String[] messageSplit = message.split(" ");

                StringBuilder reportBuilder = new StringBuilder();
                for (int x = 1; x < messageSplit.length; x++) {
                    reportBuilder.append(messageSplit[x] + " ");
                }
                for (User staffUser : UserPrivs.getAllStaff()) {
                    staffUser.getPrivateChannel().sendMessage(event.getAuthor().getUsername() + " has sent the following report: " + reportBuilder.toString());
                }
                event.getAuthor().getPrivateChannel().sendMessage("Thank you for submitting your report.  Staff will get back to you shortly");
            } else {
                if (!event.getAuthor().getId().equals(Main.jda.getSelfInfo().getId())) {
                    BotLogger.messageLog("PM", message);
                }
            }
        }
    }
}
