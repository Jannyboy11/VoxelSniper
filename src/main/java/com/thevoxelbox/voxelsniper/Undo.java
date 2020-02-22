package com.thevoxelbox.voxelsniper;

import org.bukkit.Material;
import org.bukkit.Nameable;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.*;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.loot.Lootable;
import org.bukkit.material.Colorable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds {@link BlockState}s that can be later on used to reset those block
 * locations back to the recorded states.
 */
public class Undo {

    private static final Set<Material> FALLING_MATERIALS = EnumSet.of(
            Material.WATER,
            Material.BUBBLE_COLUMN,
            Material.LAVA); //also include materials for which material.hasGravity returns true?
    private static final Set<Material> FALLOFF_MATERIALS = Arrays.stream(Material.values())
            .filter(Materials::fallsOff)
            .collect(Collectors.toCollection(() -> EnumSet.noneOf(Material.class)));

    private final Set<Vector> containing = new HashSet<>();
    private final List<BlockState> all;
    private final List<BlockState> falloff;
    private final List<BlockState> dropdown;

    /**
     * Default constructor of a Undo container.
     */
    public Undo() {
        all = new LinkedList<>();
        falloff = new LinkedList<>();
        dropdown = new LinkedList<>();
    }

    /**
     * Get the number of blocks in the collection.
     *
     * @return size of the Undo collection
     */
    public int getSize() {
        return containing.size();
    }

    /**
     * Adds a Block to the collection.
     *
     * @param block Block to be added
     */
    public void put(Block block) {
        Vector pos = block.getLocation().toVector();
        if (this.containing.contains(pos)) {
            return;
        }
        this.containing.add(pos);
        if (Undo.FALLING_MATERIALS.contains(block.getType())) {
            dropdown.add(block.getState());
        } else if (Undo.FALLOFF_MATERIALS.contains(block.getType())) {
            falloff.add(block.getState());
        } else {
            all.add(block.getState());
        }
    }

    /**
     * Set the blockstates of all recorded blocks back to the state when they
     * were inserted.
     */
    public void undo() {

        for (BlockState blockState : all) {
            blockState.update(true, false);
            updateSpecialBlocks(blockState);
        }

        for (BlockState blockState : falloff) {
            blockState.update(true, false);
            updateSpecialBlocks(blockState);
        }

        for (BlockState blockState : dropdown) {
            blockState.update(true, false);
            updateSpecialBlocks(blockState);
        }
    }

    /**
     * @param oldState
     */
    private void updateSpecialBlocks(BlockState oldState) {
        //TODO Java 14 pattern matching

        //unfortunately there is no Block#setState(BlockState newState, boolean update) in bukkit..
        //so here we go - special casing a bunch of block states..
        BlockState newState = oldState.getBlock().getState();
        newState.setBlockData(oldState.getBlockData());

        //special cases
        if (oldState instanceof Banner && newState instanceof Banner) {
            Banner oldBanner = (Banner) oldState;
            Banner newBanner = (Banner) newState;

            newBanner.setBaseColor(oldBanner.getBaseColor());
            newBanner.setPatterns(oldBanner.getPatterns());
        } else if (oldState instanceof Beacon && newState instanceof Beacon) {
            Beacon oldBeacon = (Beacon) oldState;
            Beacon newBeacon = (Beacon) newState;

            newBeacon.setPrimaryEffect(oldBeacon.getPrimaryEffect().getType());
            newBeacon.setSecondaryEffect(oldBeacon.getSecondaryEffect().getType());
        } else if (oldState instanceof Beehive && newState instanceof Beehive) {
            Beehive oldHive = (Beehive) oldState;
            Beehive newHive = (Beehive) newState;

            newHive.setFlower(oldHive.getFlower()); //is this safe? what if there is no flower on the location?
        } else if (oldState instanceof Furnace && newState instanceof Furnace) {
            //also covers BlastFurnace and Smoker
            Furnace oldFurnace = (Furnace) oldState;
            Furnace newFurnace = (Furnace) newState;

            newFurnace.setBurnTime(oldFurnace.getBurnTime());
            newFurnace.setCookTime(oldFurnace.getCookTime());
            newFurnace.setCookTimeTotal(oldFurnace.getCookTimeTotal());
        } else if (oldState instanceof BrewingStand && newState instanceof BrewingStand) {
            BrewingStand oldStand = (BrewingStand) oldState;
            BrewingStand newStand = (BrewingStand) newState;

            newStand.setBrewingTime(oldStand.getBrewingTime());
            newStand.setFuelLevel(oldStand.getFuelLevel());
        } else if (oldState instanceof Campfire && newState instanceof Campfire) {
            Campfire oldFire = (Campfire) oldState;
            Campfire newFire = (Campfire) newState;

            for (int i = 0; i < oldFire.getSize() && i < newFire.getSize(); i++) {
                newFire.setCookTime(i, oldFire.getCookTime(i));
                newFire.setCookTimeTotal(i, oldFire.getCookTimeTotal(i));
                newFire.setItem(i, oldFire.getItem(i));
            }
        } else if (oldState instanceof CommandBlock && newState instanceof CommandBlock) {
            //CommandBlock is not Nameable because of the "@" semantics I guess
            CommandBlock oldBlock = (CommandBlock) oldState;
            CommandBlock newBlock = (CommandBlock) newState;

            newBlock.setName(oldBlock.getName());
            newBlock.setCommand(oldBlock.getCommand());
        } else if (oldState instanceof CreatureSpawner && newState instanceof CreatureSpawner) {
            CreatureSpawner oldSpawner = (CreatureSpawner) oldState;
            CreatureSpawner newSpawner = (CreatureSpawner) newState;

            newSpawner.setMinSpawnDelay(oldSpawner.getMinSpawnDelay());
            newSpawner.setMaxSpawnDelay(oldSpawner.getMaxSpawnDelay());
            newSpawner.setDelay(oldSpawner.getDelay());
            newSpawner.setMaxNearbyEntities(oldSpawner.getMaxNearbyEntities());
            newSpawner.setRequiredPlayerRange(oldSpawner.getRequiredPlayerRange());
            newSpawner.setSpawnCount(oldSpawner.getSpawnCount());
            newSpawner.setSpawnRange(oldSpawner.getSpawnRange());
            newSpawner.setSpawnedType(oldSpawner.getSpawnedType());
        } else if (oldState instanceof EndGateway && newState instanceof EndGateway) {
            EndGateway oldGateway = (EndGateway) oldState;
            EndGateway newGateway = (EndGateway) newState;

            newGateway.setAge(oldGateway.getAge());
            newGateway.setExactTeleport(oldGateway.isExactTeleport());
            newGateway.setExitLocation(oldGateway.getExitLocation());
        } else if (oldState instanceof Jukebox && newState instanceof Jukebox) {
            Jukebox oldJukebox = (Jukebox) oldState;
            Jukebox newJukebox = (Jukebox) newState;

            newJukebox.setPlaying(oldJukebox.isPlaying() ? oldJukebox.getPlaying() : null);
            newJukebox.setRecord(oldJukebox.getRecord());
        } else if (oldState instanceof Lectern && newState instanceof Lectern) {
            Lectern oldLectern = (Lectern) oldState;
            Lectern newLectern = (Lectern) newState;

            newLectern.setPage(oldLectern.getPage());
            newLectern.getInventory().setContents(oldLectern.getSnapshotInventory().getContents());
        } else if (oldState instanceof Sign && newState instanceof Sign) {
            Sign oldSign = (Sign) oldState;
            Sign newSign = (Sign) newState;

            newSign.setEditable(oldSign.isEditable());
            String[] oldLines = oldSign.getLines();
            for (int i = 0; i < oldLines.length; i++) {
                newSign.setLine(i, oldLines[i]);
            }
        } else if (oldState instanceof Skull && newState instanceof Skull) {
            Skull oldSkull = (Skull) oldState;
            Skull newSkull = (Skull) newState;

            OfflinePlayer oldPlayer = oldSkull.getOwningPlayer();
            if (oldPlayer != null) {
                newSkull.setOwningPlayer(oldPlayer);
            }
        } else if (oldState instanceof Structure && newState instanceof Structure) {
            Structure oldStructure = (Structure) oldState;
            Structure newStructure = (Structure) newState;

            newStructure.setAuthor(oldStructure.getAuthor());
            newStructure.setIntegrity(oldStructure.getIntegrity());
            newStructure.setBoundingBoxVisible(oldStructure.isBoundingBoxVisible());
            newStructure.setMetadata(oldStructure.getMetadata());
            newStructure.setMirror(oldStructure.getMirror());
            newStructure.setRelativePosition(oldStructure.getRelativePosition());
            newStructure.setRotation(oldStructure.getRotation());
            newStructure.setSeed(oldStructure.getSeed());
            newStructure.setStructureName(oldStructure.getStructureName());
            newStructure.setStructureSize(oldStructure.getStructureSize());
            newStructure.setUsageMode(oldStructure.getUsageMode());
            newStructure.setIgnoreEntities(oldStructure.isIgnoreEntities());
            newStructure.setShowAir(oldStructure.isShowAir());
        }

        //general cases
        if (oldState instanceof InventoryHolder && newState instanceof InventoryHolder) {
            InventoryHolder oldHolder = (InventoryHolder) oldState;
            InventoryHolder newHolder = (InventoryHolder) newState;

            Inventory oldInventory = oldHolder instanceof Container
                    ? ((Container) oldHolder).getSnapshotInventory()
                    : oldHolder.getInventory();
            newHolder.getInventory().setContents(oldInventory.getContents());
        }
        if (oldState instanceof Lootable && newState instanceof Lootable) {
            Lootable oldLootable = (Lootable) oldState;
            Lootable newLootable = (Lootable) newState;

            newLootable.setSeed(oldLootable.getSeed());
            newLootable.setLootTable(oldLootable.getLootTable());
        }
        if (oldState instanceof Lockable && newState instanceof Lockable) {
            Lockable oldLockable = (Lockable) oldState;
            Lockable newLockable = (Lockable) newState;

            newLockable.setLock(oldLockable.isLocked() ? oldLockable.getLock() : null);
        }
        if (oldState instanceof Nameable && newState instanceof Nameable) {
            Nameable oldNameable = (Nameable) oldState;
            Nameable newNameable = (Nameable) newState;

            newNameable.setCustomName(oldNameable.getCustomName());
        }
        if (oldState instanceof Colorable && newState instanceof Colorable) {
            Colorable oldColorable = (Colorable) oldState;
            Colorable newColorable = (Colorable) newState;

            newColorable.setColor(oldColorable.getColor());
        }
        if (oldState instanceof EntityBlockStorage && newState instanceof EntityBlockStorage) {
            EntityBlockStorage<Entity> oldStorage = (EntityBlockStorage<Entity>) oldState;
            EntityBlockStorage<Entity> newStorage = (EntityBlockStorage<Entity>) newState;

            newStorage.setMaxEntities(oldStorage.getMaxEntities());
            //first make sure the new store has no entities
            newStorage.releaseEntities();
            //then, add all the old entities to the new store
            for (Entity released : oldStorage.releaseEntities()) {
                newStorage.addEntity(released);
            }
            //TODO check whether this works
        }

//        if (oldState instanceof TileState) {
//            TileState oldTile = (TileState) oldState;
//            TileState newTile = (TileState) currentState;
//            PersistentDataContainer persistentDataContainer = oldTile.getPersistentDataContainer();
//            //one can dream this will eventually get added...
//            newTile.getPersistentDataContainer().setAll(persistentDataContainer.getValues());
//        }
        newState.update();

    }
}
