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
package net.voxton.mafiacraft.core.action.action;

import net.voxton.mafiacraft.core.geo.MWorld;
import net.voxton.mafiacraft.core.geo.WorldToggle;
import net.voxton.mafiacraft.core.player.MPlayer;
import net.voxton.mafiacraft.core.chat.MsgColor;
import java.util.Arrays;
import java.util.List;
import net.voxton.mafiacraft.core.action.PlayerActions;
import net.voxton.mafiacraft.core.geo.MPoint;
import net.voxton.mafiacraft.core.help.MenuType;

/**
 * Manages the CityWorld.
 */
public final class CWorldActions extends PlayerActions {

    public CWorldActions() {
        super(MenuType.CWORLD);
    }

    @Override
    public String performActionCommand(MPlayer performer, String action,
            List<String> args) {
        if (args.size() < 1) {
            if (action.equalsIgnoreCase("spawn")) {
                return doSpawn(performer);
            } else {
                doHelp(performer, action);
            }
        } else if (args.size() < 2) {
            if (action.equalsIgnoreCase("toggle")) {
                return doToggle(performer, args.get(0));
            } else if (action.equalsIgnoreCase("help")) {
                doHelp(performer, args.get(0));
            } else {
                doHelp(performer, action);
            }
        } else {
            doHelp(performer, action);
        }
        return null;
    }

    public String doToggle(MPlayer player, String toggle) {
        if (!player.hasPermission("mafiacraft.admin")) {
            return player.getLocale().localize("action.general.not-allowed");
        }

        MWorld world = player.getWorld();

        WorldToggle tog = null;
        try {
            tog = WorldToggle.valueOf(toggle.toUpperCase().replace('-', '_'));
        } catch (IllegalArgumentException ex) {
            return player.getLocale().localize("action.cworld.toggle-invalid",
                    Arrays.asList(WorldToggle.values()));
        }

        boolean val = world.toggle(tog);
        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "action.cworld.toggle-set", tog, val));
        return null;
    }

    public String doSpawn(MPlayer player) {
        MPoint spawn = player.getWorld().getSpawnPoint();
        player.teleportWithCountdown(spawn);
        return null;
    }

}
