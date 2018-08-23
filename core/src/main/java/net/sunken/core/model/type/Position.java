package net.sunken.core.model.type;

import lombok.Getter;

public class Position {

    @Getter
    private double x;
    @Getter
    private double y;
    @Getter
    private double z;

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

}
