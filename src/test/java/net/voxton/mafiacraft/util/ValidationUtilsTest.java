/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ianschool
 */
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
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of validateName method, of class ValidationUtils.
     */
    @Test
    public void testValidateName() {
        System.out.println("validateName");
        String name = "";
        String expResult = "";
        String result = ValidationUtils.validateName(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of validateDescription method, of class ValidationUtils.
     */
    @Test
    public void testValidateDescription() {
        System.out.println("validateDescription");
        String description = "";
        String expResult = "";
        String result = ValidationUtils.validateDescription(description);
        assertEquals(expResult, result);
    }
    
}
