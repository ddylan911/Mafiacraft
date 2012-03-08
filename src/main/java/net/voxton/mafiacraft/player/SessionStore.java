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
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
 */
package net.voxton.mafiacraft.player;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds session data about the player. Gets erased on player logout.
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

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public long getLong(String key, long def) {
        String s = getString(key);
        if (s == null) {
            return def;
        }
        try {
            def = Long.parseLong(s);
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
