package com.thevoxelbox.voxelsniper.brush;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeAction;
import com.thevoxelbox.voxelsniper.SnipeData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;

import java.util.List;

/**
 * Brush Interface.
 *
 */
public interface IBrush
{

    /**
     * @param vm Message object
     */
    void info(Message vm);

    /**
     * Handles parameters passed to brushes.
     *
     * @param par Array of string containing parameters
     * @param v   Snipe Data
     */
    public default void parameters(final String[] par, final SnipeData v)
    {
        v.sendMessage(ChatColor.RED + "This brush does not accept additional parameters.");
    }

    public default List<String> tabcompleteParameters(final String[] par, final SnipeData v) {
        return null;
    }

    boolean perform(SnipeAction action, SnipeData data, Block targetBlock, Block lastBlock);

    /**
     * @return The name of the Brush
     */
    String getName();

    /**
     * @param name New name for the Brush
     */
    void setName(String name);

    /**
     * @return The name of the category the brush is in.
     */
    String getBrushCategory();

    /**
     * @return Permission node required to use this brush
     */
    String getPermissionNode();
}
