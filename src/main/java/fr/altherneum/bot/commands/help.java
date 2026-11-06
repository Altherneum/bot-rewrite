package fr.altherneum.bot.commands;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

public final class help {
    private help() {
    }

    public static void onHelp() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("help")) {
                    try {
                        commandes(event.getUser(), event);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    public static void commandes(User user, SlashCommandInteractionEvent event) {
        Main.jda.retrieveCommands().queue(commands -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setThumbnail(Main.jda.getSelfUser().getEffectiveAvatarUrl());
            embedBuilder.setTitle("📚 Commandes d'" + Main.jda.getSelfUser().getName());

            boolean moderation = Discord.hasRole(user, Discord.guild(), IDs.RoleModeration);
            for (Command slashCommand : commands) {
                if (slashCommand.getType() != Command.Type.SLASH) {
                    continue;
                }
                Long permissions = slashCommand.getDefaultPermissions().getPermissionsRaw();
                if (permissions == null || moderation) {
                    embedBuilder.addField(slashCommand.getName(), slashCommand.getDescription(), false);
                }
            }

            event.replyEmbeds(embedBuilder.build()).queue();
        });
    }
}
