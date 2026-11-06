package fr.altherneum.bot.commands;

import java.util.ArrayList;
import java.util.List;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.listeners.casinoCoinFlip;
import fr.altherneum.bot.listeners.createButton;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public final class commands {
    private commands() {
    }

    public static void onCommands() {
        List<CommandData> builders = new ArrayList<>();
        builders.addAll(arrays());
        builders.addAll(arraysUserContextMenu());
        builders.addAll(arraysMessageContextMenu());
        Main.jda.updateCommands().addCommands(builders).complete();

        EmptyChannel.onEmptyChannel();
        testping.onTestPing();
        profil.onProfil();
        profil.onProfilUserContextMenu();
        mute.setMute();
        mute.removeMute();
        kick.onKickUser();
        ban.banUser();
        ban.unbanUser();
        addReaction.onAddReaction();
        copyMessage.onCopyMessage();
        createInvite.onCreateInvite();
        help.onHelp();
        casinoCoinFlip.coinFlipCmd();
        createButton.onCreateButton();
        ip.onIP();
        upvote.onUpvote();
        banqueRoute.banquerouteCMD();
        getYT.onYT();
    }

    public static List<SlashCommandData> arrays() {
        ArrayList<SlashCommandData> arrays = new ArrayList<>();
        arrays.add(EmptyChannel());
        arrays.add(Test());
        arrays.add(Ping());
        arrays.add(Profil());
        arrays.add(Mute());
        arrays.add(Unmute());
        arrays.add(Kick());
        arrays.add(addReaction());
        arrays.add(Ban());
        arrays.add(unBan());
        arrays.add(copyMessage());
        arrays.add(createInvite());
        arrays.add(Help());
        arrays.add(CoinFlip());
        arrays.add(CreateButton());
        arrays.add(IP());
        arrays.add(BanqueRoute());
        arrays.add(Upvote());
        arrays.add(Yt());
        return arrays;
    }

    public static List<CommandData> arraysUserContextMenu() {
        ArrayList<CommandData> arrays = new ArrayList<>();
        arrays.add(profilUserContext());
        return arrays;
    }

    public static List<CommandData> arraysMessageContextMenu() {
        return new ArrayList<>();
    }

    /*

     */

    public static SlashCommandData EmptyChannel() {
        return Commands.slash("emptychannel", "Vide le channel de tout ses messages")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    public static SlashCommandData Yt() {
        return Commands.slash("yt", "Obtenir les URL YT")
                .addOptions(option(OptionType.STRING, "channel_id", "L'ID du salon avec les messages contenant des URL YT", true))
                .addOptions(option(OptionType.NUMBER, "amount", "Messages amount", true))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    public static SlashCommandData Upvote() {
        return Commands.slash("upvote", "Voter pour l'utilisateur")
                .addOptions(option(OptionType.USER, "utilisateur", "L'utilisateur pour qui vous votez", true))
                .addOptions(option(OptionType.STRING, "raison",
                        "La raison pour la quelle vous votez pour cet utilisateur", true));
    }

    public static SlashCommandData Test() {
        return Commands.slash("test", "Test")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    public static SlashCommandData addReaction() {
        return Commands.slash("addreaction", "Ajoute une réaction sur tout les messages")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    public static SlashCommandData CreateButton() {
        return Commands.slash("createbutton", "Crée un bouton de test")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .addOptions(option(OptionType.CHANNEL, "channel", "Le salon où envoyer le bouton", true))
                .addOptions(option(OptionType.STRING, "button_id", "L'identifiant du bouton", true))
                .addOptions(option(OptionType.STRING, "button_text", "Le texte du bouton", true));
    }

    public static SlashCommandData copyMessage() {
        return Commands.slash("copymessage", "Copie et republie un message")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .addOptions(option(OptionType.STRING, "message_id", "L'identifiant du message à recopier", true))
                .addOptions(option(OptionType.STRING, "channel_id", "L'identifiant du salon où envouyer le message", true));
    }

    public static SlashCommandData createInvite() {
        return Commands.slash("createinvite", "Créer une invitation")
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .addOptions(option(OptionType.STRING, "channel_id", "L'identifiant du salon pour l'invitation", true));
    }

    public static SlashCommandData Ping() {
        return Commands.slash("ping", "Ping le bot altherneum.fr");
    }

    public static SlashCommandData IP() {
        return Commands.slash("ip", "Affiche les adresses serveurs");
    }

    public static SlashCommandData Help() {
        return Commands.slash("help", "Liste des commandes");
    }

    public static SlashCommandData Profil() {
        return Commands.slash("profil", "Affiche le profil discord d'un membre")
                .addOptions(option(OptionType.USER, "utilisateur", "Un utilisateur du serveur à vérifier", false));
    }

    public static CommandData profilUserContext() {
        return Commands.user("profil").setDefaultPermissions(DefaultMemberPermissions.ENABLED);
    }

    public static SlashCommandData CoinFlip() {
        return Commands.slash("coinflip", "Faire un lancé de pièce")
                .addOptions(option(OptionType.NUMBER, "argent", "Argent à parier (0 par défaut)", false));
    }

    public static SlashCommandData BanqueRoute() {
        return Commands.slash("banqueroute", "Lance la Banque route plusieurs fois")
                .addOptions(option(OptionType.NUMBER, "lancer",
                        "Le nombre de lancer à faire tant que vous n'avez pas gagné et de l'argent", true));
    }

    public static SlashCommandData Mute() {
        return Commands.slash("mute", "Exclu un utilisateur")
                .addOptions(option(OptionType.USER, "utilisateur", "Un utilisateur du serveur à rendre muet", true))
                .addOptions(option(OptionType.NUMBER, "minutes",
                        "Le temps en minutes pour le mute (1 à 43200 minutes [30j])", true))
                .addOptions(option(OptionType.STRING, "raison", "Raison du mute", true))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS));
    }

    public static SlashCommandData Unmute() {
        return Commands.slash("unmute", "Retire l'exclusion d'un utilisateur")
                .addOptions(option(OptionType.USER, "utilisateur", "Un utilisateur du serveur à ne plus exclure", true))
                .addOptions(option(OptionType.STRING, "raison", "Raison du retrait de l'exclusion", true))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MODERATE_MEMBERS));
    }

    public static SlashCommandData Ban() {
        return Commands.slash("ban", "Banni un utilisateur")
                .addOptions(option(OptionType.USER, "utilisateur", "Un utilisateur du serveur à bannir", true))
                .addOptions(option(OptionType.NUMBER, "jours",
                        "Le temps en jours pour la suppression des messages (1 à 7 jours)", true))
                .addOptions(option(OptionType.STRING, "raison", "Raison du ban", true))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS));
    }

    public static SlashCommandData unBan() {
        return Commands.slash("unban", "Débanni un utilisateur")
                .addOptions(option(OptionType.USER, "utilisateur", "Un utilisateur du serveur à débannir", true))
                .addOptions(option(OptionType.STRING, "raison", "Raison du ban", true))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS));
    }

    public static SlashCommandData Kick() {
        return Commands.slash("kick", "Exclure un utilisateur")
                .addOptions(option(OptionType.USER, "utilisateur", "Un utilisateur du serveur à exclure", true))
                .addOptions(option(OptionType.STRING, "raison", "Raison de l'exclusion", true))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.KICK_MEMBERS));
    }

    public static OptionData option(OptionType type, String name, String description, boolean required) {
        return new OptionData(type, name, description, required);
    }
}
