package fr.altherneum.bot.commands;

import fr.altherneum.bot.api.checkDeleteUserMessage;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class EmptyChannel {
    private EmptyChannel() {
    }

    public static void onEmptyChannel() {
        fr.altherneum.bot.Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("emptychannel")) {
                    try {
                        checkDeleteUserMessage.deleteAllMessageFromChannel(
                                event.getChannel().asTextChannel(), "Commande manuel", event.getUser());
                        event.reply("\uD83D\uDC8E").queue();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }
}
