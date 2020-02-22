package com.thevoxelbox.voxelsniper.brush;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;

/**
 * @author MikeMatrix
 */
public class BlockResetBrush extends Brush
{
    private static final Set<Material> DENIED_UPDATES = EnumSet.noneOf(Material.class);

    static
    {
        DENIED_UPDATES.addAll(Tag.SIGNS.getValues());
        DENIED_UPDATES.add(Material.CHEST);
        DENIED_UPDATES.add(Material.FURNACE);
        DENIED_UPDATES.add(Material.SMOKER);
        DENIED_UPDATES.add(Material.BLAST_FURNACE);
        DENIED_UPDATES.add(Material.CAMPFIRE);
        DENIED_UPDATES.add(Material.BREWING_STAND);
        DENIED_UPDATES.add(Material.REDSTONE_TORCH);
        DENIED_UPDATES.add(Material.REDSTONE_WALL_TORCH);
        DENIED_UPDATES.add(Material.REDSTONE_WIRE);
        DENIED_UPDATES.add(Material.REPEATER);
        DENIED_UPDATES.addAll(Tag.DOORS.getValues());
        DENIED_UPDATES.addAll(EnumSet.of( //how is this not a built-in Tag?
                Material.ACACIA_FENCE_GATE,
                Material.BIRCH_FENCE_GATE,
                Material.OAK_FENCE_GATE,
                Material.SPRUCE_FENCE_GATE,
                Material.JUNGLE_FENCE_GATE,
                Material.DARK_OAK_FENCE_GATE));
        DENIED_UPDATES.addAll(EnumSet.of(Material.AIR, Material.CAVE_AIR, Material.VOID_AIR));
    }

    /**
     *
     */
    public BlockResetBrush()
    {
        this.setName("Block Reset Brush");
    }

    @SuppressWarnings("deprecation")
	private void applyBrush(final SnipeData v)
    {
        for (int z = -v.getBrushSize(); z <= v.getBrushSize(); z++)
        {
            for (int x = -v.getBrushSize(); x <= v.getBrushSize(); x++)
            {
                for (int y = -v.getBrushSize(); y <= v.getBrushSize(); y++)
                {
                    final Block block = this.getWorld().getBlockAt(this.getTargetBlock().getX() + x, this.getTargetBlock().getY() + y, this.getTargetBlock().getZ() + z);
                    if (BlockResetBrush.DENIED_UPDATES.contains(block.getType()))
                    {
                        continue;
                    }

                    block.setBlockData(block.getType().createBlockData(), true);
                }
            }
        }
    }

    @Override
    protected final void arrow(final SnipeData v)
    {
        applyBrush(v);
    }

    @Override
    protected final void powder(final SnipeData v)
    {
        applyBrush(v);
    }

    @Override
    public final void info(final Message vm)
    {
        vm.brushName(this.getName());
    }

    @Override
    public String getPermissionNode()
    {
        return "voxelsniper.brush.blockreset";
    }
}
