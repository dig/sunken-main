package net.sunken.common.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoConnection extends Database<MongoClient> {

    private MongoClient client;

    public MongoConnection(String host, int port, String username, String password, String database) {
        try {
            String uri = "mongodb://" + username + ":" + password + "@" + host + ":" + port + "/" + database;

            CodecRegistry registry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                                                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));
            MongoClientURI connectionString = new MongoClientURI(uri,
                                                                 MongoClientOptions.builder().codecRegistry(registry));
            this.client = new MongoClient(connectionString);
        } catch (MongoException e) {
            e.printStackTrace();
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
