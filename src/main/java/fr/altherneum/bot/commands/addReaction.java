package fr.altherneum.bot.commands;

import java.util.Collections;
import java.util.List;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class addReaction {
    private addReaction() {
    }

    public static void onAddReaction() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("addreaction")) {
                    event.reply("\uD83D\uDC8E").queue();
                    new Thread(() -> {
                        try {
                            addReactionEveryWhere();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }).start();
                }
            }
        });
    }

    private static void addReactionEveryWhere() {
        for (net.dv8tion.jda.api.entities.channel.concrete.TextChannel channel : Discord.guild().getTextChannels()) {
            addReactions(channel);
        }
        for (ThreadChannel thread : Discord.guild().getThreadChannels()) {
            if (thread.getType().equals(ChannelType.GUILD_PUBLIC_THREAD)) {
                addReactions(thread);
            }
        }
    }

    private static void addReactions(GuildMessageChannel channel) {
        List<Message> messages = channel.getHistory().retrievePast(5).complete();
        Collections.reverse(messages);
        for (Message message : messages) {
            for (MessageReaction reaction : message.getReactions()) {
                message.addReaction(reaction.getEmoji()).queue();
            }
        }
    }
}
