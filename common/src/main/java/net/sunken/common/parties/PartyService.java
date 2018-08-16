package net.sunken.common.parties;

public interface PartyService {

    PartyCreateStatus createParty(PartyPlayer leader, PartyPlayer toInvite);
}
