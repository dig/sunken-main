package net.sunken.common.packet;

import com.google.common.collect.Maps;
import lombok.Getter;
import net.sunken.common.Common;

import java.util.Map;
import java.util.logging.Level;

public final class PacketHandlerRegistry {

    @Getter
    private static final Map<Class<? extends Packet>, PacketHandler> handlers;

    static {
        handlers = Maps.newHashMap();
    }

    public static <T extends Packet> void registerHandler(T packet, PacketHandler<T> handler) {
        Common.getLogger().log(Level.INFO, "registerHandler() Registered " + packet.getClass());
        handlers.put(packet.getClass(), handler);
    }

    private PacketHandlerRegistry() {
    }
}
