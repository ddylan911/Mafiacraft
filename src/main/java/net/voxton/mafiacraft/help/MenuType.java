/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.help;

import net.voxton.mafiacraft.help.menu.CWorldMenu;
import net.voxton.mafiacraft.help.menu.CityMenu;

/**
 * The type of menu.
 */
public class MenuType {

    /**
     * City help menu.
     */
    public static final HelpMenu CITY = new CityMenu();

    /**
     * CWorld help menu.
     */
    public static final HelpMenu CWORLD = new CWorldMenu();

}
