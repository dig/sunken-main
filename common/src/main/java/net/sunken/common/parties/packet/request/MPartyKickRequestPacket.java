package net.sunken.common.parties.packet.request;

import lombok.Getter;
import net.sunken.common.packet.Packet;

import javax.annotation.Nullable;
import java.util.UUID;

public class MPartyKickRequestPacket extends Packet {

    @Getter
    private final UUID requestingKick;
    @Getter
    private final String toKick;
    @Getter
    private boolean requestingKickOffline;

    public MPartyKickRequestPacket(UUID requestingKick, @Nullable String toKick, boolean requestingKickOffline) {
        this.requestingKick = requestingKick;
        this.toKick = toKick;
        this.requestingKickOffline = requestingKickOffline;
    }
}
