package com.thevoxelbox.voxelsniper.brush;

import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Undo;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#Blend_Brushes
 */
public class BlendVoxelDiscBrush extends BlendBrushBase
{
    /**
     *
     */
    public BlendVoxelDiscBrush()
    {
        this.setName("Blend Voxel Disc");
    }

    @SuppressWarnings("deprecation")
	@Override
    protected final void blend(final SnipeData v)
    {
        final int brushSize = v.getBrushSize();
        final int brushSizeDoubled = 2 * brushSize;
        final Material[][] oldMaterials = new Material[2 * (brushSize + 1) + 1][2 * (brushSize + 1) + 1]; // Array that holds the original materials plus a buffer
        final Material[][] newMaterials = new Material[brushSizeDoubled + 1][brushSizeDoubled + 1]; // Array that holds the blended materials

        // Log current materials into oldmats
        for (int x = 0; x <= 2 * (brushSize + 1); x++)
        {
            for (int z = 0; z <= 2 * (brushSize + 1); z++)
            {
                oldMaterials[x][z] = this.getBlockIdAt(this.getTargetBlock().getX() - brushSize - 1 + x, this.getTargetBlock().getY(), this.getTargetBlock().getZ() - brushSize - 1 + z);
            }
        }

        // Log current materials into newmats
        for (int x = 0; x <= brushSizeDoubled; x++)
        {
            for (int z = 0; z <= brushSizeDoubled; z++)
            {
                newMaterials[x][z] = oldMaterials[x + 1][z + 1];
            }
        }

        // Blend materials
        for (int x = 0; x <= brushSizeDoubled; x++)
        {
            for (int z = 0; z <= brushSizeDoubled; z++)
            {
                final Map<Material, Integer> materialFrequency = new EnumMap<>(Material.class);

                for (int m = -1; m <= 1; m++)
                {
                    for (int n = -1; n <= 1; n++)
                    {
                        for (int o = -1; o <= 1; o++)
                        {
                            if (!(m == 0 && n == 0 && o == 0))
                            {
                                Material oldMaterial = oldMaterials[x + 1 + m][z + 1 + o];
                                if (excludeAir && oldMaterial.isAir()) continue;
                                if (excludeWater && oldMaterial == Material.WATER) continue;
                                if (!oldMaterial.isBlock()) continue; //should always be true tho.
                                materialFrequency.compute(oldMaterial, (oldMat, i) -> i == null ? 1 : i + 1);
                            }
                        }
                    }
                }

                Material modeMatId = materialFrequency.entrySet()
                        .stream()
                        .max(Comparator.comparing(Map.Entry::getValue))
                        .map(Map.Entry::getKey)
                        .orElse(Material.AIR);

                newMaterials[x][z] = modeMatId;
            }
        }

        final Undo undo = new Undo();

        // Make the changes
        for (int x = brushSizeDoubled; x >= 0; x--)
        {
            for (int z = brushSizeDoubled; z >= 0; z--)
            {
                if (!(this.excludeAir && newMaterials[x][z].isAir()) && !(this.excludeWater && (newMaterials[x][z] == Material.WATER)))
                {
                    if (this.getBlockIdAt(this.getTargetBlock().getX() - brushSize + x, this.getTargetBlock().getY(), this.getTargetBlock().getZ() - brushSize + z) != newMaterials[x][z])
                    {
                        undo.put(this.clampY(this.getTargetBlock().getX() - brushSize + x, this.getTargetBlock().getY(), this.getTargetBlock().getZ() - brushSize + z));
                    }
                    this.setBlockIdAt(this.getTargetBlock().getZ() - brushSize + z, this.getTargetBlock().getX() - brushSize + x, this.getTargetBlock().getY(), newMaterials[x][z]);

                }
            }
        }

        v.owner().storeUndo(undo);
    }

    @Override
    public final void parameters(final String[] par, final SnipeData v)
    {
        if (par[1].equalsIgnoreCase("info"))
        {
            v.sendMessage(ChatColor.GOLD + "Blend Voxel Disc Parameters:");
            v.sendMessage(ChatColor.AQUA + "/b bvd water -- toggle include or exclude (default) water");
            return;
        }

        super.parameters(par, v);
    }

    @Override
    public String getPermissionNode()
    {
        return "voxelsniper.brush.blendvoxeldisc";
    }
}
