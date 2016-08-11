package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.ResumedEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberBanEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberUnbanEvent;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.events.user.UserNameUpdateEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContent().startsWith(Constants.COMMAND_PREFIX) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfInfo().getId()) {
            Main.handleCommand(Main.parser.parse(event.getMessage().getContent().toLowerCase(), event));

        } else if(event.getMessage().getContent().equalsIgnoreCase("Hey developer bot")){
            String message = "Hey my name is Elite Developer Bot, you can view my commands by entering the command " + Constants.COMMAND_PREFIX + " help";
            event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock(message));
        }else if(Main.enableSpellChecker){
            SpellCheckerListener.handleMessage(event);
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if(Constants.CURRENT_VERSION > Constants.OLD_VERSION) {
            String message = "Hey my name is Elite Developer Bot, you can view my commands by entering the command " + Constants.COMMAND_PREFIX;
            event.getGuild().getPublicChannel().sendMessage(MessageUtils.wrapStringInCodeBlock(message));
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String welcomeMessage = "Welcome " + event.getUser().getAsMention() + " make sure you read the rules!  If have a new account you wont be able to speak for 5 minutes!";
        event.getGuild().getPublicChannel().sendMessage(welcomeMessage);

        String logMessage = "Player " + event.getUser().getUsername() + " has joined server " + event.getGuild().getName();
        BotLogger.log("Player Join", logMessage);

        MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
    }

    @Override
    public void onUserNameUpdate(UserNameUpdateEvent event) {
        String changeNameMessage = "Player " + event.getPreviousUsername() + " is now known as " + event.getUser().getUsername();
        BotLogger.log("Username change", changeNameMessage);

        MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), changeNameMessage);
    }

    @Override
    public void onGuildMemberBan(GuildMemberBanEvent event) {
        String banMessage = "The ban hammer has spoken! Goodbye " + event.getUser().getUsername();
        event.getGuild().getPublicChannel().sendMessage(MessageUtils.wrapStringInCodeBlock(banMessage));

        String logMessage = "User has been banned: " + event.getUser().getUsername() + " on server " + event.getGuild().getName();
        BotLogger.log("Player Ban", logMessage);

        MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
    }

    @Override
    public void onGuildMemberUnban(GuildMemberUnbanEvent event) {
        String unbanMessage = "The ban hammer has been lifted on " + event.getUserName();
        event.getGuild().getPublicChannel().sendMessage(MessageUtils.wrapStringInCodeBlock(unbanMessage));

        String logMessage = "User has been unbanned: " + event.getUserName() + " on server " + event.getGuild().getName();
        BotLogger.log("Player Unban", logMessage);

        MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), logMessage);
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        String leaveMessage = "User " + event.getUser().getAsMention() + " has either been kicked and/or banned.";

        BotLogger.log("Player Leave", leaveMessage.toString());
        MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), leaveMessage);
    }

    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        String messageDelete = "Message has been deleted by: " + event.getMessage().getAuthor();

        BotLogger.log("Message Deleted", messageDelete + " on server " + event.getGuild().getName());
        MessageUtils.sendMessageToStaffDebugChat(event.getJDA(), messageDelete);
    }
}
