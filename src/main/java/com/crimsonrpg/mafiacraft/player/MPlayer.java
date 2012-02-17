/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.player;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.MafiacraftPlugin;
import com.crimsonrpg.mafiacraft.chat.ChatType;
import com.crimsonrpg.mafiacraft.classes.UtilityClass;
import com.crimsonrpg.mafiacraft.geo.*;
import com.crimsonrpg.mafiacraft.gov.Division;
import com.crimsonrpg.mafiacraft.gov.Government;
import com.crimsonrpg.mafiacraft.gov.Position;
import com.crimsonrpg.mafiacraft.vault.Transactable;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

/**
 *
 * @author simplyianm
 */
public class MPlayer extends Transactable implements LandPurchaser {
    private final Player player;

    private String title;

    private SessionStore store;

    private UtilityClass utilityClass;

    private ChatType chatType;

    private int land;

    public MPlayer(Player player) {
        this.player = player;
    }

    @Override
    public double getMoney() {
        return Mafiacraft.getVaultHelper().getEconomy().getBalance(player.getName());
    }

    @Override
    public double setMoney(double amt) {
        return Mafiacraft.getVaultHelper().getEconomy().depositPlayer(player.getName(), amt - getMoney()).balance;
    }

    @Override
    public double addMoney(double amount) {
        EconomyResponse response = Mafiacraft.getVaultHelper().getEconomy().depositPlayer(player.getName(), amount);
        return response.balance;
    }

    @Override
    public double subtractMoney(double amount) {
        EconomyResponse response = Mafiacraft.getVaultHelper().getEconomy().withdrawPlayer(player.getName(), amount);
        return response.balance;
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

    /**
     * Gets the player's current chatType.
     * 
     * @return 
     */
    public ChatType getChatType() {
        if (chatType == null) {
            chatType = ChatType.DEFAULT;
        }
        return chatType;
    }

    public UtilityClass getUtilityClass() {
        return utilityClass;
    }

    public void setUtilityClass(UtilityClass classType) {
        this.utilityClass = classType;
    }

    public Division getDivision() {
        return this.getGovernment().getDivision(this);
    }

    public City getCity() {
        return this.getDistrict().getCity();
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }

    public Government getGovernment() {
        return Mafiacraft.getGovernmentManager().getGovernment(this);
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
        return player.getName();
    }

    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    public String getOwnerId() {
        return "P-" + getName();
    }

    /**
     * Gets the chunk the player is currently in.
     *
     * @return
     */
    public Chunk getChunk() {
        return player.getLocation().getChunk();
    }

    /**
     * Gets the district the player is currently in.
     *
     * @return
     */
    public District getDistrict() {
        return Mafiacraft.getCityManager().getDistrict(getChunk());
    }

    /**
     * {@inheritDoc}
     */
    public boolean canBeClaimed(Chunk chunk, LandOwner futureOwner) {
        return false;
    }

    /**
     * Gets the kill score of this player.
     *
     * @return
     */
    public int getKillScore() {
        return Mafiacraft.getPlayerManager().getKillTracker().getKillScore(this);
    }

    public OwnerType getOwnerType() {
        return OwnerType.PLAYER;
    }

    /**
     * {@inheritDoc}
     */
    public int getLand() {
        return land;
    }

    /**
     * {@inheritDoc}
     */
    public MPlayer setLand(int amt) {
        this.land = amt;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public MPlayer incLand() {
        land++;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public MPlayer decLand() {
        land--;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public MPlayer claim(Chunk chunk) {
        District district = Mafiacraft.getDistrict(chunk);
        district.setOwner(chunk, this);
        incLand();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public MPlayer unclaim(Chunk chunk) {
        District district = Mafiacraft.getDistrict(chunk);
        district.setOwner(chunk, null);
        decLand();
        return this;
    }

}
