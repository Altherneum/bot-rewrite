package fr.altherneum.bot.utils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Petit remplaçant JSON de {@code YamlConfiguration} (Paper/Bukkit).
 *
 * <p>Les clés à point de l'ancien code sont conservées : par exemple
 * {@code InvitesCounted.<code>} devient un objet JSON imbriqué. Les dates sont
 * stockées en epoch millis.
 */
public final class JsonFile {
    private final File file;
    private final JSONObject root;

    public JsonFile(File file) {
        this.file = file;
        this.root = load(file);
    }

    private static JSONObject load(File file) {
        if (!file.exists() || file.length() == 0) {
            return new JSONObject();
        }
        try (Reader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            Object parsed = new JSONTokener(reader).nextValue();
            return parsed instanceof JSONObject object ? object : new JSONObject();
        } catch (Exception exception) {
            exception.printStackTrace();
            return new JSONObject();
        }
    }

    public int getInt(String path) {
        Object value = get(path);
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String string) {
            try {
                return (int) Math.round(Double.parseDouble(string));
            } catch (NumberFormatException ignored) {
                return 0;
            }
        }
        return 0;
    }

    public long getLong(String path) {
        Object value = get(path);
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value instanceof String string) {
            try {
                return Long.parseLong(string);
            } catch (NumberFormatException ignored) {
                return 0L;
            }
        }
        return 0L;
    }

    public String getString(String path) {
        Object value = get(path);
        return value == null || value == JSONObject.NULL ? null : String.valueOf(value);
    }

    public boolean getBoolean(String path) {
        Object value = get(path);
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value instanceof String string) {
            return Boolean.parseBoolean(string);
        }
        return false;
    }

    public List<String> getStringList(String path) {
        List<String> result = new ArrayList<>();
        Object value = get(path);
        if (value instanceof JSONArray array) {
            for (int i = 0; i < array.length(); i++) {
                Object item = array.opt(i);
                if (item != null && item != JSONObject.NULL) {
                    result.add(String.valueOf(item));
                }
            }
        }
        return result;
    }

    public Date getDate(String path) {
        Object value = get(path);
        if (value instanceof Number number) {
            return new Date(number.longValue());
        }
        if (value instanceof String string) {
            try {
                return new Date(Long.parseLong(string));
            } catch (NumberFormatException ignored) {
                try {
                    return Date.from(Instant.parse(string));
                } catch (Exception ignoredToo) {
                    return null;
                }
            }
        }
        return null;
    }

    public Object get(String path) {
        String[] parts = path.split("\\.");
        Object current = root;
        for (String part : parts) {
            if (!(current instanceof JSONObject object)) {
                return null;
            }
            current = object.opt(part);
            if (current == null || current == JSONObject.NULL) {
                return null;
            }
        }
        return current;
    }

    public void set(String path, Object value) {
        String[] parts = path.split("\\.");
        JSONObject current = root;
        for (int i = 0; i < parts.length - 1; i++) {
            Object next = current.opt(parts[i]);
            JSONObject object;
            if (next instanceof JSONObject existing) {
                object = existing;
            } else {
                object = new JSONObject();
                current.put(parts[i], object);
            }
            current = object;
        }
        current.put(parts[parts.length - 1], normalize(value));
    }

    public void setDate(String path, Date date) {
        set(path, date == null ? JSONObject.NULL : date.getTime());
    }

    private static Object normalize(Object value) {
        if (value instanceof Date date) {
            return date.getTime();
        }
        if (value instanceof Collection<?> collection) {
            JSONArray array = new JSONArray();
            for (Object item : collection) {
                array.put(normalize(item));
            }
            return array;
        }
        if (value == null) {
            return JSONObject.NULL;
        }
        return value;
    }

    public void save() {
        try {
            File parent = file.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            try (Writer writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
                writer.write(root.toString(4));
                writer.write(System.lineSeparator());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
