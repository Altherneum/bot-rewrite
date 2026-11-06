package fr.altherneum.bot.commands;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildMessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class createInvite {
    private createInvite() {
    }

    public static void onCreateInvite() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("createInvite")) {
                    event.reply("\uD83D\uDC8E").queue();
                    try {
                        String id = event.getOption("channel_id").getAsString();
                        StandardGuildMessageChannel serverTextChannel = Discord.textChannel(id);
                        serverTextChannel.createInvite()
                                .setMaxUses(0)
                                .setMaxAge(0)
                                .queue(invite -> Discord.dm(event.getUser(),
                                        "Invitation crée par " + Main.jda.getSelfUser().getAsMention()
                                                + " : **__" + invite.getUrl() + "__**"));
                    } catch (Exception ignored) {
                        // Comportement identique à l'ancien bot.
                    }
                }
            }
        });
    }
}
