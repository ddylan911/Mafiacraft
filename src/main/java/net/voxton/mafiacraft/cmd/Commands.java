/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.cmd;

import net.voxton.mafiacraft.MafiacraftPlugin;
import net.voxton.mafiacraft.gov.GovType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Registers all commands with the server.
 */
public class Commands {
    public static void registerAll(MafiacraftPlugin plugin) {
        plugin.getCommand("mafia").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
                GovernmentCommand.parseCmd(cs, cmnd, string, strings, GovType.MAFIA);
                return true;
            }

        });
        plugin.getCommand("police").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
                GovernmentCommand.parseCmd(cs, cmnd, string, strings, GovType.POLICE);
                return true;
            }

        });
        plugin.getCommand("city").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
                CityCommand.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        plugin.getCommand("district").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
                DistrictCommand.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        plugin.getCommand("section").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
                SectionCommand.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        plugin.getCommand("chat").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
                ChatCommand.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        plugin.getCommand("cworld").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
                CWorldCommand.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        plugin.getCommand("regime").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
                DivisionCommand.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        plugin.getCommand("squad").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
                DivisionCommand.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
    }

}