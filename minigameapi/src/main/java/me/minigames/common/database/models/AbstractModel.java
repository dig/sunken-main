package me.minigames.common.database.models;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

public class AbstractModel {
    @Getter
    @Setter
    private ObjectId id;
}
