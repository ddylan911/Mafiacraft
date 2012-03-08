/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.help.menu;

import net.voxton.mafiacraft.help.HelpMenu;

/**
 * Government help menu.
 */
public abstract class GovernmentMenu extends HelpMenu {

    public GovernmentMenu(String name) {
        super(name);
    }

    @Override
    public void loadMenu() {
        getName(); //TODO this
    }
    
}
