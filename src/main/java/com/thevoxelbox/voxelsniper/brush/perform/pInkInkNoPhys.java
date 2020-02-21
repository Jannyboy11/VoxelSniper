package com.thevoxelbox.voxelsniper.brush.perform;

import org.bukkit.block.Block;

import com.thevoxelbox.voxelsniper.Message;
import org.bukkit.block.data.BlockData;

/**
 * @author Voxel
 */
public class pInkInkNoPhys extends vPerformer
{

    private BlockData d;
    private BlockData dr;

    public pInkInkNoPhys()
    {
        name = "Ink-Ink, No Physics";
    }

    @Override
    public void init(com.thevoxelbox.voxelsniper.SnipeData v)
    {
        w = v.getWorld();
        d = v.getData();
        dr = v.getReplaceData();
    }

    @Override
    public void info(Message vm)
    {
        vm.performerName(name);
        vm.data();
        vm.replaceData();
    }

    @SuppressWarnings("deprecation")
	@Override
    public void perform(Block b)
    {
        if (b.getBlockData().matches(dr))
        {
            h.put(b);
            b.setBlockData(d, false);
        }
    }

    @Override
    public boolean isUsingReplaceMaterial()
    {
        return true;
    }
}