package com.thevoxelbox.voxelsniper;

import com.thevoxelbox.voxelsniper.util.VoxelList;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

/**
 * @author Piotr
 */
public class SnipeData
{

    public static final int DEFAULT_CYLINDER_CENTER = 0;
    public static final int DEFAULT_VOXEL_HEIGHT = 1;
    public static final int DEFAULT_BRUSH_SIZE = 3;

    public static final Material DEFAULT_VOXEL_ID = Material.AIR;
    public static final Material DEFAULT_REPLACE_ID = Material.AIR;
    public static final BlockData DEFAULT_DATA_VALUE = DEFAULT_VOXEL_ID.createBlockData();
    public static final BlockData DEFAULT_REPLACE_DATA_VALUE = DEFAULT_REPLACE_ID.createBlockData();

    private final Sniper owner;
    private Message voxelMessage;
    /**
     * Brush size -- set blockPositionY /b #.
     */
    private int brushSize = SnipeData.DEFAULT_BRUSH_SIZE;
    /**
     * Voxel Id -- set blockPositionY /v (#,name).
     */
    private Material voxelId = DEFAULT_VOXEL_ID;
    /**
     * Voxel Replace Id -- set blockPositionY /vr #.
     */
    private Material replaceId = DEFAULT_REPLACE_ID;
    /**
     * Voxel 'ink' -- set blockPositionY /vi #.
     */
    private BlockData data = DEFAULT_DATA_VALUE;
    /**
     * Voxel 'ink' Replace -- set blockPositionY /vir #.
     */
    private BlockData replaceData = DEFAULT_REPLACE_DATA_VALUE;
    /**
     * Voxel List of ID's -- set blockPositionY /vl # # # -#.
     */
    private VoxelList voxelList = new VoxelList();
    /**
     * Voxel 'heigth' -- set blockPositionY /vh #.
     */
    private int voxelHeight = SnipeData.DEFAULT_VOXEL_HEIGHT;
    /**
     * Voxel centroid -- set Cylynder center /vc #.
     */
    private int cCen = DEFAULT_CYLINDER_CENTER;
    private int range = 0;
    private boolean ranged = false;
    private boolean lightning = false;
    private BlockData blockData = Material.AIR.createBlockData();

    /**
     * @param vs
     */
    public SnipeData(final Sniper vs)
    {
        this.owner = vs;
    }

    /**
     * @return the brushSize
     */
    public final int getBrushSize()
    {
        return this.brushSize;
    }

    /**
     * @return the cCen
     */
    public final int getcCen()
    {
        return this.cCen;
    }

    /**
     * @return the data
     */
    public final BlockData getData()
    {
        return this.data;
    }

    /**
     * @return the replaceData
     */
    public final BlockData getReplaceData()
    {
        return this.replaceData;
    }

    /**
     * @return the replaceId
     */
    public final Material getReplaceId()
    {
        return this.replaceId;
    }

    /**
     * @return the voxelHeight
     */
    public final int getVoxelHeight()
    {
        return this.voxelHeight;
    }

    /**
     * @return the voxelId
     */
    public final Material getVoxelId()
    {
        return this.voxelId;
    }

    /**
     * @return the voxelList
     */
    public final VoxelList getVoxelList()
    {
        return this.voxelList;
    }

    /**
     * @return the voxelMessage
     */
    public final Message getVoxelMessage()
    {
        return this.voxelMessage;
    }

    /**
     * @return World
     */
    public final World getWorld()
    {
        return this.owner.getPlayer().getWorld();
    }

    /**
     * @return Sniper
     */
    public final Sniper owner()
    {
        return this.owner;
    }

    /**
     * Reset to default values.
     */
    public final void reset()
    {
        this.voxelId = SnipeData.DEFAULT_VOXEL_ID;
        this.replaceId = SnipeData.DEFAULT_REPLACE_ID;
        this.data = SnipeData.DEFAULT_DATA_VALUE;
        this.brushSize = SnipeData.DEFAULT_BRUSH_SIZE;
        this.voxelHeight = SnipeData.DEFAULT_VOXEL_HEIGHT;
        this.cCen = SnipeData.DEFAULT_CYLINDER_CENTER;
        this.replaceData = SnipeData.DEFAULT_REPLACE_DATA_VALUE;
        this.voxelList = new VoxelList();
    }

    /**
     * @param message
     */
    public final void sendMessage(final String message)
    {
        this.owner.getPlayer().sendMessage(message);
    }

    /**
     * @param brushSize
     *         the brushSize to set
     */
    public final void setBrushSize(final int brushSize)
    {
        this.brushSize = brushSize;
    }

    /**
     * @param cCen
     *         the cCen to set
     */
    public final void setcCen(final int cCen)
    {
        this.cCen = cCen;
    }

    /**
     * @param data
     *         the data to set
     */
    public final void setData(final BlockData data)
    {
        this.data = data;
    }

    /**
     * @param replaceData
     *         the replaceData to set
     */
    public final void setReplaceData(final BlockData replaceData)
    {
        this.replaceData = replaceData;
    }

    /**
     * @param replaceId
     *         the replaceId to set
     */
    public final void setReplaceId(final Material replaceId)
    {
        this.replaceId = replaceId;
    }

    /**
     * @param voxelHeight
     *         the voxelHeight to set
     */
    public final void setVoxelHeight(final int voxelHeight)
    {
        this.voxelHeight = voxelHeight;
    }

    /**
     * @param voxelId
     *         the voxelId to set
     */
    public final void setVoxelId(final Material voxelId)
    {
        this.voxelId = voxelId;
    }

    /**
     * @param voxelList
     *         the voxelList to set
     */
    public final void setVoxelList(final VoxelList voxelList)
    {
        this.voxelList = voxelList;
    }

    /**
     * @param voxelMessage
     *         the voxelMessage to set
     */
    public final void setVoxelMessage(final Message voxelMessage)
    {
        this.voxelMessage = voxelMessage;
    }

    public int getRange()
    {
        return range;
    }

    public void setRange(int range)
    {
        this.range = range;
    }

    public boolean isRanged()
    {
        return ranged;
    }

    public void setRanged(boolean ranged)
    {
        this.ranged = ranged;
    }

    public boolean isLightningEnabled()
    {
        return lightning;
    }

    public void setLightningEnabled(boolean lightning)
    {
        this.lightning = lightning;
    }

    public void setVoxelData(BlockData blockData) {
        this.blockData = blockData;
    }

    public BlockData getVoxelData() {
        return this.blockData;
    }
}
