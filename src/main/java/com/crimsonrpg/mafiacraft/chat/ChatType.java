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
    public static final ChatType GOVERNMENT = new GovernmentChat();
    public static final ChatType DIVISION = new DivisionChat();
    
    public abstract void chat(MPlayer player, String message);
}
