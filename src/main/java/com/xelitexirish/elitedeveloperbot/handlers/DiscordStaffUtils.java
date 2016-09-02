package com.xelitexirish.elitedeveloperbot.handlers;

import com.xelitexirish.elitedeveloperbot.UserPrivs;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DiscordStaffUtils {

    private static String[] commands = {"/rm", "/kick", "/purge", "/help"};
    private static String[] commandsHelp = {"Right click on a message and press copy id and it will delete it from chat Usage: /rm <message id>",
            "Kick a player who is in the server Usage: /kick <user mention>",
            "This will delete recent messages from a user (60 messages) Usage: /purge <user mention>",
            "Displays help information for the command Usage: /help"};

    public static void handleMessage(MessageReceivedEvent event) {
        String[] lineSplit = event.getMessage().getContent().split(" ");
        if (event.getMessage().getContent().startsWith(Constants.DISCORD_COMMAND_PREFIX) && Arrays.asList(commands).contains(lineSplit[0])) {
            if (UserPrivs.isUserStaff(event.getAuthor())) {
                if (lineSplit[0].equalsIgnoreCase(commands[0])) {
                    //rm <message id>
                    if (lineSplit.length >= 1) {
                        String messageId = lineSplit[1];
                        try {
                            Message message = event.getChannel().getMessageById(messageId);
                            event.getChannel().deleteMessageById(messageId);

                            String logMessage = event.getAuthor().getAsMention() + " has removed a message in " + event.getTextChannel().getName() + " the message said: ``" + message.getContent() + "``";

                            event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("Message Deleted"));
                            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
                            BotLogger.log("Message Removed", logMessage);
                        } catch (Exception e) {
                            event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("Invalid Parameters"));
                        }
                    }

                } else if (lineSplit[0].equalsIgnoreCase(commands[1])) {
                    // kick
                    if (lineSplit.length >= 1) {
                        try {
                            List<User> mentionedUsers = event.getMessage().getMentionedUsers();

                            for (User user : mentionedUsers) {
                                event.getGuild().getManager().kick(user);

                                String logMessage = event.getAuthor().getUsername() + " has kicked the player: " + user.getAsMention();

                                event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("Player Kicked"));
                                MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
                                BotLogger.log("Player Kicked", logMessage);
                            }
                        } catch (Exception e) {
                            event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("Invalid Parameters"));
                        }
                    }

                } else if (lineSplit[0].equalsIgnoreCase(commands[2])) {
                    // purge
                    int clearedMessages = 0;
                    Collection<Message> messageCollection = new ArrayList<>();
                    try {
                        if (lineSplit.length >= 1) {
                            List<User> mentionedUsers = event.getMessage().getMentionedUsers();

                            MessageChannel channel = event.getChannel();
                            List<Message> recentMessages = channel.getHistory().retrieve(60);

                            for (Message message : recentMessages) {
                                for (User user : mentionedUsers) {
                                    if (message.getAuthor().getId().equals(user.getId())) {
                                        messageCollection.add(message);
                                        clearedMessages++;
                                    }
                                }
                            }
                            event.getTextChannel().deleteMessages(messageCollection);
                        }
                        event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("Cleared " + clearedMessages + " messages from chat."));
                    } catch (Exception e) {
                        event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("Invalid Parameters"));
                    }

                } else if (lineSplit[0].equalsIgnoreCase(commands[3])) {
                    //help
                    sendGeneralHelp(event);
                }
            } else {
                MessageUtils.sendNoPermissionMessage(event.getAuthor(), event.getTextChannel());
            }
        }
    }

    private static void sendGeneralHelp(MessageReceivedEvent event) {

        StringBuilder builder = new StringBuilder();
        builder.append("The following commands can be used by the bot:\n\n");
        for (int x = 0; x < commands.length; x++) {
            builder.append(commands[x] + ": " + commandsHelp[x] + "\n");
        }
        event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + "\n" + MessageUtils.wrapStringInCodeBlock(builder.toString()));
    }
}
