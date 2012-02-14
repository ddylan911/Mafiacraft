/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.vault;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 *
 * @author simplyianm
 */
public class VaultHelper {
    private final Mafiacraft mc;

    private Permission permission = null;

    private Economy economy = null;

    private Chat chat = null;

    public VaultHelper(Mafiacraft mc) {
        this.mc = mc;
        setupEconomy();
    }

    private Boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private Boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    private Boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    public Chat getChat() {
        return chat;
    }

    public Economy getEconomy() {
        return economy;
    }

    public Permission getPermission() {
        return permission;
    }

}
