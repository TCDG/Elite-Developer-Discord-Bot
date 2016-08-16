package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.UserPrivs;
import com.xelitexirish.elitedeveloperbot.listeners.SpellCheckerListener;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.time.LocalDate;

public class AdminCommand implements ICommand {

    private static String helpMessage = "You can use the following admin commands: reload/playing/joindate/username";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (UserPrivs.isUserAdmin(event.getAuthor())) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    SpellCheckerListener.reloadLists();
                    event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("Successfully reloaded bot data"));
                } else if (args[0].equalsIgnoreCase("announcement")) {
                    for (Guild guild : event.getJDA().getGuilds()) {
                        if (guild.getId().equals(Constants.BOT_TESTING_DISCORD)) {
                            for (User user : guild.getUsers()) {

                                StringBuilder builder = new StringBuilder();
                                builder.append("[ANNOUNCEMENT] ");
                                for (int x = 1; x < args.length; x++) {
                                    builder.append(args[x] + " ");
                                }

                            }
                        }
                    }
                } else if (args[0].equalsIgnoreCase("playing")) {
                    if (args.length >= 2) {
                        StringBuilder builder = new StringBuilder();

                        for (int x = 1; x < args.length; x++) {
                            builder.append(args[x] + " ");
                        }
                        event.getJDA().getAccountManager().setGame(builder.toString());
                        event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " has changed the title to: " + builder.toString());
                    } else {
                        event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("Use '!dev admin playing <title>'"));
                    }
                } else if (args[0].equalsIgnoreCase("joindate")) {
                    if (args.length == 2) {
                        String userId = args[1];
                        User user = event.getJDA().getUserById(userId);
                        LocalDate date = event.getGuild().getJoinDateForUser(user).toLocalDate();
                        event.getTextChannel().sendMessage(user.getAsMention() + " has joined the server on " + date);
                    } else {
                        LocalDate date = event.getGuild().getJoinDateForUser(event.getAuthor()).toLocalDate();
                        event.getTextChannel().sendMessage("You joined the server on " + date);
                    }
                } else if (args[0].equalsIgnoreCase("username")) {
                    if (args.length == 2) {
                        String userId = args[1];
                        User user = event.getJDA().getUserById(userId);

                        event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " the username ``" + user.getUsername() + "`` has the id: " + userId);
                    } else {
                        event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("Use '!dev admin username <user id>'"));
                    }
                }
            } else {
                event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock(helpMessage));
            }
        } else {
            MessageUtils.sendNoPermissionMessage(event.getAuthor(), event.getGuild());
        }
    }

    @Override
    public String help() {
        return helpMessage;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String getTag() {
        return "admin";
    }
}
