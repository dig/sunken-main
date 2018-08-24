package net.sunken.core.model.type;

import lombok.Getter;
import org.bukkit.Material;

import java.util.Map;

public class Structure {

    @Getter
    private String fileName;

    @Getter
    private String material;
    @Getter
    private StructureSize size;
    @Getter
    private boolean visible;

    @Getter
    private Position position;
    @Getter
    private Rotation rotation;

    @Getter
    private Map<String, Position> pose;

    @Getter
    private Head head;

    public Structure(String fileName, String material, StructureSize size, boolean visible, Position position,
                     Rotation rotation, Map<String, Position> pose){
        this.fileName = fileName;
        this.material = material;
        this.size = size;
        this.visible = visible;
        this.position = position;
        this.rotation = rotation;
        this.pose = pose;
        this.head = null;
    }

    public Structure(String fileName, String material, StructureSize size, boolean visible, Position position,
                     Rotation rotation, Map<String, Position> pose, Head head){
        this(fileName, material, size, visible, position, rotation, pose);
        this.head = head;
    }

}
