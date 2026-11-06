package fr.altherneum.bot.commands;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.listeners.casinoBanqueroute;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class banqueRoute {
    private banqueRoute() {
    }

    public static void banquerouteCMD() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("banqueroute")) {
                    try {
                        event.reply("\uD83D\uDC8E Merci de patienter").queue();
                        int tryAmount = (int) Math.round(event.getOption("lancer").getAsDouble());
                        casinoBanqueroute.doBanqueRoute(event.getUser(), event.getChannel().asTextChannel(), tryAmount);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }
}
