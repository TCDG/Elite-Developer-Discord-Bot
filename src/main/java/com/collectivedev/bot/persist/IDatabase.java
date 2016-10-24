package com.collectivedev.bot.persist;

import java.util.Map;

public interface IDatabase {

    Map<String, Map<String, Integer>> getRolePower();

    Map<String, Integer> getRolePowerForServer(String server);

    int getPowerForRole(String server, String role);

    void updateRolePower(String server, String role, int power);

    /*========================================================================*/

    Map<String, Map<String, Integer>> getCommands();

    Map<String, Integer> getCommandsForServer(String server);

    int getPowerForCommand(String server, String command);

    void updateCommandPower(String server, String command, int power);

    /*========================================================================*/

    void createTables();
}