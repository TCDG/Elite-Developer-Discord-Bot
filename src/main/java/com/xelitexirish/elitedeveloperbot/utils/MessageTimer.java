package com.xelitexirish.elitedeveloperbot.utils;

import com.xelitexirish.elitedeveloperbot.Main;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

import java.util.Timer;
import java.util.TimerTask;

public class MessageTimer {

    private static long waitTime = 300000; // 5 mins

    public static void init() {
        if(Main.enableTimerMessages){
            Timer timerReport = new Timer();
            timerReport.scheduleAtFixedRate(new TimerReport(), waitTime, waitTime);
        }
    }

    public static class TimerReport extends TimerTask{

        @Override
        public void run() {
            TextChannel textChannel = Main.jda.getGuildById(Constants.SCAMMER_SUB_LOUNGE_ID).getPublicChannel();
            User eliteBot = Main.jda.getUserById(Main.jda.getSelfInfo().getId());

            String message = "Has any member been abusing you and you would like to let the staff know about it?  " +
                    "Send a private message to " + eliteBot.getAsMention() + " starting with the word 'report' and then your message";

            textChannel.sendMessage(message);
        }
    }
}
