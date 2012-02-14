/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.player;

/**
 * Tracks kills
 */
public class KillTracker {
	public int getKillScore(MPlayer player) {
		return -1;
	}
	
	public int setKillScore(MPlayer player, int score) {
		return -1;
	}
	
	public KillTracker incScore(MPlayer player) {
		return addScore(player, 1);
	}
	
	public KillTracker decScore(MPlayer player) {
		return subScore(player, 1);
	}
	
	public KillTracker addScore(MPlayer player, int amt) {
		return this;
	}
	
	public KillTracker subScore(MPlayer player, int amt) {
		return this;
	}
}
