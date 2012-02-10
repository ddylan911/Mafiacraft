/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.session;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author simplyianm
 */
public class SessionStore {
    private Map<String, Object> data = new HashMap<String, Object>();

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean def) {
        String s = getString(key);
        if (s == null) {
            return def;
        }
        if (s != null) {
            return true;
        }
        return false;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int def) {
        String s = getString(key);
        if (s == null) {
            return def;
        }
        try {
            def = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return def;
        }
        return def;
    }

    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public double getDouble(String key, double def) {
        String s = getString(key);
        if (s == null) {
            return def;
        }
        try {
            def = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return def;
        }
        return def;
    }

    public String getString(String key) {
        Object ob = data.get(key);
        if (ob == null) {
            return null;
        }
        return ob.toString();
    }

    public Object getObject(String key) {
        return data.get(key);
    }

    public <T> T getObject(String key, Class<T> type) {
        Object obj = getObject(key);
        if (obj == null) {
            return null;
        }
        if (!type.isInstance(obj)) {
            return null;
        }
        return type.cast(obj);
    }

    public void setData(String key, Object value) {
        data.put(key, value);
    }

    public void resetData(String key) {
        data.remove(key);
    }

}
