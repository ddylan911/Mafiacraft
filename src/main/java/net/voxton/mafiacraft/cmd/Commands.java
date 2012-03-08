/*
 * This file is part of Mafiacraft.
 * 
 * Mafiacraft is released under the Voxton License version 1.
 *
 * Mafiacraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition to this, you must also specify that this product includes 
 * software developed by Voxton.net and may not remove any code
 * referencing Voxton.net directly or indirectly.
 * 
 * Mafiacraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
 */
package net.voxton.mafiacraft.cmd;

import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.MafiacraftPlugin;
import net.voxton.mafiacraft.gov.GovType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Registers all commands with the server.
 */
public final class Commands {

    /**
     * Registers all commands with the server.
     */
    public static void registerAll() {
        MafiacraftPlugin plugin = Mafiacraft.getPlugin();

        plugin.getCommand("mafia").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                GovernmentCommand.parseCmd(cs, cmnd, string, strings,
                        GovType.MAFIA);
                return true;
            }

        });
        plugin.getCommand("police").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                GovernmentCommand.parseCmd(cs, cmnd, string, strings,
                        GovType.POLICE);
                return true;
            }

        });
        plugin.getCommand("city").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                CityCommand.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        plugin.getCommand("district").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                DistrictCommand.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        plugin.getCommand("section").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                SectionCommand.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        plugin.getCommand("chat").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                ChatCommand.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        plugin.getCommand("cworld").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                CWorldCommand.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        plugin.getCommand("regime").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                DivisionCommand.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        plugin.getCommand("squad").setExecutor(new CommandExecutor() {

            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                DivisionCommand.parseCmd(cs, cmnd, string, strings);
                return true;
            }

        });
        plugin.getCommand("mafiacraft").setExecutor(new CommandExecutor() {

            @Override
            public boolean onCommand(CommandSender cs, Command cmnd,
                    String string, String[] strings) {
                MafiacraftCommand.parseCmd(cs, cmnd, string, strings);
                return true;
            }
        });
    }

}
