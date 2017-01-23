package TCDG.EliteDevBot.Neo.API;

import TCDG.EliteDevBot.Neo.Listeners.BotListener;
import TCDG.EliteDevBot.Neo.Utils.Reference;
import TCDG.EliteDevBot.Neo.Main;
import TCDG.EliteDevBot.Neo.Music.MusicControl;
import TCDG.EliteDevBot.Neo.Utils.BotLogger;
import net.dv8tion.jda.core.*;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;

import java.util.*;
import java.util.stream.Collectors;

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
 * File Created @ [ 30.12.2016, 11:02 (GMT +02) ]
 *
 * Warning! This class is only supposed to be used for Sharding! (this if you have more than 1000 servers)
 * Shard 0 is ALWAYS handling the DMs, and 1000 servers!
 * Everything is handled by Discord, this is just a helper class!
 */
public class ShardingManager {

    private ShardingManager() { }

    /**
     * The Shard List, holds all shards.
     * Can be used to get a specific shard and get the info from it using <tt>get(shardNumber)</tt>
     * Warning! Getting a shard of 2 when the shard total is 2 will error!
     * Shards in this list start at 0, so Shard no. 1 is 0 in the list
     * All the getBot functions base off the fact that all shards have the same info
     * If at any point in time one shard has different info than another one, it could
     * be very bad
     */
    public static List<JDA> shards = new ArrayList<>();

    /**
     * The Shard Count. Initialization will be handled automatically,
     * and shards will be made based off of this number!
     * Each Shard holds 1000 servers. So 10 shards would hold 10000 servers!
     * Increasing this number is only necessary if the bot is on more servers than the shards can handle!
     * One Shard = 1000; formula would be <tt>shard * 1000 guilds</tt> for each shard!
     * The total must start from 2, cannot be lower than 2, but can probably be infinite. (Or however many the host can handle)
     * The Shard ID is always outputted lower than the total. So, without a formula of <tt>(id + 1)</tt>, a shard of 2 would be outputted as 1.
     * Better example: 2 Shards, First Shard starts at 0. With the formula (id + 1), First shard would be 1. For ease of understanding,
     * all sharding done with the bot should make 100% sense. Example: Shard 2 out of 2 is 2, not 1.
     */
    private static int shardTotal = 2;

    /**
     * Helper method to handle getting all the guilds from all the shards
     * @return same as {@link net.dv8tion.jda.core.JDA JDA} getGuilds()
     */
    public static List<Guild> getAllGuilds() {
        return shards.stream().map(JDA::getGuilds).flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * Helper method to obtain the Bot's Mention while Sharded
     * @return The Bot's name as mention
     */
    public static String getBotMention() {
        return shards.get(0).getSelfUser().getAsMention();
    }

    /**
     * Helper method to obtain the Bot's name while Sharded
     * @return The Bot's name as a String
     */
    public static String getBotName() {
        return shards.get(0).getSelfUser().getName();
    }

    /**
     * Helper method to obtain the Bot's Avatar while Sharded
     * @return String containing the URL
     */
    public static String getBotAvatar() {
        return shards.get(0).getSelfUser().getEffectiveAvatarUrl();
    }

    /**
     * Helper method to obtain the Bot's ID while Sharded
     * @return String containing the ID
     */
    public static String getBotID() {
        return shards.get(0).getSelfUser().getId();
    }

    /**
     * Helper method to obtain the Bot's discriminator while Sharded
     * @return String containing the Discriminator
     */
    public static String getBotDiscriminator() {
        return shards.get(0).getSelfUser().getDiscriminator();
    }

    /**
     * Initialises all the Shards and connects them.
     * This should only be called in case of {@link TCDG.EliteDevBot.Neo.Main Main} sharding returning true!
     */
    public static void init() {
        for (int shardId = 0; shardId < shardTotal; shardId++) {
            try {
                shards.add(new JDABuilder(AccountType.BOT)
                        .setToken(Main.DISCORD_TOKEN)
                        .addListener(new BotListener())
                        .addListener(new MusicControl())
                        .useSharding(shardId, shardTotal)
                        .setAutoReconnect(true)
                        .buildBlocking());
                BotLogger.logShardLogging(shardId, shardTotal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        shards.forEach(jda -> {
            String shardMessage = "Shard ID: " + (jda.getShardInfo().getShardId() + 1) + " | `" + Reference.COMMAND_PREFIX + "help` for info!";
            if (Main.debugMode) BotLogger.debug(shardMessage);
            jda.getPresence().setGame(Game.of(shardMessage));
        });
    }
}