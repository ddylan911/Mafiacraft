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
package net.voxton.mafiacraft.help;

import net.voxton.mafiacraft.gov.GovType;
import net.voxton.mafiacraft.help.menu.CWorldMenu;
import net.voxton.mafiacraft.help.menu.CityMenu;
import net.voxton.mafiacraft.help.menu.DistrictMenu;
import net.voxton.mafiacraft.help.menu.DivisionMenu;
import net.voxton.mafiacraft.help.menu.GovernmentMenu;

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

    /**
     * District help menu.
     */
    public static final HelpMenu DISTRICT = new DistrictMenu();

    /**
     * Mafia help menu.
     */
    public static final HelpMenu MAFIA = new GovernmentMenu(GovType.MAFIA);

    /**
     * Police help menu.
     */
    public static final HelpMenu POLICE = new GovernmentMenu(GovType.POLICE);

    /**
     * Regime help menu.
     */
    public static final HelpMenu REGIME = new DivisionMenu(GovType.MAFIA);

    /**
     * Squad help menu.
     */
    public static final HelpMenu SQUAD = new DivisionMenu(GovType.POLICE);

}
