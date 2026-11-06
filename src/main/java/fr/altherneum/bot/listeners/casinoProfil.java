package fr.altherneum.bot.listeners;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import fr.altherneum.bot.utils.FileSystem;
import fr.altherneum.bot.utils.JsonFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;

public final class casinoProfil {
    private casinoProfil() {
    }

    public static MessageEmbed profil(User user) {
        net.dv8tion.jda.api.entities.Guild server = Discord.guild();
        File file = FileSystem.file(user);
        JsonFile fileConfiguration = new JsonFile(file);

        int gold = fileConfiguration.getInt("Gold");
        int messages = fileConfiguration.getInt("Messages");
        int voc = fileConfiguration.getInt("voiceTime");
        int passifs = getPlayPassif(user);
        int dayliClaim = fileConfiguration.getInt("DayliClaimCount");
        int totalInvites = 0;
        for (String invite : fileConfiguration.getStringList("ListeInvites")) {
            totalInvites += fileConfiguration.getInt("InvitesCounted." + invite);
        }
        int userUpvote = fileConfiguration.getInt("userUpvote");
        int userUpvoteStaff = fileConfiguration.getInt("userUpvoteStaff");

        Member member = server.getMember(user);
        if (member == null) {
            // JavaCord levait une exception via getJoinedAtTimestamp(server).get().
            throw new IllegalStateException("Membre introuvable dans le serveur : " + user.getId());
        }
        Date date = new Date(member.getTimeJoined().toInstant().toEpochMilli());
        Date date2 = new Date(user.getTimeCreated().toInstant().toEpochMilli());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setThumbnail(Discord.avatar(user));
        embedBuilder.setTitle("\uD83D\uDC8E Profil");
        embedBuilder.setDescription(Discord.mention(user) + "  (`" + Discord.tag(user)
                + "`)\n \uD83D\uDE80 a rejoint le : **__" + StatsTimer.dateFormated(date) + "__**"
                + "\nSur Discord depuis le : **__" + StatsTimer.dateFormated(date2) + "__**"
                + "\nIdentifiant unique : `" + user.getId() + "`");
        embedBuilder.addField("Rôle le plus haut", Discord.highestRole(user, server).getAsMention(), true);
        embedBuilder.addField("    💰 Argent", "" + gold, true);
        embedBuilder.addField("    💬 Messages", "" + messages, true);
        embedBuilder.addField("    📞 Vocal", "" + VoiceCounter.voiceSecFormat(voc), true);
        embedBuilder.addField("    💌 Invitations", "" + totalInvites, true);
        embedBuilder.addField("    ⏳ Gains passifs", "" + passifs, true);
        embedBuilder.addField("    🕙 Clique journalier", "" + dayliClaim, true);
        embedBuilder.addField("    👍 Réputation", "" + userUpvote, true);
        embedBuilder.addField("    👍 Réputation staff", "" + userUpvoteStaff, true);

        List<Role> roles = member == null ? new ArrayList<>() : member.getRoles();
        ArrayList<Role> rolesEnd = new ArrayList<>();

        if (roles.contains(IDs.RoleHomme)) {
            rolesEnd.add(IDs.RoleHomme);
        } else if (roles.contains(IDs.RoleFemme)) {
            rolesEnd.add(IDs.RoleFemme);
        }

        if (roles.contains(IDs.RoleMineur)) {
            rolesEnd.add(IDs.RoleMineur);
        } else if (roles.contains(IDs.RoleMajeur)) {
            rolesEnd.add(IDs.RoleMajeur);
        }

        if (roles.contains(IDs.RoleModeration)) {
            rolesEnd.add(IDs.RoleModeration);
        }

        StringBuilder rolesStr = new StringBuilder();
        for (Role role : rolesEnd) {
            rolesStr.append(role.getAsMention()).append(", ");
        }
        embedBuilder.addField("📍 Rôles spéciaux", rolesStr.toString(), false);

        return embedBuilder.build();
    }

    public static MessageEmbed gainGold(User user, int montant, String text, GuildMessageChannel textChannel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("\uD83C\uDFB0 Casino \uD83C\uDFB2 \uD83C\uDFB3");
        embedBuilder.setDescription(textChannel.getAsMention() + " " + Discord.mention(user));
        embedBuilder.setThumbnail(Discord.avatar(user));
        if (montant >= 1) {
            embedBuilder.addField("Gains : " + text, montant + " \uD83D\uDCB0", true);
        } else {
            embedBuilder.addField("Pertes : " + text, montant + " \uD83D\uDCB0", true);
        }
        JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
        int gold = fileConfiguration.getInt("Gold");
        embedBuilder.addField("Total", (montant + gold) + " \uD83D\uDCB0", true);
        fileConfiguration.set("Gold", gold + montant);
        fileConfiguration.save();
        return embedBuilder.build();
    }

    public static MessageEmbed gainGold(User user,
            int montant, String text, int montant2,
            int montant3, String text2,
            int montant4, String text3,
            GuildMessageChannel textChannel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("\uD83C\uDFB0 Casino \uD83C\uDFB2 \uD83C\uDFB3");
        embedBuilder.setDescription(textChannel.getAsMention() + " " + Discord.mention(user));
        embedBuilder.setThumbnail(Discord.avatar(user));
        if (montant >= 1) {
            embedBuilder.addField("Gains : " + text, montant + " \uD83D\uDCB0", true);
        } else {
            embedBuilder.addField("Pertes : " + text, montant + " \uD83D\uDCB0", true);
        }

        JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
        int gold = fileConfiguration.getInt("Gold");
        embedBuilder.addField("Total", (montant + gold) + " \uD83D\uDCB0", true);
        fileConfiguration.set("Gold", gold + montant);
        fileConfiguration.save();

        embedBuilder.addField(text2 + " : ", montant2 + " + " + montant3 + " \uD83D\uDCB0", true);
        embedBuilder.addField(text3 + " : ", montant4 + "", true);

        return embedBuilder.build();
    }

    public static boolean canPlay(User user, int min, String gameName) {
        JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
        Date date = fileConfiguration.getDate(gameName);
        if (date != null) {
            Date dateNow = new Date();
            Date dateCheck = returnDateWithXMin(date, min);
            return dateNow.after(dateCheck);
        }
        return true;
    }

    public static void updatePlayDate(User user, String gameName) {
        JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
        fileConfiguration.setDate(gameName, new Date());
        fileConfiguration.set(gameName + "Count", fileConfiguration.getInt(gameName + "Count") + 1);
        fileConfiguration.save();
    }

    public static Date getPlayDate(User user, String gameName) {
        JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
        return fileConfiguration.getDate(gameName);
    }

    public static void clickDoPassif(User user) {
        updatePlayPassif(user, 100);
        updatePlayPassif(user, (int) (getPlayPassif(user) * 0.25));
    }

    public static void updatePlayPassif(User user, int montant) {
        JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
        fileConfiguration.set("GainsPassifs", fileConfiguration.getInt("GainsPassifs") + montant);
        fileConfiguration.save();
    }

    public static int getPlayPassif(User user) {
        JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
        return fileConfiguration.getInt("GainsPassifs");
    }

    public static int getPlayCount(User user) {
        JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
        return fileConfiguration.getInt("DayliClaimCount");
    }

    public static int getGold(User user) {
        JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
        return fileConfiguration.getInt("Gold");
    }

    public static Date returnDateWithXMin(Date date, int mins) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, mins);
        return calendar.getTime();
    }

    public static void msgPasAssezArgent(User user, int prix) {
        Discord.dmComplete(user, "__**Vous n'avez pas assez d'argent**__.\nVous avez " + getGold(user) + " sur " + prix
                + IDs.EmojiMoneyWithWings + " requis");
    }
}
