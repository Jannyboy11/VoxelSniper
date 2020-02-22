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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VoxelListCommand extends VoxelCommand
{
    public VoxelListCommand(final VoxelSniper plugin)
    {
        super("VoxelList", plugin);
        setIdentifier("vl");
        setPermission("voxelsniper.sniper");
    }

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
                tmpint = string.replace("-", "");
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

    @Override
    public List<String> onTabComplete(Player player, String[] args)
    {
        Sniper sniper = plugin.getSniperManager().getSniperForPlayer(player);
        SnipeData snipeData = sniper.getSnipeData(sniper.getCurrentToolId());

        if (args.length == 0) {
            List<String> result = new ArrayList<>();
            result.add("clear");
            if (snipeData != null) {
                result.addAll(snipeData.getVoxelList().stream()
                    .map(blockData -> blockData.getAsString(true))
                    .collect(Collectors.toList()));
            }
            return result;
        } else if (args.length == 1) {
            List<String> result = new ArrayList<>();
            String firstArg = args[0];
            if (startsWithIgnoreCase("clear", firstArg)) result.add("clear");
            if (snipeData != null) {
                boolean startsWithDash = firstArg.startsWith("-");
                for (BlockData blockData : snipeData.getVoxelList()) {
                    String asString = blockData.getAsString(true);
                    String completedValue = (startsWithDash ? "-" : "") + asString;
                    if (startsWithIgnoreCase(completedValue, firstArg)) {
                        result.add(asString);
                        result.add("-" + asString);
                    }
                }
            }
            return result;
        }

        return Collections.emptyList();
    }

}
