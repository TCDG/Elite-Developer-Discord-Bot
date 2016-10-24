package com.collectivedev.bot.commands;

import com.collectivedev.bot.IBot;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class CommandDispatcher extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getMessage().getRawContent().startsWith(IBot.getInstance().getConfiguration().getString("commandChar"))) {
            IBot.getInstance().getBotManager().dispatchCommand(event.getGuild(), new CommandSender() {
                @Override
                public User asUser() {
                    return event.getAuthor();
                }

                @Override
                public Guild getGuild() {
                    return event.getGuild();
                }

                @Override
                public Message getMessage() {
                    return event.getMessage();
                }
            }, event.getMessage().getRawContent());
        }
    }
}