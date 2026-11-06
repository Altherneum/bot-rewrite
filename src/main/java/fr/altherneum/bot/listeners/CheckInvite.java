package fr.altherneum.bot.listeners;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import fr.altherneum.bot.utils.FileSystem;
import fr.altherneum.bot.utils.JsonFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class CheckInvite {
    public static List<Invite> before = null;

    private CheckInvite() {
    }

    public static void start() {
        before = Discord.guild().retrieveInvites().complete();
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onGuildInviteCreate(GuildInviteCreateEvent event) {
                try {
                    before = Discord.guild().retrieveInvites().complete();
                    String code = event.getInvite().getCode();
                    for (Invite invite : Discord.guild().retrieveInvites().complete()) {
                        if (invite.getCode().equals(code)) {
                            User inviter = invite.getInviter();
                            if (inviter == null) {
                                break;
                            }
                            if (invite.getMaxUses() == 0 && invite.getMaxAge() == 0) {
                                Discord.dm(inviter,
                                        "\uD83D\uDE80Tu a crée une **invitation pour rejoindre le serveur sans limite**, elle sera **compté par le bot**, la voici : "
                                                + invite.getUrl());
                            } else {
                                Discord.dm(inviter,
                                        "\uD83D\uDE80Tu a crée une **invitation pour rejoindre le serveur**, elle sera **compté par le bot**, la voici : "
                                                + invite.getUrl()
                                                + "\n__**Attention, vous n'avez pas généré une invitation définitive**__ !");
                            }
                            break;
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    public static void onCheckInvite(User user) {
        List<Invite> after = Discord.guild().retrieveInvites().complete();
        for (Invite inviteBefore : before) {
            for (Invite inviteAfter : after) {
                if (inviteBefore.getUses() < inviteAfter.getUses()
                        && inviteBefore.getCode().equals(inviteAfter.getCode())) {
                    before = Discord.guild().retrieveInvites().complete();
                    User inviter = inviteBefore.getInviter();
                    if (inviter == null) {
                        continue;
                    }

                    File file = FileSystem.file(inviter);
                    File fileGlobal = FileSystem.fileGlobal();
                    JsonFile fileConfiguration = new JsonFile(file);
                    JsonFile fileConfigurationGlobal = new JsonFile(fileGlobal);

                    List<String> listGlobal = fileConfigurationGlobal.getStringList("globalInvites");
                    if (!listGlobal.contains(user.getId())) {
                        listGlobal.add(user.getId());
                        fileConfigurationGlobal.set("globalInvites", listGlobal);
                        fileConfigurationGlobal.save();

                        int gold = fileConfiguration.getInt("Gold") + 250;
                        fileConfiguration.set("Gold", gold);
                        fileConfiguration.set("InvitesCounted." + inviteAfter.getCode(),
                                fileConfiguration.getInt("InvitesCounted." + inviteAfter.getCode()) + 1);
                        List<String> list = fileConfiguration.getStringList("ListeInvites");
                        if (!list.contains(inviteBefore.getCode())) {
                            list.add(inviteBefore.getCode());
                            fileConfiguration.set("ListeInvites", list);
                        }
                        fileConfiguration.save();

                        fileConfiguration = new JsonFile(file);
                        int linkCountedUses = fileConfiguration.getInt("InvitesCounted." + inviteAfter.getCode());
                        int total = 0;
                        for (String invite : fileConfiguration.getStringList("ListeInvites")) {
                            total += fileConfiguration.getInt("InvitesCounted." + invite);
                        }
                        bonusRoles.InviteRole(total, inviter);

                        File fileEvent = FileSystem.event();
                        JsonFile fileConfigurationEvent = new JsonFile(fileEvent);
                        boolean started = fileConfigurationEvent.getBoolean("Started");
                        String eventMsg = "";
                        if (started) {
                            Date date = new Date();
                            int day = fileConfigurationEvent.getInt("DayEnd");
                            int month = fileConfigurationEvent.getInt("MonthEnd") - 1;
                            int year = fileConfigurationEvent.getInt("YearEnd");
                            Date dateEventEnd = new GregorianCalendar(year, month, day).getTime();
                            if (date.before(dateEventEnd)) {
                                fileConfiguration = new JsonFile(FileSystem.file(inviter));
                                fileConfiguration.set("event", fileConfiguration.getInt("event") + 1);
                                fileConfiguration.save();
                                eventMsg = "\n\nTu est à " + fileConfiguration.getInt("event")
                                        + " invitations pour le concours ! \uD83C\uDF81 \uD83D\uDCC8";
                                Discord.dmComplete(user, eventMsg);
                            }
                        }

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setThumbnail(Discord.avatar(user));
                        embedBuilder.setTitle("\uD83C\uDD95 Arrivée");
                        Date date2 = new Date(user.getTimeCreated().toInstant().toEpochMilli());
                        embedBuilder.setDescription("Bienvenue à toi " + Discord.mention(user) + "  (`"
                                + Discord.tag(user) + "`)\nSur Discord depuis le : **__"
                                + StatsTimer.dateFormated(date2) + "__**\nIdentifiant unique : `" + user.getId()
                                + "`\n\n");
                        embedBuilder.addField("Invité par", Discord.mention(inviter), true);
                        embedBuilder.addField("Lien", ".gg/" + inviteAfter.getCode(), true);
                        embedBuilder.addField("Cliques", "" + inviteAfter.getUses(), true);
                        embedBuilder.addField("Invitations", "" + linkCountedUses, true);
                        embedBuilder.addField("Totaux", "" + total, true);

                        Discord.textChannel(IDs.Invitation).sendMessageEmbeds(embedBuilder.build()).queue();
                    } else {
                        int linkCountedUses = fileConfiguration.getInt("InvitesCounted." + inviteAfter.getCode());
                        int total = 0;
                        for (String invite : fileConfiguration.getStringList("ListeInvites")) {
                            total += fileConfiguration.getInt("InvitesCounted." + invite);
                        }

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setThumbnail(Discord.avatar(user));
                        embedBuilder.setTitle("\uD83C\uDD95 Retour");
                        Date date2 = new Date(user.getTimeCreated().toInstant().toEpochMilli());
                        embedBuilder.setDescription("Bon retour à toi " + Discord.mention(user) + "  (`"
                                + Discord.tag(user) + "`)\nSur Discord depuis le : **__"
                                + StatsTimer.dateFormated(date2) + "__**\nIdentifiant unique : `" + user.getId()
                                + "`\n\n");
                        embedBuilder.addField("Invité par", Discord.mention(inviter), true);
                        embedBuilder.addField("Lien", ".gg/" + inviteAfter.getCode(), true);
                        embedBuilder.addField("Cliques", "" + inviteAfter.getUses(), true);
                        embedBuilder.addField("Invitations", "" + linkCountedUses, true);
                        embedBuilder.addField("Totaux", "" + total, true);

                        Discord.textChannel(IDs.Invitation).sendMessageEmbeds(embedBuilder.build()).queue();
                    }

                    Date dateUserJoinDiscord = new Date(user.getTimeCreated().toInstant().toEpochMilli());

                    Date dateNow = new Date();
                    int monthToCheck = 3;
                    dateNow.setMonth(dateNow.getMonth() - monthToCheck);

                    Date dateNow2 = new Date();
                    int monthToCheck2 = 9;
                    dateNow2.setMonth(dateNow2.getMonth() - monthToCheck2);

                    if (dateUserJoinDiscord.after(dateNow)) {
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setThumbnail(Discord.avatar(user));
                        embedBuilder.setTitle("🚨 Refus automatique");
                        embedBuilder.addField("Utilisateur", Discord.mention(user), true);
                        embedBuilder.addField("ID", user.getId(), true);
                        embedBuilder.addField("Raison", "Compte crée il y a moins de " + monthToCheck + " mois", true);
                        embedBuilder.addField("Date du compte",
                                "<t:" + user.getTimeCreated().toEpochSecond() + ":R>", true);

                        Discord.textChannel(IDs.Sanctions).sendMessageEmbeds(embedBuilder.build()).queue();
                        Discord.textChannel(IDs.AutoMod).sendMessageEmbeds(embedBuilder.build()).queue();
                        Discord.dmComplete(user, embedBuilder.build());

                        Discord.guild().kick(user).reason("Auto kick " + monthToCheck + " mois").queue();
                    } else if (dateUserJoinDiscord.after(dateNow2)) {
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setThumbnail(Discord.avatar(user));
                        embedBuilder.setTitle("🚼 Compte récent");
                        embedBuilder.addField("Utilisateur", Discord.mention(user), true);
                        embedBuilder.addField("ID", user.getId(), true);
                        embedBuilder.addField("Raison", "Compte crée il y a moins de " + monthToCheck2 + " mois", true);
                        embedBuilder.addField("Date du compte",
                                "<t:" + user.getTimeCreated().toEpochSecond() + ":R>", true);

                        Discord.textChannel(IDs.AutoMod).sendMessageEmbeds(embedBuilder.build()).queue();
                    }
                    return;
                }
            }
        }
        before = Discord.guild().retrieveInvites().complete();
    }
}
