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
package net.voxton.mafiacraft.config;

import java.util.List;

/**
 * Configuration access object.
 */
public class Config {

    private static MafiacraftConfig config;

    public static void setConfig(MafiacraftConfig config) {
        Config.config = config;
    }

    public static MafiacraftConfig getConfig() {
        return config;
    }

    public static List<String> getStringList(String string) {
        return getConfig().getStringList(string);
    }

    public static String getString(String string) {
        return getConfig().getString(string);
    }

    public static long getLong(String string) {
        return getConfig().getLong(string);
    }

    public static int getInt(String string) {
        return getConfig().getInt(string);
    }

    public static double getDouble(String string) {
        return getConfig().getDouble(string);
    }

    public static List<Boolean> getBooleanList(String string) {
        return getConfig().getBooleanList(string);
    }

    public static boolean getBoolean(String string) {
        return getConfig().getBoolean(string);
    }

    public static Object get(String string) {
        return getConfig().get(string);
    }

    /**
     * Sets a certain path to a certain value. (Persists)
     *
     * @param path
     * @param value
     */
    public static void set(String path, Object value) {
        getConfig().set(path, value);
        getConfig().saveConfig();
    }

}
