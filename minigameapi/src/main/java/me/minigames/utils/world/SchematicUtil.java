package me.minigames.utils.world;

import me.minigames.Minigame;
import me.minigames.MinigamePlugin;
import net.minecraft.server.v1_12_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BlockVector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Digital on 21/01/2018.
 */
public class SchematicUtil {

    /**
     * Very hacky schematicloader based off WorldEdits loader
     *
     * @param filename the filename of the schematic
     * @param w the world to paste it in
     * @param loc the location to base the paste off
     * @return list of affected blocks
     * @throws IOException when shit hits the fan
     */
    public static ArrayList<Block> loadAndPaste(String filename, World w, Location loc) throws IOException {
        File schematicFile = new File(MinigamePlugin.getInstance().getDataFolder().getParentFile().getParentFile(), filename);
        FileInputStream stream = new FileInputStream(schematicFile);

        NBTTagCompound nbtdata = NBTCompressedStreamTools.a(stream);
        short width = nbtdata.getShort("Width");
        short height = nbtdata.getShort("Height");
        short length = nbtdata.getShort("Length");

        byte[] blockBytes = nbtdata.getByteArray("Blocks");
        byte[] datas = nbtdata.getByteArray("Data");

        short[] blocks = new short[blockBytes.length];
        byte[] addId = new byte[0];

        if (nbtdata.hasKey("AddBlocks")) {
            addId = nbtdata.getByteArray("AddBlocks");
        }

        for (int index = 0; index < blockBytes.length; index++) {
            if ((index >> 1) >= addId.length) {
                blocks[index] = (short) (blockBytes[index] & 0xFF);
            } else {
                if ((index & 1) == 0) {
                    blocks[index] = (short) (((addId[index >> 1] & 0x0F) << 8) + (blockBytes[index] & 0xFF));
                } else {
                    blocks[index] = (short) (((addId[index >> 1] & 0xF0) << 4) + (blockBytes[index] & 0xFF));
                }
            }
        }

        ArrayList<Block> blockList = new ArrayList<>();

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    BlockVector pt = new BlockVector(x, y, z);

                    Byte data = datas[index];
                    Short block = blocks[index];

                    Location cloned = loc.clone();
                    cloned.add(x, y, z);

                    blockList.add(cloned.getBlock());
                    cloned.getBlock().setTypeIdAndData(block, data, true);
                }
            }
        }

        return blockList;
    }

}
