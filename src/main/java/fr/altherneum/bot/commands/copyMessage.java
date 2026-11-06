package fr.altherneum.bot.commands;

import fr.altherneum.bot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class copyMessage {
    private copyMessage() {
    }

    public static void onCopyMessage() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("copyMessage")) {
                    event.reply("\uD83D\uDC8E").queue();
                    try {
                        String id = event.getOption("message_id").getAsString();
                        String channelId = event.getOption("channel_id").getAsString();
                        TextChannel target = Main.jda.getTextChannelById(channelId);
                        if (target == null) {
                            return;
                        }

                        event.getChannel().retrieveMessageById(id).queue(message -> {
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setDescription(message.getContentRaw());
                            target.sendMessageEmbeds(embedBuilder.build()).queue(newMessage -> {
                                for (MessageReaction reaction : message.getReactions()) {
                                    newMessage.addReaction(reaction.getEmoji()).queue();
                                }
                            });
                        });
                    } catch (Exception ignored) {
                        // L'ancien bot ignorait silencieusement les erreurs de cette commande.
                    }
                }
            }
        });
    }
}
