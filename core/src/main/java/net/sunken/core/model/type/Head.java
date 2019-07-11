package net.sunken.core.model.type;

import lombok.Getter;

public class Head {

    @Getter
    private String id;
    @Getter
    private String texture;

    public Head(String id, String texture) {
        this.id = id;
        this.texture = texture;
    }

}
