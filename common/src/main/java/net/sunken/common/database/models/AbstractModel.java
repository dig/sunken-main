package net.sunken.common.database.models;

import lombok.Getter;
import org.bson.types.ObjectId;

public class AbstractModel {
    @Getter
    private ObjectId id;
}
