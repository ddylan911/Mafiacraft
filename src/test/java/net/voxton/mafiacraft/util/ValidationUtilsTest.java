/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.util;

import net.voxton.mafiacraft.MConfig;
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
@PrepareForTest(MConfig.class)
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
        mockStatic(MConfig.class);
        when(MConfig.getInt(Matchers.anyString())).thenReturn(25);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testValidateName_valid() {
        System.out.println("Testing of name validation with a valid name.");
        String name = "MyName1239M";
        String result = ValidationUtils.validateName(name);

        assertNull(result);
    }

    @Test
    public void testValidateDescription_valid() {
        System.out.println("Testing of description validation with a valid description.");
        String description = "MY spacey descRIPti23123";
        String result = ValidationUtils.validateDescription(description);

        assertNull(result);
    }

}
