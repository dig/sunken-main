package net.sunken.master.friend;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.sunken.common.Common;
import net.sunken.common.DataManager;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.friend.packet.FriendAcceptStatusPacket;
import net.sunken.common.friend.packet.MFriendRemovePacket;
import net.sunken.common.packet.PacketHandler;
import net.sunken.common.packet.PacketUtil;
import net.sunken.common.packet.SendPlayerBungeeMessagePacket;
import net.sunken.common.player.AbstractPlayer;
import net.sunken.common.player.PlayerRank;
import net.sunken.common.util.PlayerDetail;
import net.sunken.master.player.MasterPlayer;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FriendRemoveHandler extends PacketHandler<MFriendRemovePacket> {

    private static DataManager dataManager;
    private static Map<UUID, AbstractPlayer> onlinePlayers;
    private static MongoCollection<Document> playerCollection;

    static {
        dataManager = Common.getInstance().getDataManager();
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
        UUID toTarget = dataManager.getNameToUUID().get(targetName.toLowerCase());

        boolean removed = false;

        //--- Remove target from creator.
        for (Document friend : creatorPlayer.getFriends()) {
            UUID uuid = UUID.fromString(friend.getString(DatabaseConstants.PLAYER_UUID_FIELD));
            String name = friend.getString(DatabaseConstants.PLAYER_NAME_FIELD);
            PlayerRank rank = PlayerRank.valueOf(friend.getString(DatabaseConstants.PLAYER_RANK_FIELD));

            if (name.equalsIgnoreCase(targetName)) {
                ObjectId creatorId = creatorPlayer.getPlayerDocument().getObjectId("_id");
                ObjectId targetId = friend.getObjectId("_id");

                //--- Remove target from creator.
                creatorPlayer.removeFriend(targetId);

                //--- Remove creator from target.
                List<ObjectId> friendObjects = (List<ObjectId>) friend.get(DatabaseConstants.PLAYER_FRIENDS_FIELD);
                friendObjects.remove(creatorId);

                //--- Update database.
                playerCollection.updateOne(new Document("_id", targetId),
                        Updates.set(DatabaseConstants.PLAYER_FRIENDS_FIELD, friendObjects));

                //--- Send removed messages to target and creator.
                PacketUtil.sendPacket(new SendPlayerBungeeMessagePacket(
                        creator, "Removed " + name + " from your friends list!"));
                PacketUtil.sendPacket(new SendPlayerBungeeMessagePacket(
                        uuid, creatorPlayer.getName() + " has removed you from their friends list!"));

                removed = true;

                break;
            }
        }

        //--- If target is online, remove friend from cache.
        if (toTarget != null) {
            MasterPlayer targetPlayer = (MasterPlayer) onlinePlayers.get(toTarget);
            targetPlayer.removeFriendFromCache(creatorPlayer.getPlayerDocument().getObjectId("_id"));
        }

        if (!removed) {
            PacketUtil.sendPacket(new SendPlayerBungeeMessagePacket(
                    creator, "Unable to find that player on your friends list."));
        }
    }

}
