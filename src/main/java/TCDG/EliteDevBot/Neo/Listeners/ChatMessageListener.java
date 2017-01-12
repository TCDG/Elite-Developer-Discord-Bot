package TCDG.EliteDevBot.Neo.Listeners;

import TCDG.EliteDevBot.Neo.API.ShardingManager;
import TCDG.EliteDevBot.Neo.Commands.DiscordStaffUtils;
import TCDG.EliteDevBot.Neo.Utils.Reference;
import TCDG.EliteDevBot.Neo.Main;
import TCDG.EliteDevBot.Neo.Utils.MessageUtils;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

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
public class ChatMessageListener {

    public static void onMessageRecieved(MessageReceivedEvent event) {
        if (event.getMessage().getRawContent().toLowerCase().startsWith(Reference.COMMAND_PREFIX) && !event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            Main.handleCommand(Main.parser.parse(event.getMessage().getContent().toLowerCase(), event));
        } else if (event.getMessage().getRawContent().equalsIgnoreCase("Hey developer bot")) {
            String message = "Hey, my name is Elite Developer Bot Neo, you can view my commands by entering the command `" + Reference.COMMAND_PREFIX + "help` in <#195179301451202560>";
            event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed(Color.cyan, message)).queue();
        } else if (event.getMessage().getRawContent().equalsIgnoreCase("Dont cut the red wire")) {
            String message = "Hey " + event.getAuthor().getAsMention() + " even I'm a bot and I know not to cut the red wire, check out the rules!";
            event.getTextChannel().sendMessage(message).queue();
        } else if(event.getMessage().getRawContent().equalsIgnoreCase("/triggered")) {
            event.getTextChannel().sendMessage("https://giphy.com/gifs/ZEVc9uplCUJFu").queue();
        } else if (Main.enableSpellChecker) {
            if (Main.sharding) {
                if (!event.getAuthor().getId().equals(ShardingManager.getBotID())) {
                    SpellCheckerListener.handleMessage(event);
                }
            } else {
                if (!event.getAuthor().getId().equals(Main.jda.getSelfUser().getId())) {
                    SpellCheckerListener.handleMessage(event);
                }
            }
        }
        DiscordStaffUtils.handleMessage(event);
    }

}
