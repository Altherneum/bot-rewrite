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
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class Events {
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
            public void onSessionResume(net.dv8tion.jda.api.events.session.SessionResumeEvent event) {
                sendReconnectMessage();
            }

            @Override
            public void onSessionRecreate(net.dv8tion.jda.api.events.session.SessionRecreateEvent event) {
                sendReconnectMessage();
            }
        });
    }

    private static void sendReconnectMessage() {
        try {
            Discord.textChannel(IDs.Annonces).sendMessage(messages.BotReconnect()).queue();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
