package fr.altherneum.bot.commands;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.api.HigherRole;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class kick {
    private kick() {
    }

    public static void onKickUser() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("kick")) {
                    try {
                        User sender = event.getUser();
                        User user = event.getOption("utilisateur").getAsUser();
                        String raison = event.getOption("raison").getAsString();

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setThumbnail(Discord.avatar(user));
                        embedBuilder.setTitle("🚪 Expulsion");
                        embedBuilder.addField("Utilisateur", Discord.mention(user), true);
                        embedBuilder.addField("ID", user.getId(), true);
                        embedBuilder.addField("Par", Discord.mention(sender), true);
                        embedBuilder.addField("Raison", raison, true);

                        Discord.textChannel(IDs.LogsCmd).sendMessageEmbeds(embedBuilder.build()).queue();

                        if (HigherRole.isRoleHigher(sender, user)) {
                            Discord.dmComplete(user, embedBuilder.build());
                            Discord.textChannel(IDs.Sanctions).sendMessageEmbeds(embedBuilder.build()).queue();
                            Discord.guild().kick(user).reason(raison).queue();
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
