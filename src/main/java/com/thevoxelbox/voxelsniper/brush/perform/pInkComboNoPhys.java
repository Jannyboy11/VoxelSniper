package com.thevoxelbox.voxelsniper.brush.perform;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.thevoxelbox.voxelsniper.Message;
import org.bukkit.block.data.BlockData;

/**
 * @author Voxel
 */
public class pInkComboNoPhys extends vPerformer
{

    private BlockData d;
    private BlockData dr;
    private Material ir;

    public pInkComboNoPhys()
    {
        name = "Ink-Combo, No Physics";
    }

    @Override
    public void init(com.thevoxelbox.voxelsniper.SnipeData v)
    {
        w = v.getWorld();
        d = v.getData();
        dr = v.getReplaceData();
        ir = v.getReplaceId();
    }

    @Override
    public void info(Message vm)
    {
        vm.performerName(name);
        vm.replace();
        vm.data();
        vm.replaceData();
    }

    @SuppressWarnings("deprecation")
	@Override
    public void perform(Block b)
    {
        if (b.getType() == ir && b.getBlockData().matches(dr))
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