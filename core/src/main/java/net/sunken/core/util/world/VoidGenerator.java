package net.sunken.core.util.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class VoidGenerator extends ChunkGenerator {

    public List<BlockPopulator> getDefaultPopulators(final World world) {
        final ArrayList<BlockPopulator> populators = new ArrayList<BlockPopulator>();
        return populators;
    }

    public ChunkGenerator.ChunkData generateChunkData(final World world, final Random random, final int ChunkX, final int ChunkZ, final ChunkGenerator.BiomeGrid biome) {
        final ChunkGenerator.ChunkData data = this.createChunkData(world);
        return data;
    }
}
