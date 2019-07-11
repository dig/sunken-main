package net.sunken.common.packet;

import javax.annotation.Nullable;
import java.io.*;

public abstract class Packet implements Serializable {

    @Nullable
    public byte[] toBytes() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {

            out.writeObject(this);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Packet fromBytes(byte[] bytes) {
        try {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                 ObjectInput in = new ObjectInputStream(bis)) {

                Object object = in.readObject();
                if (object instanceof Packet) {
                    return (Packet) object;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
