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
package net.voxton.mafiacraft.data;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import net.voxton.mafiacraft.logging.MLogger;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.impl.MafiacraftImpl;
import org.bukkit.Bukkit;

/**
 * A little slave that will manage all data for us.
 */
public class DataWorker {

    /**
     * Reference to the plugin instance.
     */
    private final MafiacraftImpl plugin;

    /**
     * Constructor.
     *
     * @param plugin The plugin instance.
     */
    public DataWorker(MafiacraftImpl plugin) {
        this.plugin = plugin;
        setupSaveTask();
    }

    /**
     * Sets up the task that will save everything every 5 minutes.
     */
    private void setupSaveTask() {
//        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
//
//            @Override
//            public void run() {
//                saveAll();
//            }
//
//        }, 0L, 6000); // Approximately once every 5 minutes.
    }

    /**
     * Loads everything that should not be lazy-loaded onto the server.
     *
     * @return This DataWorker.
     */
    public DataWorker loadAll() {
        Mafiacraft.getCityManager().load();
        Mafiacraft.getGovernmentManager().load();
        return this;
    }

    /**
     * Saves everything possible to save on the server.
     *
     * @return This DataWorker.
     */
    public DataWorker saveAll() {
        Mafiacraft.getCityManager().save();
        Mafiacraft.getGovernmentManager().save();
        Mafiacraft.getPlayerManager().saveAll();
        return this;
    }

    /**
     * Gets a certain sub-folder of the plugin and makes directories if it
     * doesn't exist.
     *
     * @param name The name of the sub-folder.
     * @return The subfolder.
     */
    public File getSubFolder(String name) {
        File folder = new File(plugin.getDataFolder().getPath() + File.separator
                + name + File.separator);
        folder.mkdirs();
        return folder;
    }

    /**
     * Gets a top-level file with the given name.
     *
     * @param name The name of the file.
     * @return The created file.
     */
    public File getTopFile(String name) {
        return getFileAndCreate(plugin.getDataFolder().getPath()
                + File.separator
                + name);
    }

    /**
     * Gets a second-level file with the given name and directory and creates it if not found.
     *
     * @param dir The directory containing the file.
     * @param name The name of the file.
     * @return The file retrieved.
     */
    public File getOrCreateSubFile(String dir, String name) {
        File folder = getSubFolder(dir);
        return getFileAndCreate(folder.getPath() + File.separator + name);
    }

    /**
     * Gets a sub file.
     * 
     * @param dir The directory containing the file.
     * @param name The name of the file.
     * @return The file retrieved.
     */
    public File getSubFile(String dir, String name) {
        File folder = getSubFolder(dir);
        File f = getFile(folder.getPath() + File.separator + name);
        System.out.println(f);
        return f;
    }

    /**
     * Gets a File and creates it if it doesn't exist.
     *
     * @param path The path to the file.
     * @return The created File.
     */
    private File getFileAndCreate(String path) {
        File file = getFile(path);
        createFileIfNotExisting(file);
        return file;
    }

    /**
     * Gets a file.
     * 
     * @param path The path to the file.
     * @return The file.
     */
    private File getFile(String path) {
        return new File(path);
    }

    /**
     * Creates a file if it doesn't exist.
     * 
     * @param file The file to create.
     * @return The created file.
     */
    private File createFileIfNotExisting(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                MLogger.log(Level.SEVERE, "Error creating the file: '" + file
                        + "'!", ex);
            }
        }
        return file;
    }

}
