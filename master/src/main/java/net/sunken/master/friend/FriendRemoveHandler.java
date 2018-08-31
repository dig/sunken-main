package net.sunken.master.friend;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.sunken.common.Common;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.friend.packet.MFriendRemovePacket;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.packet.SendPlayerBungeeMessagePacket;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.player.PlayerRank;
import net.sunken.master.player.MasterPlayer;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FriendRemoveHandler extends PacketHandler<MFriendRemovePacket> {

    private static Map<UUID, AbstractPlayer> onlinePlayers;
    private static MongoCollection<Document> playerCollection;

    static {
        onlinePlayers = Common.getInstance().getDataManager().getOnlinePlayers();
        playerCollection = Common.getInstance()
                .getMongo()
                .getConnection()
                .getDatabase(DatabaseConstants.DATABASE_NAME)
                .getCollection(DatabaseConstants.PLAYER_COLLECTION);
    }

    @Override
    public void onReceive(MFriendRemovePacket packet) {
        UUID creator = packet.getCreator();
        MasterPlayer creatorPlayer = (MasterPlayer) onlinePlayers.get(creator);
        String targetName = packet.getTarget();

        boolean removed = false;

        for (Document friend : creatorPlayer.getFriends()) {
            UUID uuid = UUID.fromString(friend.getString(DatabaseConstants.PLAYER_UUID_FIELD));
            String name = friend.getString(DatabaseConstants.PLAYER_NAME_FIELD);
            PlayerRank rank = PlayerRank.valueOf(friend.getString(DatabaseConstants.PLAYER_RANK_FIELD));

            if (name.equalsIgnoreCase(targetName)) {
                ObjectId creatorId = creatorPlayer.getPlayerDocument().getObjectId("_id");
                ObjectId targetId = friend.getObjectId("_id");

                // Remove target from creator
                // creatorPlayer.removeFriend(targetId);

                // Remove creator from target, I'm calling a fresh copy
                // of the target document and not using friend document
                // because they could be online and need the most up to
                // date version.
                Document targetDocument = playerCollection.find(Filters.eq("_id", targetId)).first();
                List<ObjectId> friendObjects = (List<ObjectId>) targetDocument.get(DatabaseConstants.PLAYER_FRIENDS_FIELD);
                friendObjects.remove(creatorId);

                targetDocument.put(DatabaseConstants.PLAYER_FRIENDS_FIELD, friendObjects);
                playerCollection.replaceOne(new Document("_id", targetId), targetDocument);

                // Send removed messages to target and creator.
                PacketUtil.sendPacket(new SendPlayerBungeeMessagePacket(
                        creator, "Removed " + name + " from your friends list!"));
                PacketUtil.sendPacket(new SendPlayerBungeeMessagePacket(
                        uuid, creatorPlayer.getName() + " has removed you from their friends list!"));

                removed = true;

                break;
            }
        }

        if (!removed) {
            PacketUtil.sendPacket(new SendPlayerBungeeMessagePacket(
                    creator, "Unable to find that player on your friends list."));
        }
    }

}
