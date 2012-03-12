/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.data;

import net.voxton.mafiacraft.impl.bukkit.BukkitDataWorker;
import java.io.File;

/**
 *
 * @author ianschool
 */
public interface DataWorker {

    /**
     * Gets a second-level file with the given name and directory and creates it if not found.
     *
     * @param dir The directory containing the file.
     * @param name The name of the file.
     * @return The file retrieved.
     */
    File getOrCreateSubFile(String dir, String name);

    /**
     * Gets a sub file.
     *
     * @param dir The directory containing the file.
     * @param name The name of the file.
     * @return The file retrieved.
     */
    File getSubFile(String dir, String name);

    /**
     * Gets a certain sub-folder of the plugin and makes directories if it
     * doesn't exist.
     *
     * @param name The name of the sub-folder.
     * @return The subfolder.
     */
    File getSubFolder(String name);

    /**
     * Gets a top-level file with the given name.
     *
     * @param name The name of the file.
     * @return The created file.
     */
    File getTopFile(String name);

    /**
     * Loads everything that should not be lazy-loaded onto the server.
     *
     * @return This DataWorker.
     */
    BukkitDataWorker loadAll();

    /**
     * Saves everything possible to save on the server.
     *
     * @return This DataWorker.
     */
    BukkitDataWorker saveAll();
    
}
