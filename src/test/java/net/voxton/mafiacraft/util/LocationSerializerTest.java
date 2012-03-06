/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.util;

import net.voxton.mafiacraft.MLogger;
import net.voxton.mafiacraft.MafiacraftPlugin;
import net.voxton.mafiacraft.Mafiacraft;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.powermock.core.classloader.annotations.PrepareForTest;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import static org.powermock.api.mockito.PowerMockito.*;
import static org.junit.Assert.*;

/**
 * Testing of the Location serializer.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Bukkit.class, Mafiacraft.class})
public class LocationSerializerTest {

    private World world;

    @Before
    public void setUp() {
        world = PowerMockito.mock(World.class);
        Mockito.when(world.getName()).thenReturn("world");

        Logger testLogger = Logger.getLogger("Test");

        Server mockServer = Mockito.mock(Server.class);
        when(mockServer.getWorld("world")).thenReturn(world);
        when(mockServer.getLogger()).thenReturn(testLogger);
        
        mockStatic(Bukkit.class);
        when(Bukkit.getServer()).thenReturn(mockServer);
        when(Bukkit.getWorld("world")).thenReturn(world);
        
        MafiacraftPlugin mockPlugin = mock(MafiacraftPlugin.class);
        when(mockPlugin.getLogger()).thenReturn(testLogger);
        
        mockStatic(Mafiacraft.class);
        when(Mafiacraft.getPlugin()).thenReturn(mockPlugin);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSerializationIntegrity_block_normal() {
        System.out.println("Testing the serialization integrity of a normal block location.");
        Location loc = new Location(world, 5, 5, 10);

        Map<String, Object> serialized = LocationSerializer.serializeBlock(loc);
        Location result = LocationSerializer.deserializeBlock(serialized);

        assertEquals(loc, result);
    }

    @Test
    public void testSerializationIntegrity_block_float() {
        System.out.println("Testing the serialization integrity of a floating-point location saved as a block.");
        Location loc = new Location(world, 5.3f, 5.988f, 10.0f);

        Map<String, Object> serialized = LocationSerializer.serializeBlock(loc);
        Location result = LocationSerializer.deserializeBlock(serialized);

        Location expected = new Location(world, 5, 5, 10);

        assertEquals(expected, result);
    }

    @Test
    public void testSerializationIntegrity_block_floatWithPitchAndYaw() {
        System.out.println("Testing the serialization integrity of a "
                + "floating-point location with pitch and yaw saved as a block.");
        Location loc = new Location(world, 5.3f, 5.988f, 10.0f, 0.12831f, 3.1414151518f);

        Map<String, Object> serialized = LocationSerializer.serializeBlock(loc);
        Location result = LocationSerializer.deserializeBlock(serialized);

        Location expected = new Location(world, 5, 5, 10);

        assertEquals(expected, result);
    }

    @Test
    public void testSerializationIntegrity_full_block() {
        System.out.println("Testing the serialization integrity of a block location saved with pitch/yaw etc.");
        Location loc = new Location(world, 5, 5, 10);

        Map<String, Object> serialized = LocationSerializer.serializeFull(loc);
        Location result = LocationSerializer.deserializeFull(serialized);

        Location expected = new Location(world, 5f, 5f, 10f);

        assertEquals(expected, result);
    }

    @Test
    public void testSerializationIntegrity_full_float() {
        System.out.println("Testing the serialization integrity of a floating-point location saved as a block.");
        Location loc = new Location(world, 5.3f, 5.988f, 10.0f);

        Map<String, Object> serialized = LocationSerializer.serializeFull(loc);
        Location result = LocationSerializer.deserializeFull(serialized);

        assertEquals(loc, result);
    }

    @Test
    public void testSerializationIntegrity_full_floatWithPitchAndYaw() {
        System.out.println("Testing the serialization integrity of a "
                + "floating-point location with pitch and yaw .");
        Location loc = new Location(world, 5.3f, 5.988f, 10.0f, 0.12831f, 3.1414151518f);

        Map<String, Object> serialized = LocationSerializer.serializeFull(loc);
        Location result = LocationSerializer.deserializeFull(serialized);

        assertEquals(loc, result);
    }

}
