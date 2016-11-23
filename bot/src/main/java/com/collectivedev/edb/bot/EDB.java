package com.collectivedev.edb.bot;

import com.collectivedev.edb.api.IBot;
import com.collectivedev.edb.api.persist.AbstractDataStore;
import com.collectivedev.edb.api.persist.JsonFile;
import com.collectivedev.edb.bot.persist.DataStore;
import com.google.gson.JsonObject;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;

public class EDB extends IBot {
    
    private JDA jda;
    private AbstractDataStore dataStore;
    private JsonFile<JsonObject> jsonConfig;
    
    @Override
    public void load() throws Exception {
        jsonConfig = new JsonFile<>("config.json");
        jsonConfig.copyDefaults("config.json");
        
        jda = new JDABuilder().setBotToken(jsonConfig.getString("token")).setAudioEnabled(false).buildBlocking();
        jda.setAutoReconnect(true);
        
        dataStore = new DataStore(jsonConfig.getJsonObject("database"));
        dataStore.createTables();
    }
    
    @Override
    public AbstractDataStore getDataStore() {
        return dataStore;
    }
    
    @Override
    public JsonFile<JsonObject> getJsonConfig() {
        return jsonConfig;
    }
    
    @Override
    public JDA getJDA() {
        return jda;
    }
}