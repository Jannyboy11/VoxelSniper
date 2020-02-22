package com.thevoxelbox.voxelsniper.brush;


import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Undo;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Snow;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#Snow_cone_brush
 *
 * @author Voxel
 */
public class SnowConeBrush extends Brush
{
    @SuppressWarnings("deprecation")
	private void addSnow(final SnipeData v, Block targetBlock)
    {
        int brushSize;
        int blockPositionX = targetBlock.getX();
        int blockPositionY = targetBlock.getY();
        int blockPositionZ = targetBlock.getZ();
        if (this.getBlockIdAt(blockPositionX, blockPositionY, blockPositionZ).isAir())
        {
            brushSize = 0;
        }
        else
        {
            brushSize = this.clampY(blockPositionX, blockPositionY, blockPositionZ).getData() + 1;
        }

        final int brushSizeDoubled = 2 * brushSize;
        final Material[][] snowcone = new Material[brushSizeDoubled + 1][brushSizeDoubled + 1]; // Will hold block IDs
        final BlockData[][] snowconeData = new BlockData[brushSizeDoubled + 1][brushSizeDoubled + 1]; // Will hold data values for snowcone
        final int[][] yOffset = new int[brushSizeDoubled + 1][brushSizeDoubled + 1];
        // prime the arrays

        for (int x = 0; x <= brushSizeDoubled; x++)
        {
            for (int z = 0; z <= brushSizeDoubled; z++)
            {
                boolean flag = true;

                for (int i = 0; i < 10; i++)
                { // overlay
                    if (flag)
                    {
                        if ((this.getBlockIdAt(blockPositionX - brushSize + x, blockPositionY - i, blockPositionZ - brushSize + z).isAir() ||
                                this.getBlockIdAt(blockPositionX - brushSize + x, blockPositionY - i, blockPositionZ - brushSize + z) == Material.SNOW)
                                && this.getBlockIdAt(blockPositionX - brushSize + x, blockPositionY - i - 1, blockPositionZ - brushSize + z).isAir()
                                && this.getBlockIdAt(blockPositionX - brushSize + x, blockPositionY - i - 1, blockPositionZ - brushSize + z) != Material.SNOW)
                        {
                            flag = false;
                            yOffset[x][z] = i;
                        }
                    }
                }
                snowcone[x][z] = this.getBlockIdAt(blockPositionX - brushSize + x, blockPositionY - yOffset[x][z], blockPositionZ - brushSize + z);
                snowconeData[x][z] = this.clampY(blockPositionX - brushSize + x, blockPositionY - yOffset[x][z], blockPositionZ - brushSize + z).getBlockData();
            }
        }

        // figure out new snowheights
        for (int x = 0; x <= brushSizeDoubled; x++)
        {
            final double xSquared = Math.pow(x - brushSize, 2);

            for (int z = 0; z <= 2 * brushSize; z++)
            {
                final double zSquared = Math.pow(z - brushSize, 2);
                final double dist = Math.pow(xSquared + zSquared, .5); // distance from center of array
                final int snowData = brushSize - (int) Math.ceil(dist);

                if (snowData >= 0)
                { // no funny business
                    switch (snowData)
                    {
                        case 0:
                            if (snowcone[x][z].isAir())
                            {
                                snowcone[x][z] = Material.SNOW;
                                snowconeData[x][z] = Material.SNOW.createBlockData();
                            }
                            break;
                        case 7: // Turn largest snowtile into snowblock
                            if (snowcone[x][z] == Material.SNOW)
                            {
                                snowcone[x][z] = Material.SNOW_BLOCK;
                                snowconeData[x][z] = Material.SNOW.createBlockData();
                            }
                            break;
                        default: // Increase snowtile size, if smaller than target

                            if (snowData > ((Snow) snowconeData[x][z]).getLayers())
                            {
                                Snow snow = (Snow) Material.SNOW.createBlockData();
                                snow.setLayers(snowData);
                                switch (snowcone[x][z])
                                {
                                    case AIR:
                                        snowcone[x][z] = Material.SNOW;
                                        snowconeData[x][z] = snow;
                                    case SNOW:
                                        snowconeData[x][z] = snow;
                                        break;
                                    default:
                                        break;

                                }
                            }
                            else if (yOffset[x][z] > 0 && snowcone[x][z] == Material.SNOW)
                            {
                                Snow snow = (Snow) snowconeData[x][z];
                                snow.setLayers(snow.getLayers() + 1);
                                if (snow.getLayers() == 7)
                                {
                                    snow.setLayers(0);
                                    snowcone[x][z] = Material.SNOW_BLOCK;
                                }
                            }
                            break;
                    }
                }
            }
        }

        final Undo undo = new Undo();

        for (int x = 0; x <= brushSizeDoubled; x++)
        {
            for (int z = 0; z <= brushSizeDoubled; z++)
            {

                if (this.getBlockIdAt(blockPositionX - brushSize + x, blockPositionY - yOffset[x][z], blockPositionZ - brushSize + z) != snowcone[x][z] ||
                        this.clampY(blockPositionX - brushSize + x, blockPositionY - yOffset[x][z], blockPositionZ - brushSize + z).getBlockData().matches(snowconeData[x][z]))
                {
                    undo.put(this.clampY(blockPositionX - brushSize + x, blockPositionY - yOffset[x][z], blockPositionZ - brushSize + z));
                }
                this.setBlockIdAt(blockPositionZ - brushSize + z, blockPositionX - brushSize + x, blockPositionY - yOffset[x][z], snowcone[x][z]);
                this.clampY(blockPositionX - brushSize + x, blockPositionY - yOffset[x][z], blockPositionZ - brushSize + z).setBlockData(snowconeData[x][z]);

            }
        }
        v.owner().storeUndo(undo);
    }

    @Override
    protected final void arrow(final SnipeData v)
    {
    }

    @Override
    protected final void powder(final SnipeData v)
    {
        switch (getTargetBlock().getType())
        {
            case SNOW:
                this.addSnow(v, this.getTargetBlock());
                break;
            default:
                Block blockAbove = getTargetBlock().getRelative(BlockFace.UP);
                if (blockAbove != null && blockAbove.getType().isAir())
                {
                    addSnow(v, blockAbove);
                }
                else
                {
                    v.owner().getPlayer().sendMessage(ChatColor.RED + "Error: Center block neither snow nor air.");
                }
                break;
        }
    }

    @Override
    public final void info(final Message vm)
    {
        vm.brushName("Snow Cone");
    }

    @Override
    public final void parameters(final String[] par, final SnipeData v)
    {
        if (par[1].equalsIgnoreCase("info"))
        {
            v.sendMessage(ChatColor.GOLD + "Snow Cone Parameters:");
        }
    }

    @Override
    public String getPermissionNode()
    {
        return "voxelsniper.brush.snowcone";
    }
}
