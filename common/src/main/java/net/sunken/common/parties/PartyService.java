package net.sunken.common.parties;

import java.util.UUID;

public interface PartyService {

    PartyCreateStatus createParty(PartyPlayer leader, PartyPlayer toInvite);

    void leaveParty(UUID leaving);
}
