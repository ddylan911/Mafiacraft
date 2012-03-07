/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.help.menu;

import net.voxton.mafiacraft.help.HelpMenu;

/**
 * The city help menu.
 */
public class CityMenu extends HelpMenu {

    public CityMenu() {
        super("City");
    }

    @Override
    public void loadMenu() {
        addEntry("annex", "Annexes the district you are standing in to the city.", "annex");
        addEntry("bus", "Teleports you to the given bus stop.", "bus <district>");
        addEntry("spawn", "Teleports you to the city's spawn location.", "spawn");
    }

}
