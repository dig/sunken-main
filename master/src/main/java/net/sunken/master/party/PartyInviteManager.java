package net.sunken.master.party;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.data.Party;
import net.sunken.common.parties.packet.changes.PartyInviteExpiredPacket;
import net.sunken.common.parties.status.PartyInviteStatus;
import net.sunken.common.util.PlayerDetail;
import net.sunken.common.util.ScheduleHelper;
import net.sunken.common.util.Tuple2;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkState;

/** Keeps track of pending invites and their expiration */
public final class PartyInviteManager {

    private static final Tuple2<Long, TimeUnit> INVITE_EXPIRY_TIME;
    /** player -> those invited */
    private static Multimap<UUID, UUID> invites = Multimaps.synchronizedSetMultimap(HashMultimap.create());
    private static Map<Tuple2<UUID, UUID>, ScheduledFuture<Void>> expiryTasks = Maps.newHashMap();

    static {
        INVITE_EXPIRY_TIME = new Tuple2<>(60L, TimeUnit.SECONDS);
    }

    public static void addInvite(PlayerDetail inviter, PlayerDetail invitee) {
        checkState(!isInvitePresent(inviter.uuid, invitee.uuid), "player has already been invited");
        Tuple2<UUID, UUID> key = new Tuple2<>(inviter.uuid, invitee.uuid);
        if (invites.put(inviter.uuid, invitee.uuid)) {
            ScheduledFuture<Void> expiryTask = ScheduleHelper.executor().schedule(
                    () -> {
                        if (invites.remove(inviter.uuid, invitee.uuid)) {
                            PartyInviteExpiredPacket partyInviteExpiredPacket = new PartyInviteExpiredPacket(
                                    inviter, invitee);
                            PacketUtil.sendPacket(partyInviteExpiredPacket);
                        }
                        expiryTasks.remove(key);
                        return null;
                    },
                    INVITE_EXPIRY_TIME.a,
                    INVITE_EXPIRY_TIME.b);
            expiryTasks.put(key, expiryTask);
        }
    }

    public static boolean isInvitePresent(UUID from, UUID toInvite) {
        return invites.containsEntry(from, toInvite);
    }

    public static void removeInvite(UUID from, UUID to) {
        invites.remove(from, to);
    }

    public static PartyInviteStatus validateInviteRequest(UUID creator, UUID invitee) {
        Party creatorParty = PartyManager.getPartyByPlayer(creator);
        if (creatorParty != null) { // if person inviting is in a party
            if (!creatorParty.getLeaderUniqueId().equals(creator)) { // if the inviter is not the leader
                return PartyInviteStatus.NOT_LEADER; // deny the invite
            }
        }
        // see if an invite is already present
        if (PartyInviteManager.isInvitePresent(creator, invitee)) {
            return PartyInviteStatus.INVITE_ALREADY_PENDING;
        }
        // see if the invitee is already in a party
        if (PartyManager.getPartyByPlayer(invitee) != null) {
            return PartyInviteStatus.INVITEE_ALREADY_IN_PARTY;
        }
        return PartyInviteStatus.SUCCESS;
    }

    /** Private constructor as it is a singleton */
    private PartyInviteManager() {
        throw new AssertionError("You must not attempt to instantiate this class.");
    }
}
