/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat;

import com.crimsonrpg.mafiacraft.player.MPlayer;

/**
 *
 * @author Dylan
 */
public class GovernmentChat extends ChatType {

    @Override
    public void chat(MPlayer player, String message) {
        for (MPlayer players : player.getGovernment().getOnlineMembers()) {
            players.sendMessage("<" + player.getDisplayName() + "> " + message);
        }
    }
}