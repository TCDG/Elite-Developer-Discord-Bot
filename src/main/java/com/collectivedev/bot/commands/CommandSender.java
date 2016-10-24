package com.collectivedev.bot.commands;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;

public interface CommandSender {

    /**
     * Returns this command sender as a {@link User} object.
     * @return this command sender as a {@link User} object
     */
    User asUser();

    /**
     * Returns the guild this command sender belongs to when executing the command.
     * @return the guild this command sender belongs to when executing the command
     */
    Guild getGuild();

    /**
     * Returns the message of the command that the sender sent when executing the command.
     * @return the message of the command that the sender sent when executing the command
     */
    Message getMessage();
}