package kingdgrizzle.elitedevbot.neo.Commands;

import kingdgrizzle.elitedevbot.neo.Listeners.SpellCheckerListener;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

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
 * File Created @ [ 03.01.2017, 15:01 (GMT +02) ]
 */
public class SpellCheckerCommand implements ICommand {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(args.length == 1){
            if (args[0].equalsIgnoreCase("false")) {
                SpellCheckerListener.blockUser(event.getGuild(), event.getAuthor());
            } else if (args[0].equalsIgnoreCase("true")) {
                SpellCheckerListener.unblockUser(event.getGuild(), event.getAuthor());
            }
        }
    }

    @Override
    public String help() {
        return "Correction of some words, special for `Scammer Sub Lounge`";
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String getTag() {
        return "correction";
    }
}
