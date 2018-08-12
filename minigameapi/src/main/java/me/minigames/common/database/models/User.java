package me.minigames.common.database.models;

import lombok.Getter;
import lombok.Setter;
import me.minigames.common.Rank;

public class User extends AbstractModel {
    @Getter
    @Setter
    private String uuid;
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private Rank rank;

    public User() {}
}
