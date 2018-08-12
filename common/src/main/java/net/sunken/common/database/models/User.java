package net.sunken.common.database.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class User extends AbstractModel {
    @Getter
    @Setter
    private String uuid;
    @Getter
    @Setter
    private String username;
}
