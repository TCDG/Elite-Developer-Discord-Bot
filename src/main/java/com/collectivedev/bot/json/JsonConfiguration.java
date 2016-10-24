package com.collectivedev.bot.json;

import com.google.gson.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract representation of a file comprised of JSON.
 */
public class JsonConfiguration {

    private final File destination;

    private JsonObject root;

    /**
     * Constructs a new JsonConfiguration from a {@link File} object.
     * @param destination location of this file
     */
    public JsonConfiguration(File destination) {
        this.destination = destination;
    }

    /**
     * Returns the root type for this file, reading the file if necessary<br>
     * If the root type is not of type {@link JsonObject},
     * a {@link JsonConfigurationException} will be thrown
     * @return the root type for this file
     */
    public JsonObject getRoot() {
        if (root == null) {
            JsonElement e = new JsonParser().parse(readJson());

            if (!e.isJsonObject()) {
                throw new JsonConfigurationException("Root type must be a JSON object; got " + e.getClass().getName() + " instead");
            }

            root = e.getAsJsonObject();
        }

        return root;
    }

    public JsonElement get(String key) {
        String[] parts = key.split("\\.");

        JsonObject obj = getRoot();

        if (parts.length == 1) {
            return obj.get(key);
        }

        for (String part : parts) {
            JsonElement e = obj.get(part);

            if (!e.isJsonObject()) {
                return e;
            } else {
                obj = e.getAsJsonObject();
            }
        }

        return null;
    }

    /**
     * Gets a string from its key.
     * @see #get(String)
     * @param key key of element
     * @return the specified element as a {@link String}
     */
    public String getString(String key) {
        return get(key).getAsString();
    }

    /**
     * Gets an integer from its key.
     * @see #get(String)
     * @param key key of element
     * @return the specified element as an integer
     */
    public int getInt(String key) {
        return get(key).getAsInt();
    }

    /**
     * Gets a long from its key.
     * @see #get(String)
     * @param key key of element
     * @return the specified element as a long
     */
    public long getLong(String key) {
        return get(key).getAsLong();
    }

    /**
     * Gets a double from its key.
     * @see #get(String)
     * @param key key of element
     * @return the specified element as a double
     */
    public double getDouble(String key) {
        return get(key).getAsDouble();
    }

    /**
     * Gets a short from its key.
     * @see #get(String)
     * @param key key of element
     * @return the specified element as a short
     */
    public short getShort(String key) {
        return get(key).getAsShort();
    }

    /**
     * Gets a byte from its key.
     * @see #get(String)
     * @param key key of element
     * @return the specified element as a byte
     */
    public byte getByte(String key) {
        return get(key).getAsByte();
    }

    /**
     * Gets a character from its key.
     * @see #get(String)
     * @param key key of element
     * @return the specified element as a character
     */
    public char getChar(String key) {
        return get(key).getAsCharacter();
    }

    /**
     * Gets a float from its key.
     * @see #get(String)
     * @param key key of element
     * @return the specified element as a float
     */
    public float getFloat(String key) {
        return get(key).getAsFloat();
    }

    /**
     * Gets a boolean from its key.
     * @see #get(String)
     * @param key key of element
     * @return the specified element as a boolean
     */
    public boolean getBoolean(String key) {
        return get(key).getAsBoolean();
    }

    /**
     * Gets a {@link Number} object from its key.
     * @see #get(String)
     * @param key key of element
     * @return the specified element as a {@link Number}
     */
    public Number getNumber(String key) {
        return get(key).getAsNumber();
    }

    /**
     * Gets a {@link BigInteger} object from its key.
     * @see #get(String)
     * @param key key of element
     * @return the specified element as a {@link BigInteger}
     */
    public BigInteger getBigInteger(String key) {
        return get(key).getAsBigInteger();
    }

    /**
     * Gets a {@link BigDecimal} object from its key.
     * @see #get(String)
     * @param key key of element
     * @return the specified element as a {@link BigDecimal}
     */
    public BigDecimal getBigDecimal(String key) {
        return get(key).getAsBigDecimal();
    }

    /**
     * Gets a {@link List} containing {@link JsonElement}s from its key.
     * @param key key of element
     * @return the specified element as a {@link List} of {@link JsonElement}s
     */
    public List<JsonElement> getList(String key) {
        List<JsonElement> list = new ArrayList<>();

        JsonElement e = get(key);

        if (!e.isJsonArray()) {
            throw new JsonConfigurationException("Cannot get list; expected array, got " + e.getClass().getName() + " instead");
        }

        for (JsonElement elem : get(key).getAsJsonArray()) {
            list.add(elem);
        }

        return list;
    }

    /**
     * Gets an array of {@link JsonElement}s from its key.<br>
     * This method converts {@link #getList(String)} to an array.
     * @see #getList(String)
     * @param key key of element
     * @return the specified element as an array of {@link JsonElement}s
     */
    public JsonElement[] getArray(String key) {
        List<JsonElement> list = getList(key);

        return list.toArray(new JsonElement[list.size()]);
    }

    public JsonObject getJsonObject(String key) {
        JsonElement e = get(key);

        if (!e.isJsonObject()) {
            throw new JsonConfigurationException("Invalid location for JSON object; found " + e.getClass().getName() + " instead");
        }

        return e.getAsJsonObject();
    }

    public JsonArray getJsonArray(String key) {
        JsonElement e = get(key);

        if (!e.isJsonArray()) {
            throw new JsonConfigurationException("Invalid location for JSON array; found " + e.getClass().getName() + " instead");
        }

        return e.getAsJsonArray();
    }

    /**
     * Saves the root type to the file with all of its values.
     */
    public void write() {
        if (root == null) {
            return;
        }

        try (Writer writer = new BufferedWriter(new FileWriter(destination))) {
            new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(root, writer);
        } catch (IOException e) {
            throw new JsonConfigurationException(String.format("Could not write to file: [%s]", e.getMessage()));
        }
    }

    /**
     * Copies a file from the jar file to the destination file, creating directories as needed.
     * @param name name of file to use
     */
    public void saveDefaultFile(String name) {
        if (!destination.getParentFile().exists()) {
            destination.getParentFile().mkdirs();
        }

        try (InputStream is = JsonConfiguration.class.getClassLoader().getResourceAsStream(name)) {
            Files.copy(is, destination.toPath());
        } catch (FileAlreadyExistsException ex) {
            // do nothing
        } catch (IOException e) {
            throw new JsonConfigurationException(String.format("Could not save default file: [%s]", e.getMessage()));
        }
    }

    private String readJson() {
        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(destination))) {
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            throw new JsonConfigurationException(String.format("Could not read JSON from file: [%s]", e.getMessage()));
        }

        return builder.toString();
    }

    private static class JsonConfigurationException extends RuntimeException {

        JsonConfigurationException(String s) {
            super(s);
        }
    }
}