package fr.altherneum.bot.commands;

import java.util.concurrent.TimeUnit;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.api.HigherRole;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class ban {
    private ban() {
    }

    public static void banUser() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("ban")) {
                    try {
                        User sender = event.getUser();
                        User user = event.getOption("utilisateur").getAsUser();
                        int days = (int) Math.round(event.getOption("jours").getAsDouble());

                        if (days >= 1 && days <= 7) {
                            event.reply(IDs.EmojiShield).queue();
                        } else {
                            event.reply(IDs.EmojiSpeechBalloon
                                    + " Erreur sur la durée (1 à 7 jours de suppression des messages)").queue();
                            return;
                        }

                        String raison = event.getOption("raison").getAsString();

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setThumbnail(Discord.avatar(user));
                        embedBuilder.setTitle("🔨 Ban");
                        embedBuilder.addField("Utilisateur", Discord.mention(user), true);
                        embedBuilder.addField("ID", user.getId(), true);
                        embedBuilder.addField("Par", Discord.mention(sender), true);
                        embedBuilder.addField("Raison", raison, true);

                        Discord.textChannel(IDs.LogsCmd).sendMessageEmbeds(embedBuilder.build()).queue();

                        if (HigherRole.isRoleHigher(sender, user)) {
                            Discord.dmComplete(user, embedBuilder.build());
                            Discord.textChannel(IDs.Sanctions).sendMessageEmbeds(embedBuilder.build()).queue();
                            Discord.guild().ban(user, days, TimeUnit.DAYS).reason(raison).queue();
                        }

                        // L'ancien bot tentait une seconde réponse (\uD83D\uDC8E) après
                        // avoir déjà répondu : Discord l'ignorait. On conserve donc
                        // uniquement la réponse visible (le bouclier).
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    public static void unbanUser() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("unban")) {
                    try {
                        User sender = event.getUser();
                        User user = event.getOption("utilisateur").getAsUser();
                        String raison = event.getOption("raison").getAsString();

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setThumbnail(Discord.avatar(user));
                        embedBuilder.setTitle("🔨 UnBan");
                        embedBuilder.addField("Utilisateur", Discord.mention(user), true);
                        embedBuilder.addField("ID", user.getId(), true);
                        embedBuilder.addField("Par", Discord.mention(sender), true);
                        embedBuilder.addField("Raison", raison, true);

                        Discord.textChannel(IDs.LogsCmd).sendMessageEmbeds(embedBuilder.build()).queue();

                        if (HigherRole.isRoleHigher(sender, user)) {
                            Discord.dmComplete(user, embedBuilder.build());
                            Discord.textChannel(IDs.Sanctions).sendMessageEmbeds(embedBuilder.build()).queue();
                            Discord.guild().unban(user).reason(raison).queue();
                        }

                        event.reply("\uD83D\uDC8E").queue();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }
}
