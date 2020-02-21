/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thevoxelbox.voxelsniper.brush.perform;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.util.VoxelList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/**
 * @author Voxel
 */
public class pExcludeCombo extends vPerformer
{

    private VoxelList excludeList;
    private Material id;
    private BlockData data;

    public pExcludeCombo()
    {
        name = "Exclude Combo";
    }

    @Override
    public void info(Message vm)
    {
        vm.performerName(name);
        vm.voxelList();
        vm.voxel();
        vm.data();
    }

    @Override
    public void init(com.thevoxelbox.voxelsniper.SnipeData v)
    {
        w = v.getWorld();
        id = v.getVoxelId();
        data = v.getData();
        excludeList = v.getVoxelList();
    }

    @SuppressWarnings("deprecation")
	@Override
    public void perform(Block b)
    {
        if (!excludeList.contains(b.getBlockData()))
        {
            h.put(b);
            b.setType(id);
            b.setBlockData(data, true);
        }
    }
}
