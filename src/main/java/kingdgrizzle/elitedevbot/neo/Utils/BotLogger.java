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
 * File Created @ [ 26.12.2016, 10:24 (GMT +02) ]
 */
package kingdgrizzle.elitedevbot.neo.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class BotLogger {

    private static File logFolder = new File("logs/");
    private static File logFile = new File(logFolder + "/logs.txt");

    private static void log(String LogMessage) {
        try {
            if (!logFolder.exists()) {
                logFolder.mkdirs();
            }
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            PrintWriter printWriter = new PrintWriter(new FileWriter(logFile, true));
            printWriter.println(LogMessage);
            printWriter.flush();
            printWriter.close();
            System.out.println(LogMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void info(String log) {
        log("[Elite Dev Bot Neo: INFO] " + log);
    }

    public static void error(String log) {
        log("[Elite Dev Bot Neo: ERROR] " + log);
    }

    public static void debug(String log) {
        log("[Elite Dev Bot Neo: DEBUG] " + log);
    }

    public static void debug(String log, Exception exception) {
        debug(log);
        exception.printStackTrace();
    }

    public static void pm(String msg) {
        log("[Elite Dev Bot Neo: PM] " + msg);
    }

    public static void listListContent(List list) {
        System.out.println("[Elite Developer Neo: LIST] " + list.toString());
    }

    public static void listHashMap(HashMap map) {
        System.out.println("[Elite Developer Neo: HashMap] " + map.toString());
    }

    /**
     * Debug Information for Sharding
     * @param shardID = the exact shard it that just connected + 1
     * @param shardTotal = the total amount of shards the bot should make
     */
    public static void logShardLogging(int shardID, int shardTotal) {
        log("[Elite Dev Bot Neo: SHARDING] Connected using Shard ID " + (shardID + 1) + " out of the total " + shardTotal + " shards!");
    }
}
