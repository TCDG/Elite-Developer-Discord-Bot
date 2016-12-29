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
 * File Created @ [ 27.12.2016, 13:17 (GMT +02) ]
 */
package kingdgrizzle.elitedevbot.neo.Listeners;

import kingdgrizzle.elitedevbot.neo.Utils.MessageUtils;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;

import java.awt.*;

public class UserCountListener {

    public static void onGuildMemberJoin(GuildMemberJoinEvent par1Event) {
        int currentMembers = par1Event.getGuild().getMembers().size();
        if (currentMembers % 1000 == 0) {
            String msg = "Congratulations! This server has reached " + currentMembers + " members!\n Make sure to go give the Staff a hug and tell them!";
            par1Event.getGuild().getPublicChannel().sendMessage(MessageBuilder.EVERYONE_MENTION + ",\n" + MessageUtils.wrapMessageInEmbed(Color.green, msg)).queue();
        }
    }
}
