/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.player;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

/**
 *
 * @author simplyianm
 */
public class SessionManager {
    private Mafiacraft core;

    private Map<String, SessionStore> sessionVars = new HashMap<String, SessionStore>();

    public SessionManager(Mafiacraft plugin) {
        this.core = plugin;
    }

    public SessionStore getStore(Player player) {
        SessionStore store = sessionVars.get(player.getName());
        if (store == null) {
            store = new SessionStore();
            sessionVars.put(player.getName(), store);
        }
        return store;
    }

    public void freeStore(Player player) {
        sessionVars.remove(player.getName());
    }

}
