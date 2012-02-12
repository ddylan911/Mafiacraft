/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.cmd;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.gov.GovType;
import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.player.MPlayer;

/**
 *
 * @author simplyianm
 */
public class GovernmentCommand {
    public static String doFound(MPlayer player, String name, GovType type) {
        double balance = player.getMoney();
        double cost = Mafiacraft.getInstance().getConfig().getDouble("prices.mafia.found", 1000000.0);
        
        if (balance < cost) {
            return "You don't have enough money to do this. (Costs $" + cost + ")";
        }
        
        if (player.getGovernment() != null) {
            return "You are already in a government!";
        }
        
        Government founded = Mafiacraft.getInstance().getGovernmentManager().createGovernment(name, type);
        
        
        return null;
    }
}
