package fr.altherneum.bot.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * Chargement du token Discord.
 *
 * <p>Ordre de priorité identique à l'ancien bot :
 * <ol>
 *   <li>variable d'environnement {@code DISCORD_BOT_TOKEN} ou
 *       {@code DISCORD_TEST_BOT_TOKEN} ;</li>
 *   <li>fichier {@code /DiscordBot/data/tokens.yml} (clé {@code Bot} ou
 *       {@code TestBot}).</li>
 * </ol>
 */
public final class Token {
    public enum Account {
        Bot, TestBot
    }

    private Token() {
    }

    public static File discordBotTokens() {
        String folderName = "/DiscordBot/data/";
        String fileName = "tokens.yml";
        return new File(folderName, fileName);
    }

    public static String getToken(Account bot) throws IOException {
        String envName = bot == Account.Bot ? "DISCORD_BOT_TOKEN" : "DISCORD_TEST_BOT_TOKEN";
        String envToken = cleanToken(System.getenv(envName));
        if (!envToken.isEmpty()) {
            System.out.println("[token] " + envName + " utilisée (longueur=" + envToken.length() + ")");
            return envToken;
        }

        File tokenFile = discordBotTokens();
        if (tokenFile.exists()) {
            String fileToken = readTokenFromYaml(tokenFile, bot);
            if (!fileToken.isEmpty()) {
                System.out.println("[token] " + tokenFile + " utilisé (longueur=" + fileToken.length() + ")");
                return fileToken;
            }
        } else {
            File parent = tokenFile.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            tokenFile.createNewFile();
        }

        throw new IOException("Token Discord introuvable : renseigne " + envName
                + " dans l'environnement ou dans " + tokenFile);
    }

    private static String readTokenFromYaml(File tokenFile, Account bot) throws IOException {
        try (Reader reader = new FileReader(tokenFile)) {
            Object loaded = new Yaml().load(reader);
            if (!(loaded instanceof Map<?, ?> map)) {
                return "";
            }
            Object value = map.get(bot.toString());
            return cleanToken(value == null ? null : String.valueOf(value));
        }
    }

    /**
     * Nettoie une valeur de token copiée depuis un fichier ou le portail Discord :
     * espaces, retours chariot, guillemets et préfixe "Bot " éventuel.
     */
    private static String cleanToken(String token) {
        if (token == null) {
            return "";
        }
        String cleaned = token.replace("\r", "").trim();
        if (cleaned.regionMatches(true, 0, "Bot ", 0, 4)) {
            cleaned = cleaned.substring(4).trim();
        }
        if (cleaned.length() >= 2) {
            char first = cleaned.charAt(0);
            char last = cleaned.charAt(cleaned.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                cleaned = cleaned.substring(1, cleaned.length() - 1).trim();
            }
        }
        return cleaned;
    }
}
