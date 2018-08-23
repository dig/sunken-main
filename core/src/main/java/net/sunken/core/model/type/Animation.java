package net.sunken.core.model.type;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
public class Animation {

    @Getter
    private String name;
    @Getter
    private Map<Integer, Frame> frames;

    public Animation(String name) {
        this.name = name;
        this.frames = new HashMap<>();
    }

}
