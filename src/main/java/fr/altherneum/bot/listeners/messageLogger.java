package fr.altherneum.bot.listeners;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import fr.altherneum.bot.utils.MessageCache;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class messageLogger {
    private messageLogger() {
    }

    public static MessageEmbed embed(String description) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("📠 Logs");
        embedBuilder.setDescription(description);
        return embedBuilder.build();
    }

    public static void onMessageLogger() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReceived(MessageReceivedEvent event) {
                MessageCache.put(event.getMessage());
                if (event.getAuthor().isBot() || event.isWebhookMessage()
                        || !(event.getChannel() instanceof TextChannel channel)) {
                    return;
                }
                Discord.textChannel(IDs.LogsMessage).sendMessageEmbeds(embed("__**Message crée**__ : "
                        + Discord.mention(event.getAuthor())
                        + ", __**ID**__ : "
                        + event.getAuthor().getId()

                        + "\n"
                        + channel.getAsMention()
                        + ", __**message ID**__ : " + event.getMessageId() + " , __**message link**__ : "
                        + event.getMessage().getJumpUrl()

                        + "\n\n"
                        + event.getMessage().getContentRaw())).queue();
            }

            @Override
            public void onMessageDelete(MessageDeleteEvent event) {
                Message message = MessageCache.remove(event.getMessageIdLong());
                if (message == null || message.getAuthor().isBot()) {
                    return;
                }
                TextChannel channel = Main.jda.getTextChannelById(message.getChannel().getId());
                if (channel == null) {
                    return;
                }
                Discord.textChannel(IDs.LogsMessage).sendMessageEmbeds(embed("__**Message supprimé**__ : "
                        + Discord.mention(message.getAuthor())
                        + ", __**ID**__ : " + message.getAuthor().getId()

                        + "\n"
                        + channel.getAsMention()
                        + ", __**message ID**__ : " + event.getMessageId() + " , __**message link**__ : "
                        + message.getJumpUrl()

                        + "\n\n"
                        + message.getContentRaw())).queue();
            }

            @Override
            public void onMessageUpdate(MessageUpdateEvent event) {
                Message oldMessage = MessageCache.get(event.getMessageIdLong());
                Message newMessage = event.getMessage();
                if (oldMessage == null || oldMessage.getAuthor().isBot()) {
                    MessageCache.put(newMessage);
                    return;
                }
                TextChannel channel = Main.jda.getTextChannelById(newMessage.getChannel().getId());
                if (channel == null) {
                    MessageCache.put(newMessage);
                    return;
                }
                Discord.textChannel(IDs.LogsMessage).sendMessageEmbeds(embed("__**Message modifié**__ : "
                        + Discord.mention(newMessage.getAuthor())
                        + ", __**ID**__ : " + newMessage.getAuthor().getId()

                        + "\n"
                        + channel.getAsMention()
                        + ", __**message ID**__ : " + event.getMessageId() + " , __**message link**__ : "
                        + newMessage.getJumpUrl()

                        + "\n\n" + oldMessage.getContentRaw()

                        + "\n\n**__Nouveau Message__** :\n\n"
                        + newMessage.getContentRaw())).queue();
                MessageCache.put(newMessage);
            }
        });
    }
}
