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
package net.voxton.mafiacraft.core.gov;

import net.voxton.mafiacraft.core.Mafiacraft;
import net.voxton.mafiacraft.core.city.District;
import net.voxton.mafiacraft.core.city.LandOwner;
import net.voxton.mafiacraft.core.gov.Division;
import net.voxton.mafiacraft.core.gov.Government;
import net.voxton.mafiacraft.core.player.MPlayer;
import net.voxton.mafiacraft.core.task.Task;

/**
 * Collects task from all land.
 */
public class PaydayTask implements Task {

    @Override
    public void run() {
        for (District district : Mafiacraft.getDistrictList()) {
            double pay = ((int) district.getLandCost()) >> 4;
            
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    LandOwner owner = district.getOwner(i, j);
                    
                    switch (owner.getOwnerType()) {
                        case PLAYER:
                            MPlayer player = (MPlayer) owner;
                            player.addMoney(pay);
                            break;
                            
                        case GOVERNMENT:
                            Government gov = (Government) owner;
                            gov.addMoney(pay);
                            break;
                            
                        case DIVISION:
                            Division div = (Division) owner;
                            div.addMoney(pay);
                            break;
                    }
                }
            }
        }
    }
    
}
