package com.glowman554.block.world;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.glowman554.block.block.*;

import java.util.Random;

public class Chunk implements Disposable {

    public final static int chunk_size = 8;
    public final static float field_size = 4;

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

    public void editBoxByRayCast(Vector3 start_point, Vector3 direction, Block.Type type) {
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
                        case DirtBlock:
                            this.field[last_point_x][last_point_y][last_point_z] = new DirtBlock();
                            this.updatePosition();
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
}
