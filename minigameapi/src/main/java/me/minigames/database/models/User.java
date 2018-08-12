package me.minigames.database.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.minigames.common.Rank;

@AllArgsConstructor
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
}
