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

import net.voxton.mafiacraft.locale.Locale;
import net.voxton.mafiacraft.locale.LocaleManager;
import net.voxton.mafiacraft.geo.CityWorld;
import net.voxton.mafiacraft.player.MPlayer;
import java.io.File;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.config.Config;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import net.voxton.mafiacraft.player.MsgColor;
import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

/**
 * Help menu test.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Config.class, Mafiacraft.class})
public class HelpMenuTest {

    private CityWorld world;

    private MPlayer aubhaze;

    private MPlayer albireox;

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
        //Locale setup
        mockStatic(Mafiacraft.class);
        when(Mafiacraft.getSubFile("locale", "en-us")).thenReturn(new File(
                "./plugins/Mafiacraft/locale/en-us.yml"));
        LocaleManager manager = new LocaleManager();
        mockStatic(Config.class);
        when(Config.getString("locale.default")).thenReturn("en-us");
        Locale locale = manager.getDefault();
        when(Mafiacraft.getDefaultLocale()).thenReturn(locale);
        when(Mafiacraft.getLocaleManager()).thenReturn(manager);
        when(Mafiacraft.getLocales()).thenReturn(manager.getLocales());
        //Locale setup end.
        
        testMenu = new HelpMenuImpl();

        //Aubhaze has no permissions.
        aubhaze = mock(MPlayer.class);
        when(aubhaze.hasPermission(anyString())).thenReturn(false);
        when(aubhaze.getLocale()).thenReturn(locale);

        //AlbireoX has all permissions.
        albireox = mock(MPlayer.class);
        when(albireox.hasPermission(anyString())).thenReturn(true);
        when(albireox.getLocale()).thenReturn(locale);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddEntry() {
        System.out.println("Testing adding an entry to the menu.");

        String command = "test";
        String desc = "cmd-help.test.test";
        String descExpected = "A test method.";
        String usage = "[opt]";

        testMenu.addEntry(command, desc, usage);

        //Make sure it went in!
        assertTrue(testMenu.hasCommand(command));

        //Check the description
        String descResult = testMenu.getEntry(command, Mafiacraft.
                getDefaultLocale());
        assertEquals(descExpected, descResult);

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
                + "============= [ Test Help -- Page 1 of 2 ] =============");
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

    @Test
    public void testGetPage_2() {
        System.out.println(
                "Testing getting of a second page that is shorter than maximum.");

        int page = 2;

        List<String> expected = new LinkedList<String>();

        expected.add(MsgColor.INFO
                + "============= [ Test Help -- Page 2 of 2 ] =============");
        expected.add(MsgColor.HELP_ENTRY + "test1: " + MsgColor.HELP_DEF
                + "A test1 method.");
        
        List<String> result = testMenu.getPage(page);
        
        assertEquals(expected, result);
    }

    @Test
    public void testGetPage_imaginary() {
        System.out.println(
                "Testing getting of an imaginary page. (One less than 0.)");

        int page = -1;

        List<String> expected = new LinkedList<String>();

        expected.add(MsgColor.INFO
                + "============ [ Test Help -- Page -1 of 2 ] ============");
        expected.add(MsgColor.SUCCESS + Mafiacraft.getDefaultLocale().localize(
                "help.imaginary-page"));
        
        List<String> result = testMenu.getPage(page);
        
        for (int i = 0; i < result.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }
        
        assertEquals(expected, result);
    }

    @Test
    public void testDoHelp_incorrectUsage() {
//        fail();
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
