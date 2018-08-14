package net.sunken.common.player;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.common.achievements.Achievement;
import net.sunken.common.achievements.AchievementRegistry;
import net.sunken.common.database.DatabaseConstants;
import net.sunken.common.exception.DocumentNotFoundException;
import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public abstract class AbstractPlayer {

    private static Common common = Common.getInstance();

    private static final String UUID_FIELD = "uuid";
    private static final String ACHIEVEMENTS_FIELD = "achievements";
    private static final String ACHIEVEMENTS_ID_FIELD = "id";

    private Document playerDocument;

    private String uuid;
    @Getter
    private String name;

    @Getter
    private final Map<String, Achievement> achievements;

    public AbstractPlayer(String uuid, String name) throws DocumentNotFoundException {
        MongoClient connection = common.getMongo().getConnection();
        MongoCollection<Document> playerCollection = connection.getDatabase(DatabaseConstants.DATABASE_NAME)
                                                               .getCollection(DatabaseConstants.PLAYER_COLLECTION);
        playerDocument = playerCollection.find(eq(UUID_FIELD, uuid)).first();
        if (playerDocument == null) {
            throw new DocumentNotFoundException("Player with UUID " + uuid + "  was not found in the collection " + DatabaseConstants.PLAYER_COLLECTION);
        }

        this.uuid = uuid;
        this.name = name;
        this.achievements = new HashMap<>();
        this.loadAchievements();
    }

    private void loadAchievements() {
        List<Document> achievements = playerDocument.get(ACHIEVEMENTS_FIELD, List.class);
        this.achievements = achievements.stream()
                                        .map(achievement -> {
                                            String id = achievement.getString(ACHIEVEMENTS_ID_FIELD);
                                            return AchievementRegistry.allAchievements().get(id);
                                        })
                                        // remove nulls from the list from bad achievement IDs present for reasons such as the removal of that achievement from the registry
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toMap(Achievement::getId, achievement -> achievement);
    }

    public UUID getUUID() {
        return UUID.fromString(this.uuid);
    }
}
