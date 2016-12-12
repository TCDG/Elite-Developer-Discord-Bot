package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.WarningHandler;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class WarningCommand implements ICommand {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(args.length == 0) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setAuthor(Constants.EMBED_AUTHOR, Constants.EMBED_AUTHOR_URL, Constants.EMBED_AUTHOR_IMAGE);
            eb.setFooter(Constants.EMBED_FOOTER_NAME, Constants.EMBED_FOOTER_IMAGE);
            eb.setColor(Color.red);
            eb.setTitle("Error!");
            eb.setDescription("You need to mention a user who shall receive a warning!");
            MessageEmbed embed = eb.build();
            event.getChannel().sendMessage(embed).queue();
        } else if(args.length == 1) {
            String username = args[0];
            //WarningHandler.addWarning(username);
            event.getTextChannel().sendMessage(username.toUpperCase() + " now has " + WarningHandler.getUserWarnings(username) + " warnings").queue();
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
        return "warn";
    }
}
