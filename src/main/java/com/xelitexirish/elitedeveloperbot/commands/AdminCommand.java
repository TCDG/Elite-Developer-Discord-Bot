package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.UserPrivs;
import com.xelitexirish.elitedeveloperbot.handlers.TwitterHandler;
import com.xelitexirish.elitedeveloperbot.listeners.SpellCheckerListener;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        if (UserPrivs.isUserAdmin(event.getAuthor())) {
            if (args.length == 0) {
                sendAdminHelpMessage(event);
            }else {
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
                        User user = event.getMessage().getMentionedUsers().get(0);
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

                } else if (args[0].equalsIgnoreCase("clear")) {
                    MessageChannel messageChannel = event.getChannel();
                    int clearedMessages = 0;
                    Collection<Message> messageCollection = new ArrayList<>();

                    if (args.length == 2) {
                        int clearMessages = Integer.parseInt(args[1]);
                        List<Message> recentMessages = messageChannel.getHistory().retrieve(clearMessages);

                        for (Message message : recentMessages) {
                            if (message.getAuthor().getId().equals(Main.jda.getSelfInfo().getId())) {
                                messageCollection.add(message);
                                clearedMessages++;
                            }
                            if(message.getContent().startsWith(Constants.COMMAND_PREFIX)){
                                message.deleteMessage();
                                clearedMessages++;
                            }
                        }
                        event.getTextChannel().deleteMessages(messageCollection);
                    } else {
                        List<Message> recentMessages = messageChannel.getHistory().retrieve(10);

                        for (Message message : recentMessages) {
                            if (message.getAuthor().getId().equals(Main.jda.getSelfInfo().getId())) {
                                message.deleteMessage();
                                clearedMessages++;
                            }
                            if(message.getContent().startsWith(Constants.COMMAND_PREFIX)){
                                message.deleteMessage();
                                clearedMessages++;
                            }
                        }
                    }
                    event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("Cleared " + clearedMessages + " messages from chat."));

                }else if (args[0].equalsIgnoreCase("tweet")) {
                    if (args.length == 2) {
                        event.getTextChannel().sendMessage(help());
                    } else {
                        StringBuilder builder = new StringBuilder();

                        for (int x = 0; x < args.length; x++) {
                            builder.append(args[x] + " ");
                        }
                        TwitterHandler.sendTweet(event.getAuthor(), event.getTextChannel(), builder.toString());
                    }

                }else if (args[0].equalsIgnoreCase("messages")){
                    if(Main.enableAutoMessages){
                        Main.enableAutoMessages = false;
                    }else {
                        Main.enableAutoMessages = true;
                    }

                    MessageUtils.sendMessageToStaffChat(event.getJDA(), MessageUtils.wrapStringInCodeBlock("Setting updated, currently: " + Main.enableAutoMessages));
                    BotLogger.log("Display Message", event.getAuthor().getUsername() + " has updated the setting to: " + Main.enableAutoMessages);

                }else {
                    sendAdminHelpMessage(event);
                }
            }
        } else {
            MessageUtils.sendNoPermissionMessage(event.getAuthor(), event.getTextChannel());
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
        event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + "\n" + MessageUtils.wrapStringInCodeBlock(builder.toString()));
    }
}
