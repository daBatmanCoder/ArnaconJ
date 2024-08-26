package com.Arnacon;

import java.io.*;
import java.util.Base64;

public class SerializationUtils {

    /**
     * Serializes an object to a byte array.
     *
     * @param obj the object to serialize.
     * @return a byte array representing the serialized object.
     * @throws IOException if an I/O error occurs during serialization.
     */
    public static String serialize(Object obj) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }

    /**
     * Deserializes a byte array back to an object.
     *
     * @param bytes the byte array to deserialize.
     * @return the deserialized object.
     * @throws IOException if an I/O error occurs during deserialization.
     * @throws ClassNotFoundException if the class of the serialized object cannot be found.
     */
    public static Object deserialize(String stringToDeserial) throws IOException, ClassNotFoundException {
        byte[] bytes = Base64.getDecoder().decode(stringToDeserial);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        }
    }

}
