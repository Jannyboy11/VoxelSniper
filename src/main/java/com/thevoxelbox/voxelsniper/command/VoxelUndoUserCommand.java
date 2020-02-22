package com.thevoxelbox.voxelsniper.command;

import com.thevoxelbox.voxelsniper.VoxelSniper;
import com.thevoxelbox.voxelsniper.api.command.VoxelCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class VoxelUndoUserCommand extends VoxelCommand
{
    public VoxelUndoUserCommand(final VoxelSniper plugin)
    {
        super("VoxelUndoUser", plugin);
        setIdentifier("uu");
        setPermission("voxelsniper.sniper");
    }

    @Override
    public boolean onCommand(Player player, String[] args)
    {
        try
        {
            plugin.getSniperManager().getSniperForPlayer(Bukkit.getPlayer(args[0])).undo();
            return true;
        }
        catch (final Exception exception)
        {
            player.sendMessage(ChatColor.GREEN + "Player not found.");
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(Player plyaer, String[] args) {
        if (args.length <= 1) {
            return null; //tabcomplete players
        } else {
            return Collections.emptyList();
        }
    }
}
