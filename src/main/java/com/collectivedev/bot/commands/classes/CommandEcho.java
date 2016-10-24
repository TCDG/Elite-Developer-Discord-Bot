package com.collectivedev.bot.commands.classes;

import com.collectivedev.bot.commands.Command;
import com.collectivedev.bot.commands.CommandSender;
import net.dv8tion.jda.entities.Message;

import java.util.Arrays;

public class CommandEcho extends Command {

    public CommandEcho() {
        super("echo", "<string>", "Echo a message back.");
    }

    @Override
    public void execute(CommandSender sender, Message message, String[] args) {
        final StringBuilder builder = new StringBuilder();

        Arrays.stream(args).forEach(arg -> builder.append(arg).append(" "));

        message.getChannel().sendMessageAsync("Echo: \n```" + builder.toString() + "```", null);
    }
}