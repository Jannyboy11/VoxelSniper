package com.thevoxelbox.voxelsniper.command;

import com.thevoxelbox.voxelsniper.RangeBlockHelper;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Sniper;
import com.thevoxelbox.voxelsniper.VoxelSniper;
import com.thevoxelbox.voxelsniper.api.command.VoxelCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class VoxelVoxelCommand extends VoxelCommand
{
    private static final String[] BLOCK_MATERIALS = Arrays.stream(Material.values())
            .filter(Material::isBlock)
            .map(m -> m.toString().toLowerCase(Locale.ENGLISH))
            .toArray(String[]::new);

    public VoxelVoxelCommand(final VoxelSniper plugin)
    {
        super("VoxelVoxel", plugin);
        setIdentifier("v");
        setPermission("voxelsniper.sniper");
    }

    @Override
    public boolean onCommand(Player player, String[] args)
    {
        Sniper sniper = plugin.getSniperManager().getSniperForPlayer(player);
        SnipeData snipeData = sniper.getSnipeData(sniper.getCurrentToolId());

        if (args.length == 0)
        {
            Block targetBlock = new RangeBlockHelper(player, player.getWorld()).getTargetBlock();
            if (targetBlock != null)
            {
                if (!player.hasPermission("voxelsniper.ignorelimitations") && plugin.getVoxelSniperConfiguration().getLiteSniperRestrictedItems().contains(targetBlock.getType()))
                {
                    player.sendMessage("You are not allowed to use " + targetBlock.getType().name() + ".");
                    return true;
                }
                snipeData.setVoxelId(targetBlock.getType());
                snipeData.getVoxelMessage().voxel();
            }
            return true;
        }

        Material material = Material.matchMaterial(args[0]);
        if (material != null && material.isBlock())
        {
            if (!player.hasPermission("voxelsniper.ignorelimitations") && plugin.getVoxelSniperConfiguration().getLiteSniperRestrictedItems().contains(material))
            {
                player.sendMessage("You are not allowed to use " + material.name() + ".");
                return true;
            }
            snipeData.setVoxelId(material);
            snipeData.getVoxelMessage().voxel();
            return true;
        }
        else
        {
            player.sendMessage(ChatColor.RED + "You have entered an invalid Item ID.");
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        if (args.length == 0) {
            return Arrays.asList(BLOCK_MATERIALS);
        } else if (args.length == 1) {
            String firstArg = args[0];
            return Arrays.stream(BLOCK_MATERIALS)
                    .filter(m -> startsWithIgnoreCase(m, firstArg))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

}
