package fr.altherneum.bot;

import java.util.Arrays;

import fr.altherneum.bot.config.Token;
import fr.altherneum.bot.events.Events;
import fr.altherneum.bot.utils.Login;
import net.dv8tion.jda.api.JDA;

/**
 * Point d'entrée du bot.
 *
 * <p>Le bot charge son token depuis l'environnement (DISCORD_BOT_TOKEN ou
 * DISCORD_TEST_BOT_TOKEN) puis, en secours, depuis /DiscordBot/data/tokens.yml.
 *
 * <p>L'argument {@code --safe} démarre le bot sans les actions de démarrage qui
 * modifient des messages (message d'online, mise à jour des modèles, timers de
 * statistiques/tops/banqueroute). Utile pour un test de connexion.
 */
public class Main {
    public static JDA jda = null;

    public static void main(String[] args) throws Exception {
        boolean safe = Arrays.asList(args).contains("--safe");
        startBot(Token.Account.Bot, safe);
    }

    public static void startBot(Token.Account account) throws Exception {
        startBot(account, false);
    }

    public static void startBot(Token.Account account, boolean safe) throws Exception {
        jda = Login.login(Token.getToken(account));
        // JDA construit le client de façon asynchrone : on attend que la session
        // et le cache des guildes soient prêts avant d'utiliser les IDs.
        jda.awaitReady();
        Events.startEvents(safe);
    }
}
