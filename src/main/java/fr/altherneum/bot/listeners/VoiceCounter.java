package fr.altherneum.bot.listeners;

import java.util.Timer;
import java.util.TimerTask;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.utils.Discord;
import fr.altherneum.bot.utils.FileSystem;
import fr.altherneum.bot.utils.JsonFile;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class VoiceCounter {
    private VoiceCounter() {
    }

    public static void startVoiceEvent() {
        voiceChannelCreator();
    }

    public static void voiceChannelCreator() {
        Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
                if (event.getChannelJoined() != null) {
                    try {
                        checkUserTime(event.getMember().getUser());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    public static void checkUserTime(User user) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    Guild server = Discord.guild();
                    Member member = server.getMember(user);
                    if (member == null) {
                        cancel();
                        return;
                    }
                    GuildVoiceState voiceState = member.getVoiceState();
                    AudioChannel channel = voiceState == null ? null : voiceState.getChannel();

                    if (channel != null) {
                        VoiceChannel afk = server.getAfkChannel();
                        if (afk != null && channel.getId().equals(afk.getId())) {
                            cancel();
                        } else if (!voiceState.isSelfMuted() || !voiceState.isSelfDeafened()
                                || !voiceState.isMuted() || !voiceState.isDeafened()) {
                            if (channel.getMembers().size() >= 2) {
                                JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
                                fileConfiguration.set("voiceTime", fileConfiguration.getInt("voiceTime") + 1);
                                fileConfiguration.save();
                            } else if (channel.getMembers().size() == 1) {
                                JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
                                fileConfiguration.set("voiceTimeSolo", fileConfiguration.getInt("voiceTimeSolo") + 1);
                                fileConfiguration.save();
                            }
                        }
                    } else {
                        cancel();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public static String voiceSecFormat(int duration) {
        if (duration < 60) {
            return duration + " secondes";
        } else if (duration >= 60 && duration < 3_600) {
            return (duration / 60) + " minutes";
        } else if (duration >= 3_600 && duration < 86_400) {
            return (duration / 3_600) + " heures";
        } else if (duration >= 86_400 && duration < 604_800) {
            return (duration / 86_400) + " jours";
        } else if (duration >= 604_800) {
            return (duration / 604_800) + " mois";
        }
        return "erreur";
    }
}
