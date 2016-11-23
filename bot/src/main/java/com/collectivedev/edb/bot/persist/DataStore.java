package com.collectivedev.edb.bot.persist;

import com.collectivedev.edb.api.persist.AbstractDataStore;
import com.google.gson.JsonObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataStore implements AbstractDataStore {
    
    private final HikariDataSource source;
    
    public DataStore(String uri, String username, String password) {
        final HikariConfig config = new HikariConfig();
        
        config.setJdbcUrl("jdbc:mysql://" + uri);
        config.setUsername(username);
        config.setPassword(password);
        
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        config.setMaximumPoolSize((Runtime.getRuntime().availableProcessors() * 2) + 1);
        
        this.source = new HikariDataSource(config);
    }
    
    public DataStore(JsonObject o) {
        this(
                o.get("uri").getAsString(),
                o.get("username").getAsString(),
                o.get("password").getAsString()
        );
    }
    
    @Override
    public Connection getConnection() {
        try {
            return source.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}