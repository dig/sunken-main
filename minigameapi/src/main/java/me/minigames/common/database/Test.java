package me.minigames.common.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import me.minigames.common.Rank;
import me.minigames.common.database.models.User;

import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        MongoConnection mongo = new MongoConnection("localhost", 27017, "", "");
        MongoDatabase db = mongo.getConnection().getDatabase("dev");
        MongoCollection<User> users = db.getCollection("player", User.class);

        User newUser = new User();
        newUser.setUuid(UUID.randomUUID().toString());
        newUser.setUsername("Venom");
        newUser.setRank(Rank.MODERATOR);
        users.insertOne(newUser);
    }
}
