/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.voxelsniper.brush.perform;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.util.VoxelList;

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * @author Voxel
 */
public class pIncludeMat extends vPerformer
{

    private VoxelList includeList;
    private Material id;

    public pIncludeMat()
    {
        name = "Include Material";
    }

    @Override
    public void info(Message vm)
    {
        vm.performerName(name);
        vm.voxelList();
        vm.voxel();
    }

    @Override
    public void init(com.thevoxelbox.voxelsniper.SnipeData v)
    {
        w = v.getWorld();
        id = v.getVoxelId();
        includeList = v.getVoxelList();
    }

    @SuppressWarnings("deprecation")
	@Override
    public void perform(Block b)
    {
        if (includeList.contains(b.getBlockData()))
        {
            h.put(b);
            b.setType(id);
        }
    }
}
