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
 * File Created @ [ 27.12.2016, 14:18 (GMT +02) ]
 */
package com.kingdgrizzle.elitedevbot.neo.Listeners;

import com.kingdgrizzle.elitedevbot.neo.Main;
import com.kingdgrizzle.elitedevbot.neo.Utils.MessageUtils;
import com.kingdgrizzle.elitedevbot.neo.Utils.Reference;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ChatMessageListener {

    public static void onMessageRecieved(MessageReceivedEvent par1Event) {
        if (par1Event.getMessage().getRawContent().toLowerCase().startsWith(Reference.COMMAND_PREFIX) && par1Event.getMessage().getAuthor().getId() != par1Event.getJDA().getSelfUser().getId()) {
            Main.handleCommand(Main.parser.parse(par1Event.getMessage().getContent().toLowerCase(), par1Event));
        } else if (par1Event.getMessage().getRawContent().equalsIgnoreCase("Hey developer bot")) {
            String message = "Hey, my name is Elite Developer Bot, you can view my commands by entering the command " + Reference.COMMAND_PREFIX + "help";
            par1Event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed(message)).queue();
        } else if (par1Event.getMessage().getRawContent().equalsIgnoreCase("Dont cut the red wire")) {
            String message = "Hey " + par1Event.getAuthor().getAsMention() + " even I'm a bot and I know not to cut the red wire, check out the rules!";
            par1Event.getTextChannel().sendMessage(message).queue();
        } else if (Main.enableSpellChecker) {
            if (!par1Event.getAuthor().getId().equals(par1Event.getJDA().getSelfUser().getId())) {
                //SpellCheckerListener.handleMessage(event);
            }
        }
        //DiscordStaffUtils.handleMessage(event);
    }

}
