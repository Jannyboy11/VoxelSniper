package com.thevoxelbox.voxelsniper.brush.perform;

import org.bukkit.block.Block;

import com.thevoxelbox.voxelsniper.Message;
import org.bukkit.block.data.BlockData;

/**
 * @author Voxel
 */
public class pInkNoPhys extends vPerformer
{

    private BlockData d;

    public pInkNoPhys()
    {
        name = "Ink, No Physics";
    }

    @Override
    public void init(com.thevoxelbox.voxelsniper.SnipeData v)
    {
        w = v.getWorld();
        d = v.getData();
    }

    @Override
    public void info(Message vm)
    {
        vm.performerName(name);
        vm.data();
    }

    @SuppressWarnings("deprecation")
	@Override
    public void perform(Block b)
    {
        h.put(b);
        b.setBlockData(d, false);
    }
}
