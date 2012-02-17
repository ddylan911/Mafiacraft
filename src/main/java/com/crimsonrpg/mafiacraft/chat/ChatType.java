/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.chat;

import com.crimsonrpg.mafiacraft.gov.GovType;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import java.util.HashMap;
import java.util.Map;

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
	public static final ChatType DEFAULT = GLOBAL;
    
	/**
	 * Map of lowercase chat types to their corresponding ChatType objects.
	 */
	private static Map<String, ChatType> chatTypes = new HashMap<String, ChatType>();
	
    public abstract void chat(MPlayer player, String message);

	/**
	 * Gets the name of this Chat type.
	 * 
	 * @param player The player that is seeing 
	 *		what this chat is called for them. Relativism ftw.
	 * @return 
	 */
	public abstract String getName(MPlayer player);
	
	/**
	 * Gets a chat type from a string.
	 * 
	 * @param name
	 * @return 
	 */
	public static ChatType valueOf(String name) {
		ChatType type = chatTypes.get(name);
		if (type == null) {
			type = ChatType.DEFAULT;
		}
		return type;
	}
	
	static {
		//City
		chatTypes.put("city", CITY);
		chatTypes.put("c", CITY);
		
		//District
		chatTypes.put("district", DISTRICT);
		chatTypes.put("dist", DISTRICT);
		chatTypes.put("d", DISTRICT);
		
		//Global
		chatTypes.put("global", GLOBAL);
		chatTypes.put("gb", GLOBAL);
		chatTypes.put("g", GLOBAL);
		
		//Local
		chatTypes.put("local", LOCAL);
		chatTypes.put("l", LOCAL);
		
		//Government
		chatTypes.put("government", GOVERNMENT);
		chatTypes.put("gov", GOVERNMENT);
		chatTypes.put("g", GOVERNMENT);
		
		//Division
		chatTypes.put("division", DIVISION);
		chatTypes.put("div", DIVISION);
		chatTypes.put("d", DIVISION);
		
		//Officer
		chatTypes.put("officer", OFFICER);
		chatTypes.put("off", OFFICER);
		chatTypes.put("o", OFFICER);
		
		//GovType stuff
		for (GovType type : GovType.getGovTypes()) {
			chatTypes.put(type.getName(), GOVERNMENT);
			chatTypes.put(type.getLocale("gov.chatpref"), GOVERNMENT);
			chatTypes.put(type.getLocale("gov.chatalias"), GOVERNMENT);
			
			chatTypes.put(type.getLocale("division"), DIVISION);
			chatTypes.put(type.getLocale("div.chatpref"), DIVISION);
			chatTypes.put(type.getLocale("div.chatalias"), DIVISION);
			
			chatTypes.put(type.getLocale("officer"), OFFICER);
			chatTypes.put(type.getLocale("off.chatpref"), OFFICER);
			chatTypes.put(type.getLocale("off.chatalias"), OFFICER);
		}
	}
}
