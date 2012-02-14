/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat;

import com.crimsonrpg.mafiacraft.player.MPlayer;

/**
 *
 * @author simplyianm
 */
public abstract class ChatType {
    //TODO chattypes
    public static final ChatType GOVERNMENT = null;
    
    public abstract void chat(MPlayer player, String message);
}
