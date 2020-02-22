package com.thevoxelbox.voxelsniper.brush;

import java.util.HashSet;

import com.thevoxelbox.voxelsniper.Materials;
import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Undo;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/**
 *
 */
public class StampBrush extends Brush
{
    /**
     * @author Voxel
     */
    protected class BlockWrapper
    {
        public Material id;
        public int x;
        public int y;
        public int z;
        public BlockData d;

        /**
         * @param b
         * @param blx
         * @param bly
         * @param blz
         */
        @SuppressWarnings("deprecation")
		public BlockWrapper(final Block b, final int blx, final int bly, final int blz)
        {
            this.id = b.getType();
            this.d = b.getBlockData();
            this.x = blx;
            this.y = bly;
            this.z = blz;
        }
    }

    /**
     * @author Monofraps
     */
    protected enum StampType
    {
        NO_AIR, FILL, DEFAULT
    }

    protected HashSet<BlockWrapper> clone = new HashSet<BlockWrapper>();
    protected HashSet<BlockWrapper> fall = new HashSet<BlockWrapper>();
    protected HashSet<BlockWrapper> drop = new HashSet<BlockWrapper>();
    protected HashSet<BlockWrapper> solid = new HashSet<BlockWrapper>();
    protected Undo undo;
    protected boolean sorted = false;

    protected StampType stamp = StampType.DEFAULT;

    /**
     *
     */
    public StampBrush()
    {
        this.setName("Stamp");
    }

    /**
     *
     */
    public final void reSort()
    {
        this.sorted = false;
    }

    /**
     * @param id
     *
     * @return
     */
    protected static final boolean falling(final Material id)
    {
        //return (id > 7 && id < 14);

        return id == Material.BUBBLE_COLUMN || id == Material.WATER || id == Material.LAVA && id.hasGravity();
    }

    /**
     * @param cb
     */
    @SuppressWarnings("deprecation")
	protected final void setBlock(final BlockWrapper cb)
    {
        final Block block = this.clampY(this.getTargetBlock().getX() + cb.x, this.getTargetBlock().getY() + cb.y, this.getTargetBlock().getZ() + cb.z);
        this.undo.put(block);
        block.setType(cb.id);
        block.setBlockData(cb.d);
    }

    /**
     * @param cb
     */
    @SuppressWarnings("deprecation")
	protected final void setBlockFill(final BlockWrapper cb)
    {
        final Block block = this.clampY(this.getTargetBlock().getX() + cb.x, this.getTargetBlock().getY() + cb.y, this.getTargetBlock().getZ() + cb.z);
        if (block.getType().isAir())
        {
            this.undo.put(block);
            block.setType(cb.id);
            block.setBlockData(cb.d);
        }
    }

    /**
     * @param type
     */
    protected final void setStamp(final StampType type)
    {
        this.stamp = type;
    }

    /**
     * @param v
     */
    protected final void stamp(final SnipeData v)
    {
        this.undo = new Undo();

        if (this.sorted)
        {
            for (final BlockWrapper block : this.solid)
            {
                this.setBlock(block);
            }
            for (final BlockWrapper block : this.drop)
            {
                this.setBlock(block);
            }
            for (final BlockWrapper block : this.fall)
            {
                this.setBlock(block);
            }
        }
        else
        {
            this.fall.clear();
            this.drop.clear();
            this.solid.clear();
            for (final BlockWrapper block : this.clone)
            {
                if (Materials.fallsOff(block.id))
                {
                    this.fall.add(block);
                }
                else if (falling(block.id))
                {
                    this.drop.add(block);
                }
                else
                {
                    this.solid.add(block);
                    this.setBlock(block);
                }
            }
            for (final BlockWrapper block : this.drop)
            {
                this.setBlock(block);
            }
            for (final BlockWrapper block : this.fall)
            {
                this.setBlock(block);
            }
            this.sorted = true;
        }

        v.owner().storeUndo(this.undo);
    }

    /**
     * @param v
     */
    protected final void stampFill(final SnipeData v)
    {

        this.undo = new Undo();

        if (this.sorted)
        {
            for (final BlockWrapper block : this.solid)
            {
                this.setBlockFill(block);
            }
            for (final BlockWrapper block : this.drop)
            {
                this.setBlockFill(block);
            }
            for (final BlockWrapper block : this.fall)
            {
                this.setBlockFill(block);
            }
        }
        else
        {
            this.fall.clear();
            this.drop.clear();
            this.solid.clear();
            for (final BlockWrapper block : this.clone)
            {
                if (Materials.fallsOff(block.id))
                {
                    this.fall.add(block);
                }
                else if (falling(block.id))
                {
                    this.drop.add(block);
                }
                else if (!block.id.isAir())
                {
                    this.solid.add(block);
                    this.setBlockFill(block);
                }
            }
            for (final BlockWrapper block : this.drop)
            {
                this.setBlockFill(block);
            }
            for (final BlockWrapper block : this.fall)
            {
                this.setBlockFill(block);
            }
            this.sorted = true;
        }

        v.owner().storeUndo(this.undo);
    }

    /**
     * @param v
     */
    protected final void stampNoAir(final SnipeData v)
    {

        this.undo = new Undo();

        if (this.sorted)
        {
            for (final BlockWrapper block : this.solid)
            {
                this.setBlock(block);
            }
            for (final BlockWrapper block : this.drop)
            {
                this.setBlock(block);
            }
            for (final BlockWrapper block : this.fall)
            {
                this.setBlock(block);
            }
        }
        else
        {
            this.fall.clear();
            this.drop.clear();
            this.solid.clear();
            for (final BlockWrapper block : this.clone)
            {
                if (Materials.fallsOff(block.id))
                {
                    this.fall.add(block);
                }
                else if (this.falling(block.id))
                {
                    this.drop.add(block);
                }
                else if (!block.id.isAir())
                {
                    this.solid.add(block);
                    this.setBlock(block);
                }
            }
            for (final BlockWrapper block : this.drop)
            {
                this.setBlock(block);
            }
            for (final BlockWrapper block : this.fall)
            {
                this.setBlock(block);
            }
            this.sorted = true;
        }

        v.owner().storeUndo(this.undo);
    }

    @Override
    protected final void arrow(final SnipeData v)
    {
        switch (this.stamp)
        {
            case DEFAULT:
                this.stamp(v);
                break;

            case NO_AIR:
                this.stampNoAir(v);
                break;

            case FILL:
                this.stampFill(v);
                break;

            default:
                v.sendMessage(ChatColor.DARK_RED + "Error while stamping! Report");
                break;
        }
    }

    @Override
    protected void powder(final SnipeData v)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void info(final Message vm)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getPermissionNode()
    {
        return "voxelsniper.brush.stamp";
    }
}
