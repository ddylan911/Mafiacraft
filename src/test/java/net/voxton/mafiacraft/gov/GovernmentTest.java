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
package net.voxton.mafiacraft.gov;

import java.util.ArrayList;
import java.util.Arrays;
import net.voxton.mafiacraft.Mafiacraft;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.util.List;
import java.util.Map;
import net.voxton.mafiacraft.geo.LandOwner;
import net.voxton.mafiacraft.geo.OwnerType;
import net.voxton.mafiacraft.player.MPlayer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

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
        List<Division> divs = Arrays.asList(div1, div2);
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
        List<Division> divs = Arrays.asList(div1, div2);
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
        List<Division> divs = Arrays.asList(div1, div2);
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
        System.out.println("getPower");
        Government instance = null;
        int expResult = 0;
        int result = instance.getPower();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMinPlayerPower method, of class Government.
     */
    @Test
    public void testGetMinPlayerPower() {
        System.out.println("getMinPlayerPower");
        Government instance = null;
        int expResult = 0;
        int result = instance.getMinPlayerPower();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMaxPlayerPower method, of class Government.
     */
    @Test
    public void testGetMaxPlayerPower() {
        System.out.println("getMaxPlayerPower");
        Government instance = null;
        int expResult = 0;
        int result = instance.getMaxPlayerPower();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPlayerPower method, of class Government.
     */
    @Test
    public void testGetPlayerPower() {
        System.out.println("getPlayerPower");
        Government instance = null;
        int expResult = 0;
        int result = instance.getPlayerPower();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setName method, of class Government.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "";
        Government instance = null;
        Government expResult = null;
        Government result = instance.setName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getType method, of class Government.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
        Government instance = null;
        GovType expResult = null;
        GovType result = instance.getType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setType method, of class Government.
     */
    @Test
    public void testSetType() {
        System.out.println("setType");
        GovType type = null;
        Government instance = null;
        instance.setType(type);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of canBuild method, of class Government.
     */
    @Test
    public void testCanBuild() {
        System.out.println("canBuild");
        MPlayer player = null;
        Chunk c = null;
        Government instance = null;
        boolean expResult = false;
        boolean result = instance.canBuild(player, c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOwnerName method, of class Government.
     */
    @Test
    public void testGetOwnerName() {
        System.out.println("getOwnerName");
        Government instance = null;
        String expResult = "";
        String result = instance.getOwnerName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLeader method, of class Government.
     */
    @Test
    public void testGetLeader() {
        System.out.println("getLeader");
        Government instance = null;
        String expResult = "";
        String result = instance.getLeader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getViceLeader method, of class Government.
     */
    @Test
    public void testGetViceLeader() {
        System.out.println("getViceLeader");
        Government instance = null;
        String expResult = "";
        String result = instance.getViceLeader();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMembers method, of class Government.
     */
    @Test
    public void testGetMembers_0args() {
        System.out.println("getMembers");
        Government instance = null;
        List expResult = null;
        List result = instance.getMembers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCouncilMembers method, of class Government.
     */
    @Test
    public void testGetCouncilMembers() {
        System.out.println("getCouncilMembers");

        String leader = "Leader";
        String viceLeader = "ViceLeader";
        government.setLeader(leader);
        government.setViceLeader(viceLeader);
        government.addOfficer("Bob");
        government.addOfficer("Tim");
        government.addOfficer("Nancy");

        List<String> expectedList = new ArrayList<String>();
        expectedList.add(leader);
        expectedList.add(viceLeader);
        expectedList.add("Bob");
        expectedList.add("Tim");
        expectedList.add("Nancy");

        List<String> resultList = government.getCouncilMembers();

        //Order to satisfy
        Object[] expected = expectedList.toArray();
        Arrays.sort(expected);
        Object[] result = resultList.toArray();
        Arrays.sort(result);

        assertArrayEquals(expected, result);
    }

    /**
     * Test of getPositions method, of class Government.
     */
    @Test
    public void testGetPositions() {
        System.out.println("getPositions");
        Government instance = null;
        Map expResult = null;
        Map result = instance.getPositions();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMembers method, of class Government.
     */
    @Test
    public void testGetMembers_Position() {
        System.out.println("getMembers");
        Position position = null;
        Government instance = null;
        List expResult = null;
        List result = instance.getMembers(position);
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
        List expResult = null;
        List result = instance.getDivisions();
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
        System.out.println("createDivision");
        Government instance = null;
        Division expResult = null;
        Division result = instance.createDivision();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCouncilMemberCount method, of class Government.
     */
    @Test
    public void testGetCouncilMemberCount() {
        System.out.println("getCouncilMemberCount");
        Government instance = null;
        int expResult = 0;
        int result = instance.getCouncilMemberCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
    public void testGetOnlineMembers_0args() {
        System.out.println("getOnlineMembers");
        Government instance = null;
        List expResult = null;
        List result = instance.getOnlineMembers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOnlineMembers method, of class Government.
     */
    @Test
    public void testGetOnlineMembers_Position() {
        System.out.println("getOnlineMembers");
        Position position = null;
        Government instance = null;
        List expResult = null;
        List result = instance.getOnlineMembers(position);
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
    public void testGetPosition_String() {
        System.out.println("getPosition");
        String player = "";
        Government instance = null;
        Position expResult = null;
        Position result = instance.getPosition(player);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPosition method, of class Government.
     */
    @Test
    public void testGetPosition_MPlayer() {
        System.out.println("getPosition");
        MPlayer player = null;
        Government instance = null;
        Position expResult = null;
        Position result = instance.getPosition(player);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        Location hq = null;
        Government instance = null;
        Government expResult = null;
        Government result = instance.setHq(hq);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLeader method, of class Government.
     */
    @Test
    public void testSetLeader_MPlayer() {
        System.out.println("setLeader");
        MPlayer player = null;
        Government instance = null;
        Government expResult = null;
        Government result = instance.setLeader(player);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLeader method, of class Government.
     */
    @Test
    public void testSetLeader_String() {
        System.out.println("setLeader");
        String leader = "";
        Government instance = null;
        Government expResult = null;
        Government result = instance.setLeader(leader);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setViceLeader method, of class Government.
     */
    @Test
    public void testSetViceLeader_MPlayer() {
        System.out.println("setViceLeader");
        MPlayer viceLeader = null;
        Government instance = null;
        Government expResult = null;
        Government result = instance.setViceLeader(viceLeader);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setViceLeader method, of class Government.
     */
    @Test
    public void testSetViceLeader_String() {
        System.out.println("setViceLeader");
        String viceLeader = "";
        Government instance = null;
        Government expResult = null;
        Government result = instance.setViceLeader(viceLeader);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        List expResult = null;
        List result = instance.getAffiliates();
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
        List expResult = null;
        List result = instance.getOfficers();
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
        List expResult = null;
        List result = instance.getOnlineOfficers();
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
        boolean result = instance.canBeClaimed(chunk, futureOwner);
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
     * Test of getMaxGovernmentLand method, of class Government.
     */
    @Test
    public void testGetMaxGovernmentLand() {
        System.out.println("getMaxGovernmentLand");
        Government instance = null;
        int expResult = 0;
        int result = instance.getMaxGovernmentLand();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        System.out.println("getLand");
        Government instance = null;
        int expResult = 0;
        int result = instance.getLand();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLand method, of class Government.
     */
    @Test
    public void testSetLand() {
        System.out.println("setLand");
        int amt = 0;
        Government instance = null;
        Government expResult = null;
        Government result = instance.setLand(amt);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of incLand method, of class Government.
     */
    @Test
    public void testIncLand() {
        System.out.println("incLand");
        Government instance = null;
        Government expResult = null;
        Government result = instance.incLand();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of decLand method, of class Government.
     */
    @Test
    public void testDecLand() {
        System.out.println("decLand");
        Government instance = null;
        Government expResult = null;
        Government result = instance.decLand();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
        Government result = instance.claim(chunk);
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
        Government result = instance.unclaim(chunk);
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
        Location expResult = null;
        Location result = instance.getHq();
        assertEquals(expResult, result);
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

        List<MPlayer> expectedList = new ArrayList<MPlayer>();
        expectedList.add(frank);
        expectedList.add(joe);
        expectedList.add(bob);
        expectedList.add(tim);
        expectedList.add(nancy);

        List<MPlayer> resultList = government.getCouncilMembersAsMPlayers();

        //Order to satisfy
        Object[] expected = expectedList.toArray();
        Arrays.sort(expected);
        Object[] result = resultList.toArray();
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
        System.out.println("getChatPrefix");
        Government instance = null;
        String expResult = "";
        String result = instance.getChatPrefix();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of broadcastMessage method, of class Government.
     */
    @Test
    public void testBroadcastMessage() {
        System.out.println("broadcastMessage");
        String message = "";
        Government instance = null;
        instance.broadcastMessage(message);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEntryMessage method, of class Government.
     */
    @Test
    public void testGetEntryMessage() {
        System.out.println("getEntryMessage");
        Government instance = null;
        String expResult = "";
        String result = instance.getEntryMessage();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
