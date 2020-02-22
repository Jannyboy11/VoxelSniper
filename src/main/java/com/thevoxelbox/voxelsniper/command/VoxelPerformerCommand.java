package com.thevoxelbox.voxelsniper.command;

import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Sniper;
import com.thevoxelbox.voxelsniper.VoxelSniper;
import com.thevoxelbox.voxelsniper.brush.IBrush;
import com.thevoxelbox.voxelsniper.brush.perform.Performer;
import com.thevoxelbox.voxelsniper.api.command.VoxelCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class VoxelPerformerCommand extends VoxelCommand
{
    public VoxelPerformerCommand(final VoxelSniper plugin)
    {
        super("VoxelPerformer", plugin);
        setIdentifier("p");
        setPermission("voxelsniper.sniper");
    }

    @Override
    public boolean onCommand(Player player, String[] args)
    {
        Sniper sniper = plugin.getSniperManager().getSniperForPlayer(player);
        SnipeData snipeData = sniper.getSnipeData(sniper.getCurrentToolId());

        try
        {
            if (args == null || args.length == 0)
            {
                IBrush brush = sniper.getBrush(sniper.getCurrentToolId());
                if (brush instanceof Performer)
                {
                    ((Performer) brush).parse(new String[]{ "m" }, snipeData);
                }
                else
                {
                    player.sendMessage("This brush is not a performer brush.");
                }
            }
            else
            {
                IBrush brush = sniper.getBrush(sniper.getCurrentToolId());
                if (brush instanceof Performer)
                {
                    ((Performer) brush).parse(args, snipeData);
                }
                else
                {
                    player.sendMessage("This brush is not a performer brush.");
                }
            }
            return true;
        }
        catch (Exception exception)
        {
            plugin.getLogger().log(Level.WARNING, "Command error from " + player.getName(), exception);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        if (args == null || args.length == 0) {
            return Collections.singletonList("m");
        }

        Sniper sniper = plugin.getSniperManager().getSniperForPlayer(player);
        IBrush currentBrush = sniper.getBrush(sniper.getCurrentToolId());

        if (currentBrush instanceof Performer) {
            Performer performerBrush = (Performer) currentBrush;
            return performerBrush.tabComplete(args, sniper.getSnipeData(sniper.getCurrentToolId()));
        } else {
            return Collections.emptyList();
        }
    }
}
