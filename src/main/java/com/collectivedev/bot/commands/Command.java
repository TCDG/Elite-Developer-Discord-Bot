package com.collectivedev.bot.commands;

import net.dv8tion.jda.entities.Message;

public abstract class Command {

    private final String name;
    private String usage;
    private String description;

    public Command(String name, String usage, String description) {
        this.name = name;
        this.usage = usage;
        this.description = description;
    }

    public Command(String name) {
        this(name, "No usage has been set for this command.", "No description has been set for this command.");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public abstract void execute(CommandSender sender, Message message, String[] args);
}