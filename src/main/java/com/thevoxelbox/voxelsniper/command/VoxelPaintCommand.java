package com.thevoxelbox.voxelsniper.command;

import com.thevoxelbox.voxelsniper.PaintingWrapper;
import com.thevoxelbox.voxelsniper.VoxelSniper;
import com.thevoxelbox.voxelsniper.api.command.VoxelCommand;
import org.bukkit.Art;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VoxelPaintCommand extends VoxelCommand
{
    private static final Art[] ALL_ART = Art.values();

    public VoxelPaintCommand(final VoxelSniper plugin)
    {
        super("VoxelPaint", plugin);
        setIdentifier("paint");
        setPermission("voxelsniper.paint");
    }

    @Override
    public boolean onCommand(Player player, String[] args)
    {
        if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("back"))
            {
                PaintingWrapper.paint(player, true, true, ALL_ART[0]);
                return true;
            }
            else
            {
                Art art = Art.getByName(args[0]);
                if (art != null) {
                    PaintingWrapper.paint(player, false, false, art);
                } else {
                    player.sendMessage(ChatColor.RED + "Unknown art: " + args[0]);
                }

                return true;
            }
        }
        else
        {
            PaintingWrapper.paint(player, true, false, ALL_ART[0]);
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        if (args.length == 0) {
            List<String> result = new ArrayList<>();
            result.add("back");
            for (Art art : Art.values()) {
                result.add(art.toString().toLowerCase(Locale.ENGLISH));
            }
            return result;
        } else if (args.length == 1) {
            String firstArg = args[0];
            return Stream.concat(Stream.of("back"), Arrays.stream(Art.values())
                    .map(art -> art.toString().toLowerCase(Locale.ENGLISH)))
                    .filter(artOrBack -> startsWithIgnoreCase(artOrBack, firstArg))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

}
