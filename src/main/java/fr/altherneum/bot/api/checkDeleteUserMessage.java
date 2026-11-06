package fr.altherneum.bot.api;

import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;

public final class checkDeleteUserMessage {
    private checkDeleteUserMessage() {
    }

    public static void deleteAllMessageFromUser(String reason, User whoRun, User leaved) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(10);
                for (GuildMessageChannel channel : Discord.textChannels()) {
                    for (Message message : channel.getIterableHistory().stream().toList()) {
                        Thread.sleep(100);
                        if (message.getAuthor().getId().equals(leaved.getId())) {
                            GuildMessageChannel logs = Discord.textChannel(IDs.LogsCmd);
                            String name = Discord.tag(message.getAuthor());
                            String messageText = message.getContentRaw();
                            logs.sendMessage("**Suppression " + reason + "** : " + name + " par __"
                                    + Discord.mention(whoRun) + "__\n||" + messageText + "||").queue();
                            message.delete().queue();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        thread.start();
    }

    public static void deleteAllMessageFromChannel(GuildMessageChannel serverTextChannel, String reason, User whoRun) {
        Thread thread = new Thread(() -> {
            logCommands.logCommandChannel("emptyChannel", reason, serverTextChannel, whoRun);
            for (Message message : serverTextChannel.getIterableHistory().stream().toList()) {
                message.delete().queue();
            }
        });
        thread.start();
    }
}
