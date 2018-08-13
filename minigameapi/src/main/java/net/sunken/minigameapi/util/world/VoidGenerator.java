package net.sunken.minigameapi.util.world;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class VoidGenerator extends ChunkGenerator {

    public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomes) {
        return new byte[16][];
    }
}
