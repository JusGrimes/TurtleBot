package info.justingrimes.turtlebot.chat.listeners;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class PingListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.getMessage().isFromType(ChannelType.TEXT)) return;

        if (event.getMessage().getContentStripped().startsWith("!ping")){
            event.getMessage().getTextChannel().sendMessage("Pong!").queue();
        }

    }
}
