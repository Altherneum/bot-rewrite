package fr.altherneum.bot.commands;

import fr.altherneum.bot.listeners.StatsTimer;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class testping {
    private testping() {
    }

    public static void onTestPing() {
        fr.altherneum.bot.Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("test") || event.getName().equalsIgnoreCase("ping")) {
                    event.reply("\uD83D\uDC8E").queue();
                    if (Discord.isBotOwner(event.getUser())) {
                        try {
                            StatsTimer.buildMessage(false, event.getChannel().getId());
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}
