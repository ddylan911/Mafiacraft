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

import net.voxton.mafiacraft.geo.City;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import net.voxton.mafiacraft.geo.CityWorld;
import net.voxton.mafiacraft.player.MPlayer;
import java.io.File;
import net.voxton.mafiacraft.MConfig;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.locale.Locale;
import net.voxton.mafiacraft.locale.LocaleManager;
import org.junit.After;
import org.junit.Before;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.mockito.Matchers.*;
import static org.junit.Assert.*;

/**
 * Testing of the city command.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Mafiacraft.class, MConfig.class})
public class CityCommandTest {

    private CityWorld world;
    
    private CityWorld metroWorld;
    
    private City metroCity;

    private MPlayer aubhaze;

    private MPlayer albireox;

    @Before
    public void setUp() {
        //Locale setup
        mockStatic(Mafiacraft.class);
        when(Mafiacraft.getSubFile("locale", "en-us")).thenReturn(new File(
                "./plugins/Mafiacraft/locale/en-us.yml"));
        LocaleManager manager = new LocaleManager();
        mockStatic(MConfig.class);
        when(MConfig.getString("locale.default")).thenReturn("en-us");
        Locale locale = manager.getDefault();
        when(Mafiacraft.getDefaultLocale()).thenReturn(locale);
        when(Mafiacraft.getLocaleManager()).thenReturn(manager);
        when(Mafiacraft.getLocales()).thenReturn(manager.getLocales());
        //Locale setup end.

        //Mock the cityworld
        world = mock(CityWorld.class);
        
        //Mock the real world
        metroCity = mock(City.class);
        metroWorld = mock(CityWorld.class);
        when(metroWorld.getCapital()).thenReturn(metroCity) ;

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
    public void testDoFound_notCitizen() {
        System.out.println("Testing found subcommand of a player not citizen.");
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.general.not-citizen");
        String result = CityCommand.doFound(aubhaze, "test");
        
        assertEquals(expected, result);
    }

    @Test
    public void testDoFound_capitalEstablished() {
        System.out.println("Testing found subcommand of a player in a world "
                + "with a capital already established.");
        
        when(albireox.getCityWorld()).thenReturn(metroWorld);
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.city.capital-established");
        String result = CityCommand.doFound(albireox, "test");
        
        assertEquals(expected, result);
    }

    @Test
    public void testDoSetSpawn_notCitizen() {
        System.out.println("Testing setspawn subcommand of a player not citizen.");
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.general.not-citizen");
        String result = CityCommand.doSetSpawn(aubhaze);
        
        assertEquals(expected, result);
    }

    @Test
    public void testDoAnnex_notCitizen() {
        System.out.println("Testing annex subcommand of a player not citizen.");
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.general.not-citizen");
        String result = CityCommand.doAnnex(aubhaze);
        
        assertEquals(expected, result);
    }

    @Test
    public void testDoUnannex_notCitizen() {
        System.out.println("Testing unannex subcommand of a player not citizen.");
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.general.not-citizen");
        String result = CityCommand.doUnannex(aubhaze);
        
        assertEquals(expected, result);
    }

    @Test
    public void testDoRename_notCitizen() {
        System.out.println("Testing rename subcommand of a player not citizen.");
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.general.not-citizen");
        String result = CityCommand.doRename(aubhaze, "test");
        
        assertEquals(expected, result);
    }

    @Test
    public void testDoFunds_notCitizen() {
        System.out.println("Testing funds subcommand of a player not citizen.");
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.general.not-citizen");
        String result = CityCommand.doFunds(aubhaze);
        
        assertEquals(expected, result);
    }

    @Test
    public void testDoDisband_notCitizen() {
        System.out.println("Testing disband subcommand of a player not citizen.");
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.general.not-citizen");
        String result = CityCommand.doDisband(aubhaze);
        
        assertEquals(expected, result);
    }

    @Test
    public void testDoBus_notCitizen() {
        System.out.println("Testing bus subcommand of a player not citizen.");
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.general.not-citizen");
        String result = CityCommand.doBus(aubhaze, "there");
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testDoDeposit_notCitizen() {
        System.out.println("Testing deposit subcommand of a player not citizen.");
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.general.not-citizen");
        String result = CityCommand.doDeposit(aubhaze, "test");
        
        assertEquals(expected, result);
    }

    @Test
    public void testDoWithdraw_notCitizen() {
        System.out.println("Testing withdraw subcommand of a player not citizen.");
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.general.not-citizen");
        String result = CityCommand.doWithdraw(aubhaze, "test");
        
        assertEquals(expected, result);
    }

    @Test
    public void testDoClaim_notCitizen() {
        System.out.println("Testing claim subcommand of a player not citizen.");
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.general.not-citizen");
        String result = CityCommand.doClaim(aubhaze);
        
        assertEquals(expected, result);
    }

    @Test
    public void testDoUnclaim_notCitizen() {
        System.out.println("Testing unclaim subcommand of a player not citizen.");
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.general.not-citizen");
        String result = CityCommand.doUnclaim(aubhaze);
        
        assertEquals(expected, result);
    }

    @Test
    public void testDoMakePolice_notCitizen() {
        System.out.println("Testing make police subcommand of a player not citizen.");
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.general.not-citizen");
        String result = CityCommand.doMakePolice(aubhaze, "asdf", "derp");
        
        assertEquals(expected, result);
    }

    @Test
    public void testDoSetChief_notCitizen() {
        System.out.println("Testing set chief subcommand of a player not citizen.");
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.general.not-citizen");
        String result = CityCommand.doSetChief(aubhaze, "asdf");
        
        assertEquals(expected, result);
    }

    @Test
    public void testDoSetAssistant_notCitizen() {
        System.out.println("Testing set assistant subcommand of a player not citizen.");
        
        String expected = Mafiacraft.getDefaultLocale().localize("command.general.not-citizen");
        String result = CityCommand.doSetAssistant(aubhaze, "asdf");
        
        assertEquals(expected, result);
    }
}
