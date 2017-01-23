package TCDG.EliteDevBot.Neo.Music;

import TCDG.EliteDevBot.Neo.Utils.Reference;
import TCDG.EliteDevBot.Neo.Utils.UserPrivs;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;

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
 * File Created @ [ 31.12.2016, 20:06 (GMT +02) ]
 */
public class MusicControl extends ListenerAdapter {

    private static int DEFAULT_VOLUME = 50;

    private final AudioPlayerManager playerManager;
    private final Map<String, GuildMusicManager> musicManagers;

    private static String[] commands = {"join", "leave", "play", "pplay", "resume", "pause", "stop", "skip", "np", "list", "queue", "volume", "restart", "repeat", "reset", "help", "shuffle", "splay"};
    private static String[] cmdsOrdered = {"help", "join", "list", "leave", "np", "pause", "play", "pplay", "queue", "resume", "restart", "repeat", "reset", "shuffle", "skip", "stop", "volume"};
    private static String[] cmdsOrderedHelp = {
            "Shows you this help message!",
            "Join a voice channel by name or ID!",
            "Shows all the songs that are currently in queue!",
            "Leaves the voice channel!",
            "Shows the song thats currently playing!",
            "Pauses the music! Run again to un-pause or use `.resume`!",
            "Plays the URL or the song found by the name you provided! *Warning! The searching happens on YouTube, and the first video is chosen!*",
            "Plays a playlist by link! You **MUST** provide a link!",
            "Shows all the songs that are currently in the queue! Alternative to `.list`!",
            "Resumes the playback of music after paused.",
            "Restarts the current song or restarts the previous song if there is no current song playing!",
            "Makes the player repeat the same song over and over. **Skipping** won't mean the same song will repeat, but the next one!",
            "Resets the player completely, clearing all the queue, and fixing any potential errors!",
            "Shuffles the entire playlist!",
            "Skips the current song!",
            "Completely stops audio playback, skipping the current song!",
            "Sets the volume of audio! Per-Guild!"
    };

    public MusicControl() {
        java.util.logging.Logger.getLogger("org.apache.http.client.protocol.ResponseProcessCookies").setLevel(Level.OFF);
        this.playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        playerManager.registerSourceManager(new SoundCloudAudioSourceManager());
        playerManager.registerSourceManager(new BandcampAudioSourceManager());
        playerManager.registerSourceManager(new VimeoAudioSourceManager());
        playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        playerManager.registerSourceManager(new HttpAudioSourceManager());
        playerManager.registerSourceManager(new LocalAudioSourceManager());
        musicManagers = new HashMap<>();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (!event.isFromType(ChannelType.TEXT)) {
            return;
        }
        String[] command = event.getMessage().getContent().split(" ", 2);
        String[] commandPlay = event.getMessage().getContent().split(" ");
        if (!command[0].startsWith(Reference.MUSIC_PREFIX)) {
            return;
        }
        Guild guild = event.getGuild();
        GuildMusicManager mng = getMusicManager(guild);
        AudioPlayer player = mng.player;
        TrackScheduler scheduler = mng.scheduler;
        if (event.getMessage().getRawContent().startsWith(Reference.MUSIC_PREFIX) && Arrays.asList(commands).contains(command[0].substring(Reference.MUSIC_PREFIX.length()))) {
            if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[0])) {
                //Join Channel Command
                if (UserPrivs.isUserStaff(event.getAuthor())) {
                    if (command.length == 1) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                        eb.setColor(Color.red);
                        eb.setTitle("Error!");
                        eb.setDescription("No channel name was provided to search with to join!");
                        MessageEmbed embed = eb.build();
                        event.getTextChannel().sendMessage(embed).queue();
                    } else {
                        VoiceChannel vc = guild.getVoiceChannelById(command[1]);
                        if (vc == null) {
                            vc = guild.getVoiceChannelsByName(command[1], true).stream().findFirst().orElse(null);
                        }
                        if (vc == null) {
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                            eb.setColor(Color.red);
                            eb.setTitle("Error!");
                            eb.setDescription("Could not find Voice Channel by name or ID: " + command[1]);
                            MessageEmbed embed = eb.build();
                            event.getTextChannel().sendMessage(embed).queue();
                        } else {
                            event.getMessage().addReaction("\u2705").complete();
                            guild.getAudioManager().setSendingHandler(mng.sendHandler);
                            try {
                                guild.getAudioManager().openAudioConnection(vc);
                            } catch (PermissionException e) {
                                if (e.getPermission() == Permission.VOICE_CONNECT || e.getPermission() == Permission.VOICE_SPEAK) {
                                    EmbedBuilder eb = new EmbedBuilder();
                                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                                    eb.setColor(Color.red);
                                    eb.setTitle("Permission Alert");
                                    eb.setDescription("I don't have the permission to connect or speak in channel: " + vc.getName());
                                    MessageEmbed embed = eb.build();
                                    event.getTextChannel().sendMessage(embed).queue();
                                }
                            }
                        }
                    }
                } else {
                    sendErrorMessage(event);
                }
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[1])) {
                //Leave Comamnd
                if (UserPrivs.isUserStaff(event.getAuthor())) {
                    guild.getAudioManager().setSendingHandler(null);
                    guild.getAudioManager().closeAudioConnection();
                    event.getMessage().addReaction("\u2705").complete();
                } else {
                    sendErrorMessage(event);
                }
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[2])) {
                //Play Command
                event.getMessage().addReaction("\u231B").complete();
                String cmdInput = String.join(" ", commandPlay).substring(6);
                if (command.length == 1) {
                    event.getMessage().addReaction("\u26A0").complete();
                } else if (cmdInput.startsWith("http")) {
                    loadAndPlay(mng, event.getChannel(), command[1], false);
                    event.getMessage().addReaction("\u2705").complete();
                } else {
                    loadAndPlay(mng, event.getChannel(), "ytsearch: " + cmdInput, false);
                    event.getMessage().addReaction("\u2705").complete();
                }
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[3])) {
                // pplay Command
                event.getMessage().addReaction("\u231B").complete();
                loadAndPlay(mng, event.getChannel(), command[1], true);
                event.getMessage().addReaction("\u2705").queue();
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[4])) {
                //Resume Command
                if (player.isPaused()) {
                    player.setPaused(false);
                    event.getMessage().addReaction("\u2705").queue();
                } else if (player.getPlayingTrack() != null) {
                    event.getMessage().addReaction("\u26A0").queue();
                } else if (scheduler.queue.isEmpty()) {
                    event.getMessage().addReaction("\u26A0").queue();
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                    eb.setColor(Color.red);
                    eb.setTitle("Error");
                    eb.setDescription("The current audio queue is empty! Add something to the queue using `.play` first!");
                    MessageEmbed embed = eb.build();
                    event.getChannel().sendMessage(embed).queue();
                }
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[5])) {
                //Pause Command
                if (UserPrivs.isUserStaff(event.getAuthor())) {
                    if (player.getPlayingTrack() == null) {
                        event.getMessage().addReaction("\u26A0").queue();
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                        eb.setColor(Color.red);
                        eb.setTitle("Error");
                        eb.setDescription("Cannot pause or resume player because no track is loaded for playing.");
                        MessageEmbed embed = eb.build();
                        event.getChannel().sendMessage(embed).queue();
                        return;
                    }
                    player.setPaused(!player.isPaused());
                    if (player.isPaused()) {
                        event.getMessage().addReaction("\u2705").queue();
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                        eb.setColor(Color.green);
                        eb.setTitle("Notification");
                        eb.setDescription("The player has been `paused`");
                        MessageEmbed embed = eb.build();
                        event.getChannel().sendMessage(embed).queue();
                    } else {
                        event.getMessage().addReaction("\u2705").queue();
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                        eb.setColor(Color.green);
                        eb.setTitle("Error");
                        eb.setDescription("The player has `resumed playing`");
                        MessageEmbed embed = eb.build();
                        event.getChannel().sendMessage(embed).queue();
                    }
                } else {
                    sendErrorMessage(event);
                }
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[6])) {
                //Stop Command
                if (UserPrivs.isUserStaff(event.getAuthor())) {
                    event.getMessage().addReaction("\u2705").queue();
                    scheduler.queue.clear();
                    player.stopTrack();
                    player.setPaused(false);
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                    eb.setColor(Color.green);
                    eb.setTitle("Notification");
                    eb.setDescription("Playback has been completely stopped and the queue has been cleared.");
                    MessageEmbed embed = eb.build();
                    event.getChannel().sendMessage(embed).queue();
                } else {
                    sendErrorMessage(event);
                }
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[7])) {
                //Skip Command
                if (UserPrivs.isUserStaff(event.getAuthor())) {
                    if (command.length == 1) {
                        scheduler.nextTrack();
                        event.getMessage().addReaction("\u2705").queue();
                    } else {
                        int numberToSkip = Integer.parseInt(command[1]);
                        for (int x = 0; x < numberToSkip; x++) {
                            scheduler.nextTrack();
                        }
                        event.getMessage().addReaction("\u2705").queue();
                    }
                } else {
                    sendErrorMessage(event);
                }
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[8]) || command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase("nowplaying")) {
                //Now Playing Command
                AudioTrack currentTrack = player.getPlayingTrack();
                if (currentTrack != null) {
                    String title = currentTrack.getInfo().title;
                    String position = getTimestamp(currentTrack.getPosition());
                    String duration = getTimestamp(currentTrack.getDuration());
                    String nowplaying = String.format("**Playing:** %s\n**Time:** [%s / %s]", title, position, duration);
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                    eb.setColor(Color.cyan);
                    eb.setTitle("Information");
                    eb.setDescription(nowplaying);
                    MessageEmbed embed = eb.build();
                    event.getChannel().sendMessage(embed).queue();
                } else {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                    eb.setColor(Color.red);
                    eb.setTitle("Error");
                    eb.setDescription("The player is not currently playing anything!");
                    MessageEmbed embed = eb.build();
                    event.getChannel().sendMessage(embed).queue();
                }
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[9]) || command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[10])) {
                //List and Queue Command > They are the same soo
                Queue<AudioTrack> queue = scheduler.queue;
                synchronized (queue) {
                    if (queue.isEmpty()) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                        eb.setColor(Color.red);
                        eb.setTitle("Error");
                        eb.setDescription("The queue is currently empty!");
                        MessageEmbed embed = eb.build();
                        event.getChannel().sendMessage(embed).queue();
                    } else {
                        int trackCount = 0;
                        long queueLength = 0;
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                        eb.setColor(Color.cyan);
                        eb.setTitle("Queue Information");
                        eb.setDescription("Current Queue Entries: " + queue.size());
                        for (AudioTrack track : queue) {
                            queueLength += track.getDuration();
                            if (trackCount < 24) {
                                eb.addField("__**" + track.getInfo().title + "**__", "Duration: " + getTimestamp(track.getDuration()), true);
                                trackCount++;
                            }
                        }
                        eb.setFooter("Total Queue Time Length: " + getTimestamp(queueLength), "https://cdn3.iconfinder.com/data/icons/seo-and-marketing-2-5/100/74-128.png");
                        MessageEmbed embed = eb.build();
                        event.getChannel().sendMessage(embed).queue();
                    }
                }
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[11])) {
                //Volume Command
                if (UserPrivs.isUserStaff(event.getAuthor())) {
                    if (command.length == 1) {
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                        eb.setColor(Color.cyan);
                        eb.setTitle("Information");
                        eb.setDescription("Current player volume: **" + player.getVolume() + "**");
                        MessageEmbed embed = eb.build();
                        event.getChannel().sendMessage(embed).queue();
                    } else {
                        try {
                            int newVolume = Math.max(10, Math.min(100, Integer.parseInt(command[1])));
                            int oldVolume = player.getVolume();
                            player.setVolume(newVolume);
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                            eb.setColor(Color.green);
                            eb.setTitle("Player Volume Changed");
                            eb.setDescription("Volume changed from `" + oldVolume + "` to `" + newVolume + "`");
                            MessageEmbed embed = eb.build();
                            event.getChannel().sendMessage(embed).queue();
                        } catch (NumberFormatException e) {
                            EmbedBuilder eb = new EmbedBuilder();
                            eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                            eb.setColor(Color.red);
                            eb.setTitle("Error");
                            eb.setDescription("`" + command[1] + "` is not a valid integer! (10 - 100)");
                            MessageEmbed embed = eb.build();
                            event.getChannel().sendMessage(embed).queue();
                        }
                    }
                } else {
                    sendErrorMessage(event);
                }
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[12])) {
                //Restart Command
                AudioTrack track = player.getPlayingTrack();
                if (track == null) {
                    track = scheduler.lastTrack;
                }
                if (track != null) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                    eb.setColor(Color.green);
                    eb.setTitle("Restarting Track");
                    eb.setDescription("Restarting track: " + track.getInfo().title);
                    MessageEmbed embed = eb.build();
                    event.getChannel().sendMessage(embed).queue();
                    player.playTrack(track.makeClone());
                } else {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                    eb.setColor(Color.red);
                    eb.setTitle("Error");
                    eb.setDescription("No track has been previously started, so the player cannot replay a track!");
                    MessageEmbed embed = eb.build();
                    event.getChannel().sendMessage(embed).queue();
                }
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[13])) {
                //Repeat Command
                if (UserPrivs.isUserStaff(event.getAuthor())) {
                    scheduler.setRepeating(!scheduler.isRepeating());
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                    eb.setColor(Color.green);
                    eb.setTitle("Replay Mode");
                    eb.setDescription("Player was set to: `" + (scheduler.isRepeating() ? "repeat" : "not repeat") + "`");
                    MessageEmbed embed = eb.build();
                    event.getChannel().sendMessage(embed).queue();
                } else {
                    sendErrorMessage(event);
                }
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[14])) {
                //Reset Command
                if (UserPrivs.isUserStaff(event.getAuthor())) {
                    synchronized (musicManagers) {
                        scheduler.queue.clear();
                        player.destroy();
                        guild.getAudioManager().setSendingHandler(null);
                        musicManagers.remove(guild.getId());
                    }
                    mng = getMusicManager(guild);
                    guild.getAudioManager().setSendingHandler(mng.sendHandler);
                    event.getMessage().addReaction("\u2705").queue();
                }
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[15])) {
                sendHelpMessage(event);
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[16])) {
                //Shuffle Command
                if (UserPrivs.isUserStaff(event.getAuthor())) {
                    event.getMessage().addReaction("\u2705").queue();
                    mng.scheduler.shuffle();
                } else {
                    sendErrorMessage(event);
                }
            } else if (command[0].substring(Reference.MUSIC_PREFIX.length()).equalsIgnoreCase(commands[17])) {
                //Silent Playlist Play
                if (UserPrivs.isUserStaff(event.getAuthor())) {
                    if (command.length == 2) {
                        silentLoadAndPlay(mng, event.getChannel(), command[1], true);
                        event.getMessage().addReaction("\u2705").queue();
                    } else {
                        int numberToSkip = Integer.parseInt(command[2]);
                        for (int x = 0; x < numberToSkip; x++) {
                            silentLoadAndPlay(mng, event.getChannel(), command[1], true);
                        }
                        event.getMessage().addReaction("\u2705").queue();
                    }
                } else {
                    sendErrorMessage(event);
                }
            }
        }
    }

    private void loadAndPlay(GuildMusicManager mng, final MessageChannel channel, final String trackUrl, final boolean addPlaylist) {
        playerManager.loadItemOrdered(mng, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                String msg = "Adding to queue: " + track.getInfo().title;
                if (mng.player.getPlayingTrack() == null) {
                    msg += "\n\tand the Player has started playing!";
                }
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                eb.setColor(Color.green);
                eb.setTitle("Information!");
                eb.setDescription(msg);
                MessageEmbed embed = eb.build();
                mng.scheduler.queue(track);
                channel.sendMessage(embed).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                List<AudioTrack> tracks = playlist.getTracks();
                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }
                if (addPlaylist) {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                    eb.setColor(Color.green);
                    eb.setTitle("Information!");
                    eb.setDescription("Adding __**" + playlist.getTracks().size() + "**__ tracks to queue from playlist: __**" + playlist.getName() + "**__");
                    MessageEmbed embed  = eb.build();
                    channel.sendMessage(embed).queue();
                    tracks.forEach(mng.scheduler::queue);
                } else {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                    eb.setColor(Color.green);
                    eb.setTitle("Information!");
                    eb.setDescription("Adding to queue: " + firstTrack.getInfo().title + "\n\t(First Track of Playlist: __**" + playlist.getName() + "**__)");
                    MessageEmbed embed = eb.build();
                    channel.sendMessage(embed).queue();
                    mng.scheduler.queue(firstTrack);
                }
            }

            @Override
            public void noMatches() {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                eb.setColor(Color.red);
                eb.setTitle("Error!");
                eb.setDescription("Nothing found by " + trackUrl);
                MessageEmbed embed = eb.build();
                channel.sendMessage(embed).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
                eb.setColor(Color.red);
                eb.setTitle("Error!");
                eb.setDescription("Could not play: " + exception.getMessage());
                MessageEmbed embed = eb.build();
                channel.sendMessage(embed).queue();
            }
        });
    }

    private void silentLoadAndPlay(GuildMusicManager mng, final MessageChannel channel, final String trackUrl, final boolean addPlaylist) {
        playerManager.loadItemOrdered(mng, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                mng.scheduler.queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();
                List<AudioTrack> tracks = playlist.getTracks();
                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }
                if (addPlaylist) {
                    tracks.forEach(mng.scheduler::queue);
                } else {
                    mng.scheduler.queue(firstTrack);
                }
            }

            @Override
            public void noMatches() {
                channel.sendMessage("No matches found").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Error while silently playing").queue();
            }
        });
    }

    private GuildMusicManager getMusicManager(Guild guild) {
        String guildId = guild.getId();
        GuildMusicManager mng = musicManagers.get(guildId);
        if (mng == null) {
            synchronized (musicManagers) {
                mng = musicManagers.get(guildId);
                if (mng == null) {
                    mng = new GuildMusicManager(playerManager);
                    mng.player.setVolume(DEFAULT_VOLUME);
                    musicManagers.put(guildId, mng);
                }
            }
        }
        return mng;
    }

    private static String getTimestamp(long milliseconds) {

        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

        if (hours > 0)
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        else
            return String.format("%02d:%02d", minutes, seconds);
    }

    private static void sendHelpMessage(MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
        eb.setColor(Color.cyan);
        eb.setTitle("Help for the music commands");
        eb.setDescription("Heres the help for all the music commands!");
        for (int x = 0; x < cmdsOrdered.length; x++) {
            eb.addField("__**" + cmdsOrdered[x] + "**__", cmdsOrderedHelp[x], true);
        }
        MessageEmbed embed = eb.build();
        event.getTextChannel().sendMessage(embed).queue();
    }

    private static void sendErrorMessage(MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(Reference.EMBED_AUTHOR, Reference.EMBED_AUTHOR_URL, Reference.EMBED_AUTHOR_IMAGE);
        eb.setColor(Color.red);
        eb.setTitle("Error");
        eb.setDescription("You aren't part of the Staff Team, so you cannot use that command!");
        MessageEmbed embed = eb.build();
        event.getTextChannel().sendMessage(embed).queue();
    }
}
