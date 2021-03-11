package com.glowman554.block;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.glowman554.block.block.Block;
import com.glowman554.block.command.CommandEvent;
import com.glowman554.block.command.CommandManager;
import com.glowman554.block.command.impl.ExitCommand;
import com.glowman554.block.command.impl.LoadCommand;
import com.glowman554.block.command.impl.SaveCommand;
import com.glowman554.block.discord.DiscordRP;
import com.glowman554.block.gui.ChatRenderer;
import com.glowman554.block.mod.ModEvent;
import com.glowman554.block.mod.ModLoader;
import com.glowman554.block.multiplayer.ServerConnection;
import com.glowman554.block.utils.FileUtils;
import com.glowman554.block.world.Chunk;
import com.glowman554.block.world.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BlockGame extends ApplicationAdapter {

    public static BlockGame game;

    public ModelBatch model_batch;
    public SpriteBatch sprite_batch;
    public PerspectiveCamera camera;
    public BitmapFont font;
    public World world;
    public ChatRenderer chatRenderer;
    public CommandManager commandManager;
    public final float field_of_view = 67;
    public final float camera_near = 1;
    public final float camera_far = 300;
    public final float camera_velocity = 40;
    public final float camera_degrees_per_pixel = 0.08f;
    public final float corsair_size = 25;
    private final String username;
    private final String host;
    private final int port;
    public FPSController camera_controller;
    public Environment environment;
    public Texture corsair;
    public Block.Type currentBlock = Block.Type.DirtBlock;
    public boolean online = false;
    public boolean debug_overlay = false;

    public List<String> chat = new ArrayList<>();
    public boolean input = false;

    public ServerConnection serverConnection;

    public String event = "";
    private ModLoader modLoader;

    public BlockGame(String name, boolean online, String host, int port) {
        this.username = name;
        this.host = host;
        this.port = port;
        this.online = online;

        game = this;
    }

    @Override
    public void create() {
        DiscordRP.getDiscordRP().start();
        try {
            modLoader = new ModLoader();
        } catch (IOException e) {
            e.printStackTrace();
        }
        model_batch = new ModelBatch();
        sprite_batch = new SpriteBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.5f));
        environment.add(new DirectionalLight().set(0.2f, 0.2f, 0.2f, 1f, 0.8f, 0.5f));

        camera = new PerspectiveCamera(field_of_view, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0, 10f, 10f);
        camera.near = camera_near;
        camera.far = camera_far;
        camera.update();

        font = new BitmapFont(Gdx.files.internal("font/Calibri.fnt"), false);
        corsair = new Texture(Gdx.files.internal("interface/corsair.png"));

        chatRenderer = new ChatRenderer();

        commandManager = new CommandManager();
        commandManager.registerCommand("exit", "exit game", new ExitCommand());
        commandManager.registerCommand("save", "save world", new SaveCommand());
        commandManager.registerCommand("load", "load world", new LoadCommand());

        camera_controller = new FPSController(camera) {

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {

                if(!ModEvent.callEvent("touchDown", new int[]{screenX, screenY, pointer, button})) {
                    return super.touchDown(screenX, screenY, pointer, button);
                }

                if (button == 0) {
                    world.editBoxByRayCast(camera, camera.position, camera.direction, currentBlock, online, serverConnection);
                } else if (button == 1) {
                    world.editBoxByRayCast(camera, camera.position, camera.direction, (Block.Type) null, online, serverConnection);
                }
                return super.touchDown(screenX, screenY, pointer, button);
            }

            @Override
            public boolean keyDown(int keycode) {
                if(!ModEvent.callEvent("keyDown", keycode)) {
                    return super.keyDown(keycode);
                }

                if(input) {
                    if(keycode == Input.Keys.BACKSPACE) {
                        if(chat.size() != 0) {
                            chat.remove(chat.size() - 1);
                        }
                    } else if(keycode == Input.Keys.SPACE) {
                        chat.add(" ");
                    } else  if(keycode == Input.Keys.ENTER) {
                        input = false;
                        commandManager.onCommand(new CommandEvent(toText(chat).toLowerCase(), toText(chat).toLowerCase().split(" ")[0], CommandEvent.getArguments(toText(chat).toLowerCase().split(" "))));
                    } else {
                        chat.add(Input.Keys.toString(keycode));
                    }

                    return false;
                }

                switch (keycode) {
                    case Input.Keys.F1:
                        try {
                            FileUtils.writeFile(world.save(), "world.msave");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Input.Keys.F2:
                        try {
                            world.load(FileUtils.readFile("world.msave"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case Input.Keys.F3:
                        debug_overlay = !debug_overlay;
                        if (debug_overlay) {
                            if (!online) {
                                DiscordRP.getDiscordRP().update("Debugging Singleplayer", "In Game");
                            } else {
                                DiscordRP.getDiscordRP().update("Debugging Multiplayer", "In Game");
                            }
                        } else {
                            if (!online) {
                                DiscordRP.getDiscordRP().update("Playing Singleplayer", "In Game");
                            } else {
                                DiscordRP.getDiscordRP().update(String.format("Playing on %s:%d", serverConnection.host, serverConnection.port), "In Game");
                            }
                        }
                        break;

                    case Input.Keys.NUM_0:
                        currentBlock = Block.Type.BerryBlock;
                        break;
                    case Input.Keys.NUM_1:
                        currentBlock = Block.Type.DirtBlock;
                        break;
                    case Input.Keys.NUM_2:
                        currentBlock = Block.Type.GlassBlock;
                        break;
                    case Input.Keys.NUM_3:
                        currentBlock = Block.Type.GrassBlock;
                        break;
                    case Input.Keys.NUM_4:
                        currentBlock = Block.Type.LeavesBlock;
                        break;
                    case Input.Keys.NUM_5:
                        currentBlock = Block.Type.StoneBlock;
                        break;
                    case Input.Keys.NUM_6:
                        currentBlock = Block.Type.TestBlock;
                        break;
                    case Input.Keys.NUM_7:
                        currentBlock = Block.Type.WoodBlock;
                        break;
                    case Input.Keys.T:
                        input = true;
                        chat = new ArrayList<>();
                        break;


                }

                return super.keyDown(keycode);
            }
        };

        world = new World();

        camera_controller.setDegreesPerPixel(camera_degrees_per_pixel);
        camera_controller.setVelocity(camera_velocity);
        Gdx.input.setInputProcessor(camera_controller);
        Gdx.input.setCursorCatched(true);

        DiscordRP.getDiscordRP().update("Playing Singleplayer", "In Game");

        modLoader.enableAll();

        new Thread("Timer callback") {
            @Override
            public void run() {
                while (true) {
                    ModEvent.callEvent("timerEvent", null);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        if (online) {
            chatRenderer.chat.add("Mod loader doesn't officially support online mode!");
            //connectToServer(host, port);
        }
    }

    @Override
    public void render() {

        if (online) {
            if (event.contains("sb")) {
                String[] t = event.split(" ");
                world.setBlock(t[1], Integer.parseInt(t[2]), Integer.parseInt(t[3]), Integer.parseInt(t[4]), Integer.parseInt(t[5]), Integer.parseInt(t[6]));
            }
        }

        Vector3 old_pos = camera.position.cpy();
        camera_controller.update();

        if (world.hittingBox(camera, camera.position)) {
            camera.position.x = old_pos.x;
            camera.position.y = old_pos.y;
            camera.position.z = old_pos.z;
            camera.update();
        }

        Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        model_batch.begin(camera);
        world.renderWorld(model_batch, environment, camera, online, serverConnection);

        if(!ModEvent.callEvent("renderModelBatch", model_batch)) {
            model_batch.end();
            return;
        }
        model_batch.end();

        float corsair_x = (Gdx.graphics.getWidth() - corsair_size) / 2;
        float corsair_y = (Gdx.graphics.getHeight() - corsair_size) / 2;

        sprite_batch.begin();
        if (debug_overlay) {
            world.renderDebug(font, sprite_batch, camera);
            font.draw(sprite_batch, String.valueOf(currentBlock), 10, Gdx.graphics.getHeight() - 5 * 30);
            font.draw(sprite_batch, String.format("%d Mods loaded!", modLoader.mods.size()), 10, Gdx.graphics.getHeight() - 6 * 30);
            chatRenderer.render(sprite_batch, 7);

        } else {
            font.draw(sprite_batch, String.valueOf(currentBlock), 10, Gdx.graphics.getHeight());
            chatRenderer.render(sprite_batch, 1);
        }

        if(input) {
            font.draw(sprite_batch, "> " + toText(chat).toLowerCase(), 10, 30);
        }
        sprite_batch.draw(corsair, corsair_x, corsair_y, corsair_size, corsair_size);

        if(!ModEvent.callEvent("renderSpriteBatch", sprite_batch)) {
            sprite_batch.end();
            return;
        }

        sprite_batch.end();

        //System.out.println(String.format("Camera: %d %d", (int) camera.position.x, (int)  camera.position.z));
    }

    public String toText(List<String> a) {
        final String[] text = {""};

        a.forEach(data -> {
            text[0] = text[0] + data;
        });

        return text[0];
    }


    public void connectToServer(String host, int port) {
        DiscordRP.getDiscordRP().update(String.format("Playing on %s:%d", host, port), "In Game");

        this.serverConnection = new ServerConnection(host, port);
        this.serverConnection.login(username);

        this.online = true;

        new Thread("Server callback") {
            @Override
            public void run() {
                while (online) {
                    event = serverConnection.getEvent();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public void dispose() {
        model_batch.dispose();
        world.dispose();
        Chunk.disposeSound();
        modLoader.disableAll();
    }
}
