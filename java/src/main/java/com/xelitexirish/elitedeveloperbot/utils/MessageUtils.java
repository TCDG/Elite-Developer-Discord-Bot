package com.xelitexirish.elitedeveloperbot.utils;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class MessageUtils {

    public static void sendMessageInCodeBlock(MessageReceivedEvent event, String message){
        event.getTextChannel().sendMessage(wrapStringInCodeBlock(message));
    }

    public static String wrapStringInCodeBlock(String message){
        String newMessage = "```" + message + "```";
        return newMessage;
    }

    public static Message appendSenderUsername(MessageReceivedEvent event, String message){
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.appendMention(event.getAuthor());
        messageBuilder.appendString(" " + message);
        return messageBuilder.build();
    }
}
