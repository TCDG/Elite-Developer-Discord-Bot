package com.xelitexirish.elitedeveloperbot.commands;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class UsageCommand implements ICommand{

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        String usageMessage = "```" + "Developer Bot Usage: !Usage" + "```";
        event.getTextChannel().sendMessage(usageMessage);
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }
}
