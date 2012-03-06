/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.util;

import java.io.*;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

/**
 * Utilities for serializing from and to Strings.
 *
 * Code taken from
 * http://stackoverflow.com/questions/134492/how-to-serialize-an-object-into-a-string
 */
public class StringSerializer {

    /**
     * Read the object from Base64 string.
     *
     * @param string The string to deserialize.
     * @param type The type of object to deserialize into.
     * @return The deserialized object.
     */
    public static <T extends Serializable> T fromString(String string, Class<T> type)
            throws IOException, ClassNotFoundException {
        byte[] data = Base64Coder.decode(string);
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        Object o = objectStream.readObject();
        objectStream.close();
        return type.cast(o);
    }

    /**
     * Write the object to a Base64 string.
     *
     * @param object The object to serialize.
     * @return The serialized object in String form.
     */
    public static String toString(Serializable object) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(object);
        objectStream.close();
        return Base64Coder.encode(byteStream.toByteArray()).toString();
    }

}
