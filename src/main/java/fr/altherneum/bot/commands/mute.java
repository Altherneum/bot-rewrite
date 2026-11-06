package fr.altherneum.bot.commands;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.api.HigherRole;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class mute {
    private mute() {
    }

    public static void setMute() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("mute")) {
                    try {
                        User sender = event.getUser();
                        User user = event.getOption("utilisateur").getAsUser();

                        int muteMin = (int) Math.round(event.getOption("minutes").getAsDouble());
                        Date date = new Date();
                        date.setMinutes(date.getMinutes() + muteMin);

                        String raison = event.getOption("raison").getAsString();

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setThumbnail(Discord.avatar(user));
                        embedBuilder.setTitle("🔇 Exclusion");
                        embedBuilder.addField("Utilisateur", Discord.mention(user), true);
                        embedBuilder.addField("ID", user.getId(), true);
                        embedBuilder.addField("Rendu muet jusqu'au",
                                "" + "<t:" + date.toInstant().getEpochSecond() + ":R>", true);
                        embedBuilder.addField("Par", Discord.mention(sender), true);
                        embedBuilder.addField("Raison", raison, true);

                        Discord.textChannel(IDs.LogsCmd).sendMessageEmbeds(embedBuilder.build()).queue();

                        if (HigherRole.isRoleHigher(sender, user)) {
                            Discord.dmComplete(user, embedBuilder.build());
                            Discord.textChannel(IDs.Sanctions).sendMessageEmbeds(embedBuilder.build()).queue();
                            Discord.guild().timeoutFor(user,
                                    Duration.between(Instant.now(), date.toInstant())).reason(raison).queue();
                        }

                        event.reply("\uD83D\uDC8E").queue();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    public static void removeMute() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("unmute")) {
                    try {
                        User sender = event.getUser();
                        User user = event.getOption("utilisateur").getAsUser();

                        Member member = Discord.member(user);
                        OffsetDateTime timeout = member == null ? null : member.getTimeOutEnd();
                        if (timeout == null) {
                            return;
                        }

                        String raison = event.getOption("raison").getAsString();

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setThumbnail(Discord.avatar(user));
                        embedBuilder.setTitle("🔇 Retrait d'exclusion");
                        embedBuilder.addField("Utilisateur", Discord.mention(user), true);
                        embedBuilder.addField("ID", user.getId(), true);
                        embedBuilder.addField("N'est plus muet jusqu'au",
                                "" + "<t:" + timeout.toEpochSecond() + ":R>", true);
                        embedBuilder.addField("Par", Discord.mention(sender), true);
                        embedBuilder.addField("Raison", raison, true);

                        Discord.textChannel(IDs.LogsCmd).sendMessageEmbeds(embedBuilder.build()).queue();

                        if (HigherRole.isRoleHigher(sender, user)) {
                            Discord.dmComplete(user, embedBuilder.build());
                            Discord.textChannel(IDs.Sanctions).sendMessageEmbeds(embedBuilder.build()).queue();
                            Discord.guild().timeoutFor(user, Duration.ZERO).reason(raison).queue();
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
