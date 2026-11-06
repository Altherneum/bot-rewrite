package fr.altherneum.bot.utils;

import java.util.EnumSet;
import java.util.concurrent.Executors;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

/**
 * Connexion et configuration JDA.
 *
 * <p>On active tous les intents et tous les caches, comme l'ancien bot JavaCord
 * ({@code setAllIntents()}). Le cache complet des membres est nécessaire aux
 * statistiques, aux tops et aux profils.
 */
public final class Login {
    private Login() {
    }

    public static JDA login(String token) {
        return JDABuilder.createDefault(token)
                .enableIntents(EnumSet.allOf(GatewayIntent.class))
                .enableCache(EnumSet.allOf(CacheFlag.class))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                // Les événements sont traités un par un, sur un thread dédié.
                // Cela reproduit le style bloquant de l'ancien bot sans bloquer
                // le thread WebSocket de JDA.
                .setEventPool(Executors.newSingleThreadExecutor(), true)
                .build();
    }
}
