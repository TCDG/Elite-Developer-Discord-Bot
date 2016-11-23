package com.collectivedev.edb.api;

import com.collectivedev.edb.api.persist.AbstractDataStore;
import com.collectivedev.edb.api.persist.JsonFile;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;

public class Bot {
    
    private static IBot instance;
    
    private Bot() { }
    
    public static void setInstance(IBot instance) {
        Preconditions.checkNotNull(instance, "instance");
        Preconditions.checkArgument(Bot.instance == null, "Instance already set");
        Bot.instance = instance;
    }
    
    public static IBot getInstance() {
        return instance;
    }
    
    public static JsonFile<JsonObject> getJsonConfig() {
        return instance.getJsonConfig();
    }
    
    public static AbstractDataStore getDataStore() {
        return instance.getDataStore();
    }
}