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

import net.voxton.mafiacraft.core.gov.Government;
import net.voxton.mafiacraft.core.gov.GovType;
import net.voxton.mafiacraft.core.gov.GovernmentManager;
import net.voxton.mafiacraft.core.gov.Position;
import net.voxton.mafiacraft.core.gov.Division;
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
        System.out.println("Testing the getMembers method with only council members.");

        String leader = "Leader";
        String viceLeader = "ViceLeader";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        Set<String> expectedSet = new HashSet<String>();
        expectedSet.add(leader);
        expectedSet.add(viceLeader);
        expectedSet.add("Bob");
        expectedSet.add("Tim");
        expectedSet.add("Nancy");

        Set<String> resultSet = government.getMembers();

        //Order to satisfy
        Object[] expected = expectedSet.toArray();
        Arrays.sort(expected);
        Object[] result = resultSet.toArray();
        Arrays.sort(result);

        assertArrayEquals(expected, result);
    }

    /**
     * Test of getMembers method, of class Government.
     */
    @Test
    public void testGetMembers_0args_govOnly() {
        System.out.println("Testing the getMembers method with council members and affiliates.");

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

        Set<String> expectedSet = new HashSet<String>();
        expectedSet.add(leader);
        expectedSet.add(viceLeader);
        expectedSet.add("Bob");
        expectedSet.add("Tim");
        expectedSet.add("Nancy");
        expectedSet.add("Steve");
        expectedSet.add("Bill");
        expectedSet.add("Billy");
        expectedSet.add("Bobby");

        Set<String> resultSet = government.getMembers();

        //Order to satisfy
        Object[] expected = expectedSet.toArray();
        Arrays.sort(expected);
        Object[] result = resultSet.toArray();
        Arrays.sort(result);

        assertArrayEquals(expected, result);
    }

    /**
     * Test of getMembers method, of class Government.
     */
    @Test
    public void testGetMembers_0args_govAndDivs() {
        System.out.println("Testing the getMembers method with government and division members.");
        
        //Mock divisions
        Division div1 = mock(Division.class);
        when(div1.getLand()).thenReturn(20);
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = Arrays.asSet(div1, div2);
        when(governmentManager.getDivisions(government)).thenReturn(divs);
        
        String leader = "Leader";
        String viceLeader = "ViceLeader";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");
        
        when(div1.getManager()).thenReturn("Fred");
        when(div1.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList("Darren", "Phillip")));
        
        when(div2.getManager()).thenReturn("Marquise");
        when(div2.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList("Steven", "Nick")));
        
        government.addAffiliate("Steve");
        government.addAffiliate("Bill");
        government.addAffiliate("Billy");
        government.addAffiliate("Bobby");

        Set<String> expectedSet = new HashSet<String>();
        expectedSet.add(leader);
        expectedSet.add(viceLeader);
        expectedSet.add("Bob");
        expectedSet.add("Tim");
        expectedSet.add("Nancy");
        
        expectedSet.add("Fred");
        expectedSet.add("Darren");
        expectedSet.add("Phillip");
        
        expectedSet.add("Marquise");
        expectedSet.add("Steven");
        expectedSet.add("Nick");
        
        expectedSet.add("Steve");
        expectedSet.add("Bill");
        expectedSet.add("Billy");
        expectedSet.add("Bobby");

        Set<String> resultSet = government.getMembers();

        //Order to satisfy
        Object[] expected = expectedSet.toArray();
        Arrays.sort(expected);
        Object[] result = resultSet.toArray();
        Arrays.sort(result);

        assertArrayEquals(expected, result);
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

        Set<String> expectedSet = new HashSet<String>();
        expectedSet.add(leader);
        expectedSet.add(viceLeader);
        expectedSet.add("Bob");
        expectedSet.add("Tim");
        expectedSet.add("Nancy");

        Set<String> resultSet = government.getCouncilMembers();

        //Order to satisfy
        Object[] expected = expectedSet.toArray();
        Arrays.sort(expected);
        Object[] result = resultSet.toArray();
        Arrays.sort(result);

        assertArrayEquals(expected, result);
    }

    /**
     * Test of getPositions method, of class Government.
     */
    @Test
    public void testGetPositions() {
        System.out.println("Testing the getPositions method.");
        
        Map<Position, Set<String>> expected = null;
        Map<Position, Set<String>> result = government.getPositions();
        assertEquals(expected, result);
    }

    /**
     * Test of getMembers method, of class Government.
     */
    @Test
    public void testGetMembers_Position() {
        System.out.println("getMembers");
        Position position = null;
        Government instance = null;
        Set expResult = null;
        Set result = instance.getMembers(position);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDivision method, of class Government.
     */
    @Test
    public void testGetDivision_String() {
        System.out.println("getDivision");
        String player = "";
        Government instance = null;
        Division expResult = null;
        Division result = instance.getDivision(player);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDivision method, of class Government.
     */
    @Test
    public void testGetDivision_MPlayer() {
        System.out.println("getDivision");
        MPlayer player = null;
        Government instance = null;
        Division expResult = null;
        Division result = instance.getDivision(player);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDivisions method, of class Government.
     */
    @Test
    public void testGetDivisions() {
        System.out.println("getDivisions");
        Government instance = null;
        Set expResult = null;
        Set result = instance.getDivisions();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDivisionByName method, of class Government.
     */
    @Test
    public void testGetDivisionByName() {
        System.out.println("getDivisionByName");
        String name = "";
        Government instance = null;
        Division expResult = null;
        Division result = instance.getDivisionByName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
     * Test of getMemberCount method, of class Government.
     */
    @Test
    public void testGetMemberCount_0args() {
        System.out.println("getMemberCount");
        Government instance = null;
        int expResult = 0;
        int result = instance.getMemberCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMemberCount method, of class Government.
     */
    @Test
    public void testGetMemberCount_Position() {
        System.out.println("getMemberCount");
        Position position = null;
        Government instance = null;
        int expResult = 0;
        int result = instance.getMemberCount(position);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        when(div1.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList("Darren", "Phillip")));
        
        when(div2.getManager()).thenReturn("Marquise");
        when(div2.getWorkers()).thenReturn(new HashSet<String>(Arrays.asList("Steven", "Nick")));
        
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
        
        Set<MPlayer> onlineMembers = new HashSet<MPlayer>();
        onlineMembers.add(jeff);
        onlineMembers.add(spe);
        onlineMembers.add(bob);
        onlineMembers.add(tim);
        onlineMembers.add(nancy);
        onlineMembers.add(fred);
        onlineMembers.add(darren);
        onlineMembers.add(phillip);
        onlineMembers.add(marquise);
        onlineMembers.add(steven);
        onlineMembers.add(nick);
        onlineMembers.add(steve);
        onlineMembers.add(bill);
        onlineMembers.add(billy);
        onlineMembers.add(bobby);
        
        Set<MPlayer> expectedSet = new HashSet<MPlayer>(onlineMembers);
        
        //Fake players now!
        MPlayer notin = mock(MPlayer.class);
        when(notin.getName()).thenReturn("notin");
        onlineMembers.add(notin);
        MPlayer albireox = mock(MPlayer.class);
        when(albireox.getName()).thenReturn("albireox");
        onlineMembers.add(albireox);
        MPlayer afforess = mock(MPlayer.class);
        when(afforess.getName()).thenReturn("Afforess");
        onlineMembers.add(afforess);
        
        Set<MPlayer> resultSet = government.getOnlineMembers();
        
        assertEquals(expectedSet, resultSet);
        
        //Order to satisfy
//        Object[] expected = expectedSet.toArray();
//        Arrays.sort(expected);
//        Object[] result = resultSet.toArray();
//        Arrays.sort(result);
//
//        assertArrayEquals(expected, result);
    }

    /**
     * Test of getOnlineMembers method, of class Government.
     */
    @Test
    public void testGetOnlineMembers_Position() {
        System.out.println("getOnlineMembers");
        Position position = null;
        Government instance = null;
        Set expResult = null;
        Set result = instance.getOnlineMembers(position);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOnlineMemberCount method, of class Government.
     */
    @Test
    public void testGetOnlineMemberCount() {
        System.out.println("getOnlineMemberCount");
        Government instance = null;
        int expResult = 0;
        int result = instance.getOnlineMemberCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of canHaveMore method, of class Government.
     */
    @Test
    public void testCanHaveMore() {
        System.out.println("canHaveMore");
        Position position = null;
        Government instance = null;
        boolean expResult = false;
        boolean result = instance.canHaveMore(position);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of canHaveLess method, of class Government.
     */
    @Test
    public void testCanHaveLess() {
        System.out.println("canHaveLess");
        Position position = null;
        Government instance = null;
        boolean expResult = false;
        boolean result = instance.canHaveLess(position);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        when(div1.getWorkers()).thenReturn(Arrays.asSet(playerName));
        Division div2 = mock(Division.class);
        when(div2.getLand()).thenReturn(40);
        Set<Division> divs = Arrays.asSet(div1, div2);
        when(governmentManager.getDivisions(government)).thenReturn(divs);

        Position expected = Position.WORKER;
        Position result = government.getPosition(player);
        assertEquals(expected, result);
    }

    /**
     * Test of unseatLeader method, of class Government.
     */
    @Test
    public void testUnseatLeader() {
        System.out.println("unseatLeader");
        Government instance = null;
        Government expResult = null;
        Government result = instance.unseatLeader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of unseatViceLeader method, of class Government.
     */
    @Test
    public void testUnseatViceLeader() {
        System.out.println("unseatViceLeader");
        Government instance = null;
        Government expResult = null;
        Government result = instance.unseatViceLeader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of succeedLeader method, of class Government.
     */
    @Test
    public void testSucceedLeader() {
        System.out.println("succeedLeader");
        Government instance = null;
        Government expResult = null;
        Government result = instance.succeedLeader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addOfficer method, of class Government.
     */
    @Test
    public void testAddOfficer_MPlayer() {
        System.out.println("addOfficer");
        MPlayer player = null;
        Government instance = null;
        Government expResult = null;
        Government result = instance.addOfficer(player);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addOfficer method, of class Government.
     */
    @Test
    public void testAddOfficer_String() {
        System.out.println("addOfficer");
        String player = "";
        Government instance = null;
        Government expResult = null;
        Government result = instance.addOfficer(player);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setHq method, of class Government.
     */
    @Test
    public void testSetHq() {
        System.out.println("setHq");

        Government instance = null;
        Government expResult = null;
        assertEquals(expResult, null);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
     * Test of canRemoveMember method, of class Government.
     */
    @Test
    public void testCanRemoveMember() {
        System.out.println("canRemoveMember");
        MPlayer player = null;
        Government instance = null;
        boolean expResult = false;
        boolean result = instance.canRemoveMember(player);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeMember method, of class Government.
     */
    @Test
    public void testRemoveMember() {
        System.out.println("removeMember");
        MPlayer player = null;
        Government instance = null;
        Government expResult = null;
        Government result = instance.removeMember(player);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeMemberAndSucceed method, of class Government.
     */
    @Test
    public void testRemoveMemberAndSucceed() {
        System.out.println("removeMemberAndSucceed");
        MPlayer player = null;
        Government instance = null;
        Government expResult = null;
        Government result = instance.removeMemberAndSucceed(player);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isMember method, of class Government.
     */
    @Test
    public void testIsMember_String() {
        System.out.println("isMember");
        String player = "";
        Government instance = null;
        boolean expResult = false;
        boolean result = instance.isMember(player);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isMember method, of class Government.
     */
    @Test
    public void testIsMember_MPlayer() {
        System.out.println("isMember");
        MPlayer player = null;
        Government instance = null;
        boolean expResult = false;
        boolean result = instance.isMember(player);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addAffiliate method, of class Government.
     */
    @Test
    public void testAddAffiliate_String() {
        System.out.println("addAffiliate");
        String player = "";
        Government instance = null;
        boolean expResult = false;
        boolean result = instance.addAffiliate(player);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addAffiliate method, of class Government.
     */
    @Test
    public void testAddAffiliate_MPlayer() {
        System.out.println("addAffiliate");
        MPlayer player = null;
        Government instance = null;
        boolean expResult = false;
        boolean result = instance.addAffiliate(player);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAffiliates method, of class Government.
     */
    @Test
    public void testGetAffiliates() {
        System.out.println("getAffiliates");
        Government instance = null;
        Set expResult = null;
        Set result = instance.getAffiliates();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOfficers method, of class Government.
     */
    @Test
    public void testGetOfficers() {
        System.out.println("getOfficers");
        Government instance = null;
        Set expResult = null;
        Set result = instance.getOfficers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOnlineOfficers method, of class Government.
     */
    @Test
    public void testGetOnlineOfficers() {
        System.out.println("getOnlineOfficers");
        Government instance = null;
        Set expResult = null;
        Set result = instance.getOnlineOfficers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOwnerId method, of class Government.
     */
    @Test
    public void testGetOwnerId() {
        System.out.println("getOwnerId");
        Government instance = null;
        String expResult = "";
        String result = instance.getOwnerId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of dispatchInvite method, of class Government.
     */
    @Test
    public void testDispatchInvite() {
        System.out.println("dispatchInvite");
        MPlayer inviter = null;
        MPlayer invited = null;
        Government instance = null;
        boolean expResult = false;
        boolean result = instance.dispatchInvite(inviter, invited);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of canBeClaimed method, of class Government.
     */
    @Test
    public void testCanBeClaimed() {
        System.out.println("canBeClaimed");
        Chunk chunk = null;
        LandOwner futureOwner = null;
        Government instance = null;
        boolean expResult = false;
        boolean result = instance.canBeClaimed(null, futureOwner);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of canRetainAllLand method, of class Government.
     */
    @Test
    public void testCanRetainAllLand() {
        System.out.println("canRetainAllLand");
        Government instance = null;
        boolean expResult = false;
        boolean result = instance.canRetainAllLand();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of canClaimMoreLand method, of class Government.
     */
    @Test
    public void testCanClaimMoreLand() {
        System.out.println("canClaimMoreLand");
        Government instance = null;
        boolean expResult = false;
        boolean result = instance.canClaimMoreLand();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
     * Test of getOwnerType method, of class Government.
     */
    @Test
    public void testGetOwnerType() {
        System.out.println("getOwnerType");
        Government instance = null;
        OwnerType expResult = null;
        OwnerType result = instance.getOwnerType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
     * Test of claim method, of class Government.
     */
    @Test
    public void testClaim() {
        System.out.println("claim");
        Chunk chunk = null;
        Government instance = null;
        Government expResult = null;
        Government result = instance.claim(null);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of unclaim method, of class Government.
     */
    @Test
    public void testUnclaim() {
        System.out.println("unclaim");
        Chunk chunk = null;
        Government instance = null;
        Government expResult = null;
        Government result = instance.unclaim(null);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHq method, of class Government.
     */
    @Test
    public void testGetHq() {
        System.out.println("getHq");
        Government instance = null;
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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

        Set<MPlayer> expectedSet = new HashSet<MPlayer>();
        expectedSet.add(frank);
        expectedSet.add(joe);
        expectedSet.add(bob);
        expectedSet.add(tim);
        expectedSet.add(nancy);

        Set<MPlayer> resultSet = government.getCouncilMembersAsMPlayers();

        //Order to satisfy
        Object[] expected = expectedSet.toArray();
        Arrays.sort(expected);
        Object[] result = resultSet.toArray();
        Arrays.sort(result);

        assertArrayEquals(expected, result);
    }

    /**
     * Test of canHaveMoreDivisions method, of class Government.
     */
    @Test
    public void testCanHaveMoreDivisions() {
        System.out.println("canHaveMoreDivisions");
        Government instance = null;
        boolean expResult = false;
        boolean result = instance.canHaveMoreDivisions();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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

        when(Mafiacraft.getPlayer("Frank")).thenReturn(frank);
        when(Mafiacraft.getPlayer("Joe")).thenReturn(joe);
        when(Mafiacraft.getPlayer("Bob")).thenReturn(bob);
        when(Mafiacraft.getPlayer("Tim")).thenReturn(tim);
        when(Mafiacraft.getPlayer("Nancy")).thenReturn(nancy);

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

    /**
     * Test of serialize method, of class Government.
     */
    @Test
    public void testSerialize() {
        System.out.println("serialize");
        Government instance = null;
        Map expResult = null;
        Map result = instance.serialize();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deserialize method, of class Government.
     */
    @Test
    public void testDeserialize() {
        System.out.println("deserialize");
        Map<String, Object> data = null;
        Government expResult = null;
        Government result = Government.deserialize(data);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
