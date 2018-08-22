package net.sunken.master.party;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.parties.packet.PartyInviteExpiredPacket;
import net.sunken.common.util.ScheduleHelper;
import net.sunken.common.util.Tuple2;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class PartyInviteManager {

    private static final Tuple2<Long, TimeUnit> INVITE_EXPIRY_TIME;
    /** player -> those invited */
    private static Multimap<UUID, UUID> invites = Multimaps.synchronizedSetMultimap(HashMultimap.create());
    private static Map<Tuple2<UUID, UUID>, ScheduledFuture<Void>> expiryTasks = Maps.newHashMap();

    static {
        INVITE_EXPIRY_TIME = new Tuple2<>(60L, TimeUnit.SECONDS);
    }

    public static void addInvite(UUID from, UUID to, String toName) {
        Tuple2<UUID, UUID> key = new Tuple2<>(from, to);
        if (!invites.put(from, to)) {
            ScheduledFuture<Void> expiryTask = expiryTasks.get(key);
            if (expiryTask != null) {
                expiryTask.cancel(true);
                expiryTasks.remove(key);
            }
        } else {
            ScheduledFuture<Void> expiryTask = ScheduleHelper.executor().schedule(
                    () -> {
                        if (invites.remove(from, to)) {
                            PartyInviteExpiredPacket partyInviteExpiredPacket = new PartyInviteExpiredPacket(
                                    from, toName);
                            PacketUtil.sendPacket(partyInviteExpiredPacket);
                        }
                        return null;
                    },
                    INVITE_EXPIRY_TIME.a,
                    INVITE_EXPIRY_TIME.b);
            expiryTasks.put(key, expiryTask);
        }
    }

    public static boolean hasInvited(UUID from, UUID toInvite) {
        return invites.containsEntry(from, toInvite);
    }

    private PartyInviteManager() {
    }
}
