/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.voxelsniper.brush.perform;

import com.thevoxelbox.voxelsniper.Message;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/**
 * @author Voxel
 */
public class pCombo extends vPerformer
{

    private Material i;
    private BlockData d;

    public pCombo()
    {
        name = "Combo";
    }

    @Override
    public void info(Message vm)
    {
        vm.performerName(name);
        vm.voxel();
        vm.data();
    }

    @Override
    public void init(com.thevoxelbox.voxelsniper.SnipeData v)
    {
        w = v.getWorld();
        i = v.getVoxelId();
        d = v.getData();
    }

    @SuppressWarnings("deprecation")
	@Override
    public void perform(Block b)
    {
        h.put(b);
        b.setType(i);
        b.setBlockData(d, true);
    }
}
