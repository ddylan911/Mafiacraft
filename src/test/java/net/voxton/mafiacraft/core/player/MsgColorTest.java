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
package net.voxton.mafiacraft.core.player;

import net.voxton.mafiacraft.core.chat.MsgColor;
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
public class MsgColorTest {

    public MsgColorTest() {
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
    public void testParseColors_one() {
        System.out.println("Testing parsing of one color within a string.");

        String input = "This is my string with one `{ERROR}ERROR.";
        String expected = "This is my string with one " + MsgColor.ERROR
                + "ERROR.";
        String result = MsgColor.parseColors(input);

        assertEquals(expected, result);
    }

    @Test
    public void testParseColors_multiple() {
        System.out.println("Testing parsing of multiple colors within a string.");

        String input = "This is my `{INFO}string with one `{ERROR}ERROR.";
        String expected = "This is my " + MsgColor.INFO + "string with one "
                + MsgColor.ERROR + "ERROR.";
        String result = MsgColor.parseColors(input);

        assertEquals(expected, result);
    }

    @Test
    public void testParseColors_multipleSame() {
        System.out.println(
                "Testing parsing of multiple colors within a string, all the same.");

        String input = "This is my `{ERROR}string with one `{ERROR}ERROR.";
        String expected = "This is my " + MsgColor.ERROR + "string with one "
                + MsgColor.ERROR + "ERROR.";
        String result = MsgColor.parseColors(input);

        assertEquals(expected, result);
    }

    @Test
    public void testParseColors_multipleSameAndOneDifferent() {
        System.out.println("Testing parsing of multiple colors within a string, "
                + "all the same but one different.");

        String input =
                "This`{INFO} is my `{ERROR}string with one `{ERROR}ERROR.";
        String expected = "This" + MsgColor.INFO + " is my " + MsgColor.ERROR
                + "string with one "
                + MsgColor.ERROR + "ERROR.";
        String result = MsgColor.parseColors(input);

        assertEquals(expected, result);
    }

    @Test
    public void testParseColors_multipleSameAndMultipleDifferent() {
        System.out.println(
                "Testing parsing of multiple colors within a string, all "
                + "different and multiple of each.");

        String input =
                "This`{INFO} is my `{ERROR}string with `{INFO}one `{ERROR}ERROR.";
        String expected = "This" + MsgColor.INFO + " is my " + MsgColor.ERROR
                + "string with " + MsgColor.INFO + "one " + MsgColor.ERROR
                + "ERROR.";
        String result = MsgColor.parseColors(input);

        assertEquals(expected, result);
    }

    @Test
    public void testParseColors_nested() {
        System.out.println(
                "Testing parsing of multiple colors within a string nested.");

        String input =
                "This`{INFO} is my `{ERROR}string with `{INF`{ERROR}O}one `{ERROR}ERROR.";
        String expected = "This" + MsgColor.INFO + " is my " + MsgColor.ERROR
                + "string with `{INF" + MsgColor.ERROR + "O}one "
                + MsgColor.ERROR
                + "ERROR.";
        String result = MsgColor.parseColors(input);

        assertEquals(expected, result);
    }

}
