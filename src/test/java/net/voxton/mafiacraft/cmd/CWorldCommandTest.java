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
package net.voxton.mafiacraft.cmd;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Matchers.*;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.geo.WorldToggle;
import net.voxton.mafiacraft.locale.Locale;
import net.voxton.mafiacraft.player.MPlayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.junit.Assert.*;

/**
 * CWorld command.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Mafiacraft.class})
public class CWorldCommandTest {

    private MPlayer aubhaze;

    private MPlayer albireox;

    @Before
    public void setUp() {
        //Mock Mafiacraft for Locale support.
        mockStatic(Mafiacraft.class);
        when(Mafiacraft.getSubFile("locale", "en-us")).thenReturn(new File(
                "./plugins/Mafiacraft/locale/en-us.yml"));
        Locale enUs = Locale.getLocale("en-us");

        //Aubhaze has no permissions.
        aubhaze = mock(MPlayer.class);
        when(aubhaze.hasPermission(anyString())).thenReturn(false);
        when(aubhaze.getLocale()).thenReturn(enUs);

        //AlbireoX has all permissions.
        albireox = mock(MPlayer.class);
        when(albireox.hasPermission(anyString())).thenReturn(true);
        when(albireox.getLocale()).thenReturn(enUs);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testToggle_notAllowed() {
        System.out.println("Testing of a toggle that should not be allowed.");

        String expected = Locale.getLocale("en-us").localize(
                "command.general.not-allowed");
        String result = CWorldCommand.doToggle(aubhaze, WorldToggle.FREE_ROAM.
                name());

        assertEquals(expected, result);
    }

    @Test
    public void testToggle_invalid() {
        System.out.println("Testing of an invalid toggle.");

        String toggleList = Arrays.asList(WorldToggle.values()).toString();

        String expected = Locale.getLocale("en-us").localize(
                "command.cworld.toggle-invalid", toggleList);
        String result = CWorldCommand.doToggle(albireox, "fure_roma");

        assertEquals(expected, result);
    }

}
