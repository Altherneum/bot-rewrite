package fr.altherneum.bot.commands;

import fr.altherneum.bot.listeners.casinoProfil;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public final class profil {
    private profil() {
    }

    public static void onProfil() {
        fr.altherneum.bot.Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("profil")) {
                    try {
                        OptionMapping option = event.getOption("utilisateur");
                        User target = option == null ? event.getUser() : option.getAsUser();
                        event.replyEmbeds(casinoProfil.profil(target)).queue();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    public static void onProfilUserContextMenu() {
        fr.altherneum.bot.Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onUserContextInteraction(UserContextInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("profil")) {
                    try {
                        event.replyEmbeds(casinoProfil.profil(event.getTarget())).queue();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }
}
