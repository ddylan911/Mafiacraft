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
package net.voxton.mafiacraft.help;

import net.voxton.mafiacraft.player.MsgColor;
import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Help menu test.
 */
public class HelpMenuTest {
    
    private HelpMenu testMenu;
    
    public HelpMenuTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        testMenu = new HelpMenuImpl();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testAddEntry() {
        System.out.println("Testing adding an entry to the menu.");
        
        String command = "mycommand";
        String desc = "A command.";
        String usage = "[opt]";
        
        testMenu.addEntry(command, desc, usage);

        //Make sure it went in!
        assertTrue(testMenu.hasCommand(command));

        //Check the description
        String descResult = testMenu.getEntry(command);
        assertEquals(desc, descResult);

        //Check the usage
        String usageResult = testMenu.getUsage(command);
        assertEquals(usage, usageResult);
    }
    
    @Test
    public void testGetPage() {
        System.out.println("Testing getting a page of the help menu.");
        
        int page = 1;
        
        List<String> expected = new LinkedList<String>();
        
        expected.add(MsgColor.INFO
                + "============= [ Test Help -- Page 1 of 1 ] =============");
        expected.add(MsgColor.HELP_ENTRY + "test: " + MsgColor.HELP_DEF
                + "A test method.");
        expected.add(MsgColor.HELP_ENTRY + "test2: " + MsgColor.HELP_DEF
                + "A test2 method.");
        expected.add(MsgColor.HELP_ENTRY + "test3: " + MsgColor.HELP_DEF
                + "A test3 method.");
        expected.add(MsgColor.HELP_ENTRY + "test4: " + MsgColor.HELP_DEF
                + "A test4 method.");
        expected.add(MsgColor.HELP_ENTRY + "test5: " + MsgColor.HELP_DEF
                + "A test5 method.");
        expected.add(MsgColor.HELP_ENTRY + "test6: " + MsgColor.HELP_DEF
                + "A test6 method.");
        expected.add(MsgColor.HELP_ENTRY + "test7: " + MsgColor.HELP_DEF
                + "A test7 method.");
        expected.add(MsgColor.HELP_ENTRY + "test8: " + MsgColor.HELP_DEF
                + "A test8 method.");
        expected.add(MsgColor.HELP_ENTRY + "test9: " + MsgColor.HELP_DEF
                + "A test9 method.");
        
        List<String> result = testMenu.getPage(page);
        assertEquals(expected, result);
    }
    
    private class HelpMenuImpl extends HelpMenu {
        
        public HelpMenuImpl() {
            super("Test");
        }
        
        @Override
        public void loadMenu() {
            addEntry("test", "A test method.", "<args>");
            addEntry("test2", "A test2 method.", "<args2>");
            addEntry("test3", "A test3 method.", "<args3>");
            addEntry("test4", "A test4 method.", "<args4>");
            addEntry("test5", "A test5 method.", "<args5>");
            addEntry("test6", "A test6 method.", "<args6>");
            addEntry("test7", "A test7 method.", "<args7>");
            addEntry("test8", "A test8 method.", "<args8>");
            addEntry("test9", "A test9 method.", "<args9>");

            //Purposely out of order.
            addEntry("test1", "A test1 method.", "<args1>");
        }
        
    }

}
