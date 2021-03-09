package com.glowman554.block.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.glowman554.block.block.Block;
import com.glowman554.block.mod.ModEvent;
import com.glowman554.block.multiplayer.ServerConnection;

public class World implements Disposable {
    private final static int world_size = 128;
    private final static int render_distance = 2;

    public Chunk world[][];

    public World() {
        this.world = new Chunk[world_size][world_size];
    }

    public void renderDebug(BitmapFont font, SpriteBatch batch, Camera camera) {
        try {

            int chunkX = (int) (camera.position.x / Chunk.chunk_size / Chunk.field_size);
            int chunkY = (int) (camera.position.z / Chunk.chunk_size / Chunk.field_size);

            int playerX = (int) (camera.position.x / Chunk.field_size);
            int playerY = (int) (camera.position.y / Chunk.field_size);
            int playerZ = (int) (camera.position.z / Chunk.field_size);


            int chunk_seed = world[chunkX][chunkY].seed;

            font.draw(batch, String.format("FPS: %d", Gdx.graphics.getFramesPerSecond()), 10, Gdx.graphics.getHeight());
            font.draw(batch, String.format("Camera: %d %d %d", (int) camera.position.x, (int) camera.position.y, (int) camera.position.z), 10, Gdx.graphics.getHeight() - 1 * 30);
            font.draw(batch, String.format("Player: %d %d %d", playerX, playerY, playerZ), 10, Gdx.graphics.getHeight() - 2 * 30);
            font.draw(batch, String.format("Chunk: %d %d", chunkX, chunkY), 10, Gdx.graphics.getHeight() - 3 * 30);
            font.draw(batch, String.format("Chunk seed: %d", chunk_seed), 10, Gdx.graphics.getHeight() - 4 * 30);
        } catch (Exception e) {
            font.draw(batch, "Something is wrong: " + e.getMessage(), 10, Gdx.graphics.getHeight());
        }
    }

    public void renderWorld(ModelBatch batch, Environment environment, Camera camera, boolean online, ServerConnection serverConnection) {

        int chunkX = (int) (camera.position.x / Chunk.chunk_size / Chunk.field_size);
        int chunkY = (int) (camera.position.z / Chunk.chunk_size / Chunk.field_size);

        for (int i = 0; i < render_distance; i++) {
            for (int k = 0; k < render_distance; k++) {
                try {
                    if (this.world[chunkX + i][chunkY + k] == null) {
                        if (online) {
                            String chunk = serverConnection.getChunk(chunkX + i, chunkY + k);
                            this.world[chunkX + i][chunkY + k] = new Chunk(false, chunkX + i, chunkY + k);
                            this.world[chunkX + i][chunkY + k].load(chunk);
                            this.world[chunkX + i][chunkY + k].updatePosition();
                        } else {
                            this.world[chunkX + i][chunkY + k] = new Chunk(true, chunkX + i, chunkY + k);
                            ModEvent.data[0] = chunkX + i;
                            ModEvent.data[1] = chunkY + k;
                            ModEvent.callEvent("newChunk");
                        }
                    }
                    this.world[chunkX + i][chunkY + k].renderChunk(batch, environment);
                } catch (Exception e) {

                }

                try {
                    if (this.world[chunkX - i][chunkY - k] == null) {
                        if (online) {
                            String chunk = serverConnection.getChunk(chunkX - i, chunkY - k);
                            this.world[chunkX - i][chunkY - k] = new Chunk(false, chunkX - i, chunkY - k);
                            this.world[chunkX - i][chunkY - k].load(chunk);
                            this.world[chunkX - i][chunkY - k].updatePosition();
                        } else {
                            this.world[chunkX - i][chunkY - k] = new Chunk(true, chunkX - i, chunkY - k);
                            ModEvent.data[0] = chunkX - i;
                            ModEvent.data[1] = chunkY - k;
                            ModEvent.callEvent("newChunk");
                        }
                    }
                    this.world[chunkX - i][chunkY - k].renderChunk(batch, environment);
                } catch (Exception e) {

                }

                try {
                    if (this.world[chunkX + i][chunkY - k] == null) {
                        if (online) {
                            String chunk = serverConnection.getChunk(chunkX + i, chunkY - k);
                            this.world[chunkX + i][chunkY - k] = new Chunk(false, chunkX + i, chunkY - k);
                            this.world[chunkX + i][chunkY - k].load(chunk);
                            this.world[chunkX + i][chunkY - k].updatePosition();
                        } else {
                            this.world[chunkX + i][chunkY - k] = new Chunk(true, chunkX + i, chunkY - k);
                            ModEvent.data[0] = chunkX + i;
                            ModEvent.data[1] = chunkY - k;
                            ModEvent.callEvent("newChunk");
                        }
                    }
                    this.world[chunkX + i][chunkY - k].renderChunk(batch, environment);
                } catch (Exception e) {

                }

                try {
                    if (this.world[chunkX - i][chunkY + k] == null) {
                        if (online) {
                            String chunk = serverConnection.getChunk(chunkX - i, chunkY + k);
                            this.world[chunkX - i][chunkY + k] = new Chunk(false, chunkX - i, chunkY + k);
                            this.world[chunkX - i][chunkY + k].load(chunk);
                            this.world[chunkX - i][chunkY + k].updatePosition();
                        } else {
                            this.world[chunkX - i][chunkY + k] = new Chunk(true, chunkX - i, chunkY + k);
                            ModEvent.data[0] = chunkX - i;
                            ModEvent.data[1] = chunkY + k;
                            ModEvent.callEvent("newChunk");
                        }
                    }
                    this.world[chunkX - i][chunkY + k].renderChunk(batch, environment);
                } catch (Exception e) {

                }
            }
        }
    }

    public void editBoxByRayCast(Camera camera, Vector3 start_point, Vector3 direction, Block.Type type, boolean online, ServerConnection serverConnection) {
        int chunkX = (int) (camera.position.x / Chunk.chunk_size / Chunk.field_size);
        int chunkY = (int) (camera.position.z / Chunk.chunk_size / Chunk.field_size);

        Vector3 new_point = new Vector3();

        new_point.x = start_point.x - (Chunk.field_size * Chunk.chunk_size) * chunkX;
        new_point.y = start_point.y;
        new_point.z = start_point.z - (Chunk.field_size * Chunk.chunk_size) * chunkY;

        try {
            this.world[chunkX][chunkY].editBoxByRayCast(new_point, direction, type, online, serverConnection);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void editBoxByRayCast(Camera camera, Vector3 start_point, Vector3 direction, Block type, boolean online, ServerConnection serverConnection) {
        int chunkX = (int) (camera.position.x / Chunk.chunk_size / Chunk.field_size);
        int chunkY = (int) (camera.position.z / Chunk.chunk_size / Chunk.field_size);

        Vector3 new_point = new Vector3();

        new_point.x = start_point.x - (Chunk.field_size * Chunk.chunk_size) * chunkX;
        new_point.y = start_point.y;
        new_point.z = start_point.z - (Chunk.field_size * Chunk.chunk_size) * chunkY;

        try {
            this.world[chunkX][chunkY].editBoxByRayCast(new_point, direction, type, online, serverConnection);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean hittingBox(Camera camera, Vector3 point) {
        int chunkX = (int) (camera.position.x / Chunk.chunk_size / Chunk.field_size);
        int chunkY = (int) (camera.position.z / Chunk.chunk_size / Chunk.field_size);

        Vector3 new_point = new Vector3();

        new_point.x = point.x - (Chunk.field_size * Chunk.chunk_size) * chunkX;
        new_point.y = point.y - Chunk.field_size;
        new_point.z = point.z - (Chunk.field_size * Chunk.chunk_size) * chunkY;

        try {
            return this.world[chunkX][chunkY].hittingBox(new_point);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public String save() {

        String text = "";

        for (int i = 0; i < world_size; i++) {
            for (int k = 0; k < world_size; k++) {
                if (world[i][k] == null) {
                    text += "0<";
                } else {
                    text += world[i][k].save() + "<";
                }
            }
        }
        return text;
    }

    public void load(String text) {

        String[] world_load = text.split("<");
        int readsofar = 0;

        for (int i = 0; i < world_size; i++) {
            for (int k = 0; k < world_size; k++) {
                if (world_load[readsofar].equals("0")) {
                    world[i][k] = null;
                } else {
                    world[i][k] = new Chunk(false, i, k);
                    world[i][k].load(world_load[readsofar]);
                    world[i][k].updatePosition();
                }
                readsofar++;
            }
        }
    }

    @Override
    public void dispose() {
        for (int i = 0; i < world_size; i++) {
            for (int k = 0; k < world_size; k++) {
                if (this.world[i][k] != null) {
                    this.world[i][k].dispose();
                }
            }
        }
    }

    public void setBlock(String block, int x, int y, int z, int chunkX, int chunkY) {
        if (world[chunkX][chunkY] != null) {
            world[chunkX][chunkY].setBlock(block, x, y, z);
            world[chunkX][chunkY].updatePosition();
        }
    }

    public void setBlock(Block block, int x, int y, int z, int chunkX, int chunkY) {
        if (world[chunkX][chunkY] != null) {
            world[chunkX][chunkY].setBlock(block, x, y, z);
            world[chunkX][chunkY].updatePosition();
        }
    }
}
