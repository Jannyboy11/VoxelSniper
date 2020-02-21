package com.thevoxelbox.voxelsniper.brush;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Undo;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Repeater;

/**
 * @author Voxel
 */
public class SetRedstoneRotateBrush extends Brush
{
    private Block block = null;
    private Undo undo;

    /**
     *
     */
    public SetRedstoneRotateBrush()
    {
        this.setName("Set Redstone Rotate");
    }

    private boolean set(final Block bl)
    {
        if (this.block == null)
        {
            this.block = bl;
            return true;
        }
        else
        {
            this.undo = new Undo();
            final int lowX = Math.min(this.block.getX(), bl.getX());
            final int lowY = Math.min(this.block.getY(), bl.getY());
            final int lowZ = Math.min(this.block.getZ(), bl.getZ());
            final int highX = Math.max(this.block.getX(), bl.getX());
            final int highY = Math.max(this.block.getY(), bl.getY());
            final int highZ = Math.max(this.block.getZ(), bl.getZ());

            for (int y = lowY; y <= highY; y++)
            {
                for (int x = lowX; x <= highX; x++)
                {
                    for (int z = lowZ; z <= highZ; z++)
                    {
                        this.perform(this.clampY(x, y, z));
                    }
                }
            }
            this.block = null;
            return false;
        }
    }

    @SuppressWarnings("deprecation")
	private void perform(final Block bl)
    {
        if (bl.getType() == Material.REPEATER) //TODO why not comparators (and other redstone componenets)?
        {
            this.undo.put(bl);
            //bl.setData((((bl.getData() % 4) + 1 < 5) ? (byte) (bl.getData() + 1) : (byte) (bl.getData() - 4)));
            //https://minecraft.gamepedia.com/Redstone_Repeater#Data_values
            //the order is: north -> east -> south -> west
            Repeater repeater = (Repeater) bl.getBlockData();
            BlockFace facing = repeater.getFacing();
            BlockFace newFace;
            //TODO switch expression
            switch (facing) {
                case NORTH:
                    newFace = BlockFace.EAST;
                    break;
                case EAST:
                    newFace = BlockFace.SOUTH;
                    break;
                case SOUTH:
                    newFace = BlockFace.WEST;
                    break;
                case WEST:
                    newFace = BlockFace.NORTH;
                    break;
                default:
                    newFace = null;
            }
            repeater.setFacing(newFace);
            bl.setBlockData(repeater);
        }
    }

    @Override
    protected final void arrow(final SnipeData v)
    {
        if (this.set(this.getTargetBlock()))
        {
            v.owner().getPlayer().sendMessage(ChatColor.GRAY + "Point one");
        }
        else
        {
            v.owner().storeUndo(this.undo);
        }
    }

    @Override
    protected final void powder(final SnipeData v)
    {
        if (this.set(this.getLastBlock()))
        {
            v.owner().getPlayer().sendMessage(ChatColor.GRAY + "Point one");
        }
        else
        {
            v.owner().storeUndo(this.undo);
        }
    }

    @Override
    public final void info(final Message vm)
    {
        this.block = null;
        vm.brushName(this.getName());
    }

    @Override
    public final void parameters(final String[] par, final SnipeData v)
    {
        super.parameters(par, v);
    }

    @Override
    public String getPermissionNode()
    {
        return "voxelsniper.brush.setredstonerotate";
    }
}
