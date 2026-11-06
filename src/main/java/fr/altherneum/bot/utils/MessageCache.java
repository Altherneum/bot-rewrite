package fr.altherneum.bot.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.dv8tion.jda.api.entities.Message;

/**
 * Cache des messages vus depuis le démarrage.
 *
 * <p>JDA ne fournit ni l'auteur ni le contenu dans un événement de suppression,
 * contrairement à JavaCord qui gardait le message en cache. Ce cache permet de
 * conserver exactement les mêmes logs de création / modification / suppression.
 */
public final class MessageCache {
    private static final int MAX_SIZE = 10_000;
    private static final Map<Long, Message> MESSAGES = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<Long> ORDER = new ConcurrentLinkedQueue<>();

    private MessageCache() {
    }

    public static void put(Message message) {
        if (message == null) {
            return;
        }
        if (MESSAGES.put(message.getIdLong(), message) == null) {
            ORDER.add(message.getIdLong());
        }
        while (MESSAGES.size() > MAX_SIZE) {
            Long oldest = ORDER.poll();
            if (oldest == null) {
                break;
            }
            MESSAGES.remove(oldest);
        }
    }

    public static Message get(long messageId) {
        return MESSAGES.get(messageId);
    }

    public static Message remove(long messageId) {
        return MESSAGES.remove(messageId);
    }

    public static void clear() {
        MESSAGES.clear();
        ORDER.clear();
    }
}
