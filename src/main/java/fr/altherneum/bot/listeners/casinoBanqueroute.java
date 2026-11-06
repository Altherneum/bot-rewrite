package fr.altherneum.bot.listeners;

import java.io.IOException;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import fr.altherneum.bot.utils.FileSystem;
import fr.altherneum.bot.utils.JsonFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class casinoBanqueroute {
    private casinoBanqueroute() {
    }

    public static void startBanqueroute() {
        setAutoUP();
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReactionAdd(MessageReactionAddEvent event) {
                try {
                    if (!event.getChannel().getId().equals(IDs.CasinoBanqueRoute) || event.getUser().isBot()) {
                        return;
                    }

                    String mentionTagEmoji = Discord.emoji(event.getReaction().getEmoji());
                    event.getReaction().removeReaction(event.getUser()).queue();

                    if (mentionTagEmoji.equals(IDs.EmojiBanque)) {
                        doBanqueRoute(event.getUser(), event.getChannel().asGuildMessageChannel(), true);
                    }
                } catch (Exception ignored) {
                    // Comportement identique à l'ancien bot.
                }
            }
        });
    }

    public static void doBanqueRoute(User user, GuildMessageChannel serverTextChannel, int amount) {
        Thread thread = new Thread(() -> {
            try {
                boolean win = false;
                int amountPlayed = 0;
                int remaining = amount;
                int cost = getCost();

                while (remaining > 0 && !win) {
                    win = doBanqueRoute(user, serverTextChannel, false);
                    amountPlayed++;
                    remaining--;
                }

                if (win) {
                    int total = getTotal();
                    Discord.textChannel(IDs.CasinoTextuelResultat)
                            .sendMessageEmbeds(casinoProfil.gainGold(user, total,
                                    "🎉 Félicitations ! " + amountPlayed + " tentatives", serverTextChannel))
                            .queue();
                } else {
                    Discord.textChannel(IDs.CasinoTextuelResultat)
                            .sendMessageEmbeds(casinoProfil.gainGold(user, -cost, "Perdu " + amountPlayed + "x",
                                    serverTextChannel))
                            .queue();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        thread.start();
    }

    public static boolean doBanqueRoute(User user, GuildMessageChannel serverTextChannel, boolean message) throws IOException {
        int gold = casinoProfil.getGold(user);
        int total = getTotal();
        int cost = getCost();

        if (gold >= cost) {
            double rng = Math.random();
            if (rng < 0.001) {
                if (message) {
                    Discord.textChannel(IDs.CasinoTextuelResultat)
                            .sendMessageEmbeds(casinoProfil.gainGold(user, total, "🎉 Félicitations !", serverTextChannel))
                            .queue();
                } else {
                    JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
                    int currentGold = fileConfiguration.getInt("Gold");
                    fileConfiguration.set("Gold", currentGold + total);
                    fileConfiguration.save();
                }
                setTotal(-total);
                updateMessage();
                return true;
            } else {
                if (message) {
                    Discord.textChannel(IDs.CasinoTextuelResultat)
                            .sendMessageEmbeds(casinoProfil.gainGold(user, -cost, "Perdu", serverTextChannel))
                            .queue();
                } else {
                    JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
                    int currentGold = fileConfiguration.getInt("Gold");
                    fileConfiguration.set("Gold", currentGold - cost);
                    fileConfiguration.save();
                }
                setTotal(cost);
                updateMessage();
                return false;
            }
        } else {
            Discord.dmComplete(user, "Vous n'avez pas assez d'argent pour jouer au"
                    + "ServerTextChannel (id: " + serverTextChannel.getId() + ", name: "
                    + serverTextChannel.getName() + ")");
            return true;
        }
    }

    public static int getCost() {
        return 1000;
    }

    public static void updateMessage() throws IOException {
        int total = getTotal();
        int cost = getCost();

        long time = Instant.now().getEpochSecond();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        String message = "<t:" + time + ":R>\n"
                + "/banqueroute Nombre de tentatives\n"
                + "\n"
                + "\n**__Faites faire faillite à la banque !__**\n"
                + "\n"
                + "Vous avez **__0.1% de chance de gagner__**\n"
                + "Les **__pertes seront ajoutés à la cagnotte__**\n"
                + "**__Toutes les heures 10'000 golds sont ajoutés__** à la cagnotte\n"
                + "\n"
                + "Total à gagner : " + total + " :coin:\n"
                + "Coût de la tentative : **__" + cost + "__** " + IDs.EmojiCoin + "\n"
                + "\n"
                + "Pour jouer cliquer sur " + IDs.EmojiBanque + ", le message sera envoyé dans "
                + Discord.textChannel(IDs.CasinoTextuelResultat).getAsMention();

        embedBuilder.setDescription(message);

        GuildMessageChannel channel = Discord.textChannel(IDs.CasinoBanqueRoute);
        long latest = channel.getLatestMessageIdLong();
        if (latest != 0) {
            channel.retrieveMessageById(latest).queue(messageById -> {
                if (Discord.isSelfMessage(messageById)) {
                    messageById.editMessageEmbeds(embedBuilder.build()).queue();
                }
            });
        }
    }

    public static int getTotal() {
        JsonFile fileConfiguration = new JsonFile(FileSystem.BanqueRoute());
        return fileConfiguration.getInt("total");
    }

    public static void setTotal(int montant) {
        JsonFile fileConfiguration = new JsonFile(FileSystem.BanqueRoute());
        fileConfiguration.set("total", getTotal() + montant);
        fileConfiguration.save();
    }

    public static void setAutoUP() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    setTotal(10000);
                    updateMessage();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 1000, 1000 * 60 * 60);
    }
}
