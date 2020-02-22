package com.thevoxelbox.voxelsniper.command;

import com.thevoxelbox.voxelsniper.VoxelSniper;
import com.thevoxelbox.voxelsniper.api.command.VoxelCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VoxelGoToCommand extends VoxelCommand
{
    public VoxelGoToCommand(final VoxelSniper plugin)
    {
        super("VoxelGoTo", plugin);
        setIdentifier("goto");
        setPermission("voxelsniper.sniper");
    }

    @Override
    public boolean onCommand(Player player, String[] args)
    {
        try
        {
            final double x = Double.parseDouble(args[0]);
            final double z = Double.parseDouble(args[1]);
            player.teleport(new Location(player.getWorld(), x, player.getWorld().getHighestBlockYAt((int) x, (int) z), z));
            player.sendMessage(ChatColor.GREEN + "Woosh!");
            return true;
        }
        catch (final Exception exception)
        {
            player.sendMessage(ChatColor.RED + "Invalid syntax.");
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args)
    {
        if (args.length == 0) return null;

        String firstArg = args[0];
        if (args.length == 1) {
            if (firstArg.isEmpty()) return Collections.singletonList("" + player.getLocation().getX());
            return Arrays.asList(firstArg, "" + player.getLocation().getX(), firstArg + "0", firstArg + "00", firstArg + "000", firstArg + "0000", firstArg + "00000");
        }

        String secondArg = args[1];
        if (args.length == 2) {
            if (secondArg.isEmpty()) return Collections.singletonList("" + player.getLocation().getZ());
            return Arrays.asList(secondArg, "" + player.getLocation().getZ(), secondArg + "0", secondArg + "00", secondArg + "000", secondArg + "0000", secondArg + "00000");
        }

        return Collections.emptyList();
    }
}
