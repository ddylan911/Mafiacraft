/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.player;

import com.crimsonrpg.mafiacraft.MafiacraftPlugin;
import com.crimsonrpg.mafiacraft.chat.ChatType;
import com.crimsonrpg.mafiacraft.gov.Division;
import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.gov.LandOwner;
import com.crimsonrpg.mafiacraft.gov.Position;
import java.util.List;
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
    private List<Division> divisions;
    private SessionStore store;
    private ChatType chatType;

    public MPlayer(Player player) {
        this.player = player;
    }

    public double addMoney(double amount) {
        EconomyResponse response = MafiacraftPlugin.getInstance().getVaultHelper().getEconomy().depositPlayer(player.getName(), amount);
        return response.balance;
    }

    public double subtractMoney(double amount) {
        EconomyResponse response = MafiacraftPlugin.getInstance().getVaultHelper().getEconomy().withdrawPlayer(player.getName(), amount);
        return response.balance;
    }

    public double getMoney() {
        return MafiacraftPlugin.getInstance().getVaultHelper().getEconomy().getBalance(player.getName());
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

    public ChatType getChatType() {
        return chatType;
    }

    /**
     * Gets the division a certain player is part of.
     * 
     * @param player
     * @return 
     */
    public Division getDivision(MPlayer player) {
        return player.getGovernment().getDivision(player.getName());
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }

    public Government getGovernment() {
        return MafiacraftPlugin.getInstance().getGovernmentManager().getGovernment(this);
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
