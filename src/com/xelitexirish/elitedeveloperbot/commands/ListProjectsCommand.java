package com.xelitexirish.elitedeveloperbot.commands;

import jdk.nashorn.internal.parser.JSONParser;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class ListProjectsCommand implements ICommand{

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        JSONParser parser = new JSONParser();

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    public static class Projects {
        String title;
        String author;
        String website;

        public Projects(String title, String author, String website){
            this.title = title;
            this.author = author;
            this.website = website;
        }
    }
}
