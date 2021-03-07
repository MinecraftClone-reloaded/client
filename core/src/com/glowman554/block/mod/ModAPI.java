package com.glowman554.block.mod;

import com.glowman554.block.BlockGame;
import com.glowman554.block.world.Chunk;

public class ModAPI {
    public static void modBlock(String path, int x, int y, int z, int chunkX, int chunkY) {
        BlockGame.world.setBlock(new ModBlock(path), x, y, z, chunkX, chunkY);
    }

    public static void newChunk(boolean force, boolean generate, int x, int y) {
        if(force) {
            BlockGame.world.world[x][y] = new Chunk(generate, x, y);
        } else {
            if(BlockGame.world.world[x][y] == null) {
                BlockGame.world.world[x][y] = new Chunk(generate, x, y);
            }
        }
    }
}
