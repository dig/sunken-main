package me.minigames.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bukkit.Bukkit;

import java.util.logging.Level;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoConnection extends Database<MongoClient> {
    private MongoClient client;
    private CodecRegistry registry = fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    public MongoConnection(String host, int port, String username, String password) {
        try {
            MongoClientURI connectionString = new MongoClientURI("mongodb://" + username + ":" + password + "@" + host + ":" + port, MongoClientOptions.builder().codecRegistry(registry));
            client = new MongoClient(connectionString);
            Bukkit.getLogger().log(Level.INFO, "Connected to MongoDB");
        } catch (MongoException me) {
            Bukkit.getLogger().log(Level.INFO, "Unable to connect to MongoDB");
            Bukkit.shutdown();
        }
    }

    @Override
    public MongoClient getConnection() {
        return client;
    }

    @Override
    public void disconnect() {
        client.close();
    }
}
