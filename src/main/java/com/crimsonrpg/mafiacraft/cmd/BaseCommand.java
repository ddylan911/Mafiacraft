/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.cmd;

import com.crimsonrpg.mafiacraft.player.MPlayer;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Dylan
 */
public abstract class BaseCommand implements CommandExecutor {
    
    /**
     * Gets the data from execute, and does it.
     * 
     * @param cs
     * @param cmnd
     * @param string
     * @param strings
     * @return 
     */
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
        this.execute(cs, cmnd, string, strings);
        return true;
    }
    /**
     * Executes the command.
     * 
     * @param sender
     * @param cmnd
     * @param string
     * @param args 
     */
    public abstract void execute(CommandSender sender, Command cmnd, String string, String[] args);
    
    /**
     * Does this if an error occurs.
     * 
     * @param player 
     */
    public abstract void doHelp(MPlayer player);
    
    /**
     * Registers all given commands.
     */
    public static void registerAll(Map<String, BaseCommand> commands, JavaPlugin plugin) {
        for (Entry<String, BaseCommand> command : commands.entrySet()) {
            plugin.getCommand(command.getKey()).setExecutor(command.getValue());
        }
    }
}