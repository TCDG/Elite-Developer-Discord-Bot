package com.xelitexirish.elitedeveloperbot.listeners;

import com.xelitexirish.elitedeveloperbot.Main;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.guild.member.*;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.events.user.UserNameUpdateEvent;
import net.dv8tion.jda.events.voice.VoiceServerDeafEvent;
import net.dv8tion.jda.events.voice.VoiceServerMuteEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class BotListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        ChatMessageListener.onMessageRecieved(event);
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if (Constants.CURRENT_VERSION > Constants.OLD_VERSION) {
            String message = "Hey my name is Elite Developer Bot, you can view my commands by entering the command " + Constants.COMMAND_PREFIX;
            event.getGuild().getPublicChannel().sendMessage(MessageUtils.wrapStringInCodeBlock(message));
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        ChatMessageListener.onGuildMemberJoin(event);

        SpammerHelper.onUserJoin(event);

        if(Main.enableUsernameChecker){
            BadUsernameListener.onUserJoin(event);
        }
    }

    @Override
    public void onUserNameUpdate(UserNameUpdateEvent event) {
        ChatMessageListener.onUsernameUpdate(event);

        if(Main.enableUsernameChecker){
            BadUsernameListener.onUsernameChange(event);
        }
    }

    @Override
    public void onGuildMemberBan(GuildMemberBanEvent event) {
        ChatMessageListener.onMemberBan(event);
    }

    @Override
    public void onGuildMemberUnban(GuildMemberUnbanEvent event) {
        ChatMessageListener.onMemberUnban(event);
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        PrivateMessageListener.privateMessageReciever(event);
    }

    @Override
    public void onVoiceServerMute(VoiceServerMuteEvent event) {
        ChatMessageListener.onVoiceServerMute(event);
    }

    @Override
    public void onVoiceServerDeaf(VoiceServerDeafEvent event) {
        ChatMessageListener.onVoiceServerDeaf(event);
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
