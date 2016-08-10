package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.WarningHandler;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class WarningCommand implements ICommand{

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        if(args.length == 0){

        }else if(args.length == 1){
            String username = args[0];

            WarningHandler.addWarning(username);
            event.getTextChannel().sendMessage(username.toUpperCase() + " now has " + WarningHandler.getUserWarnings(username) + " warnings");
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
        return null;
    }
}
