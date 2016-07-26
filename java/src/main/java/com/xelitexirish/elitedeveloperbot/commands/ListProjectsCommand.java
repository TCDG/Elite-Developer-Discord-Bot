package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.JsonReader;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ListProjectsCommand implements ICommand {

    public static ArrayList<Project> projects = new ArrayList<>();

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (projects.isEmpty()) {
            fillProjectList();
        }

        if (args.length >= 1){
            String projectId = args[0];
            event.getTextChannel().sendMessage("ID = " + projectId);
        }else{
            sendProjectListMessage(event);
        }

        System.out.println(args);
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    private static void sendProjectListMessage(MessageReceivedEvent event){
        for (Project p : projects) {
            event.getTextChannel().sendMessage(p.toString());
        }
    }

    /**
     * Utils
     */
    public static class Project {
        String title;
        String id;
        String author;
        String website;

        public Project(String title, String id, String author, String website) {
            this.title = title;
            this.id = id;
            this.author = author;
            this.website = website;
        }

        @Override
        public String toString() {
            return title.toUpperCase() + " : " + author + " : " + website + " ID=" + id;
        }
    }

    private static void fillProjectList() {

        try {
            JSONObject jsonObject = JsonReader.readJsonFromUrl(Constants.PROJECTS_LIST_URL);
            JSONArray jsonArray = jsonObject.getJSONArray("projects");
            if (jsonArray != null) {
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(x);

                    String title = jsonItem.getString("title");
                    String id = jsonItem.getString("id");
                    String author = jsonItem.getString("author");
                    String website = jsonItem.getString("website");

                    Project project = new Project(title, id, author, website);
                    projects.add(project);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
