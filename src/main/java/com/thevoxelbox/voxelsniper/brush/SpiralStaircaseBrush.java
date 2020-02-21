package com.thevoxelbox.voxelsniper.brush;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import com.thevoxelbox.voxelsniper.Undo;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;

/**
 * http://www.voxelwiki.com/minecraft/Voxelsniper#Spiral_Staircase_Brush
 *
 * @author giltwist
 */
public class SpiralStaircaseBrush extends Brush
{
    private enum StairType {
        STAIR("stair"), SLAB("step"), BLOCK("block");

        private final String string;

        private StairType(String string) {
            this.string = string;
        }

        @Override
        public String toString() {
            return string;
        }

        static StairType fromString(String s) {
            switch (s.toLowerCase()) {
                case "block":
                    return BLOCK;
                case "step":
                case "slab":
                    return SLAB;
                case "stair":
                case "woodstair":
                case "cobblestair":
                    return STAIR;
                default:
                    return null;
            }
        }
    }

    private enum ClockDirection {
        CLOCKWISE("c"), COUNTER_CLOCKWISE("cc");

        private String string;

        private ClockDirection(String string) {
            this.string = string;
        }

        public String toString() {
            return string;
        }

        static ClockDirection fromString(String s) {
            switch (s.toLowerCase()) {
                case "c":
                case "clockwise":
                    return CLOCKWISE;
                case "cc":
                case "counterclockwise":
                    return COUNTER_CLOCKWISE;
                default:
                    return null;
            }
        }
    }

    private enum WindDirection {
        NORTH("n"), EAST("e"), SOUTH("s"), WEST("w");

        private final String string;

        private WindDirection(String string) {
            this.string = string;
        }

        @Override
        public String toString() {
            return string;
        }

        static WindDirection fromString(String s) {
            switch (s.toLowerCase()) {
                case "n":
                case "north":
                    return NORTH;
                case "e":
                case "east":
                    return EAST;
                case "s":
                case "south":
                    return SOUTH;
                case "w":
                case "west":
                    return WEST;
                default:
                    return null;
            }
        }
    }

    private StairType stairtype = StairType.BLOCK;              //"block"; // "block" 1x1 blocks (default), "step" alternating step double step, "stair" staircase with blocks on corners
    private ClockDirection sdirect = ClockDirection.CLOCKWISE;  //"c"; // "c" clockwise (default), "cc" counter-clockwise
    private WindDirection sopen = WindDirection.NORTH;          //"n"; // "n" north (default), "e" east, "world" south, "world" west

    /**
     *
     */
    public SpiralStaircaseBrush()
    {
        this.setName("Spiral Staircase");
    }

    @SuppressWarnings("deprecation")
	private void buildStairWell(final SnipeData v, Block targetBlock)
    {
        if (v.getVoxelHeight() < 1)
        {
            v.setVoxelHeight(1);
            v.sendMessage(ChatColor.RED + "VoxelHeight must be a natural number! Set to 1.");
        }

        final int[][][] spiral = new int[2 * v.getBrushSize() + 1][v.getVoxelHeight()][2 * v.getBrushSize() + 1];

        // locate first block in staircase
        // Note to self, fix these
        int startX = 0;
        int startZ = 0;
        int y = 0;
        int xOffset = 0;
        int zOffset = 0;
        int toggle = 0;

        if (this.sdirect == ClockDirection.CLOCKWISE)
        {
            if (this.sopen == WindDirection.NORTH)
            {
                startX = 0;
                startZ = 2 * v.getBrushSize();
            }
            else if (this.sopen == WindDirection.EAST)
            {
                startX = 0;
                startZ = 0;
            }
            else if (this.sopen == WindDirection.SOUTH)
            {
                startX = 2 * v.getBrushSize();
                startZ = 0;
            }
            else /*this.sopen == WindDirection.WEST ?*/
            {
                startX = 2 * v.getBrushSize();
                startZ = 2 * v.getBrushSize();
            }
        }
        else
        {
            if (this.sopen == WindDirection.NORTH)
            {
                startX = 0;
                startZ = 0;
            }
            else if (this.sopen == WindDirection.EAST)
            {
                startX = 2 * v.getBrushSize();
                startZ = 0;
            }
            else if (this.sopen == WindDirection.SOUTH)
            {
                startX = 2 * v.getBrushSize();
                startZ = 2 * v.getBrushSize();
            }
            else /*this.sopen == WindDirection.WEST ?*/
            {
                startX = 0;
                startZ = 2 * v.getBrushSize();
            }
        }

        while (y < v.getVoxelHeight())
        {
            if (this.stairtype == StairType.BLOCK)
            {
                // 1x1x1 voxel material steps
                spiral[startX + xOffset][y][startZ + zOffset] = 1;
                y++;
            }
            else if (this.stairtype == StairType.SLAB)
            {
                // alternating step-doublestep, uses data value to determine type
                switch (toggle)
                {
                    case 0:
                        toggle = 2;
                        spiral[startX + xOffset][y][startZ + zOffset] = 1;
                        break;
                    case 1:
                        toggle = 2;
                        spiral[startX + xOffset][y][startZ + zOffset] = 1;
                        break;
                    case 2:
                        toggle = 1;
                        spiral[startX + xOffset][y][startZ + zOffset] = 2;
                        y++;
                        break;
                    default:
                        break;
                }

            }

            //TODO there are more stair types then just wood staris and cobble stairs
            //TODO such as sandstone, endstone, nether quartz, purpur and more...
            //TODO can I use the tag system here?

            // Adjust horizontal position and do stair-option array stuff
            if (startX + xOffset == 0)
            { // All North
                if (startZ + zOffset == 0)
                { // NORTHEAST
                    //if (this.stairtype.equalsIgnoreCase("woodstair") || this.stairtype.equalsIgnoreCase("cobblestair"))
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 1;
                    }
                    if (this.sdirect == ClockDirection.CLOCKWISE)
                    {
                        xOffset++;
                    }
                    else /*this.sdirect == ClockDirection.COUNTER_CLOCKWISE*/
                    {
                        zOffset++;
                    }
                }
                else if (startZ + zOffset == 2 * v.getBrushSize())
                { // NORTHWEST
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 1;
                    }
                    if (this.sdirect == ClockDirection.CLOCKWISE)
                    {
                        zOffset--;
                    }
                    else
                    {
                        xOffset++;
                    }
                }
                else
                { // JUST PLAIN NORTH
                    if (this.sdirect == ClockDirection.CLOCKWISE)
                    {
                        if (this.stairtype == StairType.STAIR)
                        {
                            spiral[startX + xOffset][y][startZ + zOffset] = 5;
                            y++;
                        }
                        zOffset--;
                    }
                    else
                    {
                        if (this.stairtype == StairType.STAIR)
                        {
                            spiral[startX + xOffset][y][startZ + zOffset] = 4;
                            y++;
                        }
                        zOffset++;
                    }
                }
            }
            else if (startX + xOffset == 2 * v.getBrushSize())
            { // ALL SOUTH
                if (startZ + zOffset == 0)
                { // SOUTHEAST
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 1;
                    }
                    if (this.sdirect == ClockDirection.CLOCKWISE)
                    {
                        zOffset++;
                    }
                    else
                    {
                        xOffset--;
                    }
                }
                else if (startZ + zOffset == 2 * v.getBrushSize())
                { // SOUTHWEST
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 1;
                    }
                    if (this.sdirect == ClockDirection.CLOCKWISE)
                    {
                        xOffset--;
                    }
                    else
                    {
                        zOffset--;
                    }
                }
                else
                { // JUST PLAIN SOUTH
                    if (this.sdirect == ClockDirection.CLOCKWISE)
                    {
                        if (this.stairtype == StairType.STAIR)
                        {
                            spiral[startX + xOffset][y][startZ + zOffset] = 4;
                            y++;
                        }
                        zOffset++;
                    }
                    else
                    {
                        if (this.stairtype == StairType.STAIR)
                        {
                            spiral[startX + xOffset][y][startZ + zOffset] = 5;
                            y++;
                        }
                        zOffset--;
                    }
                }
            }
            else if (startZ + zOffset == 0)
            { // JUST PLAIN EAST
                if (this.sdirect == ClockDirection.CLOCKWISE)
                {
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 2;
                        y++;
                    }
                    xOffset++;
                }
                else
                {
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 3;
                        y++;
                    }
                    xOffset--;
                }
            }
            else
            { // JUST PLAIN WEST
                if (this.sdirect == ClockDirection.CLOCKWISE)
                {
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 3;
                        y++;
                    }
                    xOffset--;
                }
                else
                {
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 2;
                        y++;
                    }
                    xOffset++;
                }
            }
        }

        final Undo undo = new Undo();
        // Make the changes

        for (int x = 2 * v.getBrushSize(); x >= 0; x--)
        {
            for (int i = v.getVoxelHeight() - 1; i >= 0; i--)
            {
                for (int z = 2 * v.getBrushSize(); z >= 0; z--)
                {
                    int blockPositionX = targetBlock.getX();
                    int blockPositionY = targetBlock.getY();
                    int blockPositionZ = targetBlock.getZ();
                    switch (spiral[x][i][z])
                    {
                        case 0:
                            if (i != v.getVoxelHeight() - 1)
                            {
                                if (!(this.stairtype == StairType.STAIR && spiral[x][i + 1][z] == 1))
                                {
                                    if (!this.getBlockIdAt(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z).isAir())
                                    {
                                        undo.put(this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z));
                                    }
                                    this.setBlockIdAt(blockPositionZ - v.getBrushSize() + z, blockPositionX - v.getBrushSize() + x, blockPositionY + i, Material.AIR);
                                }

                            }
                            else
                            {
                                if (!this.getBlockIdAt(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z).isAir())
                                {
                                    undo.put(this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z));
                                }
                                this.setBlockIdAt(blockPositionZ - v.getBrushSize() + z, blockPositionX - v.getBrushSize() + x, blockPositionY + i, Material.AIR);
                            }

                            break;
                        case 1:
                            if (this.stairtype == StairType.BLOCK)
                            {
                                if (this.getBlockIdAt(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z) != v.getVoxelId())
                                {
                                    undo.put(this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z));
                                }
                                this.setBlockIdAt(blockPositionZ - v.getBrushSize() + z, blockPositionX - v.getBrushSize() + x, blockPositionY + i, v.getVoxelId());
                            }
                            else if (this.stairtype == StairType.SLAB)
                            {
                                if (!Tag.SLABS.isTagged(this.getBlockIdAt(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z)))
                                {
                                    undo.put(this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z));
                                }
                                this.setBlockIdAt(blockPositionZ - v.getBrushSize() + z, blockPositionX - v.getBrushSize() + x, blockPositionY + i, Material.STONE_SLAB);
                                this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z).setBlockData(v.getData());
                            }
                            else if (this.stairtype == StairType.STAIR)
                            {
                                if (this.getBlockIdAt(blockPositionX - v.getBrushSize() + x, blockPositionY + i - 1, blockPositionZ - v.getBrushSize() + z) != v.getVoxelId())
                                {
                                    undo.put(this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY + i - 1, blockPositionZ - v.getBrushSize() + z));
                                }
                                this.setBlockIdAt(blockPositionZ - v.getBrushSize() + z, blockPositionX - v.getBrushSize() + x, blockPositionY + i - 1, v.getVoxelId());

                            }
                            break;
                        case 2:
                            if (this.stairtype == StairType.SLAB)
                            {
                                // id 43 == LEGACY DOUBLE STEP
                                if (!Tag.SLABS.isTagged(this.getBlockIdAt(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z)))
                                {
                                    undo.put(this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z));
                                }
                                Material stoneSlab = Material.STONE_SLAB;
                                Slab slab = (Slab) stoneSlab.createBlockData();
                                slab.setType(Slab.Type.DOUBLE);
                                this.setBlockIdAndDataAt(blockPositionZ - v.getBrushSize() + z, blockPositionX - v.getBrushSize() + x, blockPositionY + i, Material.STONE_SLAB, slab);
                                this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z).setBlockData(v.getData());
                            }
                            else if (this.stairtype == StairType.STAIR)
                            {
                                // id 53 == LEGACY WOOD STAIRS, id 67 == LEGACY COBBLESTONE STAIRS
                                if (!Tag.STAIRS.isTagged(this.getBlockIdAt(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z)))
                                {
                                    undo.put(this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z));
                                }
                                this.setBlockIdAt(blockPositionZ - v.getBrushSize() + z, blockPositionX - v.getBrushSize() + x, blockPositionY + i, Material.OAK_STAIRS);
                                this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z).setBlockData(Material.AIR.createBlockData());
                            }
                            break;
                        default:
                            if (this.stairtype == StairType.STAIR)
                            {
                                //wood stair
                                if (!Tag.STAIRS.isTagged(this.getBlockIdAt(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z)))
                                {
                                    undo.put(this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z));
                                }
                                this.setBlockIdAt(blockPositionZ - v.getBrushSize() + z, blockPositionX - v.getBrushSize() + x, blockPositionY + i, Material.OAK_STAIRS);
                                Stairs stairs = (Stairs) Material.OAK_STAIRS.createBlockData();
                                stairs.setFacing(BlockFace.NORTH); //TODO is this correct??? I REALLY DO NOT KNOW AT THIS POINT
                                this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z).setBlockData(stairs);
                                //this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z).setData((spiral[x][i][z] - 2));
                            }
                            break;
                    }
                }
            }
        }
        v.owner().storeUndo(undo);
    }

    @SuppressWarnings("deprecation")
	private void digStairWell(final SnipeData v, Block targetBlock)
    {
        if (v.getVoxelHeight() < 1)
        {
            v.setVoxelHeight(1);
            v.sendMessage(ChatColor.RED + "VoxelHeight must be a natural number! Set to 1.");
        }

        // initialize array
        final int[][][] spiral = new int[2 * v.getBrushSize() + 1][v.getVoxelHeight()][2 * v.getBrushSize() + 1];

        // locate first block in staircase
        // Note to self, fix these
        int startX = 0;
        int startZ = 0;
        int y = 0;
        int xOffset = 0;
        int zOffset = 0;
        int toggle = 0;

        if (this.sdirect == ClockDirection.COUNTER_CLOCKWISE)
        {
            if (this.sopen == WindDirection.NORTH)
            {
                startX = 0;
                startZ = 2 * v.getBrushSize();
            }
            else if (this.sopen == WindDirection.EAST)
            {
                startX = 0;
                startZ = 0;
            }
            else if (this.sopen == WindDirection.SOUTH)
            {
                startX = 2 * v.getBrushSize();
                startZ = 0;
            }
            else    //WindDirection.WEST
            {
                startX = 2 * v.getBrushSize();
                startZ = 2 * v.getBrushSize();
            }
        }
        else //CLOCKWISE
        {
            if (this.sopen == WindDirection.NORTH)
            {
                startX = 0;
                startZ = 0;
            }
            else if (this.sopen == WindDirection.EAST)
            {
                startX = 2 * v.getBrushSize();
                startZ = 0;
            }
            else if (this.sopen == WindDirection.SOUTH)
            {
                startX = 2 * v.getBrushSize();
                startZ = 2 * v.getBrushSize();
            }
            else    //WindDirection.WEST
            {
                startX = 0;
                startZ = 2 * v.getBrushSize();
            }
        }

        while (y < v.getVoxelHeight())
        {
            if (this.stairtype == StairType.BLOCK)
            {
                // 1x1x1 voxel material steps
                spiral[startX + xOffset][y][startZ + zOffset] = 1;
                y++;
            }
            else if (this.stairtype == StairType.SLAB)
            {
                // alternating step-doublestep, uses data value to determine type
                switch (toggle)
                {
                    case 0:
                        toggle = 2;
                        spiral[startX + xOffset][y][startZ + zOffset] = 2;
                        break;
                    case 1:
                        toggle = 2;
                        spiral[startX + xOffset][y][startZ + zOffset] = 2;
                        break;
                    case 2:
                        toggle = 1;
                        spiral[startX + xOffset][y][startZ + zOffset] = 1;
                        y++;
                        break;
                    default:
                        break;
                }

            }

            // Adjust horizontal position and do stair-option array stuff
            if (startX + xOffset == 0)
            { // All North
                if (startZ + zOffset == 0)
                { // NORTHEAST
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 1;
                    }
                    if (this.sdirect == ClockDirection.CLOCKWISE)
                    {
                        xOffset++;
                    }
                    else
                    {
                        zOffset++;
                    }
                }
                else if (startZ + zOffset == 2 * v.getBrushSize())
                { // NORTHWEST
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 1;
                    }
                    if (this.sdirect == ClockDirection.CLOCKWISE)
                    {
                        zOffset--;
                    }
                    else
                    {
                        xOffset++;
                    }
                }
                else
                { // JUST PLAIN NORTH
                    if (this.sdirect == ClockDirection.CLOCKWISE)
                    {
                        if (this.stairtype == StairType.STAIR)
                        {
                            spiral[startX + xOffset][y][startZ + zOffset] = 4;
                            y++;
                        }
                        zOffset--;
                    }
                    else
                    {
                        if (this.stairtype == StairType.STAIR)
                        {
                            spiral[startX + xOffset][y][startZ + zOffset] = 5;
                            y++;
                        }
                        zOffset++;
                    }
                }

            }
            else if (startX + xOffset == 2 * v.getBrushSize())
            { // ALL SOUTH
                if (startZ + zOffset == 0)
                { // SOUTHEAST
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 1;
                    }
                    if (this.sdirect  == ClockDirection.CLOCKWISE)
                    {
                        zOffset++;
                    }
                    else
                    {
                        xOffset--;
                    }
                }
                else if (startZ + zOffset == 2 * v.getBrushSize())
                { // SOUTHWEST
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 1;
                    }
                    if (this.sdirect == ClockDirection.CLOCKWISE)
                    {
                        xOffset--;
                    }
                    else
                    {
                        zOffset--;
                    }
                }
                else
                { // JUST PLAIN SOUTH
                    if (this.sdirect == ClockDirection.CLOCKWISE)
                    {
                        if (this.stairtype == StairType.STAIR)
                        {
                            spiral[startX + xOffset][y][startZ + zOffset] = 5;
                            y++;
                        }
                        zOffset++;
                    }
                    else
                    {
                        if (this.stairtype == StairType.STAIR)
                        {
                            spiral[startX + xOffset][y][startZ + zOffset] = 4;
                            y++;
                        }
                        zOffset--;
                    }
                }

            }
            else if (startZ + zOffset == 0)
            { // JUST PLAIN EAST
                if (this.sdirect == ClockDirection.CLOCKWISE)
                {
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 3;
                        y++;
                    }
                    xOffset++;
                }
                else
                {
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 2;
                        y++;
                    }
                    xOffset--;
                }
            }
            else
            { // JUST PLAIN WEST
                if (this.sdirect == ClockDirection.CLOCKWISE)
                {
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 2;
                        y++;
                    }
                    xOffset--;
                }
                else
                {
                    if (this.stairtype == StairType.STAIR)
                    {
                        spiral[startX + xOffset][y][startZ + zOffset] = 3;
                        y++;
                    }
                    xOffset++;
                }
            }

        }

        final Undo undo = new Undo();
        // Make the changes

        for (int x = 2 * v.getBrushSize(); x >= 0; x--)
        {

            for (int i = v.getVoxelHeight() - 1; i >= 0; i--) //can't use y because that's already defined.
            {

                for (int z = 2 * v.getBrushSize(); z >= 0; z--)
                {

                    int blockPositionX = targetBlock.getX();
                    int blockPositionY = targetBlock.getY();
                    int blockPositionZ = targetBlock.getZ();
                    switch (spiral[x][i][z])
                    {
                        case 0:
                            if (!this.getBlockIdAt(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z).isAir())
                            {
                                undo.put(this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z));
                            }
                            this.setBlockIdAt(blockPositionZ - v.getBrushSize() + z, blockPositionX - v.getBrushSize() + x, blockPositionY - i, Material.AIR);
                            break;
                        case 1:
                            if (this.stairtype == StairType.BLOCK)
                            {
                                if (this.getBlockIdAt(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z) != v.getVoxelId())
                                {
                                    undo.put(this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z));
                                }
                                this.setBlockIdAt(blockPositionZ - v.getBrushSize() + z, blockPositionX - v.getBrushSize() + x, blockPositionY - i, v.getVoxelId());
                            }
                            else if (this.stairtype == StairType.SLAB)
                            {
                                if (!Tag.SLABS.isTagged(this.getBlockIdAt(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z)))
                                {
                                    undo.put(this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z));
                                }
                                Material stoneSlab = Material.STONE_SLAB;
                                Slab slab = (Slab) stoneSlab.createBlockData();
                                slab.setType(Slab.Type.BOTTOM);
                                this.setBlockIdAndDataAt(blockPositionZ - v.getBrushSize() + z, blockPositionX - v.getBrushSize() + x, blockPositionY - i, stoneSlab, slab);
                                this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z).setBlockData(v.getData());
                            }
                            else if (this.stairtype == StairType.STAIR)
                            {
                                if (this.getBlockIdAt(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z) != v.getVoxelId())
                                {
                                    undo.put(this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z));
                                }
                                this.setBlockIdAt(blockPositionZ - v.getBrushSize() + z, blockPositionX - v.getBrushSize() + x, blockPositionY - i, v.getVoxelId());
                            }
                            break;
                        case 2:
                            if (this.stairtype == StairType.SLAB)
                            {
                                if (!Tag.SLABS.isTagged(this.getBlockIdAt(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z)))
                                {
                                    undo.put(this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z));
                                }
                                Material stoneSlab = Material.STONE_SLAB; //TODO what about the other types of slabs?
                                Slab slab = (Slab) stoneSlab.createBlockData();
                                slab.setType(Slab.Type.DOUBLE);
                                this.setBlockIdAndDataAt(blockPositionZ - v.getBrushSize() + z, blockPositionX - v.getBrushSize() + x, blockPositionY - i, stoneSlab, slab);
                                this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z).setBlockData(v.getData());
                            }
                            else if (this.stairtype == StairType.STAIR)
                            {
                                if (!Tag.STAIRS.isTagged(this.getBlockIdAt(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z)))
                                {
                                    undo.put(this.clampY(blockPositionX - v.getBrushSize() - x, blockPositionY + i, blockPositionZ - v.getBrushSize() + z));
                                }
                                Material oakWoodStairs = Material.OAK_STAIRS; //TODO what about the other types of stairs?
                                this.setBlockIdAt(blockPositionZ - v.getBrushSize() + z, blockPositionX - v.getBrushSize() + x, blockPositionY - i, oakWoodStairs);
                                this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z).setBlockData(Material.AIR.createBlockData());
                            }
                            break;
                        default:
                            if (this.stairtype == StairType.STAIR)
                            {
                                if (!Tag.STAIRS.isTagged(this.getBlockIdAt(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z)))
                                {
                                    undo.put(this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z));
                                }
                                Material oakStairs = Material.OAK_STAIRS;
                                Stairs stairs = (Stairs) oakStairs.createBlockData();
                                stairs.setFacing(BlockFace.NORTH); //TODO I REALLY DON'T KNOW IF THIS IS CORRECT!!!
                                this.setBlockIdAt(blockPositionZ - v.getBrushSize() + z, blockPositionX - v.getBrushSize() + x, blockPositionY - i, oakStairs);
                                this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z).setBlockData(stairs);
                                //this.clampY(blockPositionX - v.getBrushSize() + x, blockPositionY - i, blockPositionZ - v.getBrushSize() + z).setData((byte) (spiral[x][i][z] - 2));
                            }
                            break;
                    }
                }
            }
        }
        v.owner().storeUndo(undo);
    }

    @Override
    protected final void arrow(final SnipeData v)
    {
        this.digStairWell(v, this.getTargetBlock()); // make stairwell below target
    }

    @Override
    protected final void powder(final SnipeData v)
    {
        this.buildStairWell(v, this.getLastBlock()); // make stairwell above target
    }

    @Override
    public final void info(final Message vm)
    {
        vm.brushName("Spiral Staircase");
        vm.size();
        vm.voxel();
        vm.height();
        vm.data();
        vm.custom(ChatColor.BLUE + "Staircase type: " + this.stairtype);
        vm.custom(ChatColor.BLUE + "Staircase turns: " + this.sdirect);
        vm.custom(ChatColor.BLUE + "Staircase opens: " + this.sopen);
    }

    @Override
    public final void parameters(final String[] par, final SnipeData v)
    {
        if (par[1].equalsIgnoreCase("info"))
        {
            v.sendMessage(ChatColor.GOLD + "Spiral Staircase Parameters:");
            v.sendMessage(ChatColor.AQUA + "/b sstair 'block' (default) | 'step' | 'woodstair' | 'cobblestair' -- set the type of staircase");
            v.sendMessage(ChatColor.AQUA + "/b sstair 'c' (default) | 'cc' -- set the turning direction of staircase");
            v.sendMessage(ChatColor.AQUA + "/b sstair 'n' (default) | 'e' | 's' | 'w' -- set the opening direction of staircase");
            return;
        }

        for (int i = 1; i < par.length; i++)
        {
            StairType stairType = StairType.fromString(par[i]);
            ClockDirection clockDirection = ClockDirection.fromString(par[i]);
            WindDirection windDirection = WindDirection.fromString(par[i]);
            if (stairType != null) {
                this.stairtype = stairType;
                v.sendMessage(ChatColor.BLUE + "Staircase type: " + this.stairtype);
            }
            else if (clockDirection != null)
            {
                this.sdirect = clockDirection;
                v.sendMessage(ChatColor.BLUE + "Staircase turns: " + this.sdirect);
            }
            else if (windDirection != null)
            {
                this.sopen = windDirection;
                v.sendMessage(ChatColor.BLUE + "Staircase opens: " + this.sopen);
            }
            else
            {
                v.sendMessage(ChatColor.RED + "Invalid brush parameters! use the info parameter to display parameter info.");
            }
        }
    }

    @Override
    public String getPermissionNode()
    {
        return "voxelsniper.brush.spiralstaircase";
    }
}
