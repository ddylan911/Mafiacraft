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
package net.voxton.mafiacraft.impl.bukkit;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import net.voxton.mafiacraft.MLogger;
import net.voxton.mafiacraft.MafiacraftCore;
import net.voxton.mafiacraft.data.DataWorker;
import org.bukkit.Bukkit;

/**
 * A little slave that will manage all data for us.
 */
public class BukkitDataWorker implements DataWorker {

    /**
     * Reference to the plugin instance.
     */
    private final MafiacraftCore mc;

    /**
     * Constructor.
     *
     * @param plugin The plugin instance.
     */
    public BukkitDataWorker(MafiacraftCore plugin) {
        mc = plugin;
        setupSaveTask();
    }

    /**
     * Sets up the task that will save everything every 5 minutes.
     */
    private void setupSaveTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(mc, new Runnable() {

            @Override
            public void run() {
                saveAll();
            }

        }, 0L, 6000); // Approximately once every 5 minutes.
    }

    /**
     * Loads everything that should not be lazy-loaded onto the server.
     *
     * @return This DataWorker.
     */
    @Override
    public BukkitDataWorker loadAll() {
        mc.getCityManager().load();
        mc.getGovernmentManager().load();
        return this;
    }

    /**
     * Saves everything possible to save on the server.
     *
     * @return This DataWorker.
     */
    @Override
    public BukkitDataWorker saveAll() {
        mc.getCityManager().save();
        mc.getGovernmentManager().save();
        mc.getPlayerManager().saveAll();
        return this;
    }

    /**
     * Gets a certain sub-folder of the plugin and makes directories if it
     * doesn't exist.
     *
     * @param name The name of the sub-folder.
     * @return The subfolder.
     */
    @Override
    public File getSubFolder(String name) {
        File folder = new File(mc.getDataFolder().getPath() + File.separator
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
    @Override
    public File getTopFile(String name) {
        return getFileAndCreate(mc.getDataFolder().getPath() + File.separator
                + name);
    }

    /**
     * Gets a second-level file with the given name and directory and creates it if not found.
     *
     * @param dir The directory containing the file.
     * @param name The name of the file.
     * @return The file retrieved.
     */
    @Override
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
    @Override
    public File getSubFile(String dir, String name) {
        File folder = getSubFolder(dir);
        File f =  getFile(folder.getPath() + File.separator + name);
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
