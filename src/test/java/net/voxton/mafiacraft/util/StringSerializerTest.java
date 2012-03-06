/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.util;

import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import junit.framework.TestCase;

/**
 *
 * @author simplyianm
 */
public class StringSerializerTest extends TestCase {

    public StringSerializerTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of fromString method, of class StringSerializer.
     */
    public void testFromString() throws Exception {
        System.out.println("fromString");
        String string = "rO0ABXNyACFnbnUudHJvdmUubWFwLmhhc2guVEludEludEhhc2hNYXAAAAAAAAAAAQwAAHhyAB9n"
                + "bnUudHJvdmUuaW1wbC5oYXNoLlRJbnRJbnRIYXNoAAAAAAAAAAEMAAB4cgAiZ251LnRyb3ZlLmlt"
                + "cGwuaGFzaC5UUHJpbWl0aXZlSGFzaAAAAAAAAAABDAAAeHIAGWdudS50cm92ZS5pbXBsLmhhc2gu"
                + "VEhhc2jnHirdPuU3yQwAAHhwdzcAAAA/AAAAPwAAAAAAAAAAAAAAAAAABAAAAAgAAAAIAAAABQAA"
                + "AAYAAAADAAAABAAAAAEAAAACeA==";
        Class<TIntIntHashMap> type = TIntIntHashMap.class;

        TIntIntHashMap expResult = new TIntIntHashMap();

        expResult.put(1, 2);
        expResult.put(3, 4);
        expResult.put(5, 6);
        expResult.put(8, 8);

        TIntIntHashMap result = StringSerializer.fromString(string, type);
        assertEquals(expResult, result);
    }

    public void testSerializationIntegrity_TIntIntHashMap() throws Exception {
        System.out.println("Serialization Integrity of TIntIntHashMap");
        TIntIntHashMap object = new TIntIntHashMap();

        object.put(1, 2);
        object.put(3, 4);
        object.put(5, 6);
        object.put(8, 8);

        String result = StringSerializer.toString(object);

        TIntIntHashMap back = StringSerializer.fromString(result, TIntIntHashMap.class);
        assertEquals(object, back);
    }

    public void testSerializationIntegrity_TIntObjectHashMap() throws Exception {
        System.out.println("Serialization Integrity of TIntObjectHashMap");
        TIntObjectHashMap<String> object = new TIntObjectHashMap<String>();

        object.put(1, "unicode TEST!!! \u1930");
        object.put(3, "spacey Spaces");
        object.put(5, "3.1415926");
        object.put(8, "(@UC2C@#RNC@#u9rc@RJIC(@ucr{");

        String result = StringSerializer.toString(object);

        TIntObjectHashMap<String> back = StringSerializer.fromString(result, TIntObjectHashMap.class);
        assertEquals(object, back);
    }

}
