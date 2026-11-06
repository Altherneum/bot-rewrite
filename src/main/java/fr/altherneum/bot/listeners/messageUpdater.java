package fr.altherneum.bot.listeners;

import java.util.List;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;

public final class messageUpdater {
    private messageUpdater() {
    }

    public static void copyMessage(String channel, String messageID, int messageNumber) {
        GuildMessageChannel source = Discord.textChannel("1081950904644816956");
        Message message = source.retrieveMessageById(messageID).complete();
        GuildMessageChannel target = Discord.textChannel(channel);
        List<Message> messages = target.getHistory().retrievePast(messageNumber).complete();
        Message messageOld = messages.get(messages.size() - 1);

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(message.getContentRaw());

        if (Discord.isSelfMessage(messageOld)) {
            messageOld.editMessageEmbeds(embedBuilder.build()).queue();
        } else {
            System.out.println("[messageUpdater] message " + messageOld.getId() + " ignoré : envoyé par "
                    + messageOld.getAuthor().getName() + " (autre compte bot)");
        }

        for (MessageReaction reaction : message.getReactions()) {
            messageOld.addReaction(reaction.getEmoji()).queue();
        }
    }

    public static void updateAllMessage() {
        copyMessage(IDs.Ticket, "1083864323300012043", 1);
        copyMessage(IDs.ReglesNew, "1117917094906953809", 2);
        copyMessage(IDs.ReglesNew, "1121133776853024808", 1);
        copyMessage(IDs.Regles, "1086666682610745364", 1);
        copyMessage(IDs.NotrePub, "1505551530365947964", 1);
        copyMessage(IDs.NotrePub, "1512328899378548857", 2);
        copyMessage(IDs.Informations, "1086667520271990856", 1);
        copyMessage(IDs.CasinoCoinFlip, "1086668476724301927", 1);
        copyMessage(IDs.CasinoBoutique, "1086668570873835602", 1);
        copyMessage(IDs.CasinoDailyClick, "1086668723349372948", 1);
        copyMessage(IDs.Notifications, "1086668968196063392", 1);
        copyMessage(IDs.AutresJeux, "1086738477414092880", 1);
        copyMessage(IDs.LogsTachesStatff, "1086739575247994881", 2);
        copyMessage(IDs.LogsTachesStatff, "1092032505466593311", 1);
        copyMessage(IDs.Casino, "1092924243416727662", 1);
        copyMessage(IDs.MinecraftInfo, "1092926001169834055", 1);
        copyMessage(IDs.Recrutement, "1095846425503477822", 2);
        copyMessage(IDs.Recrutement, "1097519605993459732", 1);
        copyMessage(IDs.EventsNote, "1112010369012076645", 1);
        copyMessage(IDs.Profil, "1229852231822610503", 1);
    }
}
