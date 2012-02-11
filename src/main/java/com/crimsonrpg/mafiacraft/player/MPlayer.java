/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.player;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.gov.Government;
import org.bukkit.entity.Player;

/**
 *
 * @author simplyianm
 */
public class MPlayer {
    private final Player player;

    private Government government;
    
    public MPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Government getGovernment() {
        return government;
    }

    public MPlayer setGovernment(Government government) {
        this.government = government;
        return this;
    }
    
    public SessionStore getSessionStore() {
        return Mafiacraft.getInstance().getSessionManager().getStore(player);
    }
}
