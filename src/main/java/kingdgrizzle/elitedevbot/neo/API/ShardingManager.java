package kingdgrizzle.elitedevbot.neo.API;

import kingdgrizzle.elitedevbot.neo.Listeners.BotListener;
import kingdgrizzle.elitedevbot.neo.Main;
import kingdgrizzle.elitedevbot.neo.Utils.BotLogger;
import kingdgrizzle.elitedevbot.neo.Utils.Reference;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;

import java.util.ArrayList;
import java.util.List;
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
 * Warning! This class is only supposed to be ran for Sharding! That means, if you have more than a 1000 servers!
 * Shard 0 is ALWAYS handling the DMs, and 1000 servers!
 * Everything is handled by Discord, this is just a helper class!
 */
public class ShardingManager {

    public static List<JDA> shards = new ArrayList<>();

    /**
     * The Shard Count. Initialization will be handled automatically,
     * and shards will me made based off this number!
     * Each Shard holds 1000 servers! So, 10 shards would hold 10000 servers!
     * Increasing this number is only necesarry if the bot is on more servers than the shards can handle!
     * One Shard = 1000, Formula would be {shard * 1000 guilds} for each shard!
     * The total must start from 2, cannot be lower than 2, but can probably be infinite. (Or how much the host can handle)
     * The Shard ID is always outputted as lower than the total. So, without a Formula of (id + 1), a shard of 2 would be outputted as 1.
     * Better Example: 2 Shards, First Shard starts at 0. With the formula (id + 1), First shard would be 1. For ease of understanding,
     * all sharding done with the bot should make 100% sense. Example: Shard 2 out of 2 is 2, not 1.
     */
    static int shardTotal = 2;

    /**
     * Helper method to handle getting all the guilds from all the shards
     * @return same as {@link net.dv8tion.jda.core.JDA JDA} getGuilds()
     */
    public static List<Guild> getAllGuilds() {
        return shards.stream().map(JDA::getGuilds).flatMap(List::stream).collect(Collectors.toList());
    }

    /**
     * Helper method to optain the Bots Name while Sharded
     * @return The Bots name as mention
     */
    public static String getBotMention() {
        return shards.get(0).getSelfUser().getAsMention();
    }

    /**
     * Helper Method to optain the Bots Avatar while Sharded
     * @return String containing the URL
     */
    public static String getBotAvatar() {
        return shards.get(0).getSelfUser().getEffectiveAvatarUrl();
    }

    /**
     * Helper Method to optain the Bots ID while Sharded
     * @return String containing the ID
     */
    public static String getBotID() {
        return shards.get(0).getSelfUser().getId();
    }

    /**
     * Helper Method to optain the Bots discriminator while Sharded
     * @return String containing the Discriminator
     */
    public static String getBotDiscriminator() {
        return shards.get(0).getSelfUser().getDiscriminator();
    }

    /**
     * Itilialises all the Shards and connects them.
     * This should only be called in case of {@link kingdgrizzle.elitedevbot.neo.Main Main} sharding returning true!
     */
    public static void init() {
        for (int shardId = 0; shardId < shardTotal; shardId++) {
            try {
                shards.add(new JDABuilder(AccountType.BOT).setToken(Main.DISCORD_TOKEN).addListener(new BotListener()).useSharding(shardId, shardTotal)
                        .setAutoReconnect(true)
                        .buildBlocking());
                BotLogger.logShardLogging(shardId, shardTotal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (JDA jda : shards) {
            String shardMessage = "Shard ID: " + (jda.getShardInfo().getShardId() + 1) + " | " + Reference.COMMAND_PREFIX + "help for info!";
            BotLogger.debug(shardMessage);
            jda.getPresence().setGame(Game.of(shardMessage));
        }
    }
}
