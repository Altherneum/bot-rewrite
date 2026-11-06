package fr.altherneum.bot.listeners;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class ticketRoleReact {
    private ticketRoleReact() {
    }

    public static void ticketEvent() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReactionAdd(MessageReactionAddEvent event) {
                try {
                    if (!event.getChannel().getId().equals(IDs.Ticket)) {
                        return;
                    }
                    if (!Discord.emoji(event.getReaction().getEmoji()).equals(IDs.EmojiTicket)
                            || event.getUser().isBot()) {
                        return;
                    }
                    User user = event.getUser();
                    event.getReaction().removeReaction(user).queue();

                    for (TextChannel channel : Discord.guild().getTextChannels()) {
                        if (channel.getName().equals(IDs.EmojiTicket + user.getId())) {
                            Discord.dm(user, "Vous avez **__déjà un ticket ouvert__** : " + channel.getAsMention());
                            return;
                        }
                    }

                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    EnumSet<Permission> memberPermissions = EnumSet.of(Permission.VIEW_CHANNEL,
                            Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND);
                    EnumSet<Permission> directionPermissions = EnumSet.of(Permission.VIEW_CHANNEL,
                            Permission.MESSAGE_HISTORY, Permission.MESSAGE_SEND, Permission.MANAGE_CHANNEL);
                    Member member = Discord.member(user);

                    Discord.guild().createTextChannel("\uD83C\uDFAB" + user.getId(), IDs.CategoryTicket)
                            .setTopic("\uD83C\uDFAB par : " + Discord.mention(user) + ", le " + dateFormat.format(date))
                            .addPermissionOverride(member, memberPermissions, null)
                            .addPermissionOverride(IDs.RoleAssistance, memberPermissions, null)
                            .addPermissionOverride(IDs.RoleDirection, directionPermissions, null)
                            .addPermissionOverride(IDs.RoleFondateur, Discord.allPermissions(), null)
                            .queue(serverTextChannel -> {
                                serverTextChannel.sendMessage("L'" + IDs.RoleAssistance.getAsMention()
                                        + " arrive bientôt\n\n"
                                        + Discord.mention(user)
                                        + " Pour toutes demandes, **questions**, **recrutements**, **__suggestions__**, ou tout **__bugs__**,\n"
                                        + ":scroll: Merci de bien vouloir :warning: **__préciser la raison__** :warning: en premier message\n**__Merci d'envoyer le message avant l'arrivé de l'"
                                        + IDs.RoleAssistance.getAsMention() + "__**\n"
                                        + "Merci de consulter les "
                                        + Discord.textChannel(IDs.Regles).getAsMention()
                                        + " et notes sur les "
                                        + Discord.textChannel(IDs.Ticket).getAsMention()
                                        + " avant").queue();
                                Discord.dm(user, Discord.mention(user)
                                        + " Votre " + Discord.textChannel(IDs.Ticket).getAsMention()
                                        + " a été crée : " + serverTextChannel.getAsMention());
                            });
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}
