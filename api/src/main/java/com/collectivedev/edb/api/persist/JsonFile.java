package com.collectivedev.edb.api.persist;

import com.collectivedev.edb.api.IBot;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class JsonFile<T extends JsonElement> {

    private final File destination;

    private T root;

    public JsonFile(File destination) {
        this.destination = destination;
    }

    public JsonFile(String destination) {
        this(new File(destination));
    }

    public T getRoot() {
        if (root == null) {
            JsonElement e = new JsonParser().parse(getJson());

            if (!(e.isJsonArray() || e.isJsonObject())) {
                throw new JsonException(
                        String.format("Root type must be a JSON object or JSON array; got %s instead",
                                e.getClass().getName()
                        )
                );
            }

            root = (T) e;
        }

        return root;
    }

    public JsonElement get(String key) throws JsonException {
        T root = getRoot();

        if (!root.isJsonObject()) {
            throw new JsonException("Root type must be of type object to retrieve value by key");
        }

        String[] parts = key.split("\\.");

        JsonObject obj = (JsonObject) root;

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

    public JsonElement get(int index) throws JsonException {
        T root = getRoot();

        if (!root.isJsonArray()) {
            throw new JsonException("Root type must be of type array to retrieve value by index");
        }

        return ((JsonArray) root).get(index);
    }

    public String getString(String key) {
        return get(key).getAsString();
    }

    public int getInt(String key) throws JsonException {
        return get(key).getAsInt();
    }

    public boolean getBoolean(String key) throws JsonException {
        return get(key).getAsBoolean();
    }

    public double getDouble(String key) throws JsonException {
        return get(key).getAsDouble();
    }

    public long getLong(String key) throws JsonException {
        return get(key).getAsLong();
    }

    public float getFloat(String key) throws JsonException {
        return get(key).getAsFloat();
    }

    public short getShort(String key) throws JsonException {
        return get(key).getAsShort();
    }

    public byte getByte(String key) throws JsonException {
        return get(key).getAsByte();
    }

    public BigDecimal getBigDecimal(String key) throws JsonException {
        return get(key).getAsBigDecimal();
    }

    public BigInteger getBigInteger(String key) throws JsonException {
        return get(key).getAsBigInteger();
    }

    public JsonObject getJsonObject(String key) throws JsonException {
        return get(key).getAsJsonObject();
    }

    public JsonArray getJsonArray(String key) throws JsonException {
        return get(key).getAsJsonArray();
    }

    public List<JsonElement> getList(String key) {
        JsonElement e = get(key);

        if (!e.isJsonArray()) {
            throw new JsonException(
                    String.format(
                            "Could not get list; expected array but got %s instead",
                            e.getClass().getName()
                    )
            );
        }

        return StreamSupport.stream(e.getAsJsonArray().spliterator(), false).collect(Collectors.toList());
    }

    public void copyDefaults(String name) throws JsonException {
        if (!destination.getParentFile().exists()) {
            destination.getParentFile().mkdirs();
        }

        try (InputStream is = IBot.class.getClassLoader().getResourceAsStream(name)) {
            Files.copy(is, destination.toPath());
        } catch (FileAlreadyExistsException ignored) {
            // do nothing
        } catch (IOException ex) {
            throw new JsonException(String.format("Could not save default file: [%s]", ex.getMessage()));
        }
    }

    public String getJson() {
        final StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(destination))) {
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            throw new JsonException(String.format("Could not parse JSON file: [%s]", e.getMessage()));
        }

        return builder.toString();
    }

    public File getDestination() {
        return destination;
    }

    public static final class JsonException extends RuntimeException {

        public JsonException(String s) {
            super(s);
        }

        public JsonException(String s, Throwable t) {
            super(s, t);
        }

        public JsonException(Throwable t) {
            super(t);
        }
    }
}