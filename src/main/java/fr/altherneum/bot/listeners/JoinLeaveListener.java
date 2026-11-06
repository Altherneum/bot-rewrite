package fr.altherneum.bot.listeners;

import java.util.Date;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import fr.altherneum.bot.utils.FileSystem;
import fr.altherneum.bot.utils.JsonFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class JoinLeaveListener {
    private JoinLeaveListener() {
    }

    public static void startLeaveJoinEvent() {
        memberLeaveListener();
        memberJoinListener();
    }

    public static void memberLeaveListener() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
                try {
                    User user = event.getUser();
                    JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
                    int total = 0;
                    int messages = fileConfiguration.getInt("Messages");
                    for (String invite : fileConfiguration.getStringList("ListeInvites")) {
                        total += fileConfiguration.getInt("InvitesCounted." + invite);
                    }
                    Date date2 = new Date(user.getTimeCreated().toInstant().toEpochMilli());
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setThumbnail(Discord.avatar(user));
                    embedBuilder.setTitle("\uD83D\uDEAB Aurevoir");
                    embedBuilder.setDescription(Discord.mention(user) + "  (`" + Discord.tag(user) + "`)"
                            + "\nSur Discord depuis le : **__" + StatsTimer.dateFormated(date2) + "__**"
                            + "\nIdentifiant unique : `" + user.getId() + "`");
                    embedBuilder.addField("Messages", "" + messages, true);
                    embedBuilder.addField("Invitations", "" + total, true);
                    Discord.textChannel(IDs.Invitation).sendMessageEmbeds(embedBuilder.build()).queue();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    public static void memberJoinListener() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onGuildMemberJoin(GuildMemberJoinEvent event) {
                try {
                    CheckInvite.onCheckInvite(event.getUser());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}
