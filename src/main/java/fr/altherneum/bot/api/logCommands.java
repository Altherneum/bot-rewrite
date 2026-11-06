package fr.altherneum.bot.api;

import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public final class logCommands {
    private logCommands() {
    }

    public static void logCommandChannel(String cmdName, String raison, TextChannel channel, User user) {
        Discord.textChannel(IDs.LogsCmd)
                .sendMessage("/**__" + cmdName + "__** : " + raison + " : " + channel.getAsMention() + " par "
                        + Discord.mention(user))
                .queue();
    }
}
