package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.ResumedEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
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
        String welcomeMessage = "Welcome " + MessageUtils.appendSenderUsername(event.getUser(), " make sure you read the rules!");
        event.getGuild().getPublicChannel().sendMessage(welcomeMessage);
        BotLogger.log("Player Join", "User has joined: " + event.getUser().getUsername() + " on server" + event.getGuild().getName());
    }

    @Override
    public void onUserNameUpdate(UserNameUpdateEvent event) {
        String changeNameMessage = "Player " + event.getPreviousUsername() + " is now known as " + event.getUser().getUsername();
        BotLogger.log("Username change", changeNameMessage);
    }

    @Override
    public void onGuildMemberBan(GuildMemberBanEvent event) {
        String banMessage = "The ban hammer has spoken! Goodbye " + event.getUser().getUsername();
        event.getGuild().getPublicChannel().sendMessage(MessageUtils.wrapStringInCodeBlock(banMessage));
        BotLogger.log("Player Ban", "User has been banned: " + event.getUser().getUsername() + " on server" + event.getGuild().getName());
    }

    @Override
    public void onGuildMemberUnban(GuildMemberUnbanEvent event) {
        BotLogger.log("Player Unban", "User has been unbaned: NPE ERROR" + " on server" + event.getGuild().getName());
    }

    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        BotLogger.log("Message deleted: ", event.getMessage().getContent() + " on server" + event.getGuild().getName());
    }

    @Override
    public void onReady(ReadyEvent event) {
        BotLogger.info("Successfully logged in as: " + event.getJDA().getSelfInfo().getUsername());
    }
}
