package com.collectivedev.bot;

import com.collectivedev.bot.commands.CommandDispatcher;
import com.collectivedev.bot.commands.classes.CommandEcho;
import com.collectivedev.bot.json.JsonConfiguration;
import com.collectivedev.bot.persist.Database;
import com.collectivedev.bot.persist.IDatabase;
import com.collectivedev.bot.task.TaskManager;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bot extends IBot {

    private final BotManager botManager = new BotManager(this);

    private JDA jda;
    private final JsonConfiguration config;
    private final IDatabase database;
    private final TaskManager taskManager;

    {
        getBotManager().registerCommand(new CommandEcho());
    }

    public Bot() throws Exception {
        final File configFile = new File("config.json");
        config = new JsonConfiguration(configFile);

        if (!configFile.exists()) {
            System.out.println("Creating default configuration file...");
            config.saveDefaultFile("defaultconfig.json");

            System.out.println("Please fill in the configuration file and re-run.");
            System.exit(0);
        }

        database = new Database(
                config.getString("database.uri"),
                config.getString("database.username"),
                config.getString("database.password")
        );

        database.createTables();

        taskManager = new TaskManager();
    }

    @Override
    public BotManager getBotManager() {
        return botManager;
    }

    @Override
    public TaskManager getTaskManager() {
        return taskManager;
    }

    @Override
    public IDatabase getDatabase() {
        return database;
    }

    @Override
    public JsonConfiguration getConfiguration() {
        return config;
    }

    private ExecutorService service;

    @Override
    public ExecutorService getExecutorService() {
        if (service == null) {
            String name = "Bot";

            service = Executors.newCachedThreadPool(
                    new ThreadFactoryBuilder()
                            .setNameFormat(name + " Pool #1$d")
                            .setThreadFactory(r -> new Thread(new ThreadGroup(name), r))
                            .build()
            );
        }

        return service;
    }

    @Override
    public JDA getJDA() {
        return null;
    }

    @Override
    public void start() throws Exception {
        jda = new JDABuilder()
                .setBotToken(config.getString("token"))
                .setAudioEnabled(false) // dont need voice support so we might as well disable it
                .buildBlocking();

        jda.addEventListener(new CommandDispatcher());
    }
}