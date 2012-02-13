/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.player;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.gov.LandOwner;
import com.crimsonrpg.mafiacraft.gov.Position;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

/**
 *
 * @author simplyianm
 */
public class MPlayer implements LandOwner {
    private final Player player;

    private String title;
    
    private Position position;
	
	private SessionStore store;
    
    public MPlayer(Player player) {
        this.player = player;
    }
    
    public double addMoney(double amount) {
        EconomyResponse response = Mafiacraft.getInstance().getVaultHelper().getEconomy().depositPlayer(player.getName(), amount);
        return response.balance;
    }
    
    public double subtractMoney(double amount) {
        EconomyResponse response = Mafiacraft.getInstance().getVaultHelper().getEconomy().withdrawPlayer(player.getName(), amount);
        return response.balance;
    }
    
    public double getMoney() {
        return Mafiacraft.getInstance().getVaultHelper().getEconomy().getBalance(player.getName());
    }

    public Player getPlayer() {
        return player;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public Government getGovernment() {
        for (Government gov : Mafiacraft.getInstance().getGovernmentManager().getGovernments()) {
            if (gov.isMember(this)) {
                return gov;
            }
        }
        return null;
    }

    public Position getPosition() {
        Government gov = getGovernment();
        if (gov == null) {
            return Position.NONE;
        }
        return gov.getPosition(this);
    }
    
    public SessionStore getSessionStore() {
        if (store == null) {
            store = new SessionStore();
        }
        return store;
    }

    public boolean canBuild(MPlayer player, Chunk chunk) {
        //TODO: check if the player lets the person build in that chunk?
        return player.equals(this);
    }

    public String getName() {
        return player.getName();
    }
    
    public String getDisplayName() {
        return player.getDisplayName();
    }
    
    public String getOwnerName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    public String getOwnerId() {
        return "P-" + getName();
    }
}
