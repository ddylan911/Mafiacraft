/*
 * This file is part of Mafiacraft.
 * 
 * Mafiacraft is released under the Voxton License version 1.
 *
 * Mafiacraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition to this, you must also specify that this product includes 
 * software developed by Voxton.net and may not remove any code
 * referencing Voxton.net directly or indirectly.
 * 
 * Mafiacraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
 */
package net.voxton.mafiacraft.gov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.voxton.mafiacraft.chat.MsgColor;

/**
 * Represents a type of Government.
 */
public abstract class GovType {

    public static GovType MAFIA;

    public static GovType POLICE;

    private static Map<String, GovType> types = new HashMap<String, GovType>();

    private Map<String, String> locale = new HashMap<String, String>();

    private final boolean canFound;

    private final String color;

    public GovType(boolean canFound, String color) {
        locale();
        this.canFound = canFound;
        this.color = color;
    }

    /**
     * Gets a List of all GovTypes.
     *
     * @return A list of all GovTypes.
     */
    public static List<GovType> getGovTypes() {
        return new ArrayList<GovType>(types.values());
    }

    /**
     * Gets a GovType from a String.
     *
     * @param typeStr The GovType string.
     * @return The GovType corresponding with the String, or null if it doesn't
     * exist.
     */
    public static GovType fromString(String typeStr) {
        return types.get(typeStr);
    }

    public abstract String getName();

    public abstract void locale();

    public String getColor() {
        return color;
    }

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

    public boolean canFound() {
        return canFound;
    }

    static {
        MAFIA = new GovType(true, MsgColor.MAFIA) {

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
                m("gov.chatp", MsgColor.MAFIA + "[M]");
                m("gov.chatalias", "maf");
                m("div.chatpref", "r"); //Regime
                m("div.chatp", MsgColor.REGIME + "[R]");
                m("div.chatalias", "reg");
                m("off.chatpref", "o"); //Officer
                m("off.chatp", MsgColor.OFFICER + "[O]");
                m("off.chatalias", "off");
            }

        };
        POLICE = new GovType(false, MsgColor.POLICE) {

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
                m("gov.chatp", MsgColor.POLICE + "[P]");
                m("gov.chatalias", "pol");
                m("div.chatpref", "s"); //Squad
                m("div.chatp", MsgColor.SQUAD + "[S]");
                m("div.chatalias", "sq");
                m("off.chatpref", "o"); //Commander
                m("off.chatp", MsgColor.COMMANDER + "[CM]");
                m("off.chatalias", "cmd");
            }

        };

        types.put(MAFIA.getName(), MAFIA);
        types.put(POLICE.getName(), POLICE);
    }

}
