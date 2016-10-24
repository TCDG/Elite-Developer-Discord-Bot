package com.collectivedev.bot;

import com.collectivedev.bot.json.JsonConfiguration;
import com.collectivedev.bot.persist.IDatabase;
import com.collectivedev.bot.task.TaskManager;
import com.google.common.base.Preconditions;
import net.dv8tion.jda.JDA;

import java.util.concurrent.ExecutorService;

public abstract class IBot {

    private static IBot instance;

    public static IBot getInstance() {
        return instance;
    }

    public static void setInstance(IBot instance) {
        Preconditions.checkNotNull(instance, "instance");
        Preconditions.checkArgument(IBot.instance == null, "Instance already set");
        IBot.instance = instance;
    }

    public abstract BotManager getBotManager();

    public abstract TaskManager getTaskManager();

    public abstract IDatabase getDatabase();

    public abstract JsonConfiguration getConfiguration();

    public abstract ExecutorService getExecutorService();

    public abstract JDA getJDA();

    public abstract void start() throws Exception;
}