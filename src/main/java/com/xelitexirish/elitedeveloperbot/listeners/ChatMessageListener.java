package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.handlers.DiscordStaffUtils;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.events.guild.member.GuildMemberBanEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberUnbanEvent;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.events.user.UserNameUpdateEvent;
import net.dv8tion.jda.events.voice.VoiceServerDeafEvent;
import net.dv8tion.jda.events.voice.VoiceServerMuteEvent;

public class ChatMessageListener {

    public static void onMessageRecieved(MessageReceivedEvent event) {
        if (event.getMessage().getContent().startsWith(Constants.COMMAND_PREFIX) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfInfo().getId()) {
            Main.handleCommand(Main.parser.parse(event.getMessage().getContent().toLowerCase(), event));

        } else if (event.getMessage().getContent().equalsIgnoreCase("Hey developer bot")) {
            String message = "Hey my name is Elite Developer Bot, you can view my commands by entering the command " + Constants.COMMAND_PREFIX + "help";
            event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock(message));
        } else if (event.getMessage().getContent().equalsIgnoreCase("Dont cut the red wire")) {
            String message = "Hey " + event.getAuthor().getAsMention() + " even I'm a bot and I know not to cut the red wire, check out the rules!";
            event.getTextChannel().sendMessage(message);
        } else if (Main.enableSpellChecker) {
            SpellCheckerListener.handleMessage(event);
        }
        DiscordStaffUtils.handleMessage(event);
    }

    public static void onGuildMemberJoin(GuildMemberJoinEvent event) {

        String welcomeMessage = "Welcome " + event.getUser().getAsMention() + " make sure you read the #guidelines!  If you have a new account you wont be able to speak for 5 minutes!";
        if(Main.enableAutoMessages) {
            event.getGuild().getPublicChannel().sendMessage(welcomeMessage);
        }


        String logMessage = "Player " + event.getUser().getUsername() + " has joined server " + event.getGuild().getName();
        BotLogger.log("Player Join", logMessage);
        if (event.getGuild().getId().equals(Constants.SCAMMER_SUB_LOUNGE_ID)) {
            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
        }
    }

    public static void onUsernameUpdate(UserNameUpdateEvent event) {
        String changeNameMessage = "Player " + event.getPreviousUsername() + " is now known as " + event.getUser().getUsername();
        BotLogger.log("Username change", changeNameMessage);

        MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), changeNameMessage);
    }

    public static void onMemberBan(GuildMemberBanEvent event) {

        String banMessage = "The ban hammer has spoken! Goodbye " + event.getUser().getUsername();
        if(Main.enableAutoMessages) {
            event.getGuild().getPublicChannel().sendMessage(MessageUtils.wrapStringInCodeBlock(banMessage));
        }

        String logMessage = "User has been banned: " + event.getUser().getUsername() + " on server " + event.getGuild().getName();
        BotLogger.log("Player Ban", logMessage);

        if (event.getGuild().getId().equals(Constants.SCAMMER_SUB_LOUNGE_ID)) {
            if (Main.isTT142Offline()) {
                MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
            }
        }
    }

    public static void onMemberUnban(GuildMemberUnbanEvent event) {

        String unbanMessage = "The ban hammer has been lifted on " + event.getUser().getUsername();
        if(Main.enableAutoMessages) {
            event.getGuild().getPublicChannel().sendMessage(MessageUtils.wrapStringInCodeBlock(unbanMessage));
        }

        String logMessage = "User has been unbanned: " + event.getUser().getUsername() + " on server " + event.getGuild().getName();
        BotLogger.log("Player Unban", logMessage);

        if (event.getGuild().getId().equals(Constants.SCAMMER_SUB_LOUNGE_ID)) {
            if (Main.isTT142Offline()) {
                MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
            }
        }
    }

    public static void onVoiceServerMute(VoiceServerMuteEvent event) {
        if (event.getVoiceStatus().isServerMuted()) {
            String logMessage = "User " + event.getUser().getAsMention() + " has been server muted.";

            BotLogger.log("Player Muted", logMessage);
            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
        } else {
            String logMessage = "User " + event.getUser().getUsername() + " has been un-muted";

            BotLogger.log("Player Un-Muted", logMessage);
            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
        }
    }

    public static void onVoiceServerDeaf(VoiceServerDeafEvent event) {
        if (event.getVoiceStatus().isServerDeaf()) {
            String logMessage = "User " + event.getUser().getAsMention() + " has been server deafened";

            BotLogger.log("Player Deafen", logMessage);
            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
        } else {
            String logMessage = "User " + event.getUser().getAsMention() + " has been server un-deafened";

            BotLogger.log("Player Un-Deafen", logMessage);
            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
        }
    }

    public static void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        String message = "This message has been deleted: " + event.getMessage().getContent() + "\nBy the user: " + event.getAuthor().getAsMention();
        BotLogger.log("Message Delete", message);

        if (Main.isTT142Offline()) {
            MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), message);
        }
    }
}
