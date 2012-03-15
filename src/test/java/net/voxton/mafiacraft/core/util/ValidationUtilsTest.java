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
package net.voxton.mafiacraft.core.util;

import net.voxton.mafiacraft.core.util.ValidationUtils;
import net.voxton.mafiacraft.core.config.Config;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Testing of validation utils.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Config.class)
public class ValidationUtilsTest {

    public ValidationUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        mockStatic(Config.class);
        when(Config.getInt("strings.maxnamelength")).thenReturn(15);
        when(Config.getInt("strings.maxdesclength")).thenReturn(45);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testValidateName_valid() {
        System.out.println("Testing of name validation with a valid name.");
        String name = "MyName1239M";
        boolean result = ValidationUtils.validateName(name);

        assertTrue(result);
    }

    @Test
    public void testValidateName_tooLong() {
        System.out.println("Testing of name validation with a name too long.");
        String name = "1234567890123456";
        boolean result = ValidationUtils.validateName(name);

        assertFalse(result);
    }

    @Test
    public void testValidateName_notAlphaNum() {
        System.out.println(
                "Testing of name validation with a name not alphanumeric.");
        String name = "@#CJOJRcweCRJJ@#PRC";
        boolean result = ValidationUtils.validateName(name);

        assertFalse(result);
    }

    @Test
    public void testValidateDescription_valid() {
        System.out.println(
                "Testing of description validation with a valid description.");
        String description = "MY spacey descRIPti23123";
        String result = ValidationUtils.validateDescription(description);

        assertNull(result);
    }

}
