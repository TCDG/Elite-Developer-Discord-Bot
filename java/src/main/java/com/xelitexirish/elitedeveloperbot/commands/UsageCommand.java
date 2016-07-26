package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.utils.Logger;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class UsageCommand implements ICommand {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String usageMessage = "```" + "Developer Bot Usage: !Usage " + "!Projects" + "```";
        event.getTextChannel().sendMessage(usageMessage);

        Logger.command("usage", event.getMessage().getAuthor().getUsername());
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
