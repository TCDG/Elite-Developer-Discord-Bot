package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.handlers.DiscordStaffUtils;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberNickChangeEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;

public class ChatMessageListener {

    public static void onMessageRecieved(MessageReceivedEvent event) {
        if (event.getMessage().getContent().startsWith(Constants.COMMAND_PREFIX) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfUser().getId()) {
            Main.handleCommand(Main.parser.parse(event.getMessage().getContent().toLowerCase(), event));
        } else if (event.getMessage().getContent().equalsIgnoreCase("Hey developer bot")) {
            String message = "Hey, my name is Elite Developer Bot, you can view my commands by entering the command " + Constants.COMMAND_PREFIX + "help";
            event.getTextChannel().sendMessage(MessageUtils.wrapMessageInEmbed(message)).queue();
        } else if (event.getMessage().getContent().equalsIgnoreCase("Dont cut the red wire")) {
            String message = "Hey " + event.getAuthor().getAsMention() + " even I'm a bot and I know not to cut the red wire, check out the rules!";
            event.getTextChannel().sendMessage(message).queue();
        } else if (Main.enableSpellChecker) {
            if (!event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) {
                SpellCheckerListener.handleMessage(event);
            }
        }
        DiscordStaffUtils.handleMessage(event);
    }

    public static void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String welcomeMessage = "Welcome " + event.getMember().getAsMention() + " to ** " + event.getGuild().getName() + "**";
        if (Main.enableAutoMessages) {
            event.getGuild().getPublicChannel().sendMessage(welcomeMessage).queue();
        } else {
            MessageUtils.sendMessageToStaffChat(event.getJDA(), "Welcome messages are off, user ``" + event.getMember().getEffectiveName() + "`` has joined the server");
        }
        String logMessage = "User ``" + event.getMember().getEffectiveName() + "`` has joined server " + event.getGuild().getName();
        BotLogger.log("User Join", logMessage);
        if (event.getGuild().getId().equals(Constants.DISCORD_SERVER_ID)) {
            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
        }
    }

    public static void onUsernameUpdate(GuildMemberNickChangeEvent event) {
        String changeNameMessage = "User ``" + event.getMember().getUser().getName() + "`` is now known as ``" + event.getNewNick() + "``";
        BotLogger.log("Username change", changeNameMessage);
        MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), changeNameMessage);
        MessageUtils.sendMessageToNickChat(event.getJDA(), changeNameMessage);
    }

    public static void onGuildBan(GuildBanEvent event) {
        String banMessage = "The ban hammer has spoken! Goodbye " + event.getUser().getName();
        if (Main.enableAutoMessages) {
            //If they've had a bad username, don't send the leave message!
            if (!BadUsernameListener.isBadUsername(event.getUser())) {
                event.getGuild().getPublicChannel().sendMessage(MessageUtils.wrapMessageInEmbed(banMessage)).queue();
            }
        }
        String logMessage = "User has been banned: ``" + event.getUser().getName() + "`` on server " + event.getGuild().getName();
        BotLogger.log("User Ban", logMessage);
        if (event.getGuild().getId().equals(Constants.DISCORD_SERVER_ID)) {
            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
        }
    }

    public static void onGuildUnban(GuildUnbanEvent event) {
        String unbanMessage = "The ban hammer has been lifted on: " + event.getUser().getName();
        if(Main.enableAutoMessages) {
            event.getGuild().getPublicChannel().sendMessage(MessageUtils.wrapMessageInEmbed(unbanMessage));
        }
        String logMessage = "User has been unbanned: ``" + event.getUser().getName() + "`` on server " + event.getGuild().getName();
        BotLogger.log("User Unban", logMessage);
        if (event.getGuild().getId().equals(Constants.DISCORD_SERVER_ID)) {
            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
        }
    }

    public static void onGuildVoiceGuildMute(GuildVoiceGuildMuteEvent event) {
        if (event.isGuildMuted()) {
            String logMessage = "User ``" + event.getMember().getEffectiveName() + "`` has been server muted.";
            BotLogger.log("Player Muted", logMessage);
            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
        } else {
            String logMessage = "User ``" + event.getMember().getEffectiveName() + "`` has been un-muted";
            BotLogger.log("Player Un-Muted", logMessage);
            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
        }
    }

    public static void onGuildVoiceGuildDeafen(GuildVoiceGuildDeafenEvent event) {
        if (event.isGuildDeafened()) {
            String logMessage = "User ``" + event.getMember().getEffectiveName() + "`` has been server deafened";
            BotLogger.log("Player Deafen", logMessage);
            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
        } else {
            String logMessage = "User ``" + event.getMember().getEffectiveName() + "`` has been server un-deafened";
            BotLogger.log("Player Un-Deafen", logMessage);
            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
        }
    }

    public static void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        Message deletedMessage = event.getMessage();
        String message = "This message has been deleted: " + deletedMessage + "\nBy the user: " + event.getAuthor().getAsMention();
        BotLogger.log("Message Delete", message);
        MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), message);
    }
}
