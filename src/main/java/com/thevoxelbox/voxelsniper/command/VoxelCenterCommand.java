package com.thevoxelbox.voxelsniper.command;

import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Sniper;
import com.thevoxelbox.voxelsniper.VoxelSniper;
import com.thevoxelbox.voxelsniper.api.command.VoxelCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VoxelCenterCommand extends VoxelCommand
{
    public VoxelCenterCommand(final VoxelSniper plugin)
    {
        super("VoxelCenter", plugin);
        setIdentifier("vc");
        setPermission("voxelsniper.sniper");
    }

    @Override
    public boolean onCommand(Player player, String[] args)
    {
        Sniper sniper = plugin.getSniperManager().getSniperForPlayer(player);
        SnipeData snipeData = sniper.getSnipeData(sniper.getCurrentToolId());

        try
        {
            int center = Integer.parseInt(args[0]);
            snipeData.setcCen(center);
            snipeData.getVoxelMessage().center();
            return true;
        }
        catch (final Exception exception)
        {
            player.sendMessage(ChatColor.RED + "Invalid input.");
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> fallback = Arrays.asList("0", "1", "2", "4", "8", "16", "32", "64");
        if (args.length == 0) {
            return fallback;
        } else if (args.length == 1) {
            String firstArg = args[0];
            try {
                int i = Integer.parseInt(firstArg);
                return Arrays.asList(firstArg, firstArg + "0", firstArg + "00");
            } catch (NumberFormatException e) {
                return fallback;
            }
        } else {
            return Collections.emptyList();
        }
    }

}
