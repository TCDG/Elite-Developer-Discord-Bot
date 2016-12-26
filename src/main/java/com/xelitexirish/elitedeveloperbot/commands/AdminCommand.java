package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.UserPrivs;
import com.xelitexirish.elitedeveloperbot.handlers.TwitterHandler;
import com.xelitexirish.elitedeveloperbot.listeners.SpellCheckerListener;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import twitter4j.PagableResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AdminCommand implements ICommand {

    private static String[] adminCommands = {"reload", "playing", "joindate", "username", "clear", "tweet", "messages"};
    private static String[] adminCommandsHelp = {"Reloads the data from the online git sources",
            "Sets the bot 'playing' message",
            "View the joindate of a specific user",
            "View the specific username associated with a user id",
            "Clears recent bot messages",
            "Send a twitter to the Scammer Sub Lounge twitter account",
            "Toggle bot messages in mainchat, will still appear in logs."};

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (UserPrivs.isUserAdmin(event.getAuthor()) || event.getAuthor().getId().equals(Constants.KING_ID)) {
            if (args.length == 0) {
                sendAdminHelpMessage(event);
            } else {
                if (args[0].equalsIgnoreCase("reload")) {
                    SpellCheckerListener.reloadLists();
                    event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed("Successfully reloaded bot data")).queue();
                } else if (args[0].equalsIgnoreCase("announcement")) {
                    for (Guild guild : event.getJDA().getGuilds()) {
                        if (guild.getId().equals(Constants.BOT_TESTING_DISCORD)) {
                            for (Member user : guild.getMembers()) {
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
                        event.getJDA().getPresence().setGame(Game.of(builder.toString()));
                        event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + " has changed the title to: " + builder.toString()).queue();
                    } else {
                        event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed("Use '" + Constants.COMMAND_PREFIX + "admin playing <title>'")).queue();
                    }
                } else if (args[0].equalsIgnoreCase("joindate")) {
                    if (args.length == 2) {
                        User user = event.getMessage().getMentionedUsers().get(0);
                        LocalDate date = event.getGuild().getMember(user).getJoinDate().toLocalDate();
                        event.getTextChannel().sendMessage(user.getAsMention() + " has joined the server on " + date).queue();
                    } else {
                        LocalDate date = event.getGuild().getMember(event.getAuthor()).getJoinDate().toLocalDate();
                        event.getTextChannel().sendMessage("You joined the server on " + date).queue();
                    }
                } else if (args[0].equalsIgnoreCase("username")) {
                    if (args.length == 2) {
                        String userId = args[1];
                        User user = event.getJDA().getUserById(userId);
                        event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + ", the username ``" + user.getName() + "`` has the id: " + userId).queue();
                    } else {
                        event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed("Use '" + Constants.COMMAND_PREFIX + "admin username <user id>'")).queue();
                    }
                } else if (args[0].equalsIgnoreCase("clear")) {
                    if (args.length == 1) {
                        int deletedMessagesCount = 0;
                        TextChannel channel = event.getTextChannel();
                        List<Message> deletedMessages = new ArrayList<>();
                        try {
                            CompletableFuture<List<Message>> task = new CompletableFuture<>();
                            channel.getHistory().retrievePast(100).queue(task::complete, task::completeExceptionally);
                            List<Message> list = task.get();
                            for (Message msg : list) {
                                if (msg.getAuthor().getId().equals(Main.jda.getSelfUser().getId())) {
                                    deletedMessages.add(msg);
                                    deletedMessagesCount++;
                                } else if (msg.getRawContent().startsWith(Constants.COMMAND_PREFIX) || msg.getRawContent().startsWith(Constants.DISCORD_COMMAND_PREFIX)) {
                                    deletedMessages.add(msg);
                                    deletedMessagesCount++;
                                }
                            }
                            channel.deleteMessages(deletedMessages).queue();
                            deletedMessages.clear();
                        } catch (Exception e) {
                            BotLogger.debug("Exception Occured", e);
                        }
                        event.getChannel().sendMessage(MessageUtils.wrapMessageInEmbed("Cleared " + deletedMessagesCount + " messages that start with the bot prefix / bots messages!")).queue();
                    } else {
                        event.getChannel().sendMessage(MessageUtils.wrapMessageInEmbed("You must include a number of messages to search and delete!\nMinimum 2, maximum 100!")).queue();
                    }
                } else if (args[0].equalsIgnoreCase("tweet")) {
                    if (args.length == 2) {
                        event.getTextChannel().sendMessage(help()).queue();
                    } else {
                        StringBuilder builder = new StringBuilder();
                        for (int x = 0; x < args.length; x++) {
                            builder.append(args[x] + " ");
                        }
                        TwitterHandler.sendTweet(event.getAuthor(), event.getTextChannel(), builder.toString());
                    }
                } else if (args[0].equalsIgnoreCase("messages")) {
                    if (Main.enableAutoMessages = true) {
                        Main.enableAutoMessages = false;
                    } else {
                        Main.enableAutoMessages = true;
                    }
                    MessageUtils.sendMessageToStaffChat(event.getJDA(), "Setting updated, currently: " + Main.enableAutoMessages);
                    BotLogger.log("Display Message", event.getAuthor().getName() + " has updated the setting to: " + Main.enableAutoMessages);
                } else {
                    sendAdminHelpMessage(event);
                }
            }
        } else{
            MessageUtils.sendNoPermissionMessage(event.getTextChannel());
        }
    }

    @Override
    public String help() {
        return "Administrative commands that must be preformed only by a server administrator";
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String getTag() {
        return "admin";
    }


    private static void sendAdminHelpMessage(MessageReceivedEvent event) {
        StringBuilder builder = new StringBuilder();
        builder.append("The following admin commands can be used by the bot: \n\n");
        for (int x = 0; x < adminCommands.length; x++) {
            builder.append(adminCommands[x] + ": " + adminCommandsHelp[x] + "\n");
        }
        builder.append("\nTo use an admin command do '" + Constants.COMMAND_PREFIX + "admin <sub_command>'");
        event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed(builder.toString())).queue();
    }
}
