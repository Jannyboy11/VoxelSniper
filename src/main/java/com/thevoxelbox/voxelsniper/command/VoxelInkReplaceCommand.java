package com.thevoxelbox.voxelsniper.command;

import com.thevoxelbox.voxelsniper.RangeBlockHelper;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Sniper;
import com.thevoxelbox.voxelsniper.VoxelSniper;
import com.thevoxelbox.voxelsniper.api.command.VoxelCommand;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VoxelInkReplaceCommand extends VoxelCommand
{
    public VoxelInkReplaceCommand(final VoxelSniper plugin)
    {
        super("VoxelInkReplace", plugin);
        setIdentifier("vir");
        setPermission("voxelsniper.sniper");
    }

    @Override
    public boolean onCommand(Player player, String[] args)
    {
        Sniper sniper = plugin.getSniperManager().getSniperForPlayer(player);

        BlockData dataValue;

        if (args.length == 0)
        {
            Block targetBlock = new RangeBlockHelper(player, player.getWorld()).getTargetBlock();
            if (targetBlock != null)
            {
                dataValue = targetBlock.getBlockData();
            }
            else
            {
                return true;
            }
        }
        else
        {
            dataValue = Bukkit.createBlockData(args[0]);
        }

        SnipeData snipeData = sniper.getSnipeData(sniper.getCurrentToolId());
        snipeData.setReplaceData(dataValue);
        snipeData.getVoxelMessage().replaceData();
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        Block targetBlock = new RangeBlockHelper(player, player.getWorld()).getTargetBlock();
        BlockData blockData = targetBlock != null ? targetBlock.getBlockData() : null;

        if (args.length == 0) {
            if (blockData != null) {
                return Collections.singletonList(blockData.getAsString(true));
            } else {
                return Collections.emptyList();
            }
        } else if (args.length == 1) {
            List<String> result = new ArrayList<>(2);
            String firstArg = args[0];
            result.add(firstArg);
            if (blockData != null) {
                if (startsWithIgnoreCase(blockData.getAsString(false), firstArg)) {
                    result.add(blockData.getAsString(true));
                }
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }
}
