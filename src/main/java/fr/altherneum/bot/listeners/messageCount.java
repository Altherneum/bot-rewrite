package fr.altherneum.bot.listeners;

import java.io.File;
import java.util.List;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.utils.FileSystem;
import fr.altherneum.bot.utils.JsonFile;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class messageCount {
    private messageCount() {
    }

    public static void messageListener() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReceived(MessageReceivedEvent event) {
                try {
                    Member member = event.getMember();
                    if (member == null || event.getAuthor().isBot() || event.isWebhookMessage()) {
                        return;
                    }
                    if (member.isTimedOut()) {
                        return;
                    }

                    User author = event.getAuthor();
                    event.getChannel().getHistoryBefore(event.getMessage(), 1).queue(history -> {
                        List<Message> previous = history.getRetrievedHistory();
                        if (previous.isEmpty()) {
                            return;
                        }
                        if (!previous.get(0).getAuthor().getId().equals(author.getId())) {
                            File file = FileSystem.file(author);
                            JsonFile fileConfiguration = new JsonFile(file);
                            int messages = fileConfiguration.getInt("Messages") + 1;
                            int gold = fileConfiguration.getInt("Gold") + 5;
                            fileConfiguration.set("Gold", gold);
                            fileConfiguration.set("Messages", messages);
                            fileConfiguration.save();
                            bonusRoles.Member(messages, author);
                        }
                    });
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}
