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
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
 */
package net.voxton.mafiacraft.action.action;

import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.action.Actions;
import net.voxton.mafiacraft.player.MsgColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Mafiacraft command.
 */
public class MafiacraftActions extends Actions {

    /**
     * Parses the command and executes it.
     *
     * @param sender The command sender.
     * @param cmd The command.
     * @param label The command label.
     * @param args The arguments.
     */
    public void parseCmd(CommandSender sender, Command cmd, String label,
            String[] args) {

        if (args.length < 1) {
            doHelp(sender);
            return;
        }

        String function = args[0];

        if (function.equalsIgnoreCase("reload")) {
            doReload(sender);
        } else if (function.equalsIgnoreCase("saveall")) {
            doSaveAll(sender);
        } else if (function.equalsIgnoreCase("version")) {
            doVersion(sender);
        } else {
            doHelp(sender);
        }
    }

    public String doHelp(CommandSender sender) {
        sender.sendMessage(MsgColor.INFO
                + "Help: /mafiacraft <version|reload|saveall>. Nuff said.");
        return null;
    }

    public String doReload(CommandSender sender) {
        if (!sender.hasPermission("mafiacraft.admin")) {
            sender.sendMessage(MsgColor.ERROR
                    + "You are not allowed to use this command.");
        }

        sender.sendMessage(MsgColor.INFO + "Mafiacraft reloading...");
        Mafiacraft.loadAll();
        sender.sendMessage(MsgColor.SUCCESS + "Mafiacraft reload complete.");
        return null;
    }

    public String doSaveAll(CommandSender sender) {
        if (!sender.hasPermission("mafiacraft.admin")) {
            sender.sendMessage(MsgColor.ERROR
                    + "You are not allowed to use this command.");
        }

        sender.sendMessage(MsgColor.SUCCESS + "Mafiacraft saving all...");
        Mafiacraft.saveAll();
        sender.sendMessage(MsgColor.SUCCESS + "Mafiacraft save complete.");
        return null;
    }

    public String doVersion(CommandSender sender) {
        sender.sendMessage(MsgColor.INFO
                + "This server is running Mafiacraft version " + Mafiacraft.
                getCore().getVersionDetailed() + ".");
        return null;
    }

}
