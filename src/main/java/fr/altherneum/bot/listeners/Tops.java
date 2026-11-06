package fr.altherneum.bot.listeners;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import fr.altherneum.bot.utils.FileSystem;
import fr.altherneum.bot.utils.JsonFile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public final class Tops {
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Integer> usersTopVal = new ArrayList<>();

    public static String msg = "";

    public static String msgGold = "\n\n**__Top fortunes__** :euro: :\n";
    public static String msgMsg = "\n\n**__Top messages__** :speech_balloon: :\n";
    public static String msgInvite = "\n\n**__Top invitations__** :envelope: :\n";
    public static String msgVoice = "\n\n**__Top vocal__** :call_me: :\n";
    public static String msgVoiceSolo = "\n\n**__Top vocal solitaire__** :call_me: :\n";

    public static String msgFinalCategorie = "";

    private Tops() {
    }

    public static void start() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    update(true, false, false, false, false);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 1000 * 10, 1000 * 60 * 60 * 8);
    }

    public static void update(boolean isGold, boolean isMsg, boolean isInvite, boolean isVoice, boolean isVoiceSolo) {
        int top = 0;
        User userTop = Main.jda.getSelfUser();
        for (Member member : Discord.guild().getMembers()) {
            User user = member.getUser();
            if (!users.contains(user) && !user.isBot()) {
                File file = FileSystem.file(user);
                JsonFile fileConfiguration = new JsonFile(file);

                int valueToCheck = 0;
                if (isGold) {
                    valueToCheck = fileConfiguration.getInt("Gold");
                } else if (isMsg) {
                    valueToCheck = fileConfiguration.getInt("Messages");
                } else if (isInvite) {
                    int invite = 0;
                    for (String inviteCode : fileConfiguration.getStringList("ListeInvites")) {
                        invite += fileConfiguration.getInt("InvitesCounted." + inviteCode);
                    }
                    valueToCheck = invite;
                } else if (isVoice) {
                    valueToCheck = fileConfiguration.getInt("voiceTime");
                } else if (isVoiceSolo) {
                    valueToCheck = fileConfiguration.getInt("voiceTimeSolo");
                }

                if (top < valueToCheck) {
                    top = valueToCheck;
                    userTop = user;
                }
            }
        }
        users.add(userTop);
        usersTopVal.add(top);
        if (users.size() <= 4) {
            update(isGold, isMsg, isInvite, isVoice, isVoiceSolo);
        } else {
            if (isGold) {
                long time = Instant.now().getEpochSecond();
                Date date = new Date();
                date.setHours(date.getHours() + 8);
                long time2 = date.toInstant().getEpochSecond();
                msgFinalCategorie += "<t:" + time + ":R>" + ", prochain : " + "<t:" + time2 + ":R>" + msgGold;
            } else if (isMsg) {
                msgFinalCategorie += msgMsg;
            } else if (isInvite) {
                msgFinalCategorie += msgInvite;
            } else if (isVoice) {
                msgFinalCategorie += msgVoice;
            } else if (isVoiceSolo) {
                msgFinalCategorie += msgVoiceSolo;
            }

            int i = 0;
            for (User user : users) {
                msgFinalCategorie += messageUser(user, i, isGold, isMsg, isInvite, isVoice, isVoiceSolo,
                        usersTopVal.get(i)) + "\n";
                JsonFile fileConfiguration = new JsonFile(FileSystem.file(user));
                int gold = fileConfiguration.getInt("Gold") + goldRewardBoost(i, isInvite, isVoice, isVoiceSolo);
                fileConfiguration.set("Gold", gold);
                fileConfiguration.save();
                i++;
            }
            msg += msgFinalCategorie;
            msgFinalCategorie = "";

            users = new ArrayList<>();
            usersTopVal = new ArrayList<>();

            if (isGold) {
                update(false, true, false, false, false);
            } else if (isMsg) {
                update(false, false, true, false, false);
            } else if (isInvite) {
                update(false, false, false, true, false);
            } else if (isVoice) {
                update(false, false, false, false, true);
            }

            if (isVoiceSolo) {
                try {
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setDescription(msg);

                    TextChannel channel = Discord.textChannel(IDs.Tops);
                    long latest = channel.getLatestMessageIdLong();
                    if (latest != 0) {
                        channel.retrieveMessageById(latest)
                                .queue(message -> message.editMessageEmbeds(embedBuilder.build()).queue());
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                msg = "";
            }
        }
    }

    public static String messageUser(User user, int i, boolean isGold, boolean isMsg, boolean isInvite, boolean isVoice,
            boolean isVoiceSolo, int valueScore) {
        int goldReward = goldRewardBoost(i, isInvite, isVoice, isVoiceSolo);

        String endMsg = "";
        if (isGold) {
            endMsg += " ";
        } else if (isMsg) {
            endMsg += " messages";
        } else if (isInvite) {
            endMsg += " invitations";
        } else if (isVoice) {
            endMsg += " (" + VoiceCounter.voiceSecFormat(valueScore) + ")";
        } else if (isVoiceSolo) {
            endMsg += " (" + VoiceCounter.voiceSecFormat(valueScore) + ")";
        }

        switch (i) {
            case 0:
                return ":first_place:" + Discord.mention(user) + "   +" + goldReward
                        + ":moneybag:   Score: __**" + valueScore + endMsg + "**__";
            case 1:
                return ":second_place:" + Discord.mention(user) + "   +" + goldReward
                        + ":moneybag:   Score: **" + valueScore + endMsg + "**";
            case 2:
                return ":third_place:" + Discord.mention(user) + "   +" + goldReward + ":moneybag:   Score: "
                        + valueScore + endMsg + " !";
            case 3:
            case 4:
                return Discord.mention(user) + "   +" + goldReward + ":moneybag:   Score: " + valueScore + endMsg;
        }
        return "";
    }

    public static int goldRewardBoost(int i, boolean isInvite, boolean isVoice, boolean isVoiceSolo) {
        int goldReward = goldReward(i);
        if (isInvite) {
            goldReward = goldReward * 5;
        } else if (isVoice) {
            goldReward = goldReward * 2;
        } else if (isVoiceSolo) {
            goldReward = goldReward * 1;
        }
        return goldReward;
    }

    public static int goldReward(int i) {
        switch (i) {
            case 0:
                return 500;
            case 1:
                return 250;
            case 2:
                return 125;
            case 3:
                return 60;
            case 4:
                return 20;
        }
        return 0;
    }
}
