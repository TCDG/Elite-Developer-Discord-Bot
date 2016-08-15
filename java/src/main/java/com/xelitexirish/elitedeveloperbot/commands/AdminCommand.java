package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.UserPrivs;
import com.xelitexirish.elitedeveloperbot.listeners.SpellCheckerListener;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class AdminCommand implements ICommand {

    private static String helpMessage = "Use !dev admin add/reload/playing";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (UserPrivs.isUserAdmin(event.getAuthor())) {
            if (args[0].equalsIgnoreCase("add")) {
                if (args.length == 2) {
                    String playerId = args[1];
                    UserPrivs.addUserToAdmin(event.getJDA().getUserById(playerId));
                } else {
                    event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("Use '!dev admin add <player id>'"));
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
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
                if(args.length >= 2) {
                    StringBuilder builder = new StringBuilder();

                    for(int x = 1; x < args.length; x++){
                        builder.append(args[x] + " ");
                    }
                    event.getJDA().getAccountManager().setGame(builder.toString());
                    event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " has changed the title to: " + builder.toString());
                }else {
                    event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("Use '!dev playing <title>'"));
                }
            }
        } else {
            MessageUtils.sendNoPermissionMessage(event);
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
