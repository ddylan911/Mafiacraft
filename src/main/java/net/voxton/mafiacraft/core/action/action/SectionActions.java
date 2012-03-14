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

import net.voxton.mafiacraft.core.player.MPlayer;
import net.voxton.mafiacraft.core.chat.MsgColor;
import java.util.List;
import net.voxton.mafiacraft.core.action.PlayerActions;

/**
 * Commands related to working with sections.
 */
public final class SectionActions extends PlayerActions {

    public SectionActions() {
        super(null); //TODO ake a help menu
    }

    @Override
    protected String performActionCommand(MPlayer performer,
            String action,
            List<String> args) {

        String result = null;
        if (args.size() < 1) {
            if (action.equalsIgnoreCase("info")) {
                result = doInfo(performer);
            } else {
                result = doHelp(performer);
            }
        } else {
            result = doHelp(performer);
        }

        return result;
    }

    public String doHelp(MPlayer player) {
        //TODO: help
        player.sendMessage(MsgColor.ERROR + "TROLOLOLOLOL");
        return null;
    }

    public String doInfo(MPlayer player) {
        //TODO: info
        player.sendMessage(MsgColor.ERROR + "TROLOLOLOLOLO");
        return null;
    }

}
