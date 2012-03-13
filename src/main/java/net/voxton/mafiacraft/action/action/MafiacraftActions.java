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

import java.util.List;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.action.ActionPerformer;
import net.voxton.mafiacraft.action.Actions;
import net.voxton.mafiacraft.player.MsgColor;

/**
 * Mafiacraft command.
 */
public class MafiacraftActions extends Actions {

    @Override
    protected String performActionCommand(ActionPerformer performer,
            String action,
            List<String> args) {

        if (action.equalsIgnoreCase("reload")) {
            doReload(performer);
        } else if (action.equalsIgnoreCase("saveall")) {
            doSaveAll(performer);
        } else if (action.equalsIgnoreCase("version")) {
            doVersion(performer);
        } else {
            doHelp(performer);
        }
        
        return null;
    }

    public String doReload(ActionPerformer performer) {
        if (!performer.hasPermission("mafiacraft.admin")) {
            performer.sendMessage(MsgColor.ERROR
                    + "You are not allowed to use this command.");
        }

        performer.sendMessage(MsgColor.INFO + "Mafiacraft reloading...");
        Mafiacraft.loadAll();
        performer.sendMessage(MsgColor.SUCCESS + "Mafiacraft reload complete.");
        return null;
    }

    public String doSaveAll(ActionPerformer performer) {
        if (!performer.hasPermission("mafiacraft.admin")) {
            performer.sendMessage(MsgColor.ERROR
                    + "You are not allowed to use this command.");
        }

        performer.sendMessage(MsgColor.SUCCESS + "Mafiacraft saving all...");
        Mafiacraft.saveAll();
        performer.sendMessage(MsgColor.SUCCESS + "Mafiacraft save complete.");
        return null;
    }

    public String doVersion(ActionPerformer performer) {
        performer.sendMessage(MsgColor.INFO
                + "This server is running Mafiacraft version " + Mafiacraft.
                getCore().getVersionDetailed() + ".");
        return null;
    }

}
