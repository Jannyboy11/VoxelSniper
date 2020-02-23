package com.thevoxelbox.voxelsniper;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 */
public class UndoTest
{
    private Undo undo;

    @Before
    public void setUp() throws Exception
    {
        Server server = Mockito.mock(Server.class);
        Set<Material> fallables = EnumSet.of(
                Material.BRAIN_CORAL,
                Material.BUBBLE_CORAL,
                Material.TUBE_CORAL,
                Material.FIRE_CORAL,
                Material.HORN_CORAL,

                Material.BRAIN_CORAL_FAN,
                Material.BUBBLE_CORAL_FAN,
                Material.TUBE_CORAL_FAN,
                Material.FIRE_CORAL_FAN,
                Material.HORN_CORAL_FAN,

                Material.OAK_SAPLING,
                Material.BIRCH_SAPLING,
                Material.SPRUCE_SAPLING,
                Material.JUNGLE_SAPLING,
                Material.DARK_OAK_SAPLING,
                Material.ACACIA_SAPLING,

                Material.WHEAT,
                Material.CARROTS,
                Material.POTATOES,
                Material.BEETROOTS,
                Material.MELON_STEM,
                Material.PUMPKIN_STEM,

                Material.STONE_BUTTON,
                Material.OAK_BUTTON,
                Material.BIRCH_BUTTON,
                Material.SPRUCE_BUTTON,
                Material.JUNGLE_BUTTON,
                Material.DARK_OAK_BUTTON,
                Material.ACACIA_BUTTON,

                Material.IRON_DOOR,
                Material.OAK_DOOR,
                Material.BIRCH_DOOR,
                Material.SPRUCE_DOOR,
                Material.JUNGLE_DOOR,
                Material.DARK_OAK_DOOR,
                Material.ACACIA_DOOR,

                Material.RAIL,
                Material.ACTIVATOR_RAIL,
                Material.DETECTOR_RAIL,
                Material.POWERED_RAIL,

                Material.CACTUS,
                Material.SUGAR_CANE,
                Material.BAMBOO,
                Material.BAMBOO_SAPLING,

                Material.POPPY,
                Material.OXEYE_DAISY,
                Material.DANDELION,
                Material.ROSE_BUSH,
                Material.CHORUS_PLANT,
                Material.CHORUS_FLOWER);

        NamespacedKey namespacedKey = NamespacedKey.minecraft("fallables");

        Mockito.when(server.getTag(Mockito.eq("blocks"), Mockito.anyObject() /*namespaced key*/, Mockito.same(Material.class)))
                .thenAnswer(new Answer<Tag<Material>>() {
                    @Override
                    public Tag<Material> answer(InvocationOnMock invocationOnMock) throws Throwable {
                        return new Tag<Material>() {
                            @Override
                            public boolean isTagged(Material material) {
                                return false;
                            }

                            @Override
                            public Set<Material> getValues() {
                                return fallables;
                            }

                            @Override
                            public NamespacedKey getKey() {
                                return namespacedKey;
                            }
                        };
                    }
                });

        Mockito.when(server.getName()).thenReturn("Mock Server");
        Mockito.when(server.getVersion()).thenReturn("Mock-1.15.2");
        Mockito.when(server.getBukkitVersion()).thenReturn("Bukkit-1.15.2");
        Mockito.when(server.getLogger()).thenAnswer(new Answer<Logger>() {
            @Override
            public Logger answer(InvocationOnMock invocationOnMock) throws Throwable {
                return Logger.getLogger("minecraft");
            }
        });

        if (Bukkit.getServer() == null /*ignore your IDE here please*/) {
            //need to check whether the server instance is not set because I can't set it twice
            //and junit runs the setup code for every test case.
            Bukkit.setServer(server);
        }

        undo = new Undo();
    }

    @Test
    public void testGetSize() throws Exception
    {
        World world = Mockito.mock(World.class);
        for (int i = 0; i < 5; i++)
        {
            Block block = Mockito.mock(Block.class);
            BlockState blockState = Mockito.mock(BlockState.class);
            Location location = new Location(world, 0, 0, i);
            Mockito.when(block.getLocation())
                   .thenReturn(location);
            Mockito.when(block.getState())
                   .thenReturn(blockState);
            Mockito.when(blockState.getLocation())
                   .thenReturn(location);
            undo.put(block);
        }
        Assert.assertEquals(5, undo.getSize());
        Block block = Mockito.mock(Block.class);
        BlockState blockState = Mockito.mock(BlockState.class);
        Location location = new Location(world, 0, 0, 6);
        Mockito.when(block.getLocation())
               .thenReturn(location);
        Mockito.when(block.getState())
               .thenReturn(blockState);
        Mockito.when(blockState.getLocation())
               .thenReturn(location);
        undo.put(block);
        Assert.assertEquals(6, undo.getSize());
        undo.put(block);
        Assert.assertEquals(6, undo.getSize());

    }

    @Test
    public void testPut() throws Exception
    {
        World world = Mockito.mock(World.class);
        Block block = Mockito.mock(Block.class);
        BlockState blockState = Mockito.mock(BlockState.class);
        Location location = new Location(world, 0, 0, 0);
        Mockito.when(block.getLocation())
               .thenReturn(location);
        Mockito.when(block.getState())
               .thenReturn(blockState);
        Mockito.when(blockState.getLocation())
               .thenReturn(location);

        undo.put(block);
    }

    @Test
    public void testUndo() throws Exception
    {
        World world = Mockito.mock(World.class);

        Block normalBlock = Mockito.mock(Block.class);
        BlockState normalBlockState = Mockito.mock(BlockState.class);
        Location normalBlockLocation = new Location(world, 0, 0, 0);
        Mockito.when(normalBlock.getLocation())
               .thenReturn(normalBlockLocation);
        Mockito.when(normalBlock.getState())
               .thenReturn(normalBlockState);
        Mockito.when(normalBlock.getType())
               .thenReturn(Material.STONE);
        Mockito.when(normalBlockState.getLocation())
               .thenReturn(normalBlockLocation);
        Mockito.when(normalBlockState.getBlock())
               .thenReturn(normalBlock);

        Block fragileBlock = Mockito.mock(Block.class);
        BlockState fragileBlockState = Mockito.mock(BlockState.class);
        Location fragileBlockLocation = new Location(world, 0, 0, 1);
        Mockito.when(fragileBlock.getLocation())
               .thenReturn(fragileBlockLocation);
        Mockito.when(fragileBlock.getState())
               .thenReturn(fragileBlockState);
        Mockito.when(fragileBlock.getType())
               .thenReturn(Material.TORCH);
        Mockito.when(fragileBlockState.getLocation())
               .thenReturn(fragileBlockLocation);
        Mockito.when(fragileBlockState.getBlock())
               .thenReturn(fragileBlock);

        Block waterBlock = Mockito.mock(Block.class);
        BlockState waterBlockState = Mockito.mock(BlockState.class);
        Location waterBlockLocation = new Location(world, 0, 0, 2);
        Mockito.when(waterBlock.getLocation())
               .thenReturn(waterBlockLocation);
        Mockito.when(waterBlock.getState())
               .thenReturn(waterBlockState);
        Mockito.when(waterBlock.getType())
               .thenReturn(Material.WATER);
        Mockito.when(waterBlockState.getLocation())
               .thenReturn(waterBlockLocation);
        Mockito.when(waterBlockState.getBlock())
               .thenReturn(waterBlock);


        undo.put(waterBlock);
        undo.put(fragileBlock);
        undo.put(normalBlock);
        undo.undo();

        InOrder inOrder = Mockito.inOrder(normalBlockState, fragileBlockState, waterBlockState);
        inOrder.verify(normalBlockState).update(Mockito.anyBoolean(), Mockito.anyBoolean());
        inOrder.verify(fragileBlockState).update(Mockito.anyBoolean(), Mockito.anyBoolean());
        inOrder.verify(waterBlockState).update(Mockito.anyBoolean(), Mockito.anyBoolean());
    }
}
