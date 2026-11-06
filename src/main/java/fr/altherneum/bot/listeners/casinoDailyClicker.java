package fr.altherneum.bot.listeners;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class casinoDailyClicker {
    private casinoDailyClicker() {
    }

    public static void onCasinoDailyClicker() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReactionAdd(MessageReactionAddEvent event) {
                if (!event.getChannel().getId().equals(IDs.CasinoDailyClick) || event.getUser().isBot()) {
                    return;
                }
                try {
                    User user = event.getUser();
                    String mentionTagEmoji = Discord.emoji(event.getReaction().getEmoji());
                    event.getReaction().removeReaction(user).queue();

                    if (mentionTagEmoji.equals(IDs.EmojiProfil)) { // profil
                        Discord.textChannel(IDs.CasinoTextuelResultat)
                                .sendMessageEmbeds(casinoProfil.profil(user))
                                .queue();
                    } else if (mentionTagEmoji.equals(IDs.EmojiMouseThreeButton)) { // dailyClick
                        if (casinoProfil.canPlay(user, 24 * 60, "DayliClaim")
                                || Discord.hasRole(user, Discord.guild(), IDs.RoleCasinoBypass)) {
                            int passifs = casinoProfil.getPlayPassif(user);
                            casinoProfil.clickDoPassif(user);
                            casinoProfil.updatePlayDate(user, "DayliClaim");
                            int passifGains = casinoProfil.getPlayPassif(user) - passifs;
                            int dayliClaim = casinoProfil.getPlayCount(user);
                            Discord.textChannel(IDs.CasinoTextuelResultat).sendMessageEmbeds(
                                    casinoProfil.gainGold(user,
                                            250, "Clique",
                                            passifs, passifGains, "Passifs 📆",
                                            dayliClaim, "Clique 🕙",
                                            event.getChannel().asTextChannel()))
                                    .queue();
                        } else {
                            Discord.dm(user, "Vous avez **__déjà joué dans les dernières 24H__** au "
                                    + event.getChannel().asTextChannel().getAsMention());
                        }
                    } else if (mentionTagEmoji.equals(IDs.EmojiCalendar)) { // passif
                        int playPassifAmount = casinoProfil.getPlayPassif(user);
                        if (playPassifAmount >= 1) {
                            casinoProfil.updatePlayPassif(user, -playPassifAmount);
                            Discord.textChannel(IDs.CasinoTextuelResultat)
                                    .sendMessageEmbeds(casinoProfil.gainGold(user, playPassifAmount,
                                            "Gains passifs",
                                            event.getChannel().asTextChannel()))
                                    .queue();
                        } else {
                            Discord.dm(user, "Vous n'avez **__pas de gains passifs à récupérer__** au "
                                    + event.getChannel().asTextChannel().getAsMention());
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}
