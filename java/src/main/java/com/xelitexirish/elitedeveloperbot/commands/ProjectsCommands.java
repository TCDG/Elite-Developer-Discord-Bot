package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.Project;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.JsonReader;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ProjectsCommands implements ICommand {

    public static ArrayList<Project> projects = new ArrayList<>();

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        projects.clear();
        updateProjectList();

        if (args.length >= 1) {
            String argument = args[0];
            for (int x = 0; x < projects.size(); x++) {
                if (argument.equals(projects.get(x).getId())) {
                    // If the project id is in the list
                    sendProjectInfoMessage(event, projects.get(x));
                    return;
                }
            }
            if(argument.equals("add")){
                event.getTextChannel().sendMessage(MessageUtils.appendSenderUsername(event.getAuthor(), "Please fill in this form to add your project: https://goo.gl/forms/6aQ8vUkkF2nGnNy62"));
            }else {
                event.getTextChannel().sendMessage(MessageUtils.appendSenderUsername(event.getAuthor(), "Project ID is invalid"));
            }
        } else {
            sendProjectListMessage(event);
        }
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    private static void sendProjectListMessage(MessageReceivedEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Current projects: ");
        for (Project p : projects) {
            stringBuilder.append(p.getTitle() + " ID=" + p.getId() + "  ");
        }
        MessageUtils.sendMessageInCodeBlock(event, stringBuilder.toString());
        BotLogger.command("Project list", event.getAuthor().getUsername());

    }

    private static void sendProjectInfoMessage(MessageReceivedEvent event, Project project) {
        String infoMessage = project.toString();
        MessageUtils.sendMessageInCodeBlock(event, infoMessage);
        BotLogger.command("Project info", event.getAuthor().getUsername());
    }

    /**
     * Utils
     */
    private static void updateProjectList() {

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
                    String infoMessage = jsonItem.getString("infoMessage");
                    String downloadLink = jsonItem.getString("downloadLink");

                    Project project = new Project(title, id, author, website, infoMessage, downloadLink);
                    projects.add(project);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
