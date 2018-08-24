package net.sunken.core.model.type;

import lombok.Getter;

public class Rotation {

    @Getter
    private float yaw;
    @Getter
    private float pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

}
