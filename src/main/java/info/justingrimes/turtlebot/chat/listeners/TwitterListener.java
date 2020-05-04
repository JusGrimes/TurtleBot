package info.justingrimes.turtlebot.chat.listeners;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.annotation.Nonnull;

public class TwitterListener extends ListenerAdapter {

    private final TwitterFactory twitterFactory;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public TwitterListener(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);

        twitterFactory = new TwitterFactory(cb.build());

    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        final boolean isBot = event.getAuthor().isBot();
        final Message message = event.getMessage();
        final String content = message.getContentDisplay();

        if (isBot || !message.isFromType(ChannelType.TEXT)) return;
        try {
            if (content.startsWith("!trump")) trumpTweet(message);
            else if (content.startsWith("!obama")) obamaTweet(message);
        } catch (TwitterException e) {
            log.warn("Could not get tweet from message {} - {}",
                    message.getAuthor().getName(),
                    message.getContentDisplay());
            log.warn(e.getMessage());
        }

    }

    private void obamaTweet(Message message) throws TwitterException {
        long obamaId = 813286;
        String baseUrl = "https://twitter.com/BarackObama/status/";
        long tweetId;
        try {
            tweetId = getMostRecentTweetId(obamaId);
        } catch (TwitterException e) {
            log.warn("Could not find trump tweet");
            throw e;
        }
        sendMessage(message.getTextChannel(), baseUrl + tweetId);
    }


    private void trumpTweet(Message message) throws TwitterException {
        long trumpId = 25073877;
        String baseUrl = "https://twitter.com/realDonaldTrump/status/";
        long tweetId;
        try {
            tweetId = getMostRecentTweetId(trumpId);
        } catch (TwitterException e) {
            log.warn("Could not find trump tweet");
            throw e;
        }
        sendMessage(message.getTextChannel(), baseUrl + tweetId);
    }

    private void sendMessage(TextChannel textChannel, String messageText) {
        textChannel.sendMessage(messageText).queue();
    }

    private long getMostRecentTweetId(long userId) throws TwitterException {
        Twitter twitter = twitterFactory.getInstance();
        final ResponseList<Status> userTimeline = twitter.getUserTimeline(userId);
        Status mostRecentStatus = userTimeline.get(0);
        return mostRecentStatus.getId();
    }

}
