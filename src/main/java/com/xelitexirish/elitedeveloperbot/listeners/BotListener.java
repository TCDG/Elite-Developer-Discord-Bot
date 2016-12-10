package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.handlers.UsercountListener;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberNickChangeEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        ChatMessageListener.onMessageRecieved(event);
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if (Constants.CURRENT_VERSION > Constants.OLD_VERSION) {
            String message = "Hey my name is Elite Developer Bot, you can view my commands by entering the command " + Constants.COMMAND_PREFIX;
            event.getGuild().getPublicChannel().sendMessage(MessageUtils.wrapMessageInEmbed(message)).queue();
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        ChatMessageListener.onGuildMemberJoin(event);
        SpammerHelper.onUserJoin(event);
        UsercountListener.onUserJoin(event);
        if (Main.enableUsernameChecker) {
            BadUsernameListener.onUserJoin(event);
        }
    }

    @Override
    public void onGuildMemberNickChange(GuildMemberNickChangeEvent event) {
        ChatMessageListener.onUsernameUpdate(event);
        if (Main.enableUsernameChecker) {
            BadUsernameListener.onUsernameChange(event);
        }
    }

    @Override
    public void onGuildBan(GuildBanEvent event) {
        ChatMessageListener.onGuildBan(event);
    }

    @Override
    public void onGuildUnban(GuildUnbanEvent event) {
        ChatMessageListener.onGuildUnban(event);
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        PrivateMessageListener.privateMessageReciever(event);
    }

    @Override
    public void onGuildVoiceGuildMute(GuildVoiceGuildMuteEvent event) {
        ChatMessageListener.onGuildVoiceGuildMute(event);
    }

    @Override
    public void onGuildVoiceGuildDeafen(GuildVoiceGuildDeafenEvent event) {
        ChatMessageListener.onGuildVoiceGuildDeafen(event);
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        SpammerHelper.onRoleAdded(event);
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        SpammerHelper.onRoleRemoved(event);
    }

    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        //ChatMessageListener.onGuildMessageDelete(event);
    }
}
