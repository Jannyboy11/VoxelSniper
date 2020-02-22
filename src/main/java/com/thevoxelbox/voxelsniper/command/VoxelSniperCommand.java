package com.thevoxelbox.voxelsniper.command;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Sniper;
import com.thevoxelbox.voxelsniper.VoxelSniper;
import com.thevoxelbox.voxelsniper.brush.IBrush;
import com.thevoxelbox.voxelsniper.api.command.VoxelCommand;
import com.thevoxelbox.voxelsniper.brush.perform.PerformerE;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VoxelSniperCommand extends VoxelCommand
{
    private static final String[] SUB_COMMANDS = new String[] {"brushes", "range", "perf", "perflong", "enable", "disable", "toggle"};

    public VoxelSniperCommand(final VoxelSniper plugin)
    {

        super("VoxelSniper", plugin);
        setIdentifier("vs");
        setPermission("voxelsniper.sniper");
    }

    @Override
    public boolean onCommand(Player player, String[] args)
    {
        Sniper sniper = VoxelSniper.getInstance().getSniperManager().getSniperForPlayer(player);

        if (args.length >= 1)
        {
            if (args[0].equalsIgnoreCase("brushes"))
            {
                Multimap<Class<? extends IBrush>, String> registeredBrushesMultimap = VoxelSniper.getInstance().getBrushManager().getRegisteredBrushesMultimap();
                List<String> allHandles = Lists.newLinkedList();
                for (Class<? extends IBrush> brushClass : registeredBrushesMultimap.keySet())
                {
                    allHandles.addAll(registeredBrushesMultimap.get(brushClass));
                }
                player.sendMessage(Joiner.on(", ").skipNulls().join(allHandles));
                return true;
            }
            else if (args[0].equalsIgnoreCase("range"))
            {
                SnipeData snipeData = sniper.getSnipeData(sniper.getCurrentToolId());
                if (args.length == 2)
                {
                    try
                    {
                        int range = Integer.parseInt(args[1]);
                        if (range < 0)
                        {
                            player.sendMessage("Negative values are not allowed.");
                        }
                        snipeData.setRange(range);
                        snipeData.setRanged(true);
                        snipeData.getVoxelMessage().toggleRange();

                    }
                    catch (NumberFormatException exception)
                    {
                        player.sendMessage("Can't parse number.");
                    }
                    return true;
                }
                else
                {
                    snipeData.setRanged(!snipeData.isRanged());
                    snipeData.getVoxelMessage().toggleRange();
                    return true;
                }
            }
            else if (args[0].equalsIgnoreCase("perf"))
            {
                player.sendMessage(ChatColor.AQUA + "Available performers (abbreviated):");
                player.sendMessage(PerformerE.performer_list_short);
                return true;
            }
            else if (args[0].equalsIgnoreCase("perflong"))
            {
                player.sendMessage(ChatColor.AQUA + "Available performers:");
                player.sendMessage(PerformerE.performer_list_long);
                return true;
            }
            else if (args[0].equalsIgnoreCase("enable"))
            {
                sniper.setEnabled(true);
                player.sendMessage("VoxelSniper is " + (sniper.isEnabled() ? "enabled" : "disabled"));
                return true;
            }
            else if (args[0].equalsIgnoreCase("disable"))
            {
                sniper.setEnabled(false);
                player.sendMessage("VoxelSniper is " + (sniper.isEnabled() ? "enabled" : "disabled"));
                return true;
            }
            else if (args[0].equalsIgnoreCase("toggle"))
            {
                sniper.setEnabled(!sniper.isEnabled());
                player.sendMessage("VoxelSniper is " + (sniper.isEnabled() ? "enabled" : "disabled"));
                return true;
            }
        }
        player.sendMessage(ChatColor.DARK_RED + "VoxelSniper - Current Brush Settings:");
        sniper.displayInfo();
        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {

        if (args.length == 0 || (args.length == 1 && args[0].isEmpty())) {
            return Arrays.asList(SUB_COMMANDS);
        }

        String firstArg = args[0];
        if (args.length == 1) {
            return Arrays.stream(SUB_COMMANDS)
                    .filter(sub -> startsWithIgnoreCase(sub, firstArg))
                    .collect(Collectors.toList());
        }

        String secondArg = args[1];
        if (args.length == 2 && "range".equalsIgnoreCase(firstArg)) {
            if (secondArg.isEmpty()) {
                //I don't know what good range values are...
                return Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
            } else {
                try {
                    int range = Integer.parseInt(secondArg);
                    return Arrays.asList(secondArg, range + "0", range + "00");
                } catch (NumberFormatException e) {
                    return Collections.singletonList(secondArg);
                }
            }
        }

        return Collections.emptyList();
    }

}
