package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class PlayerIdCommand implements ICommand {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (args.length == 0) {
            String userId = event.getAuthor().getId();

            event.getTextChannel().sendMessage(MessageUtils.appendSenderUsername(event.getAuthor(), "Your user id is: " + userId));
            event.getAuthor().getPrivateChannel().sendMessage("Your user id is: " + userId);
        }
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String getTag() {
        return "playerid";
    }
}
