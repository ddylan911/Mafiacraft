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
package net.voxton.mafiacraft.locale;

import java.util.logging.Level;
import org.bukkit.Bukkit;
import net.voxton.mafiacraft.config.Config;
import java.io.File;
import java.util.logging.Logger;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import net.voxton.mafiacraft.Mafiacraft;
import org.bukkit.configuration.file.YamlConfiguration;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Testing of locale.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Mafiacraft.class, Config.class, Bukkit.class})
public class LocaleTest {

    private LocaleManager manager;

    public LocaleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        mockStatic(Mafiacraft.class);
        when(Mafiacraft.getSubFile("locale", "en-us")).thenReturn(new File(
                "./plugins/Mafiacraft/locale/en-us.yml"));

        mockStatic(Config.class);
        when(Config.getString("locale.default")).thenReturn("en-us");

        manager = new LocaleManager();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTopLevelLocalization() {
        System.out.println("Testing a top-level localization.");

        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File("/Users/ianschool/Desktop/p/mfiacraft/target/classes/locale/en-us.yml"));
        System.out.println(config);
        
        Locale locale = manager.getDefault();
        String expected = "en-us";
        String result = locale.localize("name");

        assertEquals(expected, result);
    }

    @Test
    public void testSecondLevelLocalization() {
        System.out.println("Testing a second-level localization.");

        Locale locale = manager.getDefault();
        String expected = "a citizen";
        String result = locale.localize("terms.a-citizen");

        assertEquals(expected, result);
    }

    @Test
    public void testThirdLevelLocalization() {
        System.out.println("Testing a third-level localization.");

        Locale locale = manager.getDefault();
        String expected = "You are not allowed to use this command.";
        String result = locale.localize("action.general.not-allowed");

        assertEquals(expected, result);
    }
}
