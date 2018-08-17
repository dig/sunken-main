package net.sunken.common.parties.service;

import net.sunken.common.parties.service.status.PartyCreateStatus;
import net.sunken.common.parties.data.PartyPlayer;

import java.util.UUID;

public interface PartyService {

    PartyCreateStatus createParty(PartyPlayer leader, PartyPlayer toInvite);

    void leaveParty(UUID leaving);
}
