/*
 * This file is part of Mafiacraft.
 * 
 * Mafiacraft is released under the Voxton License version 1.
 *
 * Mafiacraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition to this, you must also specify that this product includes 
 * software developed by Voxton.net and may not remove any code
 * referencing Voxton.net directly or indirectly.
 * 
 * Mafiacraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
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
        world = mock(World.class);
        when(world.getName()).thenReturn("world");

        Logger testLogger = Logger.getLogger("Test");

        Server mockServer = mock(Server.class);
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
        System.out.println(
                "Testing the serialization integrity of a normal block location.");
        Location loc = new Location(world, 5, 5, 10);

        Map<String, Object> serialized = LocationSerializer.serializeBlock(loc);
        Location result = LocationSerializer.deserializeBlock(serialized);

        assertEquals(loc, result);
    }

    @Test
    public void testSerializationIntegrity_block_float() {
        System.out.println(
                "Testing the serialization integrity of a floating-point location saved as a block.");
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
        Location loc = new Location(world, 5.3f, 5.988f, 10.0f, 0.12831f,
                3.1414151518f);

        Map<String, Object> serialized = LocationSerializer.serializeBlock(loc);
        Location result = LocationSerializer.deserializeBlock(serialized);

        Location expected = new Location(world, 5, 5, 10);

        assertEquals(expected, result);
    }

    @Test
    public void testSerializationIntegrity_full_block() {
        System.out.println(
                "Testing the serialization integrity of a block location saved with pitch/yaw etc.");
        Location loc = new Location(world, 5, 5, 10);

        Map<String, Object> serialized = LocationSerializer.serializeFull(loc);
        Location result = LocationSerializer.deserializeFull(serialized);

        Location expected = new Location(world, 5f, 5f, 10f);

        assertEquals(expected, result);
    }

    @Test
    public void testSerializationIntegrity_full_float() {
        System.out.println(
                "Testing the serialization integrity of a floating-point location saved as a block.");
        Location loc = new Location(world, 5.3f, 5.988f, 10.0f);

        Map<String, Object> serialized = LocationSerializer.serializeFull(loc);
        Location result = LocationSerializer.deserializeFull(serialized);

        assertEquals(loc, result);
    }

    @Test
    public void testSerializationIntegrity_full_floatWithPitchAndYaw() {
        System.out.println("Testing the serialization integrity of a "
                + "floating-point location with pitch and yaw .");
        Location loc = new Location(world, 5.3f, 5.988f, 10.0f, 0.12831f,
                3.1414151518f);

        Map<String, Object> serialized = LocationSerializer.serializeFull(loc);
        Location result = LocationSerializer.deserializeFull(serialized);

        assertEquals(loc, result);
    }

}
