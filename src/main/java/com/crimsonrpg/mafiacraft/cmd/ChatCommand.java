/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.cmd;

import com.crimsonrpg.mafiacraft.chat.ChatType;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;

/**
 * /chat command.
 */
public class ChatCommand {
	public static String parseCmd(MPlayer player) {
		parseCmd(player, ChatType.DEFAULT);
		return null;
	}
	
    public static String parseCmd(MPlayer player, ChatType type) {
        player.setChatType(type);
		player.sendMessage(MsgColor.SUCCESS + "You have changed to " + type.getName(player) + " chat.");
		return null;
    }
}
