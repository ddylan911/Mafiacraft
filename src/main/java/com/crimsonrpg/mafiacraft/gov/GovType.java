/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;

/**
 *
 * @author simplyianm
 */
public abstract class GovType {
    public static GovType MAFIA;

    public static GovType POLICE;

	private static List<GovType> types = new ArrayList<GovType>();
	
    private Map<String, String> locale = new HashMap<String, String>();

    public GovType() {
        locale();
    }
    
	public static List<GovType> getGovTypes() {
		return new ArrayList<GovType>(types);
	}
	
    public abstract String getName();

    public abstract void locale();

    public void m(String string, String localized) {
        locale.put(string, localized);
    }

    public String getLocale(String string) {
        String localized = locale.get(string);
        if (localized == null) {
            localized = "unnamed";
        }
        return localized;
    }

    static {
        MAFIA = new GovType() {
            @Override
            public String getName() {
                return "mafia";
            }

            @Override
            public void locale() {
                m("command", "mafia");
                
                //Ranks
                m("leader", "godfather");
                m("vice leader", "consigliere");
                m("officer", "officer");
                m("manager", "caporegime");
                m("worker", "soldier");
                m("affiliate", "associate");
                m("affiliates", "associates");
                
                //Groups
                m("division", "regime");
				
				//Chat
				m("gov.chatpref", "m"); //Mafia
				m("gov.chatp", ChatColor.DARK_RED + "[M]");
				m("gov.chatalias", "maf");
				m("div.chatpref", "r"); //Regime
				m("div.chatp", ChatColor.GOLD + "[R]");
				m("div.chatalias", "reg");
				m("off.chatpref", "o"); //Officer
				m("off.chatp", ChatColor.GREEN + "[O]");
				m("off.chatalias", "off");
            }

        };
        POLICE = new GovType() {
            @Override
            public String getName() {
                return "police";
            }

            @Override
            public void locale() {
                m("command", "police");
                
                //Ranks
                m("leader", "chief of police");
                m("vice leader", "assistant chief");
                m("officer", "commander");
                m("manager", "major");
                m("worker", "sergeant");
                m("affiliate", "officer");
                m("affiliates", "officers");
                
                //Groups
                m("division", "squad");
				
				//Chat
				m("gov.chatpref", "p"); //Police
				m("gov.chatp", ChatColor.DARK_BLUE + "[P]");
				m("gov.chatalias", "pol");
				m("div.chatpref", "s"); //Squad
				m("div.chatp", ChatColor.AQUA + "[S]");
				m("div.chatalias", "sq");
				m("off.chatpref", "o"); //Commander
				m("off.chatp", ChatColor.GRAY + "[CM]");
				m("off.chatalias", "cmd");
            }

        };
		
		types.add(MAFIA);
		types.add(POLICE);
    }

}
