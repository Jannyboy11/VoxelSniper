package com.thevoxelbox.voxelsniper.brush;

import java.util.EnumSet;
import java.util.Set;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/**
 * This brush only looks for solid blocks, and then changes those plus any air blocks touching them. If it works, this brush should be faster than the original
 * blockPositionY an amount proportional to the volume of a snipe selection area / the number of blocks touching air in the selection. This is because every solid block
 * surrounded blockPositionY others should take equally long to check and not change as it would take MC to change them and then check and find no lighting to update. For
 * air blocks surrounded blockPositionY other air blocks, this brush saves about 80-100 checks blockPositionY not updating them or their lighting. And for air blocks touching solids,
 * this brush is slower, because it replaces the air once per solid block it is touching. I assume on average this is about 2 blocks. So every air block
 * touching a solid negates one air block floating in air. Thus, for selections that have more air blocks surrounded blockPositionY air than air blocks touching solids,
 * this brush will be faster, which is almost always the case, especially for undeveloped terrain and for larger brush sizes (unlike the original brush, this
 * should only slow down blockPositionY the square of the brush size, not the cube of the brush size). For typical terrain, blockPositionY my calculations, overall speed increase is
 * about a factor of 5-6 for a size 20 brush. For a complicated city or ship, etc., this may be only a factor of about 2. In a hypothetical worst case scenario
 * of a 3d checkerboard of stone and air every other block, this brush should only be about 1.5x slower than the original brush. Savings increase for larger
 * brushes.
 *
 * @author GavJenks
 */
public class BlockResetSurfaceBrush extends Brush
{
    //TODO un-duplicate (e.g. BlockResetBrush.java)
    private static final Set<Material> DENIED_UPDATES = EnumSet.noneOf(Material.class);

    static
    {
        DENIED_UPDATES.addAll(Tag.SIGNS.getValues());
        DENIED_UPDATES.add(Material.CHEST);
        DENIED_UPDATES.add(Material.FURNACE);
        DENIED_UPDATES.add(Material.SMOKER);
        DENIED_UPDATES.add(Material.BLAST_FURNACE);
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
    public BlockResetSurfaceBrush()
    {
        this.setName("Block Reset Brush Surface Only");
    }

    @SuppressWarnings("deprecation")
	private void applyBrush(final SnipeData v)
    {
        final World world = this.getWorld();

        for (int z = -v.getBrushSize(); z <= v.getBrushSize(); z++)
        {
            for (int x = -v.getBrushSize(); x <= v.getBrushSize(); x++)
            {
                for (int y = -v.getBrushSize(); y <= v.getBrushSize(); y++)
                {

                    Block block = world.getBlockAt(this.getTargetBlock().getX() + x, this.getTargetBlock().getY() + y, this.getTargetBlock().getZ() + z);
                    if (BlockResetSurfaceBrush.DENIED_UPDATES.contains(block.getType()))
                    {
                        continue;
                    }

                    boolean airFound = false;

                    if (world.getBlockAt(this.getTargetBlock().getX() + x + 1, this.getTargetBlock().getY() + y, this.getTargetBlock().getZ() + z).getType().isAir())
                    {
                        block = world.getBlockAt(this.getTargetBlock().getX() + x + 1, this.getTargetBlock().getY() + y, this.getTargetBlock().getZ() + z);
                        final BlockData oldData = block.getBlockData();
                        resetBlock(block, oldData);
                        airFound = true;
                    }

                    if (world.getBlockAt(this.getTargetBlock().getX() + x - 1, this.getTargetBlock().getY() + y, this.getTargetBlock().getZ() + z).getType().isAir())
                    {
                        block = world.getBlockAt(this.getTargetBlock().getX() + x - 1, this.getTargetBlock().getY() + y, this.getTargetBlock().getZ() + z);
                        final BlockData oldData = block.getBlockData();
                        resetBlock(block, oldData);
                        airFound = true;
                    }

                    if (world.getBlockAt(this.getTargetBlock().getX() + x, this.getTargetBlock().getY() + y + 1, this.getTargetBlock().getZ() + z).getType().isAir())
                    {
                        block = world.getBlockAt(this.getTargetBlock().getX() + x, this.getTargetBlock().getY() + y + 1, this.getTargetBlock().getZ() + z);
                        final BlockData oldData = block.getBlockData();
                        resetBlock(block, oldData);
                        airFound = true;
                    }

                    if (world.getBlockAt(this.getTargetBlock().getX() + x, this.getTargetBlock().getY() + y - 1, this.getTargetBlock().getZ() + z).getType().isAir())
                    {
                        block = world.getBlockAt(this.getTargetBlock().getX() + x, this.getTargetBlock().getY() + y - 1, this.getTargetBlock().getZ() + z);
                        final BlockData oldData = block.getBlockData();
                        resetBlock(block, oldData);
                        airFound = true;
                    }

                    if (world.getBlockAt(this.getTargetBlock().getX() + x, this.getTargetBlock().getY() + y, this.getTargetBlock().getZ() + z + 1).getType().isAir())
                    {
                        block = world.getBlockAt(this.getTargetBlock().getX() + x, this.getTargetBlock().getY() + y, this.getTargetBlock().getZ() + z + 1);
                        final BlockData oldData = block.getBlockData();
                        resetBlock(block, oldData);
                        airFound = true;
                    }

                    if (world.getBlockAt(this.getTargetBlock().getX() + x, this.getTargetBlock().getY() + y, this.getTargetBlock().getZ() + z - 1).getType().isAir())
                    {
                        block = world.getBlockAt(this.getTargetBlock().getX() + x, this.getTargetBlock().getY() + y, this.getTargetBlock().getZ() + z - 1);
                        final BlockData oldData = block.getBlockData();
                        resetBlock(block, oldData);
                        airFound = true;
                    }

                    if (airFound)
                    {
                        block = world.getBlockAt(this.getTargetBlock().getX() + x, this.getTargetBlock().getY() + y, this.getTargetBlock().getZ() + z);
                        final BlockData oldData = block.getBlockData();
                        resetBlock(block, oldData);
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
	private void resetBlock(Block block, final BlockData oldData)
    {
        block.setBlockData(block.getBlockData().merge(oldData), true);
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
        return "voxelsniper.brush.blockresetsurface";
    }
}
