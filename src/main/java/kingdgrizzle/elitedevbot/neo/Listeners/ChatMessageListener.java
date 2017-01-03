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
package kingdgrizzle.elitedevbot.neo.Listeners;

import kingdgrizzle.elitedevbot.neo.API.ShardingManager;
import kingdgrizzle.elitedevbot.neo.Commands.DiscordStaffUtils;
import kingdgrizzle.elitedevbot.neo.Main;
import kingdgrizzle.elitedevbot.neo.Utils.BotLogger;
import kingdgrizzle.elitedevbot.neo.Utils.MessageUtils;
import kingdgrizzle.elitedevbot.neo.Utils.Reference;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class ChatMessageListener {

    public static void onMessageRecieved(MessageReceivedEvent event) {
        if (event.getMessage().getRawContent().toLowerCase().startsWith(Reference.COMMAND_PREFIX) && !event.getMessage().getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
            Main.handleCommand(Main.parser.parse(event.getMessage().getContent().toLowerCase(), event));
        } else if (event.getMessage().getRawContent().equalsIgnoreCase("Hey developer bot")) {
            String message = "Hey, my name is Elite Developer Bot, you can view my commands by entering the command " + Reference.COMMAND_PREFIX + "help";
            event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed(Color.green, message)).queue();
        } else if (event.getMessage().getRawContent().equalsIgnoreCase("Dont cut the red wire")) {
            String message = "Hey " + event.getAuthor().getAsMention() + " even I'm a bot and I know not to cut the red wire, check out the rules!";
            event.getTextChannel().sendMessage(message).queue();
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
        } else if (event.getMessage().getRawContent().toLowerCase().startsWith(Reference.DISCORD_COMMAND_PREFIX)) {
            DiscordStaffUtils.handleMessage(event);
        }
    }

}
