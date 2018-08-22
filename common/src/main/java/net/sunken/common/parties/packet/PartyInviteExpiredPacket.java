package net.sunken.common.parties.packet;

import lombok.Getter;
import net.sunken.common.packet.PlayerPacket;

import java.util.UUID;

public class PartyInviteExpiredPacket extends PlayerPacket {

    @Getter
    private final String attemptedInviteName;

    public PartyInviteExpiredPacket(UUID playerTargeted, String attemptedInviteName) {
        super(playerTargeted);
        this.attemptedInviteName = attemptedInviteName;
    }
}
