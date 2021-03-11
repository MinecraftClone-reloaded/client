package com.glowman554.block.mod;

import com.glowman554.block.BlockGame;
import com.glowman554.block.block.Block;
import com.glowman554.block.discord.WebHookAPI;
import com.glowman554.block.world.Chunk;

import java.io.IOException;

public class ModAPI {
    public static void modBlock(String path, int x, int y, int z, int chunkX, int chunkY) {
        BlockGame.game.world.setBlock(new ModBlock(path), x, y, z, chunkX, chunkY);
    }

    public static ModBlock newModBlock(String path) {
        return new ModBlock(path);
    }

    public static void newChunk(boolean force, boolean generate, int x, int y) {
        if (force) {
            BlockGame.game.world.world[x][y] = new Chunk(generate, x, y);
        } else {
            if (BlockGame.game.world.world[x][y] == null) {
                BlockGame.game.world.world[x][y] = new Chunk(generate, x, y);
            }
        }
    }

    public static void sendHookMessage(String hook, String what) throws IOException {
        WebHookAPI webHookAPI = new WebHookAPI(hook);
        webHookAPI.setContent(what);
        webHookAPI.execute();
    }

    public static void sendHookMessage(String hook, String what, String user) throws IOException {
        WebHookAPI webHookAPI = new WebHookAPI(hook);
        webHookAPI.setUsername(user);
        webHookAPI.setContent(what);
        webHookAPI.execute();
    }

    public static void sendHookMessage(String hook, String what, String user, String icon) throws IOException {
        WebHookAPI webHookAPI = new WebHookAPI(hook);
        webHookAPI.setUsername(user);
        webHookAPI.setAvatarUrl(icon);
        webHookAPI.setContent(what);
        webHookAPI.execute();
    }
}
