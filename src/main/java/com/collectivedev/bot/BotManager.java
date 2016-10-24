package com.collectivedev.bot;

import com.collectivedev.bot.commands.Command;
import com.collectivedev.bot.commands.CommandSender;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class BotManager {

    private final IBot instance;

    private final Map<String, Command> commands = new LinkedHashMap<>();
    private final Map<String, Map<String, Integer>> commandPower = new LinkedHashMap<>();
    private final Map<String, Map<String, Integer>> rolePower = new LinkedHashMap<>();

    public BotManager(IBot instance) {
        this.instance = instance;
    }

    public void registerCommand(Command command) {
        commands.put(command.getName().toLowerCase(), command);
    }

    public Command getCommand(String name) {
        return commands.get(name.toLowerCase());
    }

    public int getUserPower(Guild guild, User user) {
        return guild.getRolesForUser(user).stream()
                .map(r -> rolePower.get(guild.getId()).get(r.getId()))
                .max(Integer::compare)
                .get();
    }

    public boolean dispatchCommand(Guild server, CommandSender sender, String commandLine) {
        String[] split = " ".split(commandLine, -1);

        if (split.length == 0) {
            return false;
        }

        String commandName = split[0].toLowerCase();

        Command command = commands.get(commandName);

        if (command == null) {
            return false;
        }

        int power = getPowerForCommand(commandName, server.getId());

        if (getUserPower(server, sender.asUser()) < power) {
            sender.getMessage().getChannel().sendMessageAsync("Err, you kinda don't have permission to do that.", null);
            return true;
        }

        String[] args = Arrays.copyOfRange(split, 1, split.length);
        command.execute(sender, sender.getMessage(), args);

        return true;
    }

    public int getPowerForCommand(String command, String server) {
        return commandPower.get(server).get(command.toLowerCase());
    }
}