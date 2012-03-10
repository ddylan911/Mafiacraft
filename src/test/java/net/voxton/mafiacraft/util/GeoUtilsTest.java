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

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Testing geo utils.
 */
public class GeoUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test of coordsToDistrictId method, of class GeoUtils.
     */
    @Test
    public void testCoordsToDistrictId() {
        System.out.println("coordsToDistrictId");
        int x = 9887;
        int z = 7773;
        int expResult = 0xa69f9e5d;
        int result = GeoUtils.coordsToDistrictId(x, z);
        assertEquals(expResult, result);
    }

    @Test
    public void testCoordsToDistrictId_negative() {
        System.out.println("coordsToDistrictId_negative");
        int x = -9887;
        int z = -7773;
        int expResult = 0x596161a3;
        int result = GeoUtils.coordsToDistrictId(x, z);
        assertEquals(expResult, result);
    }

    /**
     * Test of xFromDistrictId method, of class GeoUtils.
     */
    @Test
    public void testXFromDistrictId() {
        System.out.println("xFromDistrictId");
        int id = 0xa69f9e5d;
        int expResult = 9887;
        int result = GeoUtils.xFromDistrictId(id);
        assertEquals(expResult, result);
    }

    /**
     * Test of xFromDistrictId method, of class GeoUtils.
     */
    @Test
    public void testXFromDistrictId_negative() {
        System.out.println("xFromDistrictId_negative");
        int id = 0x596161a3;
        int expResult = -9887;
        int result = GeoUtils.xFromDistrictId(id);
        assertEquals(expResult, result);
    }

    /**
     * Test of zFromDistrictId method, of class GeoUtils.
     */
    @Test
    public void testZFromDistrictId() {
        System.out.println("zFromDistrictId");
        int id = 0xa69f9e5d;
        int expResult = 7773;
        int result = GeoUtils.zFromDistrictId(id);
        assertEquals(expResult, result);
    }

    @Test
    public void testZFromDistrictId_negative() {
        System.out.println("zFromDistrictId_negative");
        int id = 0x596161a3;
        int expResult = -7773;
        int result = GeoUtils.zFromDistrictId(id);
        assertEquals(expResult, result);
    }

    /**
     * Test of coordsToSectionId method, of class GeoUtils.
     */
    @Test
    public void testCoordsToSectionId() {
        System.out.println("coordsToSectionId");
        int x = 1;
        int z = 1;
        byte expResult = 0;
        byte result = GeoUtils.coordsToSectionId(x, z);
        assertEquals(expResult, result);
    }

}
