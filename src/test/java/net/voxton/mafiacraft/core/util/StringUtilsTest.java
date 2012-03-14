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

import net.voxton.mafiacraft.core.util.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Testing of String utilities.
 */
public class StringUtilsTest {
    
    public StringUtilsTest() {
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
    
    @Test
    public void testFormatCurrency() {
        System.out.println("Testing formatting currency with cents and dollars.");
        
        double num = 209.46;
        
        String expected = "$209.46";
        String result = StringUtils.formatCurrency(num);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatCurrency_rounding() {
        System.out.println("Testing formatting currency with enough to round.");
        
        double num = 209.467;
        
        String expected = "$209.47";
        String result = StringUtils.formatCurrency(num);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatCurrency_noRounding() {
        System.out.println("Testing formatting currency with "
                + "enough to round but no rounding.");
        
        double num = 209.46292572457492875982759827589;
        
        String expected = "$209.46";
        String result = StringUtils.formatCurrency(num);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatCurrency_commas() {
        System.out.println("Testing formatting currency with enough to "
                + "have commas.");
        
        double num = 3141592209.46;
        
        String expected = "$3,141,592,209.46";
        String result = StringUtils.formatCurrency(num);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatCurrency_commasAndRounding() {
        System.out.println("Testing formatting currency with enough "
                + "to have commas and rounding.");
        
        double num = 3141592209.4698217914;
        
        String expected = "$3,141,592,209.47";
        String result = StringUtils.formatCurrency(num);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatCurrency_commasAndNoRound() {
        System.out.println("Testing formatting currency with enough to "
                + "have commas and rounding.");
        
        double num = 3141592209.4638217914;
        
        String expected = "$3,141,592,209.46";
        String result = StringUtils.formatCurrency(num);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTitleize() {
        System.out.println("Testing titleize method.");
        
        String string = "This is titleized text.";
        
        String expected = "This Is Titleized Text.";
        String result = StringUtils.titleize(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTitleize_trailing() {
        System.out.println("Testing titleize method with trailing whitespace.");
        
        String string = "This is titleized text.   ";
        
        String expected = "This Is Titleized Text.   ";
        String result = StringUtils.titleize(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTitleize_bigSpaces() {
        System.out.println("Testing titleize method with big spaces.");
        
        String string = "This    is    titleized text.";
        
        String expected = "This    Is    Titleized Text.";
        String result = StringUtils.titleize(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTitleize_trailingAndBigSpaces() {
        System.out.println("Testing titleize method with trailing whitespace and big spaces.");
        
        String string = "This    is    titleized text.   ";
        
        String expected = "This    Is    Titleized Text.   ";
        String result = StringUtils.titleize(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTitleize_noSpaces() {
        System.out.println("Testing titleize method with no spaces.");
        
        String string = "this_is_titleized_text.";
        
        String expected = "This_is_titleized_text.";
        String result = StringUtils.titleize(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTitleize_uppercaseString() {
        System.out.println("Testing titleize method with an uppercase string.");
        
        String string = "THIS IS TITLEIZED TEXT.";
        
        String expected = "This Is Titleized Text.";
        String result = StringUtils.titleize(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTitleize_mixedcaseString() {
        System.out.println("Testing titleize method with a mixed case string.");
        
        String string = "tHiS Is tItLeIzeD tEXt.";
        
        String expected = "This Is Titleized Text.";
        String result = StringUtils.titleize(string);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTitleize_mixedcaseWithNumbersString() {
        System.out.println("Testing titleize method with a mixed case with numbers string.");
        
        String string = "tHiS 4Is tIt123LeIzeD tEXt.";
        
        String expected = "This 4is Tit123leized Text.";
        String result = StringUtils.titleize(string);
        
        assertEquals(expected, result);
    }
}
