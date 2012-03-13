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

import net.voxton.mafiacraft.action.ActionType;
import net.voxton.mafiacraft.config.Config;
import net.voxton.mafiacraft.locale.LocaleManager;
import net.voxton.mafiacraft.geo.MWorld;
import java.io.File;
import java.util.Arrays;
import static org.mockito.Matchers.*;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.geo.WorldToggle;
import net.voxton.mafiacraft.locale.Locale;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * CWorld command.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Mafiacraft.class, Config.class})
public class CWorldCommandTest {

    private MWorld world;

    private MPlayer aubhaze;

    private MPlayer albireox;

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

        //Mock the cityworld
        world = mock(MWorld.class);

        //Aubhaze has no permissions.
        aubhaze = mock(MPlayer.class);
        when(aubhaze.hasPermission(anyString())).thenReturn(false);
        when(aubhaze.getLocale()).thenReturn(locale);
        when(aubhaze.getCityWorld()).thenReturn(world);

        //AlbireoX has all permissions.
        albireox = mock(MPlayer.class);
        when(albireox.hasPermission(anyString())).thenReturn(true);
        when(albireox.getLocale()).thenReturn(locale);
        when(albireox.getCityWorld()).thenReturn(world);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testToggle_notAllowed() {
        System.out.println("Testing of a toggle that should not be allowed.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "command.general.not-allowed");
        String result = ActionType.CWORLD.doToggle(aubhaze, WorldToggle.FREE_ROAM.
                name());

        assertEquals(expected, result);
    }

    @Test
    public void testToggle_invalid() {
        System.out.println("Testing of an invalid toggle.");

        String toggleList = Arrays.asList(WorldToggle.values()).toString();

        String expected = Mafiacraft.getDefaultLocale().localize(
                "command.cworld.toggle-invalid", toggleList);
        String result = ActionType.CWORLD.doToggle(albireox, "free_rome"); //Intentional derp

        assertEquals(expected, result);
    }

    @Test
    public void testToggle_success() {
        System.out.println("Testing of a successful toggle.");

        String toggle = WorldToggle.FREE_ROAM.name();
        boolean value = false;

        String result = ActionType.CWORLD.doToggle(albireox, toggle);
        assertNull(result);

        String message = Mafiacraft.getDefaultLocale().localize(
                "command.cworld.toggle-set", toggle, value);
        verify(albireox).sendMessage(MsgColor.SUCCESS + message);
        verify(world).toggle(WorldToggle.FREE_ROAM);
    }

    @Test
    public void testToggle_caseInsensitive() {
        System.out.println("Testing of a successful toggle, case insensitive.");

        String toggle = "free_RoaM";
        boolean value = false;

        String result = ActionType.CWORLD.doToggle(albireox, toggle);
        assertNull(result);

        String message = Mafiacraft.getDefaultLocale().localize(
                "command.cworld.toggle-set", WorldToggle.FREE_ROAM, value);
        verify(albireox).sendMessage(MsgColor.SUCCESS + message);
        verify(world).toggle(WorldToggle.FREE_ROAM);
    }

    @Test
    public void testToggle_dashes() {
        System.out.println("Testing of a successful toggle "
                + "with dashes instead of underscores.");

        String toggle = "free-RoaM";
        boolean value = false;

        String result = ActionType.CWORLD.doToggle(albireox, toggle);
        assertNull(result);

        String message = Mafiacraft.getDefaultLocale().localize(
                "command.cworld.toggle-set", WorldToggle.FREE_ROAM, value);
        verify(albireox).sendMessage(MsgColor.SUCCESS + message);
        verify(world).toggle(WorldToggle.FREE_ROAM);
    }

}
