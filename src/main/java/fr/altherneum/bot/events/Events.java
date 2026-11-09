package fr.altherneum.bot.events;

import fr.altherneum.bot.api.messages;
import fr.altherneum.bot.commands.commands;
import fr.altherneum.bot.config.IDs;
import fr.altherneum.bot.listeners.AutoVoice;
import fr.altherneum.bot.listeners.CheckInvite;
import fr.altherneum.bot.listeners.JoinLeaveListener;
import fr.altherneum.bot.listeners.StatsTimer;
import fr.altherneum.bot.listeners.Tops;
import fr.altherneum.bot.listeners.VoiceCounter;
import fr.altherneum.bot.listeners.boutique;
import fr.altherneum.bot.listeners.casinoBanqueroute;
import fr.altherneum.bot.listeners.casinoCoinFlip;
import fr.altherneum.bot.listeners.casinoDailyClicker;
import fr.altherneum.bot.listeners.messageCount;
import fr.altherneum.bot.listeners.messageLogger;
import fr.altherneum.bot.listeners.messageUpdater;
import fr.altherneum.bot.listeners.reactionRole;
import fr.altherneum.bot.listeners.ticketRoleReact;
import fr.altherneum.bot.listeners.userAcceptRules;
import fr.altherneum.bot.utils.Discord;
import net.dv8tion.jda.api.events.session.SessionDisconnectEvent;
import net.dv8tion.jda.api.events.session.SessionRecreateEvent;
import net.dv8tion.jda.api.events.session.SessionResumeEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class Events {
    /**
     * Discord demande régulièrement au bot de se reconnecter à la passerelle
     * (opcode 7) pour rééquilibrer ses connexions. Ces reprises de session sont
     * transparentes : JDA rejoue les événements manqués et le bot n'est jamais
     * réellement hors ligne. On ne prévient donc le salon que si la coupure a
     * duré assez longtemps pour être visible par les membres.
     */
    private static final long RECONNECT_NOTIFY_MIN_DOWNTIME_MS = 5 * 60 * 1000L;

    private static volatile long disconnectedAt = 0L;

    private Events() {
    }

    public static void startEvents() {
        startEvents(false);
    }

    public static void startEvents(boolean safe) {
        reconnect();
        if (!safe) {
            messages.sendmessageOnline();
            messageUpdater.updateAllMessage();
        }
        //
        try {
            CheckInvite.start();
        } catch (RuntimeException exception) {
            if (!safe) {
                throw exception;
            }
            exception.printStackTrace();
        }
        messageCount.messageListener();
        JoinLeaveListener.startLeaveJoinEvent();
        AutoVoice.startVoiceEvent();
        reactionRole.startListener();
        ticketRoleReact.ticketEvent();
        if (!safe) {
            StatsTimer.statsTimerRunner();
            Tops.start();
        }
        userAcceptRules.onUserAcceptRules();
        boutique.onBoutique();
        casinoCoinFlip.startCoinFlip();
        casinoDailyClicker.onCasinoDailyClicker();
        messageLogger.onMessageLogger();
        if (!safe) {
            casinoBanqueroute.startBanqueroute();
        }
        VoiceCounter.startVoiceEvent();
        //
        commands.onCommands();
    }

    public static void commands() {
        commands.onCommands();
    }

    public static void reconnect() {
        fr.altherneum.bot.Main.jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onSessionDisconnect(SessionDisconnectEvent event) {
                disconnectedAt = System.currentTimeMillis();
            }

            @Override
            public void onSessionResume(SessionResumeEvent event) {
                notifyReconnectIfSignificant();
            }

            @Override
            public void onSessionRecreate(SessionRecreateEvent event) {
                notifyReconnectIfSignificant();
            }
        });
    }

    private static void notifyReconnectIfSignificant() {
        long since = disconnectedAt;
        disconnectedAt = 0L;
        if (since == 0L || System.currentTimeMillis() - since < RECONNECT_NOTIFY_MIN_DOWNTIME_MS) {
            return;
        }
        sendReconnectMessage();
    }

    private static void sendReconnectMessage() {
        try {
            Discord.textChannel(IDs.Annonces).sendMessage(messages.BotReconnect()).queue();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
