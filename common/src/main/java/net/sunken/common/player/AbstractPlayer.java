package net.sunken.common.player;

import com.google.common.collect.ImmutableMap;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.achievements.Achievement;
import net.sunken.common.achievements.AchievementRegistry;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.trigger.TriggerManager;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractPlayer {

    protected static Common common = Common.getInstance();

    protected MongoCollection<Document> playerCollection;
    @Getter
    protected Document playerDocument;

    protected String uuid;
    @Getter
    protected String name;
    @Getter
    protected PlayerRank rank;

    @Getter
    protected boolean firstJoin;

    /**
     * ID of the achievement mapped against the achievement itself for O(1) achievement retrieval
     * NOTE: These are achieved achievements, not all the achievements themselves
     */
    @Getter
    protected Map<String, Achievement> achievements;

    @Getter
    protected List<Document> friends;

    public AbstractPlayer (String uuid, String name, Document document, boolean firstJoin) {
        this.uuid = uuid;
        this.name = name;

        this.playerCollection = Common.getInstance()
                .getMongo()
                .getConnection()
                .getDatabase(DatabaseConstants.DATABASE_NAME)
                .getCollection(DatabaseConstants.PLAYER_COLLECTION);

        // Load from database if supplied none
        if (document == null) {
            this.playerDocument = this.playerCollection.find(Filters.eq(DatabaseConstants.PLAYER_UUID_FIELD, uuid)).first();
        } else {
            this.playerDocument = document;
        }

        // Load basic information from document
        this.rank = PlayerRank.valueOf(this.playerDocument.getString(DatabaseConstants.PLAYER_RANK_FIELD));
        this.firstJoin = firstJoin;

        // Load achievements from document
        this.achievements = new HashMap<>();
        this.loadAchievements();

        // Load friends from document
        this.loadFriends();

        // Trigger first join achievement
        if (this.firstJoin) {
            TriggerManager.NETWORK_JOIN_TRIGGER.trigger(this, false);
        }
    }

    public void progressAchievement(Achievement achievement, int progressToAdd) {
        String achievementId = achievement.getId();
        int targetProgress = achievement.getTargetProgress();
        List<Document> persistedAchievements = this.getPersistedAchievements();
        // this achievement has not yet been progressed
        if (!this.achievements.containsKey(achievementId)) {
            persistedAchievements.add(new Document(ImmutableMap.of(DatabaseConstants.ACHIEVEMENTS_ID_FIELD, achievementId,
                    DatabaseConstants.ACHIEVEMENTS_PROGRESS_FIELD, progressToAdd,
                    DatabaseConstants.ACHIEVEMENTS_DONE_FIELD, progressToAdd >= targetProgress)));

            playerCollection.updateOne(new Document(DatabaseConstants.PLAYER_UUID_FIELD, uuid),
                    Updates.set(DatabaseConstants.PLAYER_ACHIEVEMENTS_FIELD, persistedAchievements));

            this.achievements.put(achievementId, achievement);

        } else {
            for (Document persistedAchievement : persistedAchievements) {
                if (persistedAchievement.getString(DatabaseConstants.ACHIEVEMENTS_ID_FIELD).equals(achievementId)) {
                    boolean done = persistedAchievement.getBoolean(DatabaseConstants.ACHIEVEMENTS_DONE_FIELD);
                    if (!done) {
                        persistedAchievement.put(DatabaseConstants.ACHIEVEMENTS_PROGRESS_FIELD, progressToAdd);
                        if (persistedAchievement.getInteger(DatabaseConstants.ACHIEVEMENTS_PROGRESS_FIELD) >= targetProgress) {
                            persistedAchievement.put(DatabaseConstants.ACHIEVEMENTS_DONE_FIELD, true);
                        }
                        playerCollection.updateOne(new Document(DatabaseConstants.PLAYER_UUID_FIELD, uuid),
                                Updates.set(DatabaseConstants.PLAYER_ACHIEVEMENTS_FIELD, persistedAchievements));

                        this.achievements.put(achievementId, achievement);
                    }
                    break;
                }
            }
        }
    }

    /** This must be called when the AbstractPlayer is being destroyed e.g. player leaving */
    public void cleanup() {
    }

    private void loadAchievements() {
        this.achievements = this.getPersistedAchievements()
                                .stream()
                                .map(achievement -> {
                                    String id = achievement.getString(DatabaseConstants.ACHIEVEMENTS_ID_FIELD);
                                    return AchievementRegistry.allAchievements().get(id);
                                })
                                // remove nulls from the list from bad achievement IDs present for reasons such as the removal of that achievement from the registry
                                .filter(Objects::nonNull)
                                .collect(Collectors.toMap(Achievement::getId, achievement -> achievement));
    }

    private List<Document> getPersistedAchievements() {
        return playerDocument.get(DatabaseConstants.PLAYER_ACHIEVEMENTS_FIELD, List.class);
    }

    public UUID getUUID() {
        return UUID.fromString(this.uuid);
    }

    private void loadFriends() {
        if (playerDocument.containsKey(DatabaseConstants.PLAYER_FRIENDS_FIELD)) {
            List<ObjectId> friendObjects = (List<ObjectId>) playerDocument.get(DatabaseConstants.PLAYER_FRIENDS_FIELD);

            for (ObjectId objId : friendObjects) {
                this.friends.add(this.playerCollection.find(Filters.eq("_id", objId)).first());
            }
        }
    }

    public boolean isFriend(UUID uuid) {
        if (this.friends.size() > 0) {
            for (Document friend : this.friends) {
                if (friend.getString(DatabaseConstants.PLAYER_UUID_FIELD).equals(uuid.toString())) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isFriend(ObjectId objId) {
        if (this.friends.size() > 0) {
            for (Document friend : this.friends) {
                if (friend.getObjectId("_id").equals(objId)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void addFriend(ObjectId objId) {
        List<ObjectId> friendObjects = new ArrayList<>();

        if (playerDocument.containsKey(DatabaseConstants.PLAYER_FRIENDS_FIELD)) {
            friendObjects = (List<ObjectId>) playerDocument.get(DatabaseConstants.PLAYER_FRIENDS_FIELD);
        }

        //--- Update cache.
        friendObjects.add(objId);
        playerDocument.put(DatabaseConstants.PLAYER_FRIENDS_FIELD, friendObjects);
        friends.add(playerCollection.find(Filters.eq("_id", objId)).first());

        //--- Update database.
        playerCollection.updateOne(new Document(DatabaseConstants.PLAYER_UUID_FIELD, uuid),
                Updates.set(DatabaseConstants.PLAYER_FRIENDS_FIELD, friendObjects));
    }

    public void removeFriend(ObjectId objId) {
        if (playerDocument.containsKey(DatabaseConstants.PLAYER_FRIENDS_FIELD)) {

            //--- Update cache.
            List<ObjectId> friendObjects = (List<ObjectId>) playerDocument.get(DatabaseConstants.PLAYER_FRIENDS_FIELD);
            friendObjects.remove(objId);
            playerDocument.put(DatabaseConstants.PLAYER_FRIENDS_FIELD, friendObjects);

            //--- Update database.
            playerCollection.updateOne(new Document(DatabaseConstants.PLAYER_UUID_FIELD, uuid),
                    Updates.set(DatabaseConstants.PLAYER_FRIENDS_FIELD, friendObjects));

            //--- Remove from cache.
            for (Document doc : friends) {
                if (doc.getObjectId("_id").equals(objId)) {
                    friends.remove(doc);
                    break;
                }
            }

        }
    }
}
