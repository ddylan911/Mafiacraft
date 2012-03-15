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
package net.voxton.mafiacraft.core.gov;

import net.voxton.mafiacraft.core.geo.MPoint;
import net.voxton.mafiacraft.core.geo.MWorld;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Arrays;
import net.voxton.mafiacraft.core.Mafiacraft;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.util.Set;
import java.util.Map;
import net.voxton.mafiacraft.core.chat.MsgColor;
import net.voxton.mafiacraft.core.city.LandOwner;
import net.voxton.mafiacraft.core.city.OwnerType;
import net.voxton.mafiacraft.core.player.MPlayer;
import org.bukkit.Chunk;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.mockito.Mockito.*;

/**
 * Government unit test.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Mafiacraft.class})
public class GovernmentTest {

    private GovernmentManager governmentManager;

    private Government government;

    public GovernmentTest() {
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

        governmentManager = mock(GovernmentManager.class);

        when(Mafiacraft.getGovernmentManager()).thenReturn(governmentManager);

        government = new Government(1);

        government.setLand(201);
        government.setName("GovarnMunt");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getId method, of class Government.
     */
    @Test
    public void testGetId() {
        System.out.println("Testing the getId method.");

        int expResult = 1;
        int result = government.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class Government.
     */
    @Test
    public void testGetName() {
        System.out.println("Testing the getName method.");

        String expected = "GovarnMunt";
        String result = government.getName();
        assertEquals(expected, result);
    }

    /**
     * Test of getTotalLand method, of class Government.
     */
    @Test
    public void testGetTotalLand_noDivs() {
        System.out.println("Testing the getTotalLand method.");

        int expected = 201;
        int result = government.getTotalLand();
        assertEquals(expected, result);
    }

    /**
     * Test of getTotalLand method, of class Government with a division.
     */
    @Test
    public void testGetTotalLand_withDiv() {
        System.out.println("Testing the getTotalLand method.");

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(203);
        Set<Division> divs = new HashSet<Division>(Arrays.asList(div1, div2));
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        int expected = 424;
        int result = government.getTotalLand();
        assertEquals(expected, result);
    }

    /**
     * Test of canHQBeClaimed method, of class Government.
     */
    @Test
    public void testCanHQBeClaimed_true() {
        System.out.println("canHQBeClaimed");

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(0);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(0);
        Set<Division> divs = new HashSet<Division>();
        divs.add(div1);
        divs.add(div2);
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        boolean expResult = true;
        boolean result = government.canHQBeClaimed();
        assertEquals(expResult, result);
    }

    /**
     * Test of canHQBeClaimed method, of class Government.
     */
    @Test
    public void testCanHQBeClaimed_false() {
        System.out.println("canHQBeClaimed");

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(1);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(0);
        Set<Division> divs = new HashSet<Division>(Arrays.asList(div1, div2));
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        boolean expResult = false;
        boolean result = government.canHQBeClaimed();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPower method, of class Government.
     */
    @Test
    public void testGetPower() {
        System.out.println("Testing the getPower method.");

        String leader = "Frank";
        String viceLeader = "Joe";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        MPlayer frank = mock(MPlayer.class);
        when(frank.getPower()).thenReturn(12);
        MPlayer joe = mock(MPlayer.class);
        when(joe.getPower()).thenReturn(4);
        MPlayer bob = mock(MPlayer.class);
        when(bob.getPower()).thenReturn(7);
        MPlayer tim = mock(MPlayer.class);
        when(tim.getPower()).thenReturn(-9);
        MPlayer nancy = mock(MPlayer.class);
        when(nancy.getPower()).thenReturn(-22);

        when(Mafiacraft.getPlayer("Frank")).thenReturn(frank);
        when(Mafiacraft.getPlayer("Joe")).thenReturn(joe);
        when(Mafiacraft.getPlayer("Bob")).thenReturn(bob);
        when(Mafiacraft.getPlayer("Tim")).thenReturn(tim);
        when(Mafiacraft.getPlayer("Nancy")).thenReturn(nancy);

        int expResult = -8;
        int result = government.getPower();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMinPlayerPower method, of class Government.
     */
    @Test
    public void testGetMinPlayerPower() {
        System.out.println("Testing the getMinPlayerPower method.");

        //Mock the council
        String leader = "Leader";
        String viceLeader = "ViceLeader";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        int expected = -80;
        int result = government.getMinPlayerPower();
        assertEquals(expected, result);
    }

    /**
     * Test of getMaxPlayerPower method, of class Government.
     */
    @Test
    public void testGetMaxPlayerPower() {
        System.out.println("Testing the getMaxPlayerPower method.");

        //Mock the council
        String leader = "Leader";
        String viceLeader = "ViceLeader";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        int expected = 80;
        int result = government.getMaxPlayerPower();
        assertEquals(expected, result);
    }

    /**
     * Test of getPlayerPower method, of class Government.
     */
    @Test
    public void testGetPlayerPower() {
        System.out.println("Testing the getPlayerPower method.");

        //Mock the council
        String leader = "Leader";
        String viceLeader = "ViceLeader";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        int expected = 40;
        int result = government.getPlayerPower();
        assertEquals(expected, result);
    }

    /**
     * Test of setName method, of class Government.
     */
    @Test
    public void testSetName() {
        System.out.println("Testing the setName method.");

        String name = "TestName123213";
        government.setName(name);

        String expected = name;
        String result = government.getName();
        assertEquals(expected, result);
    }

    /**
     * Test of getType method, of class Government.
     */
    @Test
    public void testGetType() {
        System.out.println("Testing the getType method.");

        GovType type = GovType.MAFIA;
        government.setType(type);

        GovType expected = type;
        GovType result = government.getType();
        assertEquals(expected, result);
    }

    /**
     * Test of setType method, of class Government.
     */
    @Test
    public void testSetType() {
        System.out.println("Testing the setType method.");

        GovType type = GovType.MAFIA;
        government.setType(type);

        GovType expected = type;
        GovType result = government.getType();
        assertEquals(expected, result);
    }

    /**
     * Test of canBuild method, of class Government.
     */
    @Test
    public void testCanBuild_leader() {
        System.out.println(
                "Testing the canBuild method with a player of leader position.");

        MPlayer frank = mock(MPlayer.class);
        when(frank.getPosition()).thenReturn(Position.LEADER);
        when(frank.getGovernment()).thenReturn(government);

        boolean expected = true;
        boolean result = government.canBuild(frank, null);
        assertEquals(expected, result);
    }

    /**
     * Test of canBuild method, of class Government.
     */
    @Test
    public void testCanBuild_viceLeader() {
        System.out.println(
                "Testing the canBuild method with a player of vice leader position.");

        MPlayer frank = mock(MPlayer.class);
        when(frank.getPosition()).thenReturn(Position.VICE_LEADER);
        when(frank.getGovernment()).thenReturn(government);

        boolean expected = true;
        boolean result = government.canBuild(frank, null);
        assertEquals(expected, result);
    }

    /**
     * Test of canBuild method, of class Government.
     */
    @Test
    public void testCanBuild_officer() {
        System.out.println(
                "Testing the canBuild method with a player of officer position.");

        MPlayer frank = mock(MPlayer.class);
        when(frank.getPosition()).thenReturn(Position.OFFICER);
        when(frank.getGovernment()).thenReturn(government);

        boolean expected = true;
        boolean result = government.canBuild(frank, null);
        assertEquals(expected, result);
    }

    /**
     * Test of canBuild method, of class Government.
     */
    @Test
    public void testCanBuild_manager() {
        System.out.println(
                "Testing the canBuild method with a player of manager position.");

        MPlayer frank = mock(MPlayer.class);
        when(frank.getPosition()).thenReturn(Position.MANAGER);
        when(frank.getGovernment()).thenReturn(government);

        boolean expected = false;
        boolean result = government.canBuild(frank, null);
        assertEquals(expected, result);
    }

    /**
     * Test of canBuild method, of class Government.
     */
    @Test
    public void testCanBuild_worker() {
        System.out.println(
                "Testing the canBuild method with a player of worker position.");

        MPlayer frank = mock(MPlayer.class);
        when(frank.getPosition()).thenReturn(Position.WORKER);
        when(frank.getGovernment()).thenReturn(government);

        boolean expected = false;
        boolean result = government.canBuild(frank, null);
        assertEquals(expected, result);
    }

    /**
     * Test of canBuild method, of class Government.
     */
    @Test
    public void testCanBuild_affiliate() {
        System.out.println(
                "Testing the canBuild method with a player of affiliate position.");

        MPlayer frank = mock(MPlayer.class);
        when(frank.getPosition()).thenReturn(Position.AFFILIATE);
        when(frank.getGovernment()).thenReturn(government);

        boolean expected = false;
        boolean result = government.canBuild(frank, null);
        assertEquals(expected, result);
    }

    /**
     * Test of canBuild method, of class Government.
     */
    @Test
    public void testCanBuild_noPosition() {
        System.out.println(
                "Testing the canBuild method with a player of no position.");

        MPlayer frank = mock(MPlayer.class);
        when(frank.getPosition()).thenReturn(Position.NONE);
        when(frank.getGovernment()).thenReturn(null);

        boolean expected = false;
        boolean result = government.canBuild(frank, null);
        assertEquals(expected, result);
    }

    /**
     * Test of getOwnerName method, of class Government.
     */
    @Test
    public void testGetOwnerName() {
        System.out.println("getOwnerName");

        String expected = "GovarnMunt";
        String result = government.getOwnerName();
        assertEquals(expected, result);
    }

    /**
     * Test of getLeader method, of class Government.
     */
    @Test
    public void testGetLeader() {
        System.out.println("Testing the getLeader method.");

        String leader = "Tim";
        government.setLeader(leader);

        String expected = leader;
        String result = government.getLeader();
        assertEquals(expected, result);
    }

    /**
     * Test of getViceLeader method, of class Government.
     */
    @Test
    public void testGetViceLeader() {
        System.out.println("Testing the getViceLeader method.");

        String viceLeader = "bob";
        government.setViceLeader(viceLeader);

        String expected = viceLeader;
        String result = government.getViceLeader();
        assertEquals(expected, result);
    }

    /**
     * Test of getMembers method, of class Government.
     */
    @Test
    public void testGetMembers_0args_councilOnly() {
        System.out.println(
                "Testing the getMembers method with only council members.");

        String leader = "Leader";
        String viceLeader = "ViceLeader";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        Set<String> expected = new HashSet<String>();
        expected.add(leader);
        expected.add(viceLeader);
        expected.add("Bob");
        expected.add("Tim");
        expected.add("Nancy");

        Set<String> result = government.getMembers();

        assertEquals(expected, result);
    }

    /**
     * Test of getMembers method, of class Government.
     */
    @Test
    public void testGetMembers_0args_govOnly() {
        System.out.println(
                "Testing the getMembers method with council members and affiliates.");

        String leader = "Leader";
        String viceLeader = "ViceLeader";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        government.addAffiliate("Steve");
        government.addAffiliate("Bill");
        government.addAffiliate("Billy");
        government.addAffiliate("Bobby");

        Set<String> expected = new HashSet<String>();
        expected.add(leader);
        expected.add(viceLeader);
        expected.add("Bob");
        expected.add("Tim");
        expected.add("Nancy");
        expected.add("Steve");
        expected.add("Bill");
        expected.add("Billy");
        expected.add("Bobby");

        Set<String> result = government.getMembers();

        assertEquals(expected, result);
    }

    /**
     * Test of getMembers method, of class Government.
     */
    @Test
    public void testGetMembers_0args_govAndDivs() {
        System.out.println(
                "Testing the getMembers method with government and division members.");

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = new HashSet<Division>(Arrays.asList(div1, div2));
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        String leader = "Leader";
        String viceLeader = "ViceLeader";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        when(div1.getManager()).thenReturn("Fred");
        when(div1.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Darren", "Phillip")));

        when(div2.getManager()).thenReturn("Marquise");
        when(div2.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Steven", "Nick")));

        government.addAffiliate("Steve");
        government.addAffiliate("Bill");
        government.addAffiliate("Billy");
        government.addAffiliate("Bobby");

        Set<String> expected = new HashSet<String>();
        expected.add(leader);
        expected.add(viceLeader);
        expected.add("Bob");
        expected.add("Tim");
        expected.add("Nancy");

        expected.add("Fred");
        expected.add("Darren");
        expected.add("Phillip");

        expected.add("Marquise");
        expected.add("Steven");
        expected.add("Nick");

        expected.add("Steve");
        expected.add("Bill");
        expected.add("Billy");
        expected.add("Bobby");

        Set<String> result = government.getMembers();

        assertEquals(expected, result);
    }

    /**
     * Test of getCouncilMembers method, of class Government.
     */
    @Test
    public void testGetCouncilMembers() {
        System.out.println("Testing the getCouncilMembers method.");

        String leader = "Leader";
        String viceLeader = "ViceLeader";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        Set<String> expected = new HashSet<String>();
        expected.add(leader);
        expected.add(viceLeader);
        expected.add("Bob");
        expected.add("Tim");
        expected.add("Nancy");

        Set<String> result = government.getCouncilMembers();

        assertEquals(expected, result);
    }

    /**
     * Test of getPositions method, of class Government.
     */
    @Test
    public void testGetPositions() {
        System.out.println("Testing the getPositions method.");

        Map<Position, Set<String>> expected =
                new EnumMap<Position, Set<String>>(Position.class);

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = new HashSet<Division>(Arrays.asList(div1, div2));
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        String leader = "Jeff";
        String viceLeader = "Spe";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        when(div1.getManager()).thenReturn("Fred");
        when(div1.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Darren", "Phillip")));

        when(div2.getManager()).thenReturn("Marquise");
        when(div2.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Steven", "Nick")));

        government.addAffiliate("Steve");
        government.addAffiliate("Bill");
        government.addAffiliate("Billy");
        government.addAffiliate("Bobby");

        MPlayer jeff = mock(MPlayer.class);
        MPlayer spe = mock(MPlayer.class);
        MPlayer bob = mock(MPlayer.class);
        MPlayer tim = mock(MPlayer.class);
        MPlayer nancy = mock(MPlayer.class);
        MPlayer fred = mock(MPlayer.class);
        MPlayer darren = mock(MPlayer.class);
        MPlayer phillip = mock(MPlayer.class);
        MPlayer marquise = mock(MPlayer.class);
        MPlayer steven = mock(MPlayer.class);
        MPlayer nick = mock(MPlayer.class);
        MPlayer steve = mock(MPlayer.class);
        MPlayer bill = mock(MPlayer.class);
        MPlayer billy = mock(MPlayer.class);
        MPlayer bobby = mock(MPlayer.class);

        when(Mafiacraft.getPlayer("Jeff")).thenReturn(jeff);
        when(Mafiacraft.getPlayer("Spe")).thenReturn(spe);
        when(Mafiacraft.getPlayer("Bob")).thenReturn(bob);
        when(Mafiacraft.getPlayer("Tim")).thenReturn(tim);
        when(Mafiacraft.getPlayer("Nancy")).thenReturn(nancy);
        when(Mafiacraft.getPlayer("Fred")).thenReturn(fred);
        when(Mafiacraft.getPlayer("Darren")).thenReturn(darren);
        when(Mafiacraft.getPlayer("Phillip")).thenReturn(phillip);
        when(Mafiacraft.getPlayer("Marquise")).thenReturn(marquise);
        when(Mafiacraft.getPlayer("Steven")).thenReturn(steven);
        when(Mafiacraft.getPlayer("Nick")).thenReturn(nick);
        when(Mafiacraft.getPlayer("Steve")).thenReturn(steve);
        when(Mafiacraft.getPlayer("Bill")).thenReturn(bill);
        when(Mafiacraft.getPlayer("Billy")).thenReturn(billy);
        when(Mafiacraft.getPlayer("Bobby")).thenReturn(bobby);

        Set<String> leaders = new HashSet<String>();
        leaders.add("Jeff");
        expected.put(Position.LEADER, leaders);

        Set<String> viceLeaders = new HashSet<String>();
        viceLeaders.add("Spe");
        expected.put(Position.VICE_LEADER, viceLeaders);

        Set<String> officers = new HashSet<String>();
        officers.add("Bob");
        officers.add("Tim");
        officers.add("Nancy");
        expected.put(Position.OFFICER, officers);

        Set<String> managers = new HashSet<String>();
        managers.add("Fred");
        managers.add("Marquise");
        expected.put(Position.MANAGER, managers);

        Set<String> workers = new HashSet<String>();
        workers.add("Darren");
        workers.add("Phillip");
        workers.add("Steven");
        workers.add("Nick");
        expected.put(Position.WORKER, workers);

        Set<String> affiliates = new HashSet<String>();
        affiliates.add("Steve");;
        affiliates.add("Bill");
        affiliates.add("Billy");
        affiliates.add("Bobby");
        expected.put(Position.AFFILIATE, affiliates);

        Set<String> none = new HashSet<String>();
        expected.put(Position.NONE, none);

        Map<Position, Set<String>> result = government.getPositions();

        assertEquals(expected, result);
    }

    /**
     * Test of getMembers method, of class Government.
     */
    @Test
    public void testGetMembers_Position_leader() {
        System.out.println(
                "Testing the getMembers (Position) method with a leader.");
        Position position = Position.LEADER;

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = new HashSet<Division>(Arrays.asList(div1, div2));
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        String leader = "Jeff";
        String viceLeader = "Spe";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        when(div1.getManager()).thenReturn("Fred");
        when(div1.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Darren", "Phillip")));

        when(div2.getManager()).thenReturn("Marquise");
        when(div2.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Steven", "Nick")));

        government.addAffiliate("Steve");
        government.addAffiliate("Bill");
        government.addAffiliate("Billy");
        government.addAffiliate("Bobby");

        MPlayer jeff = mock(MPlayer.class);
        MPlayer spe = mock(MPlayer.class);
        MPlayer bob = mock(MPlayer.class);
        MPlayer tim = mock(MPlayer.class);
        MPlayer nancy = mock(MPlayer.class);
        MPlayer fred = mock(MPlayer.class);
        MPlayer darren = mock(MPlayer.class);
        MPlayer phillip = mock(MPlayer.class);
        MPlayer marquise = mock(MPlayer.class);
        MPlayer steven = mock(MPlayer.class);
        MPlayer nick = mock(MPlayer.class);
        MPlayer steve = mock(MPlayer.class);
        MPlayer bill = mock(MPlayer.class);
        MPlayer billy = mock(MPlayer.class);
        MPlayer bobby = mock(MPlayer.class);

        when(Mafiacraft.getPlayer("Jeff")).thenReturn(jeff);
        when(Mafiacraft.getPlayer("Spe")).thenReturn(spe);
        when(Mafiacraft.getPlayer("Bob")).thenReturn(bob);
        when(Mafiacraft.getPlayer("Tim")).thenReturn(tim);
        when(Mafiacraft.getPlayer("Nancy")).thenReturn(nancy);
        when(Mafiacraft.getPlayer("Fred")).thenReturn(fred);
        when(Mafiacraft.getPlayer("Darren")).thenReturn(darren);
        when(Mafiacraft.getPlayer("Phillip")).thenReturn(phillip);
        when(Mafiacraft.getPlayer("Marquise")).thenReturn(marquise);
        when(Mafiacraft.getPlayer("Steven")).thenReturn(steven);
        when(Mafiacraft.getPlayer("Nick")).thenReturn(nick);
        when(Mafiacraft.getPlayer("Steve")).thenReturn(steve);
        when(Mafiacraft.getPlayer("Bill")).thenReturn(bill);
        when(Mafiacraft.getPlayer("Billy")).thenReturn(billy);
        when(Mafiacraft.getPlayer("Bobby")).thenReturn(bobby);

        Set<String> expected = new HashSet<String>(Arrays.asList("Jeff"));
        Set<String> result = government.getMembers(position);
        assertEquals(expected, result);
    }

    /**
     * Test of getMembers method, of class Government.
     */
    @Test
    public void testGetMembers_Position_viceLeader() {
        System.out.println(
                "Testing the getMembers (Position) method with a vice leader.");
        Position position = Position.VICE_LEADER;

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = new HashSet<Division>(Arrays.asList(div1, div2));
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        String leader = "Jeff";
        String viceLeader = "Spe";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        when(div1.getManager()).thenReturn("Fred");
        when(div1.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Darren", "Phillip")));

        when(div2.getManager()).thenReturn("Marquise");
        when(div2.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Steven", "Nick")));

        government.addAffiliate("Steve");
        government.addAffiliate("Bill");
        government.addAffiliate("Billy");
        government.addAffiliate("Bobby");

        MPlayer jeff = mock(MPlayer.class);
        MPlayer spe = mock(MPlayer.class);
        MPlayer bob = mock(MPlayer.class);
        MPlayer tim = mock(MPlayer.class);
        MPlayer nancy = mock(MPlayer.class);
        MPlayer fred = mock(MPlayer.class);
        MPlayer darren = mock(MPlayer.class);
        MPlayer phillip = mock(MPlayer.class);
        MPlayer marquise = mock(MPlayer.class);
        MPlayer steven = mock(MPlayer.class);
        MPlayer nick = mock(MPlayer.class);
        MPlayer steve = mock(MPlayer.class);
        MPlayer bill = mock(MPlayer.class);
        MPlayer billy = mock(MPlayer.class);
        MPlayer bobby = mock(MPlayer.class);

        when(Mafiacraft.getPlayer("Jeff")).thenReturn(jeff);
        when(Mafiacraft.getPlayer("Spe")).thenReturn(spe);
        when(Mafiacraft.getPlayer("Bob")).thenReturn(bob);
        when(Mafiacraft.getPlayer("Tim")).thenReturn(tim);
        when(Mafiacraft.getPlayer("Nancy")).thenReturn(nancy);
        when(Mafiacraft.getPlayer("Fred")).thenReturn(fred);
        when(Mafiacraft.getPlayer("Darren")).thenReturn(darren);
        when(Mafiacraft.getPlayer("Phillip")).thenReturn(phillip);
        when(Mafiacraft.getPlayer("Marquise")).thenReturn(marquise);
        when(Mafiacraft.getPlayer("Steven")).thenReturn(steven);
        when(Mafiacraft.getPlayer("Nick")).thenReturn(nick);
        when(Mafiacraft.getPlayer("Steve")).thenReturn(steve);
        when(Mafiacraft.getPlayer("Bill")).thenReturn(bill);
        when(Mafiacraft.getPlayer("Billy")).thenReturn(billy);
        when(Mafiacraft.getPlayer("Bobby")).thenReturn(bobby);

        Set<String> expected = new HashSet<String>(Arrays.asList("Spe"));
        Set<String> result = government.getMembers(position);
        assertEquals(expected, result);
    }

    /**
     * Test of getMembers method, of class Government.
     */
    @Test
    public void testGetMembers_Position_officer() {
        System.out.println(
                "Testing the getMembers (Position) method with officers.");
        Position position = Position.OFFICER;

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = new HashSet<Division>(Arrays.asList(div1, div2));
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        String leader = "Jeff";
        String viceLeader = "Spe";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        when(div1.getManager()).thenReturn("Fred");
        when(div1.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Darren", "Phillip")));

        when(div2.getManager()).thenReturn("Marquise");
        when(div2.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Steven", "Nick")));

        government.addAffiliate("Steve");
        government.addAffiliate("Bill");
        government.addAffiliate("Billy");
        government.addAffiliate("Bobby");

        MPlayer jeff = mock(MPlayer.class);
        MPlayer spe = mock(MPlayer.class);
        MPlayer bob = mock(MPlayer.class);
        MPlayer tim = mock(MPlayer.class);
        MPlayer nancy = mock(MPlayer.class);
        MPlayer fred = mock(MPlayer.class);
        MPlayer darren = mock(MPlayer.class);
        MPlayer phillip = mock(MPlayer.class);
        MPlayer marquise = mock(MPlayer.class);
        MPlayer steven = mock(MPlayer.class);
        MPlayer nick = mock(MPlayer.class);
        MPlayer steve = mock(MPlayer.class);
        MPlayer bill = mock(MPlayer.class);
        MPlayer billy = mock(MPlayer.class);
        MPlayer bobby = mock(MPlayer.class);

        when(Mafiacraft.getPlayer("Jeff")).thenReturn(jeff);
        when(Mafiacraft.getPlayer("Spe")).thenReturn(spe);
        when(Mafiacraft.getPlayer("Bob")).thenReturn(bob);
        when(Mafiacraft.getPlayer("Tim")).thenReturn(tim);
        when(Mafiacraft.getPlayer("Nancy")).thenReturn(nancy);
        when(Mafiacraft.getPlayer("Fred")).thenReturn(fred);
        when(Mafiacraft.getPlayer("Darren")).thenReturn(darren);
        when(Mafiacraft.getPlayer("Phillip")).thenReturn(phillip);
        when(Mafiacraft.getPlayer("Marquise")).thenReturn(marquise);
        when(Mafiacraft.getPlayer("Steven")).thenReturn(steven);
        when(Mafiacraft.getPlayer("Nick")).thenReturn(nick);
        when(Mafiacraft.getPlayer("Steve")).thenReturn(steve);
        when(Mafiacraft.getPlayer("Bill")).thenReturn(bill);
        when(Mafiacraft.getPlayer("Billy")).thenReturn(billy);
        when(Mafiacraft.getPlayer("Bobby")).thenReturn(bobby);

        Set<String> expected = new HashSet<String>(Arrays.asList("Bob", "Tim",
                "Nancy"));
        Set<String> result = government.getMembers(position);
        assertEquals(expected, result);
    }

    /**
     * Test of getMembers method, of class Government.
     */
    @Test
    public void testGetMembers_Position_manager() {
        System.out.println(
                "Testing the getMembers (Position) method with managers.");
        Position position = Position.MANAGER;

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = new HashSet<Division>(Arrays.asList(div1, div2));
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        String leader = "Jeff";
        String viceLeader = "Spe";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        when(div1.getManager()).thenReturn("Fred");
        when(div1.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Darren", "Phillip")));

        when(div2.getManager()).thenReturn("Marquise");
        when(div2.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Steven", "Nick")));

        government.addAffiliate("Steve");
        government.addAffiliate("Bill");
        government.addAffiliate("Billy");
        government.addAffiliate("Bobby");

        MPlayer jeff = mock(MPlayer.class);
        MPlayer spe = mock(MPlayer.class);
        MPlayer bob = mock(MPlayer.class);
        MPlayer tim = mock(MPlayer.class);
        MPlayer nancy = mock(MPlayer.class);
        MPlayer fred = mock(MPlayer.class);
        MPlayer darren = mock(MPlayer.class);
        MPlayer phillip = mock(MPlayer.class);
        MPlayer marquise = mock(MPlayer.class);
        MPlayer steven = mock(MPlayer.class);
        MPlayer nick = mock(MPlayer.class);
        MPlayer steve = mock(MPlayer.class);
        MPlayer bill = mock(MPlayer.class);
        MPlayer billy = mock(MPlayer.class);
        MPlayer bobby = mock(MPlayer.class);

        when(Mafiacraft.getPlayer("Jeff")).thenReturn(jeff);
        when(Mafiacraft.getPlayer("Spe")).thenReturn(spe);
        when(Mafiacraft.getPlayer("Bob")).thenReturn(bob);
        when(Mafiacraft.getPlayer("Tim")).thenReturn(tim);
        when(Mafiacraft.getPlayer("Nancy")).thenReturn(nancy);
        when(Mafiacraft.getPlayer("Fred")).thenReturn(fred);
        when(Mafiacraft.getPlayer("Darren")).thenReturn(darren);
        when(Mafiacraft.getPlayer("Phillip")).thenReturn(phillip);
        when(Mafiacraft.getPlayer("Marquise")).thenReturn(marquise);
        when(Mafiacraft.getPlayer("Steven")).thenReturn(steven);
        when(Mafiacraft.getPlayer("Nick")).thenReturn(nick);
        when(Mafiacraft.getPlayer("Steve")).thenReturn(steve);
        when(Mafiacraft.getPlayer("Bill")).thenReturn(bill);
        when(Mafiacraft.getPlayer("Billy")).thenReturn(billy);
        when(Mafiacraft.getPlayer("Bobby")).thenReturn(bobby);

        Set<String> expected = new HashSet<String>(Arrays.asList("Fred",
                "Marquise"));
        Set<String> result = government.getMembers(position);
        assertEquals(expected, result);
    }

    /**
     * Test of getMembers method, of class Government.
     */
    @Test
    public void testGetMembers_Position_worker() {
        System.out.println(
                "Testing the getMembers (Position) method with workers.");
        Position position = Position.WORKER;

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = new HashSet<Division>(Arrays.asList(div1, div2));
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        String leader = "Jeff";
        String viceLeader = "Spe";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        when(div1.getManager()).thenReturn("Fred");
        when(div1.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Darren", "Phillip")));

        when(div2.getManager()).thenReturn("Marquise");
        when(div2.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Steven", "Nick")));

        government.addAffiliate("Steve");
        government.addAffiliate("Bill");
        government.addAffiliate("Billy");
        government.addAffiliate("Bobby");

        MPlayer jeff = mock(MPlayer.class);
        MPlayer spe = mock(MPlayer.class);
        MPlayer bob = mock(MPlayer.class);
        MPlayer tim = mock(MPlayer.class);
        MPlayer nancy = mock(MPlayer.class);
        MPlayer fred = mock(MPlayer.class);
        MPlayer darren = mock(MPlayer.class);
        MPlayer phillip = mock(MPlayer.class);
        MPlayer marquise = mock(MPlayer.class);
        MPlayer steven = mock(MPlayer.class);
        MPlayer nick = mock(MPlayer.class);
        MPlayer steve = mock(MPlayer.class);
        MPlayer bill = mock(MPlayer.class);
        MPlayer billy = mock(MPlayer.class);
        MPlayer bobby = mock(MPlayer.class);

        when(Mafiacraft.getPlayer("Jeff")).thenReturn(jeff);
        when(Mafiacraft.getPlayer("Spe")).thenReturn(spe);
        when(Mafiacraft.getPlayer("Bob")).thenReturn(bob);
        when(Mafiacraft.getPlayer("Tim")).thenReturn(tim);
        when(Mafiacraft.getPlayer("Nancy")).thenReturn(nancy);
        when(Mafiacraft.getPlayer("Fred")).thenReturn(fred);
        when(Mafiacraft.getPlayer("Darren")).thenReturn(darren);
        when(Mafiacraft.getPlayer("Phillip")).thenReturn(phillip);
        when(Mafiacraft.getPlayer("Marquise")).thenReturn(marquise);
        when(Mafiacraft.getPlayer("Steven")).thenReturn(steven);
        when(Mafiacraft.getPlayer("Nick")).thenReturn(nick);
        when(Mafiacraft.getPlayer("Steve")).thenReturn(steve);
        when(Mafiacraft.getPlayer("Bill")).thenReturn(bill);
        when(Mafiacraft.getPlayer("Billy")).thenReturn(billy);
        when(Mafiacraft.getPlayer("Bobby")).thenReturn(bobby);

        Set<String> expected = new HashSet<String>(Arrays.asList("Darren",
                "Phillip", "Steven", "Nick"));
        Set<String> result = government.getMembers(position);
        assertEquals(expected, result);
    }

    /**
     * Test of getMembers method, of class Government.
     */
    @Test
    public void testGetMembers_Position_affiliate() {
        System.out.println(
                "Testing the getMembers (Position) method with affiliates.");
        Position position = Position.AFFILIATE;

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = new HashSet<Division>(Arrays.asList(div1, div2));
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        String leader = "Jeff";
        String viceLeader = "Spe";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        when(div1.getManager()).thenReturn("Fred");
        when(div1.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Darren", "Phillip")));

        when(div2.getManager()).thenReturn("Marquise");
        when(div2.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Steven", "Nick")));

        government.addAffiliate("Steve");
        government.addAffiliate("Bill");
        government.addAffiliate("Billy");
        government.addAffiliate("Bobby");

        MPlayer jeff = mock(MPlayer.class);
        MPlayer spe = mock(MPlayer.class);
        MPlayer bob = mock(MPlayer.class);
        MPlayer tim = mock(MPlayer.class);
        MPlayer nancy = mock(MPlayer.class);
        MPlayer fred = mock(MPlayer.class);
        MPlayer darren = mock(MPlayer.class);
        MPlayer phillip = mock(MPlayer.class);
        MPlayer marquise = mock(MPlayer.class);
        MPlayer steven = mock(MPlayer.class);
        MPlayer nick = mock(MPlayer.class);
        MPlayer steve = mock(MPlayer.class);
        MPlayer bill = mock(MPlayer.class);
        MPlayer billy = mock(MPlayer.class);
        MPlayer bobby = mock(MPlayer.class);

        when(Mafiacraft.getPlayer("Jeff")).thenReturn(jeff);
        when(Mafiacraft.getPlayer("Spe")).thenReturn(spe);
        when(Mafiacraft.getPlayer("Bob")).thenReturn(bob);
        when(Mafiacraft.getPlayer("Tim")).thenReturn(tim);
        when(Mafiacraft.getPlayer("Nancy")).thenReturn(nancy);
        when(Mafiacraft.getPlayer("Fred")).thenReturn(fred);
        when(Mafiacraft.getPlayer("Darren")).thenReturn(darren);
        when(Mafiacraft.getPlayer("Phillip")).thenReturn(phillip);
        when(Mafiacraft.getPlayer("Marquise")).thenReturn(marquise);
        when(Mafiacraft.getPlayer("Steven")).thenReturn(steven);
        when(Mafiacraft.getPlayer("Nick")).thenReturn(nick);
        when(Mafiacraft.getPlayer("Steve")).thenReturn(steve);
        when(Mafiacraft.getPlayer("Bill")).thenReturn(bill);
        when(Mafiacraft.getPlayer("Billy")).thenReturn(billy);
        when(Mafiacraft.getPlayer("Bobby")).thenReturn(bobby);

        Set<String> expected = new HashSet<String>(Arrays.asList("Steve", "Bill",
                "Billy", "Bobby"));
        Set<String> result = government.getMembers(position);
        assertEquals(expected, result);
    }

    /**
     * Test of createDivision method, of class Government.
     */
    @Test
    public void testCreateDivision() {
        System.out.println("Testing the createDivision method.");

        Division div = mock(Division.class);
        when(governmentManager.createDivision(government)).thenReturn(div);

        Division expected = div;
        Division result = government.createDivision();
        assertEquals(expected, result);
    }

    /**
     * Test of getCouncilMemberCount method, of class Government.
     */
    @Test
    public void testGetCouncilMemberCount() {
        System.out.println("Testing the getCouncilMemberCount method.");

        String leader = "Leader";
        String viceLeader = "ViceLeader";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        int expected = 5;
        int result = government.getCouncilMemberCount();
        assertEquals(expected, result);
    }

    /**
     * Test of getCouncilMemberCount method, of class Government.
     */
    @Test
    public void testGetCouncilMemberCount_noOfficers() {
        System.out.println(
                "Testing the getCouncilMemberCount method with no officers.");

        String leader = "Leader";
        String viceLeader = "ViceLeader";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);

        int expected = 2;
        int result = government.getCouncilMemberCount();
        assertEquals(expected, result);
    }

    /**
     * Test of getCouncilMemberCount method, of class Government.
     */
    @Test
    public void testGetCouncilMemberCount_noLeader() {
        System.out.println(
                "Testing the getCouncilMemberCount method with no leader.");

        String viceLeader = "ViceLeader";
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        int expected = 4;
        int result = government.getCouncilMemberCount();
        assertEquals(expected, result);
    }

    /**
     * Test of getCouncilMemberCount method, of class Government.
     */
    @Test
    public void testGetCouncilMemberCount_noViceLeader() {
        System.out.println(
                "Testing the getCouncilMemberCount method with no vice leader.");

        String leader = "Leader";
        government.setLeader(leader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        int expected = 4;
        int result = government.getCouncilMemberCount();
        assertEquals(expected, result);
    }

    /**
     * Test of getCouncilMemberCount method, of class Government.
     */
    @Test
    public void testGetCouncilMemberCount_noLeaderOrViceLeader() {
        System.out.println(
                "Testing the getCouncilMemberCount method with no leader or vice leader.");

        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("RedBirdAlley");

        int expected = 3;
        int result = government.getCouncilMemberCount();
        assertEquals(expected, result);
    }

    /**
     * Test of getOnlineMembers method, of class Government.
     */
    @Test
    public void testGetOnlineMembers_0args_govAndDivs() {
        System.out.println("Testing the getOnlineMembers method.");

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = new HashSet<Division>(Arrays.asList(div1, div2));
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        String leader = "Jeff";
        String viceLeader = "Spe";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        when(div1.getManager()).thenReturn("Fred");
        when(div1.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Darren", "Phillip")));

        when(div2.getManager()).thenReturn("Marquise");
        when(div2.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                "Steven", "Nick")));

        government.addAffiliate("Steve");
        government.addAffiliate("Bill");
        government.addAffiliate("Billy");
        government.addAffiliate("Bobby");

        MPlayer jeff = mock(MPlayer.class);
        MPlayer spe = mock(MPlayer.class);
        MPlayer bob = mock(MPlayer.class);
        MPlayer tim = mock(MPlayer.class);
        MPlayer nancy = mock(MPlayer.class);
        MPlayer fred = mock(MPlayer.class);
        MPlayer darren = mock(MPlayer.class);
        MPlayer phillip = mock(MPlayer.class);
        MPlayer marquise = mock(MPlayer.class);
        MPlayer steven = mock(MPlayer.class);
        MPlayer nick = mock(MPlayer.class);
        MPlayer steve = mock(MPlayer.class);
        MPlayer bill = mock(MPlayer.class);
        MPlayer billy = mock(MPlayer.class);
        MPlayer bobby = mock(MPlayer.class);

        when(jeff.getName()).thenReturn("Jeff");
        when(spe.getName()).thenReturn("Spe");
        when(bob.getName()).thenReturn("Bob");
        when(tim.getName()).thenReturn("Tim");
        when(nancy.getName()).thenReturn("Nancy");
        when(fred.getName()).thenReturn("Fred");
        when(darren.getName()).thenReturn("Darren");
        when(phillip.getName()).thenReturn("Phillip");
        when(marquise.getName()).thenReturn("Marquise");
        when(steven.getName()).thenReturn("Steven");
        when(nick.getName()).thenReturn("Nick");
        when(steve.getName()).thenReturn("Steve");
        when(bill.getName()).thenReturn("Bill");
        when(billy.getName()).thenReturn("Billy");
        when(bobby.getName()).thenReturn("Bobby");

        when(Mafiacraft.getPlayer("Jeff")).thenReturn(jeff);
        when(Mafiacraft.getPlayer("Spe")).thenReturn(spe);
        when(Mafiacraft.getPlayer("Bob")).thenReturn(bob);
        when(Mafiacraft.getPlayer("Tim")).thenReturn(tim);
        when(Mafiacraft.getPlayer("Nancy")).thenReturn(nancy);
        when(Mafiacraft.getPlayer("Fred")).thenReturn(fred);
        when(Mafiacraft.getPlayer("Darren")).thenReturn(darren);
        when(Mafiacraft.getPlayer("Phillip")).thenReturn(phillip);
        when(Mafiacraft.getPlayer("Marquise")).thenReturn(marquise);
        when(Mafiacraft.getPlayer("Steven")).thenReturn(steven);
        when(Mafiacraft.getPlayer("Nick")).thenReturn(nick);
        when(Mafiacraft.getPlayer("Steve")).thenReturn(steve);
        when(Mafiacraft.getPlayer("Bill")).thenReturn(bill);
        when(Mafiacraft.getPlayer("Billy")).thenReturn(billy);
        when(Mafiacraft.getPlayer("Bobby")).thenReturn(bobby);

        Set<MPlayer> onlinePlayers = new HashSet<MPlayer>();
        onlinePlayers.add(jeff);
        onlinePlayers.add(spe);
        onlinePlayers.add(bob);
        onlinePlayers.add(tim);
        onlinePlayers.add(nancy);
        onlinePlayers.add(fred);
        onlinePlayers.add(darren);
        onlinePlayers.add(phillip);
        onlinePlayers.add(marquise);
        onlinePlayers.add(steven);
        onlinePlayers.add(nick);
        onlinePlayers.add(steve);
        onlinePlayers.add(bill);
        onlinePlayers.add(billy);
        onlinePlayers.add(bobby);

        Set<MPlayer> expected = new HashSet<MPlayer>(onlinePlayers);

        //Fake players now!
        MPlayer notin = mock(MPlayer.class);
        when(notin.getName()).thenReturn("notin");
        onlinePlayers.add(notin);
        MPlayer albireox = mock(MPlayer.class);
        when(albireox.getName()).thenReturn("albireox");
        onlinePlayers.add(albireox);
        MPlayer afforess = mock(MPlayer.class);
        when(afforess.getName()).thenReturn("Afforess");
        onlinePlayers.add(afforess);

        //Put them in
        when(Mafiacraft.getOnlinePlayers()).thenReturn(onlinePlayers);

        Set<MPlayer> result = government.getOnlineMembers();

        assertEquals(expected, result);
    }

    /**
     * Test of getPosition method, of class Government.
     */
    @Test
    public void testGetPosition_String_Leader() {
        System.out.println("Testing the getPosition method with a leader.");

        String player = "Bob";
        government.setLeader(player);

        Position expected = Position.LEADER;
        Position result = government.getPosition(player);
        assertEquals(expected, result);
    }

    /**
     * Test of getPosition method, of class Government.
     */
    @Test
    public void testGetPosition_String_ViceLeader() {
        System.out.println("Testing the getPosition method with a vice leader.");

        String player = "Bob";
        government.setViceLeader(player);

        Position expected = Position.VICE_LEADER;
        Position result = government.getPosition(player);
        assertEquals(expected, result);
    }

    /**
     * Test of getPosition method, of class Government.
     */
    @Test
    public void testGetPosition_String_Officer() {
        System.out.println("Testing the getPosition method with an officer.");

        String player = "Bob";
        government.addOfficer(player);

        Position expected = Position.OFFICER;
        Position result = government.getPosition(player);
        assertEquals(expected, result);
    }

    /**
     * Test of getPosition method, of class Government.
     */
    @Test
    public void testGetPosition_String_Affiliate() {
        System.out.println("Testing the getPosition method with an affiliate.");

        String player = "Bob";
        government.addAffiliate(player);

        Position expected = Position.AFFILIATE;
        Position result = government.getPosition(player);
        assertEquals(expected, result);
    }

    /**
     * Test of getPosition method, of class Government.
     */
    @Test
    public void testGetPosition_String_Manager() {
        System.out.println("Testing the getPosition method with a manager.");

        String player = "Bob";

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        when(div1.getManager()).thenReturn(player);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = new HashSet<Division>(Arrays.asList(div1, div2));
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        Position expected = Position.MANAGER;
        Position result = government.getPosition(player);
        assertEquals(expected, result);
    }

    /**
     * Test of getPosition method, of class Government.
     */
    @Test
    public void testGetPosition_String_Worker() {
        System.out.println("Testing the getPosition method with a manager.");

        String player = "Bob";

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        Set<String> workerSet = new HashSet<String>();
        workerSet.add(player);
        when(div1.getWorkers()).thenReturn(workerSet);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = new HashSet<Division>();
        divs.add(div1);
        divs.add(div2);

        when(governmentManager.getDivisions(government)).thenReturn(divs);

        Position expected = Position.WORKER;
        Position result = government.getPosition(player);
        assertEquals(expected, result);
    }

    /**
     * Test of getPosition method, of class Government.
     */
    @Test
    public void testGetPosition_MPlayer() {
        System.out.println("Testing the getPosition method with an MPlayer.");

        MPlayer player = mock(MPlayer.class);
        String playerName = "Bob";
        when(player.getName()).thenReturn(playerName);

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        when(div1.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList(
                playerName)));
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = new HashSet<Division>(Arrays.asList(div1, div2));
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        Position expected = Position.WORKER;
        Position result = government.getPosition(player);
        assertEquals(expected, result);
    }

    /**
     * Test of setHq method, of class Government.
     */
    @Test
    public void testSetHq() {
        System.out.println("Testing the setHq method.");

        MWorld world = mock(MWorld.class);
        MPoint hq = new MPoint(world, 0, 2, 4);

        government.setHq(hq);

        MPoint expected = hq;
        MPoint result = government.getHq();
        assertEquals(expected, result);
    }

    /**
     * Test of setLeader method, of class Government.
     */
    @Test
    public void testSetLeader_MPlayer() {
        System.out.println("Testing the setLeader method with a MPlayer.");

        MPlayer leader = mock(MPlayer.class);
        when(leader.getName()).thenReturn("Tim");
        government.setLeader(leader);

        String expected = "Tim";
        String result = government.getLeader();
        assertEquals(expected, result);
    }

    /**
     * Test of setLeader method, of class Government.
     */
    @Test
    public void testSetLeader_String() {
        System.out.println("Testing the setLeader method with a string.");

        String leader = "Tim";
        government.setLeader(leader);

        String expected = leader;
        String result = government.getLeader();
        assertEquals(expected, result);
    }

    /**
     * Test of setViceLeader method, of class Government.
     */
    @Test
    public void testSetViceLeader_MPlayer() {
        System.out.println("Testing the setViceLeader method with an MPlayer.");

        MPlayer viceLeader = mock(MPlayer.class);
        when(viceLeader.getName()).thenReturn("prince");
        government.setViceLeader(viceLeader);

        String expected = "prince";
        String result = government.getViceLeader();
        assertEquals(expected, result);
    }

    /**
     * Test of setViceLeader method, of class Government.
     */
    @Test
    public void testSetViceLeader_String() {
        System.out.println("Testing the setViceLeader method with a string.");

        String viceLeader = "bob";
        government.setViceLeader(viceLeader);

        String expected = viceLeader;
        String result = government.getViceLeader();
        assertEquals(expected, result);
    }

    /**
     * Test of getAffiliates method, of class Government.
     */
    @Test
    public void testGetAffiliates() {
        System.out.println("getAffiliates");

        government.setLeader("Billy");
        government.addAffiliate("Jim");
        government.addAffiliate("John");
        government.addAffiliate("Phil");

        Set<String> expected = new HashSet<String>();
        expected.add("Jim");
        expected.add("John");
        expected.add("Phil");

        Set<String> result = government.getAffiliates();
        assertEquals(expected, result);
    }

    /**
     * Test of getOfficers method, of class Government.
     */
    @Test
    public void testGetOfficers() {
        System.out.println("Testing the getOfficers method.");

        government.setLeader("Billy");
        government.addOfficer("Jim");
        government.addOfficer("John");
        government.addOfficer("Phil");

        Set<String> expected = new HashSet<String>();
        expected.add("Jim");
        expected.add("John");
        expected.add("Phil");

        Set<String> result = government.getOfficers();
        assertEquals(expected, result);
    }

    /**
     * Test of getMaxGovernmentLand method with divs.
     */
    @Test
    public void testGetMaxGovernmentLand_withDivs() {
        System.out.println("Testing the getMaxGovernmentLand method.");

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = new HashSet<Division>(Arrays.asList(div1, div2));
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        int expected = 16;
        int result = government.getMaxGovernmentLand();
        assertEquals(expected, result);
    }

    /**
     * Test of getMaxGovernmentLand method without divs.
     */
    @Test
    public void testGetMaxGovernmentLand_withoutDivs() {
        System.out.println("Testing the getMaxGovernmentLand method.");

        int expected = 12;
        int result = government.getMaxGovernmentLand();
        assertEquals(expected, result);
    }

    /**
     * Test of getLand method, of class Government.
     */
    @Test
    public void testGetLand() {
        System.out.println("Testing the getLand method.");

        int expected = 201;
        int result = government.getLand();
        assertEquals(expected, result);
    }

    /**
     * Test of setLand method, of class Government.
     */
    @Test
    public void testSetLand() {
        System.out.println("Testing the setLand method.");

        int expected = 201;
        int result = government.getLand();
        assertEquals(expected, result);
    }

    /**
     * Test of incLand method, of class Government.
     */
    @Test
    public void testIncLand() {
        System.out.println("Testing the incLand method.");

        int expected = 201;
        int result = government.getLand();
        assertEquals(expected, result);

        government.incLand();
        expected = 202;
        result = government.getLand();
        assertEquals(expected, result);

        government.incLand();
        expected = 203;
        result = government.getLand();
        assertEquals(expected, result);
    }

    /**
     * Test of decLand method, of class Government.
     */
    @Test
    public void testDecLand() {
        System.out.println("Testing the decLand method.");

        int expected = 201;
        int result = government.getLand();
        assertEquals(expected, result);

        government.decLand();
        expected = 200;
        result = government.getLand();
        assertEquals(expected, result);

        government.decLand();
        expected = 199;
        result = government.getLand();
        assertEquals(expected, result);
    }

    /**
     * Test of getHq method, of class Government.
     */
    @Test
    public void testGetHq() {
        System.out.println("Testing the getHq method.");

        MWorld world = mock(MWorld.class);
        MPoint hq = new MPoint(world, 0, 2, 4);

        government.setHq(hq);

        MPoint expected = hq;
        MPoint result = government.getHq();
        assertEquals(expected, result);
    }

    /**
     * Test of getCouncilMembersAsMPlayers method, of class Government.
     */
    @Test
    public void testGetCouncilMembersAsMPlayers() {
        System.out.println("Testing the getCouncilMembersAsMPlayers method.");

        String leader = "Frank";
        String viceLeader = "Joe";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        MPlayer frank = mock(MPlayer.class);
        MPlayer joe = mock(MPlayer.class);
        MPlayer bob = mock(MPlayer.class);
        MPlayer tim = mock(MPlayer.class);
        MPlayer nancy = mock(MPlayer.class);

        when(Mafiacraft.getPlayer("Frank")).thenReturn(frank);
        when(Mafiacraft.getPlayer("Joe")).thenReturn(joe);
        when(Mafiacraft.getPlayer("Bob")).thenReturn(bob);
        when(Mafiacraft.getPlayer("Tim")).thenReturn(tim);
        when(Mafiacraft.getPlayer("Nancy")).thenReturn(nancy);

        Set<MPlayer> expected = new HashSet<MPlayer>();
        expected.add(frank);
        expected.add(joe);
        expected.add(bob);
        expected.add(tim);
        expected.add(nancy);

        Set<MPlayer> result = government.getCouncilMembersAsMPlayers();

        assertEquals(expected, result);
    }

    /**
     * Test of canHaveMoreDivisions method, of class Government.
     */
    @Test
    public void testCanHaveMoreDivisions() {
        System.out.println("Testing the canHaveMoreDivisions method.");

        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = new HashSet<Division>(Arrays.asList(div1, div2));
        when(governmentManager.getDivisions(government)).thenReturn(divs);
    }

    /**
     * Test of getChatPrefix method, of class Government.
     */
    @Test
    public void testGetChatPrefix() {
        System.out.println("Testing the getChatPrefix method.");

        government.setType(GovType.MAFIA);

        String expected = GovType.MAFIA.getColor() + "[GovarnMunt]";
        String result = government.getChatPrefix();
        assertEquals(expected, result);
    }

    /**
     * Test of broadcastMessage method, of class Government.
     */
    @Test
    public void testBroadcastMessage_councilOnly() {
        System.out.println(
                "Testing the broadcastMessage method with only a council.");

        String message = "This is a " + MsgColor.INFO_HILIGHT + "TEST";
        String expectedMessage = MsgColor.INFO_GOV + message;

        //Setup council
        String leader = "Frank";
        String viceLeader = "Joe";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        MPlayer frank = mock(MPlayer.class);
        MPlayer joe = mock(MPlayer.class);
        MPlayer bob = mock(MPlayer.class);
        MPlayer tim = mock(MPlayer.class);
        MPlayer nancy = mock(MPlayer.class);

        when(frank.getName()).thenReturn("Frank");
        when(joe.getName()).thenReturn("Joe");
        when(bob.getName()).thenReturn("Bob");
        when(tim.getName()).thenReturn("Tim");
        when(nancy.getName()).thenReturn("Nancy");

        when(Mafiacraft.getPlayer("Frank")).thenReturn(frank);
        when(Mafiacraft.getPlayer("Joe")).thenReturn(joe);
        when(Mafiacraft.getPlayer("Bob")).thenReturn(bob);
        when(Mafiacraft.getPlayer("Tim")).thenReturn(tim);
        when(Mafiacraft.getPlayer("Nancy")).thenReturn(nancy);

        Set<MPlayer> onlineMembers = new HashSet<MPlayer>();
        onlineMembers.add(frank);
        onlineMembers.add(joe);
        onlineMembers.add(bob);
        onlineMembers.add(tim);
        onlineMembers.add(nancy);
        when(Mafiacraft.getOnlinePlayers()).thenReturn(onlineMembers);

        //Broadcast
        government.broadcastMessage(message);

        //Verify
        verify(frank).sendMessage(expectedMessage);
        verify(joe).sendMessage(expectedMessage);
        verify(bob).sendMessage(expectedMessage);
        verify(tim).sendMessage(expectedMessage);
        verify(nancy).sendMessage(expectedMessage);
    }

    /**
     * Test of getEntryMessage method, of class Government.
     */
    @Test
    public void testGetEntryMessage() {
        System.out.println("Testing the getEntryMessage method.");

        String expected = "GovarnMunt";
        String result = government.getEntryMessage();
        assertEquals(expected, result);
    }

}
