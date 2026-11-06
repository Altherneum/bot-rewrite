package fr.altherneum.bot.listeners;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.management.OperatingSystemMXBean;

import fr.altherneum.bot.Main;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;

public final class StatsTimer {
    private StatsTimer() {
    }

    public static void statsTimerRunner() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                buildMessage(true, IDs.Statistiques);
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task, 1000 * 5, 1000 * 60 * 15);
    }

    public static void buildMessage(boolean timed, String channel) {
        StringBuilder messageRole = new StringBuilder();
        for (Role role : roles()) {
            if (role.equals(IDs.RoleGenshinImpact) || role.equals(IDs.RoleHomme) || role.equals(IDs.RolePC)) {
                messageRole.append("\n");
            }
            messageRole.append(rolesString(role)).append("\n");
        }

        int dnd = 0;
        int online = 0;
        int idle = 0;
        int offline = 0;
        int bot = 0;
        int roleLess = 0;
        int onlyRules = 0;
        Guild guild = Discord.guild();
        for (Member member : guild.getMembers()) {
            if (member.getUser().isBot()) {
                bot++;
            } else {
                int roleCount = member.getRoles().size();
                if (roleCount == 0) {
                    roleLess++;
                }
                if (roleCount == IDs.defaultRoleSizeWithSplitterAndRulesAndEveryOne - 1) {
                    if (member.getRoles().contains(IDs.RoleReglesValides)) {
                        onlyRules++;
                    }
                }
                OnlineStatus status = member.getOnlineStatus();
                if (status == OnlineStatus.DO_NOT_DISTURB) {
                    dnd++;
                } else if (status == OnlineStatus.ONLINE) {
                    online++;
                } else if (status == OnlineStatus.IDLE) {
                    idle++;
                } else if (status == OnlineStatus.OFFLINE || status == OnlineStatus.UNKNOWN) {
                    offline++;
                }
            }
        }

        String uptime = "";
        try {
            uptime = getUptime();
        } catch (Exception ignored) {
            // Comme l'ancien bot : on laisse la section vide en cas d'erreur.
        }

        String messageUser = ":green_circle: **En ligne** : " + online + "\n" + ":red_circle: **Hors ligne** : "
                + offline + "\n" + ":no_entry: **Ne pas déranger** : " + dnd + "\n"
                + ":crescent_moon: **Absent** : " + idle + "\n" + ":robot: Bot : " + bot;

        Role everyone = IDs.RoleEveryone;
        String everyoneString = rolesString(everyone);
        String stringRoleLess = IDs.EmojiRocket + " Sans rôle : " + roleLess;
        String rulesOnly = IDs.RoleReglesValides.getAsMention() + " uniquement : " + onlyRules;

        long time = Instant.now().getEpochSecond();
        Date date = new Date();
        date.setMinutes(date.getMinutes() + 15);
        long time2 = date.toInstant().getEpochSecond();

        String constructorForTimedString = "";
        if (timed) {
            constructorForTimedString = ", prochain : " + "<t:" + time2 + ":R>";
        }

        String finalMessage = "<t:" + time + ":R>" + constructorForTimedString + "\n\n"
                + messageUser + "\n\n" + stringRoleLess
                + "\n" + everyoneString + "\n" + rulesOnly + "\n\n" + messageRole + "\n\n\n\n" + uptime;

        try {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setDescription(finalMessage);

            if (timed) {
                GuildMessageChannel textChannel = Discord.textChannel(IDs.Statistiques);
                long latest = textChannel.getLatestMessageIdLong();
                if (latest != 0) {
                    textChannel.retrieveMessageById(latest).queue(message -> {
                        if (Discord.isSelfMessage(message)) {
                            message.editMessageEmbeds(embedBuilder.build()).queue();
                        }
                    });
                }
            } else {
                Discord.textChannel(channel).sendMessageEmbeds(embedBuilder.build()).queue();
            }

            setCounterOnCategory((online + dnd + idle + offline), (online + dnd + idle));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static ArrayList<Role> roles() {
        ArrayList<Role> role = new ArrayList<>();
        role.add(IDs.RoleBoost);
        role.add(IDs.RoleVIP);
        role.add(IDs.RoleReglesValides);
        //
        for (Role role2 : IDs.rolesJeux()) {
            role.add(role2);
        }
        //
        role.add(IDs.RoleHomme);
        role.add(IDs.RoleFemme);
        role.add(IDs.RoleMineur);
        role.add(IDs.RoleMajeur);
        //
        role.add(IDs.RolePC);
        role.add(IDs.RoleTelephone);
        role.add(IDs.RoleXBox);
        role.add(IDs.RolePS);
        role.add(IDs.RoleSwitch);
        return role;
    }

    public static String rolesString(Role role) {
        int count;
        if (role.isPublicRole()) {
            count = role.getGuild().getMembers().size();
        } else {
            count = role.getGuild().getMembersWithRoles(role).size();
        }
        return role.getAsMention() + " : " + count;
    }

    public static DateFormat dateFormat() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    public static String dateFormated(Date date) {
        return dateFormat().format(date);
    }

    public static void setCounterOnCategory(int users, int online) {
        IDs.CategoryAltherneum.getManager()
                .setName("●▬▬▬▬▬▬▬▬๑ " + users + "🙋" + "      " + online + "🟢")
                .queue();
    }

    public static String getUptime()
            throws UnknownHostException, IOException, InterruptedException, NullPointerException {

        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();

        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = allocatedMemory - freeMemory;
        long totalRam = operatingSystemMXBean.getTotalMemorySize();
        long totalRamFree = operatingSystemMXBean.getFreeMemorySize();
        long totalRamUsed = totalRam - totalRamFree;

        double cpuusage = operatingSystemMXBean.getSystemCpuLoad() * 100;
        double cpuusageaverage = operatingSystemMXBean.getSystemLoadAverage() * 100;

        File diskPartition = new File("/");
        long free = diskPartition.getFreeSpace();
        long total = diskPartition.getTotalSpace();

        String adress1 = "google.fr";
        boolean isPinged;
        boolean alternativePing = true;
        String adress2 = "github.com";
        boolean isPinged2;
        boolean alternativePing2 = false;

        int port = 80;
        int timeout = 2000;

        long currentTime = System.currentTimeMillis();
        if (alternativePing) {
            isPinged = connectSocket(adress1, port, timeout);
        } else {
            isPinged = InetAddress.getByName(adress1).isReachable(timeout);
        }
        currentTime = System.currentTimeMillis() - currentTime;

        long currentTime2 = System.currentTimeMillis();
        if (alternativePing2) {
            isPinged2 = InetAddress.getByName(adress2).isReachable(timeout);
        } else {
            isPinged2 = connectSocket(adress2, port, timeout);
        }
        currentTime2 = System.currentTimeMillis() - currentTime2;

        String message = "";

        message += "\n**__Bot RAM__** : " + Math.round(usedMemory / 1024 / 1024) + "/"
                + Math.round(maxMemory / 1024 / 1024) + " Mo";
        message += "\n**__RAM VPS__** : " + Math.round(totalRamUsed / 1024 / 1024) + "/"
                + Math.round(totalRam / 1024 / 1024) + " Mo";
        message += "\n**__CPU__** : " + Math.round(cpuusage) + "%";
        message += "\n  **__Average__** : ~" + Math.round(cpuusageaverage) + "%";
        message += "\n**__HDD__** : " + Math.round(free / 1024 / 1024 / 1024) + "/"
                + Math.round(total / 1024 / 1024 / 1024) + " Go";

        if (isPinged) {
            message += "\n\n**__Ping__** " + adress1 + " : " + currentTime + " Ms";
        } else {
            message += "\n\n**__Ping__** : __Erreur__";
        }

        if (isPinged2) {
            message += "\n**__Ping__** " + adress2 + " : " + currentTime2 + " Ms";
        } else {
            message += "\n**__Ping__** : __Erreur__";
        }

        return message;
    }

    public static boolean connectSocket(String address, int port, int timeout) throws IOException {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(address, port), timeout);
            return true;
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        } finally {
            socket.close();
        }
    }
}
