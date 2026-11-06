package fr.altherneum.bot.api;

import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public final class HigherRole {
    private HigherRole() {
    }

    public static boolean isRoleHigher(User user, User user2) {
        Guild guild = Discord.guild();
        return Discord.highestRolePosition(user, guild) > Discord.highestRolePosition(user2, guild);
    }
}
