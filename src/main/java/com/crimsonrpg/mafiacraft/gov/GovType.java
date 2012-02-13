/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.gov;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author simplyianm
 */
public abstract class GovType {
    public static GovType MAFIA;

    public static GovType POLICE;

    private Map<String, String> locale = new HashMap<String, String>();

    public abstract String getName();

    public abstract void locale();

    public void m(String string, String localized) {
        locale.put(string, localized);
    }

    public String getLocale(String string) {
        String localized = locale.get(string);
        if (localized == null) {
            localized = "unknown";
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
                
                m("leader", "godfather");
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
                
                m("leader", "chief of police");
            }

        };
    }

}
