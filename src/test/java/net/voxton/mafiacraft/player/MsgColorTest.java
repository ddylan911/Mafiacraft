/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.player;

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
