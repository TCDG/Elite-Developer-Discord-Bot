package com.collectivedev.bot.task;

import com.collectivedev.bot.IBot;
import com.google.common.base.Preconditions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskManager {

    private final Object lock = new Object();
    private final AtomicInteger taskCounter = new AtomicInteger();

    private final Map<Integer, Task> tasks = new ConcurrentHashMap<>();

    void cancel0(Task task) {
        synchronized (lock) {
            tasks.remove(task.getId());
        }
    }

    public void cancel(int id) {
        Task task = tasks.get(id);
        Preconditions.checkArgument(task != null, "%s is not a valid task", id);

        task.cancel();
    }

    public void cancel(Task task) {
        task.cancel();
    }

    public Task runAsync(Runnable task) {
        Task prepared = new Task(this, taskCounter.getAndIncrement(), task);

        synchronized (lock) {
            tasks.put(prepared.getId(), prepared);
        }

        IBot.getInstance().getExecutorService().execute(prepared);
        return prepared;
    }
}