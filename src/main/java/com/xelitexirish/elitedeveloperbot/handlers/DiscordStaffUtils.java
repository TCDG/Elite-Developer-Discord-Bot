package com.xelitexirish.elitedeveloperbot.handlers;

import com.xelitexirish.elitedeveloperbot.UserPrivs;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.core.MessageHistory;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class DiscordStaffUtils {

    private static String[] commands = {"/rm", "/kick", "/ban", "//purge", "/help"};
    private static String[] commandsHelp = {"Right click on a message and press copy id and it will delete it from chat. Usage: /rm <message id>",
            "Kick a user who is in the server. Usage: /kick <user mention>",
            "Bans a user. Usage /ban <user mention>",
            "This will delete recent messages from a user (100 messages). Usage: /purge <user mention>",
            "Displays help information for the command. Usage: /help"};

    public static void handleMessage(MessageReceivedEvent event) {
        String[] lineSplit = event.getMessage().getContent().split(" ");
        if (event.getMessage().getContent().startsWith(Constants.DISCORD_COMMAND_PREFIX) && Arrays.asList(commands).contains(lineSplit[0])) {
            if (UserPrivs.isUserStaff(event.getAuthor()) || event.getAuthor().getId().equals(Constants.KING_ID)) {
                if (lineSplit[0].equalsIgnoreCase(commands[0])) {
                    //rm <message id>
                    if (lineSplit.length >= 1) {
                        String messageId = lineSplit[1];
                        try {
                            Message message = event.getChannel().getMessageById(messageId).block();
                            event.getChannel().deleteMessageById(messageId);
                            String logMessage = event.getAuthor().getAsMention() + " has removed a message in " + event.getTextChannel().getName() + " the message said: ``" + message.getContent() + "``";
                            event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed("Message Deleted")).queue();
                            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
                            BotLogger.log("Message Removed", logMessage);
                        } catch (Exception e) {
                            event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed("Invalid Parameters")).queue();
                        }
                    }
                } else if (lineSplit[0].equalsIgnoreCase(commands[1])) {
                    // kick
                    if (lineSplit.length >= 1) {
                        try {
                            List<User> mentionedUsers = event.getMessage().getMentionedUsers();
                            for (User user : mentionedUsers) {
                                event.getGuild().getController().kick(user.getName()).queue();
                                String logMessage = event.getAuthor().getName() + " has kicked the player: " + user.getAsMention();
                                event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed("User Kicked")).queue();
                                MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
                                BotLogger.log("User Kicked", logMessage);
                            }
                        } catch (Exception e) {
                            event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed("Invalid Parameters")).queue();
                            e.printStackTrace();
                        }
                    }
                } else if (lineSplit[0].equalsIgnoreCase(commands[2])) {
                    // ban
                    if (lineSplit.length >= 1){
                        try {
                            List<User> mentionedUsers = event.getMessage().getMentionedUsers();
                            for (User user : mentionedUsers) {
                                event.getGuild().getController().ban(user, 1);
                                String logMessage = event.getAuthor().getName() + " has banned the player: " + user.getAsMention();
                                event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed("User Banned " + user.getAsMention())).queue();
                                MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
                                BotLogger.log("User Banned", logMessage);
                            }
                        } catch (Exception e) {
                            event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed("Invalid Parameters")).queue();
                            e.printStackTrace();
                        }
                    }
                } else if (lineSplit[0].equalsIgnoreCase(commands[3])) {
                    // purge
                    try {
                        int historyLookup = Integer.parseInt(lineSplit[1]);
                        int deletedMsgs = 0;
                        if (lineSplit.length >= 2) {
                            List<User> mentionedUsers = event.getMessage().getMentionedUsers();
                            if (!mentionedUsers.isEmpty()) {
                                TextChannel channel = event.getTextChannel();
                                List<Message> deletedMessages = new ArrayList<>();
                                CompletableFuture<List<Message>> task = new CompletableFuture<>();
                                channel.getHistory().retrievePast(historyLookup).queue(task::complete, task::completeExceptionally);
                                List<Message> list = task.get();
                                for (Message msg : list) {
                                    for (User usr : mentionedUsers) {
                                        if (msg.getAuthor().getId().equals(usr.getId())) {
                                            deletedMessages.add(msg);
                                            deletedMsgs++;
                                        }
                                    }
                                }
                                channel.deleteMessages(deletedMessages).queue();
                                deletedMessages.clear();
                                event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed("Cleared the last " + deletedMsgs + " messages from the mentioned users from chat.")).queue();
                            } else {
                                event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed("You didn't mention anyone! Please use the syntax `" + commands[3] + " number @user`. You can mention multiple users!")).queue();
                            }
                        }
                    } catch (Exception e) {
                        event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed("Invalid Parameters!\n You can use a number between 2 and 100! (Inclusive)")).queue();
                    }
                } else if (lineSplit[0].equalsIgnoreCase(commands[4])) {
                    //help
                    sendGeneralHelp(event);
                }
            } else {
                MessageUtils.sendNoPermissionMessage(event.getTextChannel());
            }
        }
    }

    private static void sendGeneralHelp(MessageReceivedEvent event) {
        StringBuilder builder = new StringBuilder();
        builder.append("The following commands can be used by the bot:\n\n");
        for (int x = 0; x < commands.length; x++) {
            builder.append(commands[x] + ": " + commandsHelp[x] + "\n");
        }
        event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed(builder.toString())).queue();
    }
}
