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
 * File Created @ [ 28.12.2016, 17:18 (GMT +02) ]
 */
package kingdgrizzle.elitedevbot.neo.Commands.Guild;

import kingdgrizzle.elitedevbot.neo.Commands.ICommand;
import kingdgrizzle.elitedevbot.neo.Utils.Reference;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GuildCommand implements ICommand {

    private static String[] guildCommands = {"emotes", "info"};
    private static String[] guildcommandsHelp = {
            "Enumerates all custom emotes on the server!",
            "Shows information about the server!"
    };

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {return true;}

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (args.length == 0) {
            sendGuildCommandHelp(event);
        } else {
            if (args[0].equalsIgnoreCase("emotes")) {
                List<Emote> emotes = event.getGuild().getEmotes();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                eb.setColor(Color.green);
                eb.setTitle("Here are the emotes for this guild!");
                for (Emote emote : emotes) {
                    eb.addField("__**" + emote.getName() + "**__", emote.getAsMention(), true);
                }
                if (emotes.size() >= 24) {
                    eb.setFooter("There are too many emotes to show!", "https://cdn2.iconfinder.com/data/icons/freecns-cumulus/32/519791-101_Warning-128.png");
                }
                MessageEmbed embed = eb.build();
                event.getTextChannel().sendMessage(embed).queue();
            } else if (args[0].equalsIgnoreCase("info")) {
                Guild currentGuild = event.getGuild();
                String members = "" + currentGuild.getMembers().size();
                String roles = "" + currentGuild.getRoles().size();
                String emotes = "" + currentGuild.getEmotes().size();
                String afkTimeout = "";
                switch (currentGuild.getAfkTimeout()) {
                    case SECONDS_60:
                        afkTimeout = "One minute";
                        break;
                    case SECONDS_300:
                        afkTimeout = "Five minutes";
                        break;
                    case SECONDS_900:
                        afkTimeout = "Fifteen Minutes";
                        break;
                    case SECONDS_1800:
                        afkTimeout = "Thirty Minutes";
                        break;
                    case SECONDS_3600:
                        afkTimeout = "One Hour!";
                        break;
                }
                String verificationLevel = "";
                switch (currentGuild.getVerificationLevel().getKey()) {
                    case 0:
                        verificationLevel = "No Verification! *Not Recommended!*";
                        break;
                    case 1:
                        verificationLevel = "Low Verification! *Must have a Verified Email*";
                        break;
                    case 2:
                        verificationLevel = "Medium Verification! *Must have a Discord Account for more than five minutes*";
                        break;
                    case 3:
                        verificationLevel = "(\u256F\u00B0\u25A1\u00B0\uFF09\u256F\uFE35 \u253B\u2501\u253B Verification! *Must be part of this server for at least ten minutes*";
                        break;
                }
                String notificationLevel = "";
                switch (currentGuild.getDefaultNotificationLevel()) {
                    case ALL_MESSAGES:
                        notificationLevel = "Every message will alert everyone! *Recommended to switch to the `Mention Only` option*";
                        break;
                    case MENTIONS_ONLY:
                        notificationLevel = "Messages will alert only the mentioned users! *Recommended!*";
                        break;
                }
                String twofaLevel = "";
                switch (currentGuild.getRequiredMFALevel()) {
                    case NONE:
                        twofaLevel = "No! *Recommended to turn on for any server!*";
                        break;
                    case TWO_FACTOR_AUTH:
                        twofaLevel = "Yes! *Recommended!*";
                        break;
                    case UNKNOWN:
                        twofaLevel = "Encountered weird error!";
                        break;
                }
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                eb.setColor(Color.green);
                eb.setTitle("Heres some information about this guild!");
                eb.addField("__**Guild Name**__", currentGuild.getName(), true);
                eb.addField("__**Guild ID**__", currentGuild.getId(), true);
                eb.addField("__**Member Count**__", members, true);
                eb.addField("__**Role Count**__", roles, true);
                eb.addField("__**Custom Emote Amount**__", emotes + "\n*Run `" + Reference.COMMAND_PREFIX + "guild emotes` to see the emotes!*", true);
                eb.addField("__**Region**__", currentGuild.getRegion().getName(), true);
                eb.addField("__**Created on**__", currentGuild.getCreationTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy 'at' HH:mm:ss")), true);
                eb.addField("__**Default Channel**__", currentGuild.getPublicChannel().getAsMention(), true);
                eb.addField("__**AFK Timeout**__", afkTimeout, true);
                try {
                    eb.addField("__**AFK Channel**__", currentGuild.getAfkChannel().getName(), true);
                } catch (NullPointerException e) {
                    eb.addField("__**AFK Channel**__", "No AFK Channel defined!", true);
                }
                eb.addField("__**Verification Level**__", verificationLevel, true);
                eb.addField("__**Notification Level**__", notificationLevel, true);
                eb.addField("__**Two-Factor Authentification Requirement**__", twofaLevel, true);
                eb.addField("__**Guild Owner**__", currentGuild.getOwner().getAsMention(), true);


                eb.setThumbnail(currentGuild.getIconUrl());
                MessageEmbed embed = eb.build();
                event.getTextChannel().sendMessage(embed).queue();
            }
        }
    }

    @Override
    public String help() {
        return "Use `" + Reference.COMMAND_PREFIX + "guild` to see more info about this command!";
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {}

    @Override
    public String getTag() {return "guild";}

    private static void sendGuildCommandHelp(MessageReceivedEvent par1Event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
        eb.setColor(Color.cyan);
        eb.setTitle("Help for the guild commands");
        eb.setDescription("The following guild commands can be used with the bot:");
        for (int x = 0; x < guildCommands.length; x++) {
            eb.addField("__**" + guildCommands[x] + "**__", guildcommandsHelp[x], true);
        }
        MessageEmbed embed = eb.build();
        par1Event.getChannel().sendMessage(embed).queue();
    }
}
