package com.xelitexirish.elitedeveloperbot.handlers;

import com.xelitexirish.elitedeveloperbot.UserPrivs;
import com.xelitexirish.elitedeveloperbot.utils.MessageUtils;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterHandler {

    private static Twitter twitter;
    private static boolean enabled = false;

    public static void init(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        if(consumerKey != null) {
            enabled = true;
            ConfigurationBuilder config = new ConfigurationBuilder();
            config.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret).setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessTokenSecret);

            TwitterFactory twitterFactory = new TwitterFactory(config.build());
            twitter = twitterFactory.getInstance();
        }
    }

    public static void sendTweet(User sender, TextChannel textChannel, String tweet){
        if (UserPrivs.isUserAdmin(sender) && enabled) {
            try {
                Status status = twitter.updateStatus(sender.getName() + " tweeted: " + tweet);
                textChannel.sendMessage(status.getText());

            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {
            MessageUtils.sendNoPermissionMessage(textChannel);
        }
    }
}
