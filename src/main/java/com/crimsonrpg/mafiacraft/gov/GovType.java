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

    public static GovType CITY;

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
                return "Mafia";
            }

            @Override
            public void locale() {
                m("leader", "godfather");
            }

        };
        CITY = new GovType() {
            @Override
            public String getName() {
                return "City";
            }

            @Override
            public void locale() {
                m("leader", "chief of police");
            }

        };
    }

}
