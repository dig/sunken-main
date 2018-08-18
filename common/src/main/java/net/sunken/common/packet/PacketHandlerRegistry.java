package net.sunken.common.packet;

import com.google.common.collect.Maps;
import lombok.Getter;

import java.util.Map;

public final class PacketHandlerRegistry {

    @Getter
    private static final Map<Class<? extends Packet>, PacketHandler> handlers;

    static {
        handlers = Maps.newHashMap();
    }

    public static <T extends Packet> void registerHandler(T packet, PacketHandler<T> handler) {
        handlers.put(packet.getClass(), handler);
    }

    private PacketHandlerRegistry() {
    }
}
