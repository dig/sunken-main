package net.sunken.bungeecord.player;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.player.AbstractPlayer;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class BungeePlayer extends AbstractPlayer {

    public BungeePlayer(String uuid, String name, Document document, boolean firstJoin) {
        super(uuid, name, document, firstJoin);
    }

    public void addFriend(ObjectId objId) {
        List<ObjectId> friendObjects = new ArrayList<>();

        if (playerDocument.containsKey(DatabaseConstants.PLAYER_FRIENDS_FIELD)) {
            friendObjects = (List<ObjectId>) playerDocument.get(DatabaseConstants.PLAYER_FRIENDS_FIELD);
        }

        // Update cache
        friendObjects.add(objId);
        playerDocument.put(DatabaseConstants.PLAYER_FRIENDS_FIELD, friendObjects);
        friends.add(playerCollection.find(Filters.eq("_id", objId)).first());

        // Update database
        playerCollection.updateOne(new Document(DatabaseConstants.PLAYER_UUID_FIELD, uuid),
                Updates.set(DatabaseConstants.PLAYER_FRIENDS_FIELD, friendObjects));
    }

    public void removeFriend(ObjectId objId) {
        if (playerDocument.containsKey(DatabaseConstants.PLAYER_FRIENDS_FIELD)) {
            // Update cache
            List<ObjectId> friendObjects = (List<ObjectId>) playerDocument.get(DatabaseConstants.PLAYER_FRIENDS_FIELD);
            friendObjects.remove(objId);
            playerDocument.put(DatabaseConstants.PLAYER_FRIENDS_FIELD, friendObjects);

            // Update database
            playerCollection.updateOne(new Document(DatabaseConstants.PLAYER_UUID_FIELD, uuid),
                    Updates.set(DatabaseConstants.PLAYER_FRIENDS_FIELD, friendObjects));

            // Remove from cache
            for (Document doc : friends) {
                if (doc.getObjectId("_id").equals(objId)) {
                    friends.remove(doc);
                    break;
                }
            }
        }
    }
}
