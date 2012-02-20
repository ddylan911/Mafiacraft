/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.cmd;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.geo.DistrictType;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.MsgColor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author simplyianm
 */
public class DistrictCommand {
    public static void parseCmd(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MsgColor.ERROR + "Sorry, this command is only usable in game.");
            return;
        }

        MPlayer player = Mafiacraft.getPlayer((Player) sender);

        if (args.length < 1) {
            doHelp(player);
            return;
        }

        //Get the function we want to do.
        String function = args[0];
        List<String> largs = new ArrayList<String>(Arrays.asList(args));
        largs.remove(0);

        String result = null;
        
        if (largs.size() < 0) {
            
        }
    }

    public static String doHelp(MPlayer player) {
        //TODO: be helpful
        player.sendMessage(MsgColor.ERROR + "TODO: help");
        return null;
    }

    public static String doZone(MPlayer player, String typeString) {
        DistrictType type = DistrictType.fromString(typeString);
        if (type == null) {
            return "No such zone!";
        }
        
        return null;
    }
}
