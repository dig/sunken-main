package net.sunken.common.player;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.achievements.Achievement;
import net.sunken.common.achievements.AchievementRegistry;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.exception.DocumentNotFoundException;
import net.sunken.common.trigger.TriggerListenerRegistry;
import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public abstract class AbstractPlayer {

    private static Common common = Common.getInstance();

    private static final String UUID_FIELD = "uuid";
    private static final String ACHIEVEMENTS_FIELD = "achievements";
    private static final String ACHIEVEMENTS_ID_FIELD = "id";

    private MongoCollection<Document> playerCollection;
    private Document playerDocument;

    private String uuid;
    @Getter
    private String name;

    /**
     * ID of the achievement mapped against the achievement itself for O(1) achievement retrieval
     * NOTE: These are achieved achievements, not all the achievements themselves
     */
    @Getter
    private Map<String, Achievement> achievements;

    public AbstractPlayer(String uuid, String name) {
        try{
            this.playerCollection = common.getMongo()
                    .getConnection()
                    .getDatabase(DatabaseConstants.DATABASE_NAME)
                    .getCollection(DatabaseConstants.PLAYER_COLLECTION);
            this.playerDocument = playerCollection.find(eq(UUID_FIELD, uuid)).first();
            if (playerDocument == null) {
                throw new DocumentNotFoundException("Player with UUID " + uuid + "  was not found in the collection " + DatabaseConstants.PLAYER_COLLECTION);
            }
        } catch (DocumentNotFoundException ev){

        }

        this.uuid = uuid;
        this.name = name;
        this.achievements = new HashMap<>();
        this.loadAchievements();
    }

    public void grantAchievement(Achievement achievement) {
        String achievementId = achievement.getId();
        if (!this.achievements.containsKey(achievementId)) {
            List<Document> persistedAchievements = this.getPersistedAchievements();
            persistedAchievements.add(new Document(ACHIEVEMENTS_ID_FIELD, achievementId));
            playerCollection.findOneAndUpdate(new Document(UUID_FIELD, uuid), playerDocument);
            this.achievements.put(achievementId, achievement);
            TriggerListenerRegistry.removeListener(achievement);
        }
    }

    /** This must be called when the AbstractPlayer is being destroyed e.g. server going down */
    public void cleanup() {
        // cleanup the trigger listeners from the achievements that have not yet been achieved
        AchievementRegistry.allAchievements().forEach((id, achievement) -> {
            if (!this.achievements.containsKey(id)) {
                TriggerListenerRegistry.removeListener(achievement);
            }
        });
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

        // then, go through all the available achievements
        AchievementRegistry.allAchievements().forEach((id, achievement) -> {
            if (!this.achievements.containsKey(id)) { // if the player has not achieved this achievement
                // add a listener for the non-achieved achievement
                TriggerListenerRegistry.addListener(achievement);
            }
        });
    }

    private List<Document> getPersistedAchievements() {
        return playerDocument.get(ACHIEVEMENTS_FIELD, List.class);
    }

    public UUID getUUID() {
        return UUID.fromString(this.uuid);
    }
}
