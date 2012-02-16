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
    
    public static final ChatType GOVERNMENT = new GovernmentChat();
    public static final ChatType DIVISION = new DivisionChat();
    public static final ChatType OFFICER = new OfficerChat();
    public static final ChatType LOCAL = new LocalChat();
    public static final ChatType CITY = new CityChat();
    public static final ChatType DISTRICT = new DistrictChat();
    public static final ChatType GLOBAL = new GlobalChat();
    
    public abstract void chat(MPlayer player, String message);
}
