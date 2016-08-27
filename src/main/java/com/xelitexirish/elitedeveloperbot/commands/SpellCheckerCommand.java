package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.listeners.SpellCheckerListener;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class SpellCheckerCommand implements ICommand{

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        if(args.length == 1){
            if(args[0].equalsIgnoreCase("false")){
                SpellCheckerListener.blockUser(event.getGuild(), event.getAuthor(), event.isPrivate());

            }else if (args[0].equalsIgnoreCase("true")){
                SpellCheckerListener.unblockUser(event.getGuild(), event.getAuthor(), event.isPrivate());
            }
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
        return "correction";
    }
}
