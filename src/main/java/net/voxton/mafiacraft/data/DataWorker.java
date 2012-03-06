/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.data;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import net.voxton.mafiacraft.MLogger;
import net.voxton.mafiacraft.MafiacraftPlugin;

/**
 * Works the data.
 */
public class DataWorker {

    private final MafiacraftPlugin mc;

    public DataWorker(MafiacraftPlugin plugin) {
        mc = plugin;
    }

    /**
     * Loads everything that should not be lazy-loaded onto the server.
     * 
     * @return This DataWorker.
     */
    public DataWorker loadAll() {
        mc.getCityManager().load();
        mc.getGovernmentManager().load();
        return this;
    }

    /**
     * Saves everything possible to save on the server.
     *
     * @return This DataWorker.
     */
    public DataWorker saveAll() {
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
    public File getSubFolder(String name) {
        File folder = new File(mc.getDataFolder().getPath() + File.separator + name + File.separator);
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
        return getFileAndCreate(mc.getDataFolder().getPath() + File.separator + name);
    }

    /**
     * Gets a second-level file with the given name and directory.
     *
     * @param dir The directory containing the file.
     * @param name The name of the file.
     * @return The file retrieved.
     */
    public File getSubFile(String dir, String name) {
        File folder = getSubFolder(dir);
        return getFileAndCreate(folder.getPath() + File.separator + name);
    }

    /**
     * Gets a File and creates it if it doesn't exist.
     *
     * @param path The path to the file.
     * @return The created File.
     */
    private File getFileAndCreate(String path) {
        File file = new File(path);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                MLogger.log(Level.SEVERE, "Error creating the file: '" + file + "'!", ex);
            }
        }

        return file;
    }

}
