package fr.altherneum.bot.listeners;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class reactionRole {
    private reactionRole() {
    }

    public static Role roleFromEmojis(String channelID, String emojiMentionTag) {
        if (channelID.equals(IDs.Notifications)) {
            switch (emojiMentionTag) {
                case IDs.EmojiArrowsClockwise:
                    return IDs.RoleTwitter;
                case IDs.EmojiMovieCamera:
                    return IDs.RoleVideoLive;
                case IDs.EmojiFramePhoto:
                    return IDs.RoleInstagram;
                case IDs.EmojiDesktop:
                    return IDs.RoleMiseAJour;
                case IDs.EmojiLoudSpeaker:
                    return IDs.RoleAnnonces;
                case IDs.EmojiCalendar:
                    return IDs.RoleEvents;
                case IDs.EmojiCrown:
                    return IDs.RoleTournois;
            }
        } else if (channelID.equals(IDs.Profil)) {
            switch (emojiMentionTag) {
                case IDs.EmojiMaleSign:
                    return IDs.RoleHomme;
                case IDs.EmojiFemaleSign:
                    return IDs.RoleFemme;
                case IDs.EmojiUnderage:
                    return IDs.RoleMineur;
                case IDs.EmojiOlderMan:
                    return IDs.RoleMajeur;
                case IDs.EmojiComputer:
                    return IDs.RolePC;
                case IDs.EmojiMobilePhone:
                    return IDs.RoleTelephone;
                case IDs.EmojiXBox:
                    return IDs.RoleXBox;
                case IDs.EmojiPS:
                    return IDs.RolePS;
                case IDs.EmojiSwitch:
                    return IDs.RoleSwitch;
            }
        } else if (channelID.equals(IDs.AutresJeux)) {
            switch (emojiMentionTag) {
                case IDs.EmojiRocketLeague:
                    return IDs.RoleRocketLeague;
                case IDs.EmojiSuperCell:
                    return IDs.RoleSuperCell;
                case IDs.EmojiGenshinImpact:
                    return IDs.RoleGenshinImpact;
                case IDs.EmojiGTAV:
                    return IDs.RoleGTAV;
                case IDs.EmojiFortnite:
                    return IDs.RoleFortnite;
                case IDs.EmojiCSGO:
                    return IDs.RoleCSGO;
                case IDs.EmojiLeagueOfLegends:
                    return IDs.RoleLeagueOfLegends;
                case IDs.EmojiValorant:
                    return IDs.RoleValorant;
                case IDs.EmojiAlbionOnline:
                    return IDs.RoleAlbionOnline;
                case IDs.EmojiShopTitans:
                    return IDs.RoleShopTitans;
            }
        }
        return null;
    }

    public static void startListener() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReactionAdd(MessageReactionAddEvent event) {
                if (event.getChannelType() != ChannelType.TEXT || event.getUser().isBot()) {
                    return;
                }
                TextChannel channel = event.getChannel().asTextChannel();
                Category category = channel.getParentCategory();
                if (category == null || !category.getId().equals(IDs.CategoryRoles.getId())) {
                    return;
                }
                Role role = roleFromEmojis(channel.getId(), Discord.emoji(event.getReaction().getEmoji()));
                if (role != null) {
                    User user = event.getUser();
                    Discord.addRole(user, role);
                    Discord.dmComplete(user, IDs.MessageRoleGain + role.getName());
                }
            }
        });

        /*
         * L'ancien bot comparait un String (getIdAsString) à un objet ChannelCategory
         * dans le listener de retrait : la condition était donc toujours fausse.
         * On conserve ce comportement (parité stricte) : le retrait de réaction ne
         * retire pas le rôle.
         */
    }
}
