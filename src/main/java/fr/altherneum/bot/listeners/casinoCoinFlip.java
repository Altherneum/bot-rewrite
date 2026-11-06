package fr.altherneum.bot.listeners;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public final class casinoCoinFlip {
    private casinoCoinFlip() {
    }

    public static void startCoinFlip() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReactionAdd(MessageReactionAddEvent event) {
                if (!event.getChannel().getId().equals(IDs.CasinoCoinFlip) || event.getUser().isBot()) {
                    return;
                }
                try {
                    String mentionTagEmoji = Discord.emoji(event.getReaction().getEmoji());
                    event.getReaction().removeReaction(event.getUser()).queue();

                    if (mentionTagEmoji.equals(IDs.EmojiCoin)) { // CoinFlip
                        doCoinFlip(event.getUser(), event.getChannel().asTextChannel(), IDs.prixCoinFlip);
                    }
                } catch (Exception ignored) {
                    // Comportement identique à l'ancien bot.
                }
            }
        });
    }

    public static int doCoinFlip(User user, TextChannel serverTextChannel, int prix) {
        int goldAmount = casinoProfil.getGold(user);
        if (goldAmount >= prix) {
            double rng = Math.random();
            if (rng > 0.5) {
                Discord.textChannel(IDs.CasinoTextuelResultat)
                        .sendMessageEmbeds(casinoProfil.gainGold(user, -prix, "Coin flip perdu", serverTextChannel))
                        .queue();
                return 0;
            } else {
                Discord.textChannel(IDs.CasinoTextuelResultat)
                        .sendMessageEmbeds(casinoProfil.gainGold(user, prix, "Coin flip gagné", serverTextChannel))
                        .queue();
                return 1;
            }
        } else {
            casinoProfil.msgPasAssezArgent(user, prix);
            return 2;
        }
    }

    public static void coinFlipCmd() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
                if (event.getName().equalsIgnoreCase("coinflip")) {
                    try {
                        OptionMapping option = event.getOption("argent");
                        int prix = (int) Math.round(option == null ? 0.0 : option.getAsDouble());
                        if (prix < 0) {
                            prix = 0;
                        }

                        int coinflipRes = doCoinFlip(event.getUser(), event.getChannel().asTextChannel(), prix);

                        String str;
                        if (coinflipRes == 0) {
                            str = "**__Perdu__**";
                        } else if (coinflipRes == 1) {
                            str = "**__Gagné !__**";
                        } else {
                            str = "**__Erreur, pas assez d'argent__**";
                        }

                        event.reply(Discord.mention(event.getUser())
                                + " coin flip lancé : "
                                + Discord.textChannel(IDs.CasinoTextuelResultat).getAsMention()
                                + ", " + str).queue();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }
}
