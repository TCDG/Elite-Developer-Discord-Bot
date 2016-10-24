package com.collectivedev.bot;

public class Main {

    public static void main(String[] args) throws Exception {
        IBot bot = IBot.getInstance();

        bot.start();
    }
}