package fr.altherneum.bot.listeners;

import fr.altherneum.bot.api.messages;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public final class bonusRoles {
    private bonusRoles() {
    }

    public static void Member(int i, User user) {
        if (user.isBot()) {
            return;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (i >= 1000) {
            Role role = IDs.Rolemsg1000;
            if (!Discord.hasRole(user, Discord.guild(), role)) {
                Discord.guild().addRoleToMember(user, role).queue();
                Discord.dm(user, Discord.mention(user) + IDs.MessageRoleGain + role.getName());
                embedBuilder.setDescription(messages.LevelUP(user, role, "1000 messages"));
                Discord.textChannel(IDs.Boost).sendMessageEmbeds(embedBuilder.build()).queue();
            }
            Role role2 = IDs.Rolemsg200;
            if (Discord.hasRole(user, Discord.guild(), role2)) {
                Discord.removeRole(user, role2);
            }
            return;
        }
        if (i >= 200) {
            Role role = IDs.Rolemsg200;
            if (!Discord.hasRole(user, Discord.guild(), role)) {
                Discord.guild().addRoleToMember(user, role).queue();
                Discord.dm(user, Discord.mention(user) + IDs.MessageRoleGain + role.getName());
                embedBuilder.setDescription(messages.LevelUP(user, role, "200 messages"));
                Discord.textChannel(IDs.Boost).sendMessageEmbeds(embedBuilder.build()).queue();
            }
            Role role2 = IDs.Rolemsg50;
            if (Discord.hasRole(user, Discord.guild(), role2)) {
                Discord.removeRole(user, role2);
            }
            return;
        }
        if (i >= 50) {
            Role role = IDs.Rolemsg50;
            if (!Discord.hasRole(user, Discord.guild(), role)) {
                Discord.guild().addRoleToMember(user, role).queue();
                Discord.dm(user, Discord.mention(user) + IDs.MessageRoleGain + role.getName());
                embedBuilder.setDescription(messages.LevelUP(user, role, "50 messages"));
                Discord.textChannel(IDs.Boost).sendMessageEmbeds(embedBuilder.build()).queue();
            }
            Role role2 = IDs.Rolemsg10;
            if (Discord.hasRole(user, Discord.guild(), role2)) {
                Discord.removeRole(user, role2);
            }
            return;
        }
        if (i >= 10) {
            Role role = IDs.Rolemsg10;
            if (!Discord.hasRole(user, Discord.guild(), role)) {
                Discord.guild().addRoleToMember(user, role).queue();
                Discord.dm(user, Discord.mention(user) + IDs.MessageRoleGain + role.getName());
                embedBuilder.setDescription(messages.LevelUP(user, role, "10 messages"));
                Discord.textChannel(IDs.Boost).sendMessageEmbeds(embedBuilder.build()).queue();
            }
        }
    }

    public static void InviteRole(int i, User user) {
        if (user.isBot()) {
            return;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (i >= 100) {
            Role role = IDs.Roleinvites100;
            if (!Discord.hasRole(user, Discord.guild(), role)) {
                Discord.guild().addRoleToMember(user, role).queue();
                Discord.dm(user, Discord.mention(user) + IDs.MessageRoleGain + role.getName());
                embedBuilder.setDescription(messages.LevelUP(user, role, "100 invitations"));
                Discord.textChannel(IDs.Boost).sendMessageEmbeds(embedBuilder.build()).queue();
            }
            Role role2 = IDs.Roleinvites50;
            if (Discord.hasRole(user, Discord.guild(), role2)) {
                Discord.removeRole(user, role2);
            }
            return;
        }
        if (i >= 50) {
            Role role = IDs.Roleinvites50;
            if (!Discord.hasRole(user, Discord.guild(), role)) {
                Discord.guild().addRoleToMember(user, role).queue();
                Discord.dm(user, Discord.mention(user) + IDs.MessageRoleGain + role.getName());
                embedBuilder.setDescription(messages.LevelUP(user, role, "50 invitations"));
                Discord.textChannel(IDs.Boost).sendMessageEmbeds(embedBuilder.build()).queue();
            }
            Role role2 = IDs.Roleinvites30;
            if (Discord.hasRole(user, Discord.guild(), role2)) {
                Discord.removeRole(user, role2);
            }
            return;
        }
        if (i >= 30) {
            Role role = IDs.Roleinvites30;
            if (!Discord.hasRole(user, Discord.guild(), role)) {
                Discord.guild().addRoleToMember(user, role).queue();
                Discord.dm(user, Discord.mention(user) + IDs.MessageRoleGain + role.getName());
                embedBuilder.setDescription(messages.LevelUP(user, role, "30 invitations"));
                Discord.textChannel(IDs.Boost).sendMessageEmbeds(embedBuilder.build()).queue();
            }
            Role role2 = IDs.Roleinvites10;
            if (Discord.hasRole(user, Discord.guild(), role2)) {
                Discord.removeRole(user, role2);
            }
            return;
        }
        if (i >= 10) {
            Role role = IDs.Roleinvites10;
            if (!Discord.hasRole(user, Discord.guild(), role)) {
                Discord.guild().addRoleToMember(user, role).queue();
                Discord.dm(user, Discord.mention(user) + IDs.MessageRoleGain + role.getName());
                embedBuilder.setDescription(messages.LevelUP(user, role, "10 invitations"));
                Discord.textChannel(IDs.Boost).sendMessageEmbeds(embedBuilder.build()).queue();
            }
        }
    }
}
