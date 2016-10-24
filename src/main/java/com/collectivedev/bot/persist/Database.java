package com.collectivedev.bot.persist;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Class used for managing connections to a database
 */
public final class Database implements IDatabase {

    private final HikariDataSource source;

    /**
     * Create an instance of this class used to connect to a database
     * @param uri URI of database {@code Eg: localhost:3306/somedb}
     * @param username username for the database. <em>Ideally, this should be root</em>
     * @param password password for said username
     */
    public Database(String uri, String username, String password) {
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

    /**
     * Returns a connection from the connection pool (if available)
     * @return {@link Connection} from the connection pool
     */
    public Connection getConnection() {
        try {
            return source.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Close a group of resources.<br>
     * The following: {@link PreparedStatement}, {@link ResultSet} and {@link Connection} should always be closed
     * @param list varargs of resources to close
     */
    public static void close(AutoCloseable... list) {
        Stream.of(list).filter(c -> c != null).forEach(c -> {
            try {
                c.close();
            } catch(Exception ignored) {}
        });
    }

    /**
     * Creates the tables from the init.sql script
     */
    @Override
    public void createTables() {
        try (
                Connection conn = getConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("init.sql")))
        ) {
            new ScriptRunner(conn).runScript(reader);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Map<String, Integer>> getRolePower() {
        Map<String, Map<String, Integer>> map = new HashMap<>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try (Connection conn = getConnection()) {
            ps = conn.prepareStatement("SELECT * FROM `roles`;");

            rs = ps.executeQuery();

            while (rs.next()) {
                String serverId = rs.getString("server_id");

                map.put(serverId,  getRolePowerForServer(serverId));
            }

            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(rs, ps);
        }

        return map;
    }

    @Override
    public Map<String, Integer> getRolePowerForServer(String server) {
        Map<String, Integer> map = new HashMap<>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try (Connection conn = getConnection()) {
            ps = conn.prepareStatement("SELECT * FROM `roles` WHERE `server_id`=?;");
            ps.setString(1, server);

            rs = ps.executeQuery();

            while (rs.next()) {
                map.put(rs.getString("role_id"),  rs.getInt("power"));
            }

            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(rs, ps);
        }

        return map;
    }

    @Override
    public int getPowerForRole(String server, String role) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try (Connection conn = getConnection()) {
            ps = conn.prepareStatement("SELECT `power` FROM `roles` WHERE `server_id`=? AND `role_id`=?;");
            ps.setString(1, server);
            ps.setString(2, role);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("power");
            }

            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(rs, ps);
        }

        return -1;
    }

    @Override
    public void updateRolePower(String server, String role, int power) {
        PreparedStatement ps = null;

        try (Connection conn = getConnection()) {
            ps = conn.prepareStatement("INSERT INTO `roles`(`server_id`, `role_id`, `power`) VALUES(?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE `power`=VALUES(`power`);");
            ps.setString(1, server);
            ps.setString(2, role);
            ps.setInt(3, power);

            ps.executeUpdate();

            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(ps);
        }
    }

    @Override
    public Map<String, Map<String, Integer>> getCommands() {
        Map<String, Map<String, Integer>> map = new HashMap<>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try (Connection conn = getConnection()) {
            ps = conn.prepareStatement("SELECT * FROM `commands`;");

            rs = ps.executeQuery();

            while (rs.next()) {
                String serverId = rs.getString("server_id");

                map.put(serverId,  getCommandsForServer(serverId));
            }

            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(rs, ps);
        }

        return map;
    }

    @Override
    public Map<String, Integer> getCommandsForServer(String server) {
        Map<String, Integer> map = new HashMap<>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try (Connection conn = getConnection()) {
            ps = conn.prepareStatement("SELECT * FROM `commands` WHERE `server_id`=?;");
            ps.setString(1, server);

            rs = ps.executeQuery();

            while (rs.next()) {
                map.put(rs.getString("command_name"),  rs.getInt("power"));
            }

            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(rs, ps);
        }

        return map;
    }

    @Override
    public int getPowerForCommand(String server, String command) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try (Connection conn = getConnection()) {
            ps = conn.prepareStatement("SELECT `power` FROM `commands` WHERE `server_id`=? AND `command_name`=?;");
            ps.setString(1, server);
            ps.setString(2, command);

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("power");
            }

            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(rs, ps);
        }

        return -1;
    }

    @Override
    public void updateCommandPower(String server, String command, int power) {
        PreparedStatement ps = null;

        try (Connection conn = getConnection()) {
            ps = conn.prepareStatement("INSERT INTO `commands`(`server_id`, `command_name`, `power`) VALUES(?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE `power`=VALUES(`power`);");
            ps.setString(1, server);
            ps.setString(2, command);
            ps.setInt(3, power);

            ps.executeUpdate();

            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(ps);
        }
    }
}