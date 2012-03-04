/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.help.menu;

import net.voxton.mafiacraft.help.HelpMenu;

/**
 *
 * @author simplyianm
 */
public class CityMenu extends HelpMenu {

    public CityMenu() {
        super("City");
    }

    @Override
    public void loadMenu() {
        addEntry("bus <district>", "Teleports you to the given bus stop.");
        addEntry("spawn", "Teleports you to the city's spawn location.");
    }
    
}
