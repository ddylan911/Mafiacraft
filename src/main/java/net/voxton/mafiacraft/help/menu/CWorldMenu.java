/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.help.menu;

import net.voxton.mafiacraft.help.HelpMenu;

/**
 * The /cworld help menu.
 */
public class CWorldMenu extends HelpMenu {

    public CWorldMenu() {
        super("CWorld");
    }

    @Override
    public void loadMenu() {
        addEntry("spawn", "Goes to the spawn location of the world.", "spawn");
    }

}
