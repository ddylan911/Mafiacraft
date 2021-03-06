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
package net.voxton.mafiacraft.core.action;

import java.util.List;
import net.voxton.mafiacraft.core.help.HelpMenu;
import net.voxton.mafiacraft.core.chat.MsgColor;

/**
 * Performs actions.
 */
public abstract class Actions {

    private final HelpMenu help;

    /**
     * Constructor for unhelpful actions.
     */
    public Actions() {
        this.help = null;
    }

    /**
     * Constructor for helpful actions.
     *
     * @param help The help menu.
     */
    public Actions(HelpMenu help) {
        this.help = help;
    }

    /**
     * Parses an action command into a distinct performer, action, and
     * arguments.
     *
     * @param performer The performer of the action.
     * @param action The action to perform.
     * @param args The arguments of the action.
     */
    public void parseActionCommand(ActionPerformer performer, String action,
            List<String> args) {
        if (action.isEmpty() && help != null) {
            doHelp(performer);
            return;
        }

        String result = performActionCommand(performer, action, args);
        if (result != null) {
            performer.sendMessage(MsgColor.ERROR + result);
        }
    }

    /**
     * Parses an action command into an action.
     *
     * @param performer The performer of the action.
     * @param action The action.
     * @param args The arguments if any.
     * @return The result of the action.
     */
    protected abstract String performActionCommand(ActionPerformer performer,
            String action, List<String> args);

    public void doHelp(ActionPerformer performer) {
        help.doHelp(performer);
    }

    public void doHelp(ActionPerformer performer, String arg) {
        help.doHelp(performer, arg);
    }

}
