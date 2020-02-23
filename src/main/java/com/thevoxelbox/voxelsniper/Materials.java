package com.thevoxelbox.voxelsniper;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;

public class Materials
{

    private Materials()
    {
    }

    public static boolean fallsOff(Material id)
    {
        if (Tag.FLOWERS.isTagged(id)) return true;
        if (Tag.FLOWER_POTS.isTagged(id)) return true;
        if (Tag.SAPLINGS.isTagged(id)) return true;
        if (Tag.BANNERS.isTagged(id)) return true;
        if (Tag.SIGNS.isTagged(id)) return true;
        if (Tag.BUTTONS.isTagged(id)) return true;
        if (Tag.CARPETS.isTagged(id)) return true;
        if (Tag.CROPS.isTagged(id)) return true;
        if (Tag.DOORS.isTagged(id)) return true;
        if (Tag.RAILS.isTagged(id)) return true;
        if (Tag.UNDERWATER_BONEMEALS.isTagged(id)) return true;
        if (Tag.WOODEN_PRESSURE_PLATES.isTagged(id)) return true;
        if (Tag.BEDS.isTagged(id)) return true;
        //BUKKIT WHY ARE CHORALS NOT IN THE TAG API YET? //TODO PR @ SpigotMC stash?
        Tag<Material> choralPlants = Bukkit.getTag(Tag.REGISTRY_BLOCKS, NamespacedKey.minecraft("corals"), Material.class);
        if (choralPlants.isTagged(id)) return true;

        switch (id)
        {
            case BROWN_MUSHROOM:
            case RED_MUSHROOM:
            case DEAD_BUSH:
            case TORCH:
            case REDSTONE_TORCH:
            case FIRE:
            case REDSTONE_WIRE:
            case WHEAT_SEEDS:
            case LADDER:
            case LEVER:
            case STONE_PRESSURE_PLATE:
            case LIGHT_WEIGHTED_PRESSURE_PLATE:
            case HEAVY_WEIGHTED_PRESSURE_PLATE:
            case SNOW:
            case SUGAR_CANE:
            case CACTUS:
            case BAMBOO_SAPLING:
            case BAMBOO:
            case REPEATER:
            case COMPARATOR:
            case LILY_PAD:
            case DRAGON_EGG:
            case CHORUS_FLOWER:
            case CHORUS_PLANT:
            case VINE:
            case NETHER_WART:
                return true;
        }

        return false;
    }

    public static boolean isLiquid(Material id)
    {
        switch (id)
        {
            case WATER:
            case LAVA:
            case BUBBLE_COLUMN:
                return true;
            default:
                return false;
        }
    }

}
