package fr.altherneum.bot.commands;

import java.io.File;
import java.util.Date;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.listeners.casinoProfil;
import fr.altherneum.bot.utils.Discord;
import fr.altherneum.bot.utils.FileSystem;
import fr.altherneum.bot.utils.JsonFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class upvote {
    private upvote() {
    }

    public static void onUpvote() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("upvote")) {
                    try {
                        User sender = event.getUser();
                        User user = event.getOption("utilisateur").getAsUser();
                        String raison = event.getOption("raison").getAsString();

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setThumbnail(Discord.avatar(user));
                        embedBuilder.setTitle("👍 Upvote");

                        if (!user.getId().equals(sender.getId())) {
                            if (canVote(sender, 60)) {
                                File file = FileSystem.file(user);
                                JsonFile fileConfiguration = new JsonFile(file);

                                embedBuilder.addField("Utilisateur", Discord.mention(user), true);
                                embedBuilder.addField("ID", user.getId(), true);
                                embedBuilder.addField("Par", Discord.mention(sender), true);
                                embedBuilder.addField("Raison", raison, true);

                                if (Discord.hasRole(sender, Discord.guild(), IDs.RoleModeration)) {
                                    int userUpvoteStaff = fileConfiguration.getInt("userUpvoteStaff");
                                    fileConfiguration.set("userUpvoteStaff", userUpvoteStaff + 1);
                                } else {
                                    int userUpvote = fileConfiguration.getInt("userUpvote");
                                    fileConfiguration.set("userUpvote", userUpvote + 1);
                                }
                                fileConfiguration.save();
                                updateCanVoteDate(sender);
                            } else {
                                embedBuilder.addField("Vous avez déjà voté il y a moins d'une heure",
                                        Discord.mention(user), true);
                            }
                        } else {
                            embedBuilder.addField("Vous ne pouvez pas voter pour vous même",
                                    Discord.mention(user), true);
                        }

                        event.replyEmbeds(embedBuilder.build()).queue();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    public static boolean canVote(User user, int min) {
        JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
        Date date = fileConfiguration.getDate("voteDate");
        if (date != null) {
            Date dateNow = new Date();
            Date dateCheck = casinoProfil.returnDateWithXMin(date, min);
            return dateNow.after(dateCheck);
        }
        return true;
    }

    public static void updateCanVoteDate(User user) {
        JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
        fileConfiguration.setDate("voteDate", new Date());
        fileConfiguration.save();
    }
}
