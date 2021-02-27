package com.glowman554.block;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
import com.glowman554.block.multiplayer.ServerConnection;
import com.glowman554.block.utils.FileUtils;
import com.glowman554.block.world.Chunk;
import com.glowman554.block.world.World;

import java.io.IOException;

public class BlockGame extends ApplicationAdapter {

	public final float field_of_view = 67;
	public final float camera_near = 1;
	public final float camera_far = 300;
	public final float camera_velocity = 40;
	public final float camera_degrees_per_pixel = 0.08f;
	public final float corsair_size = 25;

	public FPSController camera_controller;
	public Environment environment;
	public ModelBatch model_batch;
	public SpriteBatch sprite_batch;
	public PerspectiveCamera camera;
	public BitmapFont font;
	public Texture corsair;

	public boolean online = false;

	public ServerConnection serverConnection;

	public World world;

	@Override
	public void create () {
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
		corsair = new Texture(Gdx.files.internal("interface/Crosshair.png"));

		camera_controller = new FPSController(camera) {
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				if(button == 0) {
					world.editBoxByRayCast(camera, camera.position, camera.direction, Block.Type.DirtBlock, online, serverConnection);
				} else if(button == 1) {
					world.editBoxByRayCast(camera, camera.position, camera.direction, null, online, serverConnection);
				}
				return super.touchDown(screenX, screenY, pointer, button);
			}
		};

		world = new World();


		camera_controller.setDegreesPerPixel(camera_degrees_per_pixel);
		camera_controller.setVelocity(camera_velocity);
		Gdx.input.setInputProcessor(camera_controller);
		Gdx.input.setCursorCatched(true);
	}

	@Override
	public void render () {
		Vector3 old_pos = camera.position.cpy();
		camera_controller.update();

		if(world.hittingBox(camera, camera.position)) {
			camera.position.x = old_pos.x;
			camera.position.y = old_pos.y;
			camera.position.z = old_pos.z;
			camera.update();
		}

		Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		model_batch.begin(camera);
		world.renderWorld(model_batch, environment, camera, online, serverConnection);
		model_batch.end();

		float corsair_x = (Gdx.graphics.getWidth() - corsair_size) / 2;
		float corsair_y = (Gdx.graphics.getHeight() - corsair_size) / 2;

		sprite_batch.begin();
		world.renderDebug(font, sprite_batch, camera);
		sprite_batch.draw(corsair, corsair_x, corsair_y, corsair_size, corsair_size);
		sprite_batch.end();

		//System.out.println(String.format("Camera: %d %d", (int) camera.position.x, (int)  camera.position.z));
	}

	public void connectToServer(String host, int port) {
		this.serverConnection = new ServerConnection(host, port);
		this.serverConnection.login("Placeholder");

		this.online = true;

		new Thread("Server callback") {
			@Override
			public void run() {
				while (online) {
					String tmp = serverConnection.getEvent();

					if (tmp.contains("SetBlock")) {
						String[] t = tmp.split(" ");
						world.setBlock(t[1], Integer.parseInt(t[2]), Integer.parseInt(t[3]), Integer.parseInt(t[4]));
					}
				}
			}
		}.start();
	}
	
	@Override
	public void dispose () {
		model_batch.dispose();
		world.dispose();
	}
}
