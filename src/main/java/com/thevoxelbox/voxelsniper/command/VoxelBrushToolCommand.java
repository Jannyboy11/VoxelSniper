package com.thevoxelbox.voxelsniper.command;

import com.thevoxelbox.voxelsniper.SnipeAction;
import com.thevoxelbox.voxelsniper.Sniper;
import com.thevoxelbox.voxelsniper.VoxelSniper;
import com.thevoxelbox.voxelsniper.api.command.VoxelCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VoxelBrushToolCommand extends VoxelCommand
{
    public VoxelBrushToolCommand(final VoxelSniper plugin)
    {
        super("VoxelBrushTool", plugin);
        setIdentifier("btool");
        setPermission("voxelsniper.sniper");
    }

    @Override
    public boolean onCommand(Player player, String[] args)
    {
        Sniper sniper = plugin.getSniperManager().getSniperForPlayer(player);

        if (args != null && args.length > 0)
        {
            if (args[0].equalsIgnoreCase("assign"))
            {
                SnipeAction action;
                if (args[1].equalsIgnoreCase("arrow"))
                {
                    action = SnipeAction.ARROW;
                }
                else if (args[1].equalsIgnoreCase("powder"))
                {
                    action = SnipeAction.GUNPOWDER;
                }
                else
                {
                    player.sendMessage("/btool assign <arrow|powder> <toolid>");
                    return true;
                }

                if (args.length == 3 && args[2] != null && !args[2].isEmpty())
                {
                    Material itemInHand = (player.getItemInHand() != null) ? player.getItemInHand().getType() : null;
                    if (itemInHand == null)
                    {
                        player.sendMessage("/btool assign <arrow|powder> <toolid>");
                        return true;
                    }
                    if (sniper.setTool(args[2], action, itemInHand))
                    {
                        player.sendMessage(itemInHand.name() + " has been assigned to '" + args[2] + "' as action " + action.name() + ".");
                    }
                    else
                    {
                        player.sendMessage("Couldn't assign tool.");
                    }
                    return true;
                }
            }
            else if (args[0].equalsIgnoreCase("remove"))
            {
                if (args.length == 2 && args[1] != null && !args[1].isEmpty())
                {
                    sniper.removeTool(args[1]);
                    return true;
                }
                else
                {
                    ItemStack stackInMainHand = player.getInventory().getItemInMainHand();
                    Material itemInHand = stackInMainHand != null ? stackInMainHand.getType() : null;
                    if (itemInHand == null)
                    {
                        player.sendMessage("Can't unassign empty hands.");
                        return true;
                    }
                    if (sniper.getCurrentToolId() == null)
                    {
                        player.sendMessage("Can't unassign default tool.");
                        return true;
                    }
                    sniper.removeTool(sniper.getCurrentToolId(), itemInHand);
                    return true;
                }
            }
        }
        player.sendMessage("/btool assign <arrow|powder> <toolid>");
        player.sendMessage("/btool remove [toolid]");
        return true;
    }

    @Override
    public List<String> onTabComplete(final Player player, String[] args) {
        Sniper sniper = plugin.getSniperManager().getSniperForPlayer(player);
        String currentToolId = sniper.getCurrentToolId();

        if (args.length == 0 || (args.length == 1 && args[0].isEmpty())) {
            return Arrays.asList("assign", "remove");
        }

        String firstArg = args[0];
        if (args.length == 1) {
            if (startsWithIgnoreCase("assign", firstArg)) return Collections.singletonList("assign");
            else if (startsWithIgnoreCase("remove", firstArg)) return Collections.singletonList("remove");
            else return Collections.emptyList();
        }

        String secondArg = args[1];
        if (args.length == 2) {
            if ("assign".equalsIgnoreCase(firstArg)) {
                if (secondArg.isEmpty()) return Arrays.asList("arrow", "powder");
                else if (startsWithIgnoreCase("arrow", secondArg)) return Collections.singletonList("arrow");
                else if (startsWithIgnoreCase("powder", secondArg)) return Collections.singletonList("powder");
                else return null;
            } else if ("remove".equalsIgnoreCase(firstArg)) {
                if (currentToolId != null && startsWithIgnoreCase(currentToolId, secondArg)) return Collections.singletonList(currentToolId);
                else return Collections.singletonList(secondArg);
            } else {
                return Collections.emptyList();
            }
        }

        String thirdArg = args[2];
        if (args.length == 3) {
            if ("assign".equalsIgnoreCase(firstArg)
                    &&( "arrow".equalsIgnoreCase(secondArg) || "powder".equalsIgnoreCase(secondArg))) {
                if (currentToolId != null && startsWithIgnoreCase(currentToolId, thirdArg)) return Collections.singletonList(currentToolId);
                else return Collections.singletonList(thirdArg);
            } else {
                return Collections.emptyList();
            }
        }

        return null;
    }
}
