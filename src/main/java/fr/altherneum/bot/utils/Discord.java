package fr.altherneum.bot.utils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import net.dv8tion.jda.api.entities.ApplicationInfo;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildMessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;

/**
 * Raccourcis de conversion entre l'API JavaCord de l'ancien bot et JDA.
 */
public final class Discord {
    private Discord() {
    }

    public static Guild guild() {
        return require(Main.jda.getGuildById(IDs.serverID), "Serveur " + IDs.serverID);
    }

    public static Guild guild(String id) {
        return require(Main.jda.getGuildById(id), "Serveur " + id);
    }

    /**
     * Retrouve un salon textuel ou d'annonces.
     *
     * <p>JavaCord exposait les salons {@code GUILD_TEXT} et {@code GUILD_NEWS}
     * sous le même type {@code ServerTextChannel}. JDA les sépare en
     * {@code TextChannel} et {@code NewsChannel} : on renvoie donc leur
     * supertype commun, qui garde toutes les opérations utilisées par le bot
     * (envoi, historique, invitation, mention...).
     */
    public static StandardGuildMessageChannel textChannel(String id) {
        return require(textChannelOrNull(id), "Salon " + id);
    }

    public static StandardGuildMessageChannel textChannelOrNull(String id) {
        GuildChannel channel = Main.jda.getGuildChannelById(id);
        return channel instanceof StandardGuildMessageChannel messageChannel ? messageChannel : null;
    }

    /**
     * Équivalent de {@code Server#getTextChannels()} JavaCord : salons texte
     * et salons d'annonces.
     */
    public static List<GuildMessageChannel> textChannels() {
        List<GuildMessageChannel> channels = new ArrayList<>();
        channels.addAll(guild().getTextChannels());
        channels.addAll(guild().getNewsChannels());
        return channels;
    }

    /**
     * Discord n'autorise un bot à modifier que ses propres messages. JDA lève
     * une exception locale avant l'appel REST, contrairement à JavaCord qui
     * laissait simplement échouer la requête. On évite donc de tenter l'édition
     * des messages d'un autre compte bot (cas d'un changement de token).
     */
    public static boolean isSelfMessage(Message message) {
        return message != null && message.getAuthor().getIdLong() == Main.jda.getSelfUser().getIdLong();
    }

    public static Category category(String id) {
        return require(Main.jda.getCategoryById(id), "Catégorie " + id);
    }

    public static Role role(String id) {
        return require(Main.jda.getRoleById(id), "Rôle " + id);
    }

    public static Member member(User user) {
        return guild().getMember(user);
    }

    public static Member member(Guild guild, User user) {
        return guild.getMember(user);
    }

    public static String mention(User user) {
        return user == null ? "" : user.getAsMention();
    }

    public static String mention(Member member) {
        return member == null ? "" : member.getUser().getAsMention();
    }

    public static String tag(User user) {
        // Équivalent exact de Javacord User#getDiscriminatedName() :
        // getName() + "#" + getDiscriminator(). Ne pas utiliser getAsTag(),
        // qui transforme le discriminateur "0" des comptes migrés en "0000".
        return user == null ? "" : user.getName() + "#" + user.getDiscriminator();
    }

    public static String avatar(User user) {
        return user == null ? null : user.getEffectiveAvatarUrl();
    }

    public static String emoji(Emoji emoji) {
        return emoji == null ? "" : emoji.getFormatted();
    }

    public static boolean hasRole(User user, Guild guild, Role role) {
        Member member = guild.getMember(user);
        return member != null && member.getRoles().contains(role);
    }

    public static void addRole(User user, Role role) {
        guild().addRoleToMember(user, role).queue();
    }

    public static void removeRole(User user, Role role) {
        guild().removeRoleFromMember(user, role).queue();
    }

    /**
     * Rôle le plus haut d'un membre, avec repli sur @everyone si le membre n'a
     * aucun rôle (JDA n'inclut pas @everyone dans {@link Member#getRoles()}).
     */
    public static Role highestRole(User user, Guild guild) {
        Member member = guild.getMember(user);
        if (member == null || member.getRoles().isEmpty()) {
            return guild.getPublicRole();
        }
        Role highest = member.getRoles().get(0);
        for (Role role : member.getRoles()) {
            if (role.getPosition() > highest.getPosition()) {
                highest = role;
            }
        }
        return highest;
    }

    public static int highestRolePosition(User user, Guild guild) {
        Member member = guild.getMember(user);
        if (member == null || member.getRoles().isEmpty()) {
            return 0;
        }
        int highest = 0;
        for (Role role : member.getRoles()) {
            highest = Math.max(highest, role.getPosition());
        }
        return highest;
    }

    public static void dm(User user, String content) {
        user.openPrivateChannel().queue(channel -> channel.sendMessage(content).queue());
    }

    public static void dm(User user, MessageEmbed embed) {
        user.openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(embed).queue());
    }

    /**
     * Envoi bloquant, utilisé quand l'ancien bot faisait {@code .get()} avant
     * de poursuivre une sanction (si le DM échoue, la sanction est annulée).
     */
    public static void dmComplete(User user, MessageEmbed embed) {
        user.openPrivateChannel().complete().sendMessageEmbeds(embed).complete();
    }

    public static void dmComplete(User user, String content) {
        user.openPrivateChannel().complete().sendMessage(content).complete();
    }

    public static boolean isBotOwner(User user) {
        if (user == null) {
            return false;
        }
        ApplicationInfo info = Main.jda.retrieveApplicationInfo().complete();
        if (info.getTeam() != null) {
            return info.getTeam().isMember(user);
        }
        return info.getOwner() != null && info.getOwner().getId().equals(user.getId());
    }

    public static EnumSet<net.dv8tion.jda.api.Permission> allPermissions() {
        return EnumSet.complementOf(EnumSet.of(net.dv8tion.jda.api.Permission.UNKNOWN));
    }

    private static <T> T require(T value, String label) {
        return Objects.requireNonNull(value, label + " introuvable");
    }
}
