package fr.altherneum.bot.listeners;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class boutique {
    private boutique() {
    }

    public static void onBoutique() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReactionAdd(MessageReactionAddEvent event) {
                if (!event.getChannel().getId().equals(IDs.CasinoBoutique) || event.getUser().isBot()) {
                    return;
                }
                try {
                    String mentionTagEmoji = Discord.emoji(event.getReaction().getEmoji());
                    event.getReaction().removeReaction(event.getUser()).queue();
                    if (mentionTagEmoji.equals(IDs.EmojiCrown)) { // VIP
                        User user = event.getUser();
                        int goldAmount = casinoProfil.getGold(user);
                        if (goldAmount >= IDs.prixVIP) {
                            if (Discord.hasRole(user, Discord.guild(), IDs.RoleVIP)) {
                                Discord.dm(user, "Vous êtes déjà " + IDs.RoleVIP.getName());
                            } else {
                                Discord.textChannel(IDs.Boost)
                                        .sendMessageEmbeds(casinoProfil.gainGold(user, -IDs.prixVIP, "Achat du VIP",
                                                Discord.textChannel(IDs.CasinoBoutique)))
                                        .queue();
                                Discord.addRole(user, IDs.RoleVIP);
                                Discord.dm(user, Discord.mention(user) + " est désormais " + IDs.RoleVIP.getName());
                            }
                        } else {
                            casinoProfil.msgPasAssezArgent(user, IDs.prixVIP);
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}
