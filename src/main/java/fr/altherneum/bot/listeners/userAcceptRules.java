package fr.altherneum.bot.listeners;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class userAcceptRules {
    private userAcceptRules() {
    }

    public static void onUserAcceptRules() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReactionAdd(MessageReactionAddEvent event) {
                if (event.getChannel().getId().equals(IDs.Regles) && !event.getUser().isBot()) {
                    User user = event.getUser();

                    Discord.addRole(user, IDs.RoleReglesValides);

                    for (Role role : IDs.rolesSplitterList()) {
                        Discord.addRole(user, role);
                    }

                    Discord.dm(user, Discord.mention(user) + IDs.MessageRoleGain
                            + IDs.RoleReglesValides.getName() + "\n" + "**__Pensez à prendre vos "
                            + Discord.textChannel(IDs.AutresJeux).getAsMention() + "__**, "
                            + Discord.textChannel(IDs.Notifications).getAsMention() + ", et votre "
                            + Discord.textChannel(IDs.Profil).getAsMention());
                }
            }
        });

        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
                if (event.getChannel().getId().equals(IDs.Regles) && !event.getUser().isBot()) {
                    User user = event.getUser();
                    Discord.removeRole(user, IDs.RoleReglesValides);
                    Discord.dm(user, Discord.mention(user) + IDs.MessageRolePerte
                            + IDs.RoleReglesValides.getName());
                }
            }
        });
    }
}
