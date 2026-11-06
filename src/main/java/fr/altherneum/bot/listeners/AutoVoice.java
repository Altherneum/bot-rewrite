package fr.altherneum.bot.listeners;

import java.util.ArrayList;
import java.util.EnumSet;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class AutoVoice {
    public static ArrayList<String> voice = new ArrayList<>();

    private AutoVoice() {
    }

    public static void startVoiceEvent() {
        voiceChannelCreator();
        leaveChannelDestructor();
    }

    public static void voiceChannelCreator() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
                try {
                    AudioChannelUnion joined = event.getChannelJoined();
                    if (joined == null || !IDs.voiceChannelsCreator().contains(joined.getId())) {
                        return;
                    }
                    Member member = event.getMember();
                    Category category = joined.asVoiceChannel().getParentCategory();
                    Discord.guild().createVoiceChannel("\uD83D\uDCDE" + Discord.tag(member.getUser()), category)
                            .addPermissionOverride(member, EnumSet.of(Permission.MANAGE_CHANNEL), null)
                            .queue(channel -> {
                                voice.add(channel.getId());
                                Discord.guild().moveVoiceMember(member, channel).queue();
                            });
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    public static void leaveChannelDestructor() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
                AudioChannelUnion left = event.getChannelLeft();
                if (left == null || !voice.contains(left.getId())) {
                    return;
                }
                VoiceChannel channel = left.asVoiceChannel();
                if (channel.getMembers().isEmpty()) {
                    voice.remove(left.getId());
                    channel.delete().queue();
                }
            }
        });
    }
}
