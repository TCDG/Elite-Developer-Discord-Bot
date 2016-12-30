/**
 * This class was created by <KingDGrizzle>. It's distributed as
 * part of the Elite-Dev-Bot-Neo Project. Get the Source Code on GitHub:
 * https://github.com/TCDG and search for the Elite-Dev-Bot-Neo project
 * <p>
 * Copyright (c) 2016 The Collective Developer Group. All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that this copyright block is included!
 * <p>
 * File Created @ [ 27.12.2016, 13:01 (GMT +02) ]
 */
package kingdgrizzle.elitedevbot.neo.Listeners;

import kingdgrizzle.elitedevbot.neo.Handlers.SpammerHelper;
import kingdgrizzle.elitedevbot.neo.Main;
import kingdgrizzle.elitedevbot.neo.Utils.Reference;
import kingdgrizzle.elitedevbot.neo.Utils.UserPrivs;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;
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

import java.awt.*;

public class BotListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        ChatMessageListener.onMessageRecieved(event);
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl());
        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
        eb.setColor(Color.green);
        eb.setTitle("Thanks for adding " + Main.jda.getSelfUser().getName() + " to your server!");
        eb.setDescription("Currently, the bot has to be configured through a config file, until I (Vlad) make it Multi-Server supportable!\nMeanwhile, you can do `" + Reference.COMMAND_PREFIX + "help` for commands!");
        MessageEmbed embed = eb.build();
        if (UserPrivs.hasPermission(event.getJDA().getSelfUser(), Permission.MESSAGE_EMBED_LINKS)) {
            event.getGuild().getPublicChannel().sendMessage(embed).queue();
        } else {
            event.getGuild().getPublicChannel().sendMessage("Hello! Thanks for inviting me! In order to use the rest of my features, please give me `Embed Links`, so I can output my pretty messages!").queue();
        }

        //ServerConfigHandler.init(event.getGuild());
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        SpammerHelper.onUserJoin(event);
        UserCountListener.onGuildMemberJoin(event);
    }

    @Override
    public void onGuildMemberNickChange(GuildMemberNickChangeEvent event) {
        super.onGuildMemberNickChange(event);
    }

    @Override
    public void onGuildBan(GuildBanEvent event) {
        super.onGuildBan(event);
    }

    @Override
    public void onGuildUnban(GuildUnbanEvent event) {
        super.onGuildUnban(event);
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        PrivateMessageListener.privateMessageReceived(event);
    }

    @Override
    public void onGuildVoiceGuildMute(GuildVoiceGuildMuteEvent event) {
        //ChatMessageListener.onGuildVoiceGuildMute(event);
    }

    @Override
    public void onGuildVoiceGuildDeafen(GuildVoiceGuildDeafenEvent event) {
       //ChatMessageListener.onGuildVoiceGuildDeafen(event);
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        //SpammerHelper.onRoleAdded(event);
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        //SpammerHelper.onRoleRemoved(event);
    }

    @Override
    public void onGuildMessageDelete(GuildMessageDeleteEvent event) {
        //ChatMessageListener.onGuildMessageDelete(event);
    }
}
