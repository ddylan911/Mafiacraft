/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.data;

import net.voxton.mafiacraft.Mafiacraft;
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
	 * Saves everything possible to save on the server.
	 * 
	 * @return This DataWorker.
	 */
	public DataWorker saveAll() {
		
		mc.getPlayerManager().saveAll();
		return this;
	}
}
