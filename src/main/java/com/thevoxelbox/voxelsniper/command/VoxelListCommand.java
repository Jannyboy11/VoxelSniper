package com.thevoxelbox.voxelsniper.command;

import com.thevoxelbox.voxelsniper.RangeBlockHelper;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Sniper;
import com.thevoxelbox.voxelsniper.VoxelSniper;
import com.thevoxelbox.voxelsniper.api.command.VoxelCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class VoxelListCommand extends VoxelCommand
{
    public VoxelListCommand(final VoxelSniper plugin)
    {
        super("VoxelList", plugin);
        setIdentifier("vl");
        setPermission("voxelsniper.sniper");
    }

    //TODO tabcompletion!!

    @Override
    public boolean onCommand(Player player, String[] args)
    {
        Sniper sniper = plugin.getSniperManager().getSniperForPlayer(player);

        SnipeData snipeData = sniper.getSnipeData(sniper.getCurrentToolId());
        if (args.length == 0)
        {
            final RangeBlockHelper rangeBlockHelper = new RangeBlockHelper(player, player.getWorld());
            final Block targetBlock = rangeBlockHelper.getTargetBlock();
            snipeData.getVoxelList().add(targetBlock.getBlockData());
            snipeData.getVoxelMessage().voxelList();
            return true;
        }
        else
        {
            if (args[0].equalsIgnoreCase("clear"))
            {
                snipeData.getVoxelList().clear();
                snipeData.getVoxelMessage().voxelList();
                return true;
            }
        }

        boolean remove = false;

        for (final String string : args)
        {
            String tmpint;
            BlockData xBlockData;

            if (string.startsWith("-"))
            {
                remove = true;
                tmpint = string.replaceAll("-", "");
            }
            else
            {
                tmpint = string;
            }

            xBlockData = Bukkit.createBlockData(tmpint);

            if (xBlockData != null && xBlockData.getMaterial().isBlock())
            {
                if (!remove)
                {
                    snipeData.getVoxelList().add(xBlockData);
                    snipeData.getVoxelMessage().voxelList();
                }
                else
                {
                    snipeData.getVoxelList().removeValue(xBlockData);
                    snipeData.getVoxelMessage().voxelList();
                }
            }


        }
        return true;
    }
}
