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
                m("vice leader", "consigliere");
                m("officer", "officer");
                m("manager", "caporegime");
                m("worker", "soldier");
                m("affiliate", "associate");
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
                m("vice leader", "assistant chief");
                m("officer", "commander");
                m("manager", "major");
                m("worker", "officer");
                m("affiliate", "citizen");
            }

        };
    }

}
