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
package net.voxton.mafiacraft.core.geo;

import net.voxton.mafiacraft.core.Mafiacraft;
import net.voxton.mafiacraft.core.city.CityManager;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.mockito.Mockito.verify;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Testing MPoints.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Mafiacraft.class})
public class MPointTest {
    
    public MPointTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getWorld method, of class MPoint.
     */
    @Test
    public void testGetWorld() {
        System.out.println("Testing the getWorld method.");
        
        MWorld world = mock(MWorld.class);
        when(world.getName()).thenReturn("world");
        MPoint instance = new MPoint(world, 1, 2, 3);
        
        MWorld expected = world;
        MWorld result = instance.getWorld();
        assertEquals(expected, result);
    }

    /**
     * Test of getX method, of class MPoint.
     */
    @Test
    public void testGetX() {
        System.out.println("Testing the getX method.");
        
        MWorld world = mock(MWorld.class);
        when(world.getName()).thenReturn("world");
        MPoint instance = new MPoint(world, 1, 2, 3);
        
        double expected = 1;
        double result = instance.getX();
        assertEquals(expected, result, 0);
    }

    /**
     * Test of getY method, of class MPoint.
     */
    @Test
    public void testGetY() {
        System.out.println("Testing the getY method.");
        
        MWorld world = mock(MWorld.class);
        when(world.getName()).thenReturn("world");
        MPoint instance = new MPoint(world, 1, 2, 3);
        
        double expected = 2;
        double result = instance.getY();
        assertEquals(expected, result, 0);
    }

    /**
     * Test of getZ method, of class MPoint.
     */
    @Test
    public void testGetZ() {
        System.out.println("Testing the getZ method.");
        
        MWorld world = mock(MWorld.class);
        when(world.getName()).thenReturn("world");
        MPoint instance = new MPoint(world, 1, 2, 3);
        
        double expected = 3;
        double result = instance.getZ();
        assertEquals(expected, result, 0);
    }

    /**
     * Test of getBlockX method, of class MPoint.
     */
    @Test
    public void testGetBlockX() {
        System.out.println("Testing the getBlockX method.");
        
        MWorld world = mock(MWorld.class);
        when(world.getName()).thenReturn("world");
        MPoint instance = new MPoint(world, 1, 2, 3);
        
        int expected = 1;
        int result = instance.getBlockX();
        assertEquals(expected, result);
    }

    /**
     * Test of getBlockY method, of class MPoint.
     */
    @Test
    public void testGetBlockY() {
        System.out.println("Testing the getBlockY method.");
        
        MWorld world = mock(MWorld.class);
        when(world.getName()).thenReturn("world");
        MPoint instance = new MPoint(world, 1, 2, 3);
        
        int expected = 2;
        int result = instance.getBlockY();
        assertEquals(expected, result);
    }

    /**
     * Test of getBlockZ method, of class MPoint.
     */
    @Test
    public void testGetBlockZ() {
        System.out.println("Testing the getBlockZ method.");
        
        MWorld world = mock(MWorld.class);
        when(world.getName()).thenReturn("world");
        MPoint instance = new MPoint(world, 1, 2, 3);
        
        int expected = 3;
        int result = instance.getBlockZ();
        assertEquals(expected, result);
    }

    /**
     * Test of getSection method, of class MPoint.
     */
    @Test
    public void testGetSection() {
        System.out.println("Testing the getSection method.");
        
        MWorld world = mock(MWorld.class);
        when(world.getName()).thenReturn("world");
        MPoint instance = new MPoint(world, 1, 2, 3);
        
        Section section = mock(Section.class);
        CityManager cm = mock(CityManager.class);
        mockStatic(Mafiacraft.class);
        when(Mafiacraft.getCityManager()).thenReturn(cm);
        when(cm.getSection(world, 0, 0, 0)).thenReturn(section);
        
        Section expected = section;
        Section result = instance.getSection();
        assertEquals(expected, result);
        
        verify(cm).getSection(world, 0, 0, 0);
    }

    /**
     * Test of getSection method, of class MPoint.
     */
    @Test
    public void testGetSection_notOrigin() {
        System.out.println("Testing the getSection method with a section not at the origin.");
        
        MWorld world = mock(MWorld.class);
        when(world.getName()).thenReturn("world");
        MPoint instance = new MPoint(world, 52, 69, 88);
        
        Section section = mock(Section.class);
        CityManager cm = mock(CityManager.class);
        mockStatic(Mafiacraft.class);
        when(Mafiacraft.getCityManager()).thenReturn(cm);
        when(cm.getSection(world, 3, 0, 5)).thenReturn(section);
        
        Section expected = section;
        Section result = instance.getSection();
        assertEquals(expected, result);
        
        verify(cm).getSection(world, 3, 0, 5);
    }

    /**
     * Test of getSection method, of class MPoint.
     */
    @Test
    public void testGetSection_negative() {
        System.out.println("Testing the getSection method with a section negative.");
        
        MWorld world = mock(MWorld.class);
        when(world.getName()).thenReturn("world");
        MPoint instance = new MPoint(world, -52, -69, -88);
        
        Section section = mock(Section.class);
        CityManager cm = mock(CityManager.class);
        mockStatic(Mafiacraft.class);
        when(Mafiacraft.getCityManager()).thenReturn(cm);
        when(cm.getSection(world, -4, -1, -6)).thenReturn(section);
        
        Section expected = section;
        Section result = instance.getSection();
        assertEquals(expected, result);
        
        verify(cm).getSection(world, -4, -1, -6);
    }

    /**
     * Test of distanceSquared method, of class MPoint.
     */
    @Test
    public void testDistanceSquared() {
        System.out.println("Testing the distanceSquared method.");
        
        MWorld world = mock(MWorld.class);
        when(world.getName()).thenReturn("world");
        MPoint instance = new MPoint(world, 1, 2, 3);
        MPoint other = new MPoint(world, 1, 56, 12);
        
        double expected = 2997.00407601;
        double result = instance.distanceSquared(other);
        assertEquals(expected, result, 0.1);
    }
    
}
