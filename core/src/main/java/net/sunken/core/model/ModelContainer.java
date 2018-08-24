package net.sunken.core.model;

import lombok.Getter;
import net.sunken.common.Common;
import net.sunken.core.model.type.*;
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

    /*
        Each model has a convert.yml, which allows to easily update the models
        when a new update is out.
    */
    @Getter
    private Map<String, String> conversion;
    @Getter
    private List<Structure> structures;
    @Getter
    private Map<String, Animation> animations;

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
        Common.getLogger().log(Level.INFO, "Loading " + this.path);
        this.conversion = new HashMap<>();
        this.structures = new ArrayList<>();
        this.animations = new HashMap<>();

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
                    } else if (fileName.equalsIgnoreCase("convert.yml")) {
                        if (result.size() > 0) {
                            for (String line : result.keySet()) {
                                String find = (String) result.get(line);
                                conversion.put(line, find);
                                Common.getLogger().log(Level.INFO, "Converting " + line + " to " + find);
                            }
                        }
                    } else if (fileName.startsWith("structure/")) {
                        fileName = fileName.replaceAll("structure/", "");
                        fileName = fileName.replaceAll(".yml", "");

                        Common.getLogger().log(Level.INFO, "Loading structure " + fileName);
                        this.structures.add(this.getStructure(fileName, result));
                    } else if (fileName.startsWith("animation/")) {
                        String[] path = fileName.split("/");

                        if (path.length == 4) {
                            String animationName = path[1];
                            int frameNum = Integer.parseInt(path[2]);
                            fileName = path[3].replaceAll(".yml", "");

                            if (!this.animations.containsKey(animationName)) {
                                this.animations.put(animationName, new Animation(animationName));
                            }

                            Animation anim = this.animations.get(animationName);
                            if (fileName.equalsIgnoreCase("frame")) {
                                Common.getLogger().log(Level.INFO, "Loading animaton frame " + frameNum + " for " + animationName);

                                int wait = (int) result.get("wait");
                                if (anim.getFrames().containsKey(frameNum)) {
                                    anim.getFrames().get(frameNum).setWait(wait);
                                } else {
                                    anim.getFrames().put(frameNum, new Frame(wait));
                                }
                            } else {
                                Common.getLogger().log(Level.INFO, "Loading animation structure " + fileName + " for " + animationName);

                                Structure structure = this.getStructure(fileName, result);
                                if (anim.getFrames().containsKey(frameNum)) {
                                    Frame frame = anim.getFrames().get(frameNum);
                                    frame.getStructures().add(structure);
                                } else {
                                    Frame frame = new Frame(50);
                                    frame.getStructures().add(structure);
                                    anim.getFrames().put(frameNum, frame);
                                }
                            }
                        }
                    }
                }
            }

            Common.getLogger().log(Level.INFO, "Container loaded!");
            this.loaded = true;
        }
    }

    private Structure getStructure(String fileName, Map<String, Object> yml) {
        String matRaw = ((String) yml.get("material")).toUpperCase();
        StructureSize size = StructureSize.valueOf((String) yml.get("size"));
        boolean visible = (boolean) yml.get("visible");

        Map<String, Object> location = (Map<String, Object>) yml.get("location");
        Position position = new Position(
                ((double) location.get("x")),
                ((double) location.get("y")),
                ((double) location.get("z"))
        );

        Rotation rotation = new Rotation(0, 0);
        if (yml.containsKey("rotation")) {
            Map<String, Object> rotate = (Map<String, Object>) yml.get("rotation");
            rotation = new Rotation(Double.valueOf((double) rotate.get("x")).floatValue(),
                    Double.valueOf((double) rotate.get("y")).floatValue());
        }

        Map<String, Position> pose = new HashMap<>();
        if (yml.containsKey("pose")) {
            Map<String, Object> poseCfg = (Map<String, Object>) yml.get("pose");

            for (String type : POSES) {
                if (poseCfg.containsKey(type)) {
                    Map<String, Object> singlePose = (Map<String, Object>) poseCfg.get(type);

                    Position pos = new Position(
                            ((double) singlePose.get("x")),
                            ((double) singlePose.get("y")),
                            ((double) singlePose.get("z"))
                    );

                    pose.put(type, pos);
                }
            }
        }

        Structure structure = new Structure(fileName, matRaw, size, visible, position, rotation, pose);
        return structure;
    }

}
