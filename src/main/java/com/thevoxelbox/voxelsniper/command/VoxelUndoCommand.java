package com.thevoxelbox.voxelsniper.command;

import com.thevoxelbox.voxelsniper.Sniper;
import com.thevoxelbox.voxelsniper.VoxelSniper;
import com.thevoxelbox.voxelsniper.api.command.VoxelCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VoxelUndoCommand extends VoxelCommand
{
    public VoxelUndoCommand(final VoxelSniper plugin)
    {
        super("VoxelUndo", plugin);
        setIdentifier("u");
        setPermission("voxelsniper.sniper");
    }

    @Override
    public boolean onCommand(Player player, String[] args)
    {
        Sniper sniper = plugin.getSniperManager().getSniperForPlayer(player);

        if (args.length == 1)
        {
            try
            {
                int amount = Integer.parseInt(args[0]);
                sniper.undo(amount);
            }
            catch (NumberFormatException exception)
            {
                player.sendMessage("Error while parsing amount of undo. Number format exception.");
            }
        }
        else
        {
            sniper.undo();
        }
        plugin.getLogger().info("Player \"" + player.getName() + "\" used /u");
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        if (args.length == 0 || (args.length == 1 && args[0].isEmpty())) {
            return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
        }

        if (args.length == 1) {
            String firstArg = args[0];
            return Arrays.asList(firstArg, firstArg + "0", firstArg + "1", firstArg + "2", firstArg + "3",
                    firstArg + "4",firstArg + "5", firstArg + "6", firstArg + "7", firstArg + "8", firstArg + "9");
        }

        return Collections.emptyList();
    }
}
