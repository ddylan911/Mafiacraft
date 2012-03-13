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
package net.voxton.mafiacraft.action;

import net.voxton.mafiacraft.action.action.CWorldActions;
import net.voxton.mafiacraft.action.action.ChatActions;
import net.voxton.mafiacraft.action.action.CityActions;
import net.voxton.mafiacraft.action.action.DistrictActions;
import net.voxton.mafiacraft.action.action.DivisionActions;
import net.voxton.mafiacraft.action.action.GovernmentActions;
import net.voxton.mafiacraft.action.action.MafiacraftActions;
import net.voxton.mafiacraft.action.action.SectionActions;
import net.voxton.mafiacraft.gov.GovType;

/**
 * Contains constants to get to each action quickly.
 */
public class ActionType {

    public static final CWorldActions CWORLD = new CWorldActions();

    public static final ChatActions CHAT = new ChatActions();

    public static final CityActions CITY = new CityActions();

    public static final DistrictActions DISTRICT = new DistrictActions();

    public static final GovernmentActions MAFIA = new GovernmentActions(
            GovType.MAFIA);
    
    public static final MafiacraftActions MAFIACRAFT = new MafiacraftActions();

    public static final DivisionActions REGIME = new DivisionActions(
            GovType.MAFIA);

    public static final GovernmentActions POLICE =
            new GovernmentActions(GovType.POLICE);

    public static final SectionActions SECTION = new SectionActions();

    public static final DivisionActions SQUAD = new DivisionActions(
            GovType.POLICE);

}
