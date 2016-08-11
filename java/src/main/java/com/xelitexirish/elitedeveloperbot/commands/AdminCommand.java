package com.xelitexirish.elitedeveloperbot.commands;

import com.xelitexirish.elitedeveloperbot.UserPrivs;
import com.xelitexirish.elitedeveloperbot.listeners.SpellCheckerListener;
import com.xelitexirish.elitedeveloperbot.utils.Constants;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class AdminCommand implements ICommand{

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(UserPrivs.isUserAdmin(event.getAuthor())){
            if(args.length == 2){
                if(args[0].equalsIgnoreCase("add")){
                    String playerId = args[1];
                    UserPrivs.addUserToAdmin(event.getJDA().getUserById(playerId));
                }else if(args[0].equalsIgnoreCase("reload")){
                    SpellCheckerListener.reloadLists();
                    event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("Successfully reloaded bot data"));
                }else if (args[0].equalsIgnoreCase("announcement")){
                    for (Guild guild : event.getJDA().getGuilds()){
                        if(guild.getId().equals(Constants.BOT_TESTING_DISCORD)){
                            for (User user : guild.getUsers()){

                                StringBuilder builder = new StringBuilder();
                                builder.append("[ANNOUNCEMENT] ");
                                for(int x = 1; x < args.length; x++){
                                    builder.append(args[x] + " ");
                                }

                            }
                        }
                    }
                }
            }else {
             event.getTextChannel().sendMessage(MessageUtils.wrapStringInCodeBlock("Use admin add <player id>"));
            }
        }else{
            MessageUtils.sendNoPermissionMessage(event);
        }
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String getTag() {
        return "admin";
    }
}
