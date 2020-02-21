package com.thevoxelbox.voxelsniper.util;

import com.google.common.collect.ImmutableList;
import org.bukkit.block.data.BlockData;

import java.util.*;

/**
 * Container class for multiple ID/Datavalue pairs.
 */
public class VoxelList implements Iterable<BlockData>
{

    private Set<BlockData> valuePairs = new LinkedHashSet<BlockData>(); //LinkedHashSet because order is important for getList

    /**
     * Adds the specified id, data value pair to the VoxelList. A data value of -1 will operate on all data values of that id.
     * 
     * @param i
     */
    public void add(BlockData i)
    {
        valuePairs.add(i);
    }

    /**
     * Removes the specified id, data value pair from the VoxelList.
     * 
     * @param i
     * @return true if this list contained the specified element
     */
    public boolean removeValue(final BlockData i)
    {
        return valuePairs.remove(i);
    }

    /**
     * @param i
     * @return true if this list contains the specified element
     */
    public boolean contains(final BlockData blockData)
    {
        return valuePairs.contains(blockData);
    }

    /**
     * Clears the VoxelList.
     */
    public void clear()
    {
        valuePairs.clear();
    }

    /**
     * Returns true if this list contains no elements.
     *
     * @return true if this list contains no elements
     */
    public boolean isEmpty()
    {
        return valuePairs.isEmpty();
    }

    /**
     * Returns a defensive copy of the List with pairs.
     *
     * @return defensive copy of the List with pairs
     */
    public List<BlockData> getList()
    {
        return ImmutableList.copyOf(valuePairs);
    }

    @Override
    public Iterator<BlockData> iterator() {
        return valuePairs.iterator();
    }
}
