package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.utils.FileHelper;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

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
        for (Project p : projects) {
            event.getTextChannel().sendMessage(p.toString());
        }
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    public static class Project {
        String title;
        String author;
        String website;

        public Project(String title, String author, String website) {
            this.title = title;
            this.author = author;
            this.website = website;
        }

        @Override
        public String toString() {
            return title.toUpperCase() + " : " + author + " : " + website;
        }
    }

    private static void fillProjectList() {
        JSONParser jsonParser = new JSONParser();

        try{
            FileHelper fileHelper = new FileHelper();
            JSONArray array = (JSONArray) jsonParser.parse(new FileReader(fileHelper.getResourceFile("project_list.json")));

            for(Object object : array){
                JSONObject jsonObject = (JSONObject) object;

                String title = (String) jsonObject.get("title");
                String author = (String) jsonObject.get("author");
                String website = (String) jsonObject.get("website");

                Project project = new Project(title, author, website);
                projects.add(project);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
