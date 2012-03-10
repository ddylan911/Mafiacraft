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
package net.voxton.mafiacraft.util;

import java.io.*;
import org.apache.commons.codec.binary.Base64;

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
    public static <T extends Serializable> T fromString(String string,
            Class<T> type)
            throws IOException, ClassNotFoundException {
        byte[] data = Base64.decodeBase64(string);
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
        byte[] data = byteStream.toByteArray();
        return Base64.encodeBase64String(data);
    }

}
