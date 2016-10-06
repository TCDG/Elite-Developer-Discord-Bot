package com.xelitexirish.elitedeveloperbot.handlers;

import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;

public class UsercountListener {

    public static void onUserJoin(GuildMemberJoinEvent event) {
        int currentMembers = event.getGuild().getUsers().size();

        if(currentMembers % 1000 == 0){

            String sendMessage = "Congratulations! This server now has hit " + currentMembers + " members!  Make sure to go give the staff a hug and tell them!";

            event.getGuild().getPublicChannel().sendMessage(MessageBuilder.EVERYONE_KEY + "\n" + MessageUtils.wrapStringInCodeBlock(sendMessage));
        }
    }
}
