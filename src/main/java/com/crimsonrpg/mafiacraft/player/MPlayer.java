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
    
    private Government government;
    
    private Position position;
    
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
        return government;
    }

    public MPlayer setGovernment(Government government) {
        this.government = government;
        return this;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    
    public SessionStore getSessionStore() {
        return Mafiacraft.getInstance().getSessionManager().getStore(player);
    }

    public boolean canBuild(MPlayer player, Chunk chunk) {
        //TODO: check if the player lets the person build in that chunk?
        return player.equals(this);
    }

    public String getOwnerName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendMessage(String message) {
        player.sendMessage(message);
    }
}
