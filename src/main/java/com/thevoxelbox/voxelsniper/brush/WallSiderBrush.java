package com.thevoxelbox.voxelsniper.brush;

import com.thevoxelbox.voxelsniper.Message;
import com.thevoxelbox.voxelsniper.SnipeData;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class WallSiderBrush extends Brush {

    private enum Orientation {
        NORTH, EAST, SOUTH, WEST, RELATIVE_TO_PLAYER;
    }

    private Orientation defaultOrientation = Orientation.RELATIVE_TO_PLAYER;
    private int depth = 1;
    private double center;
    private boolean replace;
    private boolean includeAir;

    public WallSiderBrush() {
        setName("Wall Sider");
    }

    private static Orientation getDirection(Player player) {
        double angle = (player.getLocation().getYaw() - 90.0F) % 360.0F;
        if (angle < 0.0D) {
            angle += 360.0D;
        }

        if (0.0D >= angle && angle < 45.0D) {
            return Orientation.SOUTH;
        } else if (45.0D >= angle && angle < 135.0D) {
            return Orientation.WEST;
        } else if (135.0D >= angle && angle < 225.0D) {
            return Orientation.NORTH;
        } else if (225.0D >= angle && angle < 315.0D) {
            return Orientation.EAST;
        } else if (315.0D >= angle && angle < 360.0D) {
            return Orientation.SOUTH;
        } else {
            throw new RuntimeException("impossible player direction");
        }
    }


    private void sideWall(SnipeData snipeData, Block block, boolean opposite) {
        double brushSizeSquared = (snipeData.getBrushSize() + this.center) * (snipeData.getBrushSize() + this.center);
        Vector vector = block.getLocation().toVector();
        Vector vectorClone = vector.clone();
        Orientation orientation = this.defaultOrientation;
        if (orientation == Orientation.RELATIVE_TO_PLAYER) {
            orientation = getDirection(snipeData.owner().getPlayer());
        }

        if (opposite) {
            switch (orientation) {
                case NORTH:
                    orientation = Orientation.SOUTH;
                    break;
                case SOUTH:
                    orientation = Orientation.NORTH;
                    break;
                case EAST:
                    orientation = Orientation.WEST;
                    break;
                case WEST:
                    orientation = Orientation.EAST;
                    break;
                default:
                    orientation = Orientation.RELATIVE_TO_PLAYER;
            }
        }

        boolean northSouth;
        if (orientation == Orientation.NORTH || orientation == Orientation.SOUTH) {
            northSouth = true;
        } else {
            northSouth = false;
        }

        for (double addXorZ = -snipeData.getBrushSize(); addXorZ <= snipeData.getBrushSize(); addXorZ += 1) {
            if (northSouth) {
                vectorClone.setX(vector.getX() + addXorZ);
            } else {
                vectorClone.setZ(vector.getZ() + addXorZ);
            }

            for (double addY = -snipeData.getBrushSize(); addY <= snipeData.getBrushSize(); addY += 1) {
                vectorClone.setY(vector.getY() + addY);
                if (vector.distanceSquared(vectorClone) <= brushSizeSquared) {
                    for(int depthCursor = 0; depthCursor < this.depth; ++depthCursor) {
                        if (northSouth) {
                            vectorClone.setZ(vector.getZ() + (orientation == Orientation.SOUTH ? depthCursor : -depthCursor));
                        } else {
                            vectorClone.setX(vector.getX() + (orientation == Orientation.EAST ? depthCursor : -depthCursor));
                        }

                        Block blockAt = this.getWorld().getBlockAt(vectorClone.getBlockX(), vectorClone.getBlockY(), vectorClone.getBlockZ());
                        if (this.replace && blockAt.getType() == snipeData.getReplaceId() || !this.replace
                                && (!blockAt.getType().isAir() || this.includeAir)) {
                            blockAt.setType(snipeData.getVoxelId());
                        }
                    }

                    //reset vectorClone
                    if (northSouth) {
                        vectorClone.setZ(vector.getZ());
                    } else {
                        vectorClone.setX(vector.getX());
                    }
                }
            }
        }

    }

    protected final void arrow(SnipeData snipeData) {
        this.sideWall(snipeData, this.getTargetBlock(), false);
    }

    protected final void powder(SnipeData snipeData) {
        this.sideWall(snipeData, this.getTargetBlock(), true);
    }

    public final void parameters(String[] args, SnipeData snipeData) {
        for(int i = 1; i < args.length; ++i) {
            String argLowerCase = args[i].toLowerCase();
            if ((argLowerCase).startsWith("d")) {
                this.depth = Integer.parseInt(argLowerCase.replace("d", ""));
                snipeData.sendMessage(ChatColor.AQUA + "Depth set to " + this.depth + " blocks");
            } else if (!argLowerCase.startsWith("s")) {
                if (argLowerCase.startsWith("true")) {
                    this.center = 0.5D;
                    snipeData.sendMessage(ChatColor.AQUA + "True circle mode ON.");
                } else if (argLowerCase.startsWith("false")) {
                    this.center = 0.0D;
                    snipeData.sendMessage(ChatColor.AQUA + "True circle mode OFF.");
                } else if (argLowerCase.startsWith("air")) {
                    this.includeAir = true;
                    snipeData.sendMessage(ChatColor.AQUA + "Including air.");
                } else if (argLowerCase.startsWith("mm")) {
                    this.replace = true;
                    snipeData.sendMessage(ChatColor.AQUA + "Replacing block.");
                }
            } else {
                try {
                    this.defaultOrientation = Orientation.valueOf(argLowerCase.replace("s", "").toUpperCase());
                } catch (IllegalArgumentException e) {
                    this.defaultOrientation = Orientation.RELATIVE_TO_PLAYER;
                }

                snipeData.sendMessage(ChatColor.AQUA + "Orientation set to " + defaultOrientation);
            }
        }

    }

    //TODO tabcomplete

    @Override
    public void info(Message vm) {
        //TODO
    }

    @Override
    public String getPermissionNode() {
        return "voxelsniper.brush.wallsider";
    }
}
