/**
 * This class was created by <KingDGrizzle>. It's distributed as
 * part of the Elite Developer Bot Project. Get the Source Code on GitHub:
 * https://github.com/TCDG and search for the Elite Dev Bot project
 * <p>
 * Copyright (c) 2016 The Collective Developer Group. All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that this copyright block is included!
 * <p>
 * File Created @ [ 14.12.2016, 20:35 (GMT +02) ]
 */
package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.utils.Constants;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class UserIDCommand implements ICommand {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (args.length == 0) {
            String userId = event.getAuthor().getId();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setAuthor(Constants.EMBED_AUTHOR, Constants.EMBED_AUTHOR_URL, Constants.EMBED_AUTHOR_IMAGE);
            eb.setFooter(Constants.EMBED_FOOTER_NAME, Constants.EMBED_FOOTER_IMAGE);
            eb.setColor(Color.green);
            eb.setTitle("Command Output");
            eb.setDescription("Your user ID is: " + userId);
            MessageEmbed embed = eb.build();
            event.getTextChannel().sendMessage(embed);
            if (!event.getAuthor().hasPrivateChannel()) {
                event.getAuthor().openPrivateChannel().queue(channel -> channel.sendMessage(embed).queue());
            } else {
                event.getAuthor().getPrivateChannel().sendMessage(embed).queue();
            }
        }
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {}

    @Override
    public String getTag() {
        return "userid";
    }
}
