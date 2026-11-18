package fr.altherneum.bot.api;

import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public final class messages {
    private messages() {
    }

    public static String message() {
        return "";
    }

    public static String pub() {
        return "🚀 Altherneum . fr / discord\n\n🎮   Jeux-vidéos rôles et forum pour rencontrer des joueurs et parler de tes jeux favoris\n    ⚔️   Hub de serveurs Minecraft      ~      play.altherneum.fr 🔱 (Premium 1.9 -> dernières versions)\n                 RPG, PvP Box, ... (En développement)\n                 SkyBlock Ouvert et public\n                 Survie Survie type HolyCube (Recrutement via ticket) Saison 1 : recrutement\n                 Anarchie Ultra HC (Anarchie avec une seul vie / 24h) Ouverture lorsqu'assez de joueurs seront présents\n    💀   Albion Online ~ Guilde HCE\n    🚙   Rocket League ~ Chill Tournois libre & événements\n\n👥   Hub communautaire et animations\n    📜   Zone & forum pour vos pubs\n    🎰   Casino (Paries & jeux [En développement ouvert !], activités, WatchTogether, ...)\n    🖥️   Cours d'informatique Débutant du hardware au développement de plugin, bot et site Web basiques ! (Bientôt)\n    💬   Bot & serveurs Minecraft codés main ~ Ouverture en cours ! \n\nRejoins-nous 🌀 https://discord.gg/rF25kjuv4v  ~  https://altherneum.fr/  ~  https://altherneum.fr/storage/img/gif.gif\nRecrutement ✅ (Je recrute toutes personnes un minimum active, même sans connaissances)\nLe projet étant jeune, je suis seul à coder et le maintenir, je cherche donc les premiers fondateurs de cette future communauté";
    }

    public static String JoinMessage(User user) {
        return "\uD83C\uDD95 Bienvenue " + Discord.mention(user);
    }

    public static String BotOnline() {
        return "Le bot est **en ligne** ✅";
    }

    public static String BotOffline() {
        return "Le bot est **hors ligne** ❌";
    }

    public static String BotReconnect() {
        return BotReconnect(-1L);
    }

    public static String BotReconnect(long downtimeSeconds) {
        if (downtimeSeconds < 0) {
            return "Le bot est **reconnecté** ⚡";
        }
        return "Le bot est **reconnecté** ⚡ après " + formatDuration(downtimeSeconds);
    }

    private static String formatDuration(long totalSeconds) {
        if (totalSeconds < 60) {
            return totalSeconds + " s";
        }
        long seconds = totalSeconds % 60;
        long minutes = totalSeconds / 60;
        if (minutes < 60) {
            return seconds == 0 ? minutes + " min" : minutes + " min " + seconds + " s";
        }
        long hours = minutes / 60;
        minutes %= 60;
        return minutes == 0 ? hours + " h" : hours + " h " + minutes + " min";
    }

    public static String LeaveMessage(User user) {
        return "\uD83D\uDEAB Aurevoir " + Discord.mention(user);
    }

    public static String LevelUP(User user, Role role, String messageTotal) {
        return "\uD83C\uDF87 Bravo " + Discord.mention(user) + " pour tes **" + messageTotal + " ** !\n"
                + "\uD83C\uDF81 Voilà ton nouveau rôle : " + role.getAsMention();
    }

    public static void sendmessageOnline() {
        Discord.textChannel(IDs.Annonces).sendMessage(BotOnline()).queue();
    }

    public static void sendmessageOffline() {
        Discord.textChannel(IDs.Annonces).sendMessage(BotOffline()).queue();
    }
}
