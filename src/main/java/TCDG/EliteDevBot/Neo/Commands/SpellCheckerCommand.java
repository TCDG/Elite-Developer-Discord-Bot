package TCDG.EliteDevBot.Neo.Commands;

import TCDG.EliteDevBot.Neo.Listeners.SpellCheckerListener;
import TCDG.EliteDevBot.Neo.Utils.Reference;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
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
 * File Created @ [ 03.01.2017, 15:01 (GMT +02) ]
 */
public class SpellCheckerCommand implements ICommand {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(args.length == 1){
            if (args[0].equalsIgnoreCase("false")) {
                SpellCheckerListener.blockUser(event.getGuild(), event.getAuthor());
            } else if (args[0].equalsIgnoreCase("true")) {
                SpellCheckerListener.unblockUser(event.getGuild(), event.getAuthor());
            } else if (args[0].equalsIgnoreCase("list")) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.cyan);
                eb.setTitle("Blacklisted members!");
                eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                for (String string : SpellCheckerListener.blackListUsers) {
                    eb.addField("Member", event.getGuild().getMemberById(string).getAsMention(), true);
                }
                if (SpellCheckerListener.blackListUsers.size() > 25) {
                    eb.setFooter("There are too many people to show! Total amount:" + SpellCheckerListener.blackListUsers.size(), Reference.WARNING_SIGN);
                }
                MessageEmbed embed = eb.build();
                event.getTextChannel().sendMessage(embed).queue();
            }
        }
    }

    @Override
    public String help() {
        return "Correction of some words, special for `Scammer Sub Lounge`";
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {}

    @Override
    public String getTag() {
        return "correction";
    }
}
