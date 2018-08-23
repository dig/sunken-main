package net.sunken.core.model.type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class Frame {

    @Getter @Setter
    private int wait;
    @Getter
    private List<Structure> structures;

    public Frame(int wait) {
        this.wait = wait;
        this.structures = new ArrayList<>();
    }

}
