package net.sunken.core.model.type;

import lombok.Getter;
import org.bukkit.Material;

import java.util.Map;

public class Structure {

    @Getter
    private String material;
    @Getter
    private StructureSize size;
    @Getter
    private boolean visible;

    @Getter
    private Position position;

    @Getter
    private Map<String, Position> pose;

    public Structure(String material, StructureSize size, boolean visible, Position position, Map<String, Position> pose){
        this.material = material;
        this.size = size;
        this.visible = visible;
        this.position = position;
        this.pose = pose;
    }

}
