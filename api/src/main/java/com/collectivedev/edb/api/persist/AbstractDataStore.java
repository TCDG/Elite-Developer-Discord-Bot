package com.collectivedev.edb.api.persist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Represents a data store that information is stored in
 */
public interface AbstractDataStore {

    /**
     * Returns the connection for this data store
     * @return connection for this data store
     */
    Connection getConnection();

    /*=================================================================*/

    

    /*=================================================================*/

    /**
     * Creates the tables for this data store
     * <p>
     * By default, tables are created from <tt>resources/init.sql</tt>
     */
    default void createTables() {
        try (
                Connection conn = getConnection();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(getClass().getClassLoader().getResourceAsStream("init.sql"))
                )
        ) {
            new ScriptRunner(conn).runScript(reader);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}