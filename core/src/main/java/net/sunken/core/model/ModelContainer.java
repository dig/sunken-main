package net.sunken.core.model;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.core.model.type.Position;
import net.sunken.core.model.type.Structure;
import net.sunken.core.model.type.StructureSize;
import org.bukkit.Material;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ModelContainer {

    private static final List<String> POSES = Arrays.asList("Head", "Body", "RightArm", "LeftArm", "RightLeg", "LeftLeg");

    @Getter
    private String path;
    @Getter
    private boolean loaded;

    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private long created;

    @Getter
    private List<Structure> structures;

    public ModelContainer(String path) {
        this.path = path;
        this.loaded = false;

        File file = new File(path);
        if (file.exists() && !file.isDirectory() && !file.isHidden()) {
            try {
                this.load(file);
            } catch (IOException e) {
                Common.getLogger().log(Level.WARNING, "Unable to load " + path + ", file might not be a sunken model.");
            }
        }
    }

    private void load(File file) throws IOException {
        try (ZipFile zipFile = new ZipFile(file.toPath().toString())) {
            Enumeration zipEntries = zipFile.entries();

            while (zipEntries.hasMoreElements()) {
                ZipEntry o = (ZipEntry) zipEntries.nextElement();
                String fileName = o.getName();
                InputStream is = zipFile.getInputStream(o);

                // Check if we are loading a yml file
                if (fileName.contains(".yml")) {
                    Yaml yaml = new Yaml();
                    Map<String, Object> result = (Map<String, Object>) yaml.load(is);

                    if (fileName.equalsIgnoreCase("model.yml")) {
                        this.name = (String) result.get("name");
                        this.description = (String) result.get("description");
                        this.created = (Long) result.get("created");
                    } else if (fileName.startsWith("structure/")) {
                        fileName = fileName.replaceAll("structure/", "");

                        Material material = Material.valueOf(((String) result.get("material")).toUpperCase());
                        StructureSize size = StructureSize.valueOf((String) result.get("size"));
                        boolean visible = (boolean) result.get("visible");

                        Position position = new Position(
                                ((Double) result.get("location.x")),
                                ((Double) result.get("location.y")),
                                ((Double) result.get("location.z"))
                        );

                        Map<String, Position> pose = new HashMap<>();
                        if (result.containsKey("pose")) {
                            for (String type : POSES) {
                                if (result.containsKey("pose." + type)) {
                                    Position pos = new Position(
                                            ((Double) result.get("pose." + type + ".x")),
                                            ((Double) result.get("pose." + type + ".y")),
                                            ((Double) result.get("pose." + type + ".z"))
                                    );

                                    pose.put(type, pos);
                                }
                            }
                        }

                        Structure structure = new Structure(material, size, visible, position, pose);
                        this.structures.add(structure);
                    }
                }
            }

            this.loaded = true;
        }
    }

}
