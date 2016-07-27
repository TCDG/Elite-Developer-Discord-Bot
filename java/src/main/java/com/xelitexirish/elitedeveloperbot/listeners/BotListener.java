package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.BotLogger;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberBanEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberUnbanEvent;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContent().startsWith(Constants.COMMAND_PREFIX) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfInfo().getId()) {
            Main.handleCommand(Main.parser.parse(event.getMessage().getContent().toLowerCase(), event));
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String welcomeMessage = "Welcome " + event.getUser().getUsername() + " make sure you read the rules!";
        event.getGuild().getPublicChannel().sendMessage(welcomeMessage);
        BotLogger.log("Player Join", "User has joined:" + event.getUser().getUsername());
    }

    @Override
    public void onGuildMemberBan(GuildMemberBanEvent event) {
        String banMessage = "The ban hammer has spoken! Goodbye " + event.getUser().getUsername();
        event.getGuild().getPublicChannel().sendMessage(MessageUtils.wrapStringInCodeBlock(banMessage));
        BotLogger.log("Player Ban", "User has been banned:" + event.getUser().getUsername());
    }

    @Override
    public void onGuildMemberUnban(GuildMemberUnbanEvent event) {
        BotLogger.log("Player Unban", "User has been unban:" + event.getUser().getUsername());
    }

    @Override
    public void onReady(ReadyEvent event) {
        BotLogger.info("Successfully logged in as: " + event.getJDA().getSelfInfo().getUsername());
    }
}
