package net.sunken.common.player;

import com.google.common.collect.ImmutableMap;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import net.sunken.common.Common;
import net.sunken.common.ServerInstance;
import net.sunken.common.achievements.Achievement;
import net.sunken.common.achievements.AchievementRegistry;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.trigger.TriggerManager;
import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractPlayer {

    protected static Common common = Common.getInstance();

    protected static final String ACHIEVEMENTS_ID_FIELD = "id";
    protected static final String ACHIEVEMENTS_PROGRESS_FIELD = "progress";
    protected static final String ACHIEVEMENTS_DONE_FIELD = "done";

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

        this.rank = PlayerRank.valueOf(this.playerDocument.getString(DatabaseConstants.PLAYER_RANK_FIELD));
        this.firstJoin = firstJoin;

        this.achievements = new HashMap<>();
        this.loadAchievements();

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
            persistedAchievements.add(new Document(ImmutableMap.of(ACHIEVEMENTS_ID_FIELD, achievementId,
                                                                   ACHIEVEMENTS_PROGRESS_FIELD, progressToAdd,
                                                                   ACHIEVEMENTS_DONE_FIELD, progressToAdd >= targetProgress)));
            playerCollection.replaceOne(new Document(DatabaseConstants.PLAYER_UUID_FIELD, uuid), playerDocument);
            this.achievements.put(achievementId, achievement);
        } else {
            for (Document persistedAchievement : persistedAchievements) {
                if (persistedAchievement.getString(ACHIEVEMENTS_ID_FIELD).equals(achievementId)) {
                    boolean done = persistedAchievement.getBoolean(ACHIEVEMENTS_DONE_FIELD);
                    if (!done) {
                        persistedAchievement.put(ACHIEVEMENTS_PROGRESS_FIELD, progressToAdd);
                        if (persistedAchievement.getInteger(ACHIEVEMENTS_PROGRESS_FIELD) >= targetProgress) {
                            persistedAchievement.put(ACHIEVEMENTS_DONE_FIELD, true);
                        }
                        playerCollection.replaceOne(new Document(DatabaseConstants.PLAYER_UUID_FIELD, uuid), playerDocument);
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
                                    String id = achievement.getString(ACHIEVEMENTS_ID_FIELD);
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
}
