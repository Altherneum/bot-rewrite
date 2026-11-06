package fr.altherneum.bot.utils;

import java.io.File;

import net.dv8tion.jda.api.entities.User;

/**
 * Fichiers de données JSON.
 *
 * <p>L'ancien bot utilisait {@code Data/<id>} (YAML, sans extension). La
 * migration JSON utilise {@code Data/<id>.json}. Les clés internes sont
 * conservées à l'identique.
 */
public final class FileSystem {
    private FileSystem() {
    }

    public static File getFolder() {
        File path = new File("Data");
        path.mkdirs();
        return path;
    }

    public static File file(User user) {
        return file(user.getId());
    }

    public static File file(String id) {
        return new File(getFolder(), id + ".json");
    }

    public static File fileGlobal() {
        return new File(getFolder(), "globalInvites.json");
    }

    public static File event() {
        return new File(getFolder(), "event.json");
    }

    public static File BanqueRoute() {
        return new File(getFolder(), "BanqueRoute.json");
    }
}
