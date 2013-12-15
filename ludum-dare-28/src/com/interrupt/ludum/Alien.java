package com.interrupt.ludum;

import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.interrupt.ludum.gfx.PlaneGroupStrategy;

public class Alien implements ApplicationListener {
	public Camera camera;
	private ArrayMap<String, Texture> textures = new ArrayMap<String, Texture>();
	private ArrayMap<String, TextureRegion> textureRegions = new ArrayMap<String, TextureRegion>();
	private DecalBatch decalBatch;
	private Entity alien;
	public Random random = new Random();
	private Input input = new Input();
	
	private PlaneGroupStrategy decalDrawingStrategy;
	
	Vector3 cameraLookAt = new Vector3();
	Vector3 cameraChase = new Vector3();
	
	public Array<Entity> entities = new Array<Entity>();
	public Array<Entity> staticEntities = new Array<Entity>();
	
	@Override
	public void create() {		
		Gdx.input.setInputProcessor(input);
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new PerspectiveCamera(45, w, h);
        camera.near = 0.8f;
        camera.far = 150;
        camera.position.set(0, 0, 5);
        camera.direction.set(0,0.65f,-1);
		
        decalDrawingStrategy = new PlaneGroupStrategy(camera);
		decalBatch = new DecalBatch(decalDrawingStrategy);
		
		alien = new Entity(new Vector3(0,0,6), makeRegion("data/alien1.png"), makeRegion("data/shadow.png"), null, 1f);
		alien.isSolid = true;
		entities.add(alien);
		
		float levelWidth = 100;
		int numEntities = 2;
		
		for(int i = 0; i < 100 * numEntities; i++) {
			Entity spire = new Entity(new Vector3((random.nextFloat() - 0.5f) * levelWidth,(random.nextFloat() - 0.5f) * levelWidth,0), makeRegion("data/spire1.png"), makeRegion("data/spire1-shadow.png"), new Vector3(1f,0,0), 2f);
			spire.size = 1f + random.nextFloat();
			spire.isSolid = true;
			entities.add(spire);
		}
		
		for(int i = 0; i < 3 * numEntities; i++) {
			Entity spire = new Entity(new Vector3((random.nextFloat() - 0.5f) * levelWidth,(random.nextFloat() - 0.5f) * levelWidth,0), makeRegion("data/spire1.png"), makeRegion("data/spire1-shadow.png"), new Vector3(1f,0,0), 2f);
			spire.size = 3f + random.nextFloat() * 2f;
			spire.isSolid = true;
			entities.add(spire);
		}
		
		for(int i = 0; i < 55 * numEntities; i++) {
			Entity shadow = new Entity(new Vector3((random.nextFloat() - 0.5f) * levelWidth,(random.nextFloat() - 0.5f) * levelWidth,0), makeRegion("data/shadow1.png"));
			shadow.size = (1.5f + random.nextFloat() * 2f);
			staticEntities.add(shadow);
		}
		
		for(int i = 0; i < 75 * numEntities; i++) {
			Entity shadow = new Entity(new Vector3((random.nextFloat() - 0.5f) * levelWidth,(random.nextFloat() - 0.5f) * levelWidth,0), makeRegion("data/shadow2.png"));
			shadow.size = (0.5f + random.nextFloat()) * 0.5f;
			staticEntities.add(shadow);
		}
		
		for(int i = 0; i < 85 * numEntities; i++) {
			Entity shadow = new Entity(new Vector3((random.nextFloat() - 0.5f) * levelWidth,(random.nextFloat() - 0.5f) * levelWidth,0), makeRegion("data/shadow3.png"));
			shadow.size = (0.1f + random.nextFloat()) * 0.2f;
			staticEntities.add(shadow);
		}
		
		for(int i = 0; i < 100 * numEntities; i++) {
			Entity shadow = new Entity(new Vector3((random.nextFloat() - 0.5f) * levelWidth,(random.nextFloat() - 0.5f) * levelWidth,0), makeRegion("data/rock1.png"));
			shadow.size = (0.1f + random.nextFloat() * 0.6f);
			staticEntities.add(shadow);
		}
		
		for(int i = 0; i < 220 * numEntities; i++) {
			Entity shadow = new Entity(new Vector3((random.nextFloat() - 0.5f) * levelWidth,(random.nextFloat() - 0.5f) * levelWidth,0), makeRegion("data/rock1.png"));
			shadow.size = (0.1f + random.nextFloat() * 0.1f);
			staticEntities.add(shadow);
		}
		
		for(int i = 0; i < 70 * numEntities; i++) {
			Cat cat = new Cat(new Vector3((random.nextFloat() - 0.5f) * levelWidth,(random.nextFloat() - 0.5f) * levelWidth,0), makeRegion("data/cat.png"), makeRegion("data/shadow.png"), null, 0.7f);
			cat.size = (0.85f);
			entities.add(cat);
		}
	}

	@Override
	public void dispose() {
		decalBatch.dispose();
	}

	@Override
	public void render() {
		
		float delta = Gdx.graphics.getDeltaTime() * 60f;
		
		tick(delta);
		
		Gdx.gl.glClearColor(0.45882352941176f, 0.25882352941176f, 0.23137254901961f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		camera.update();
		
		cameraLookAt.set(camera.direction);
		cameraLookAt.scl(-1f);
		
		cameraChase.set(alien.position);
		cameraChase.y -= 6f;
		cameraChase.z += 5f;
		
		camera.direction.set(0,1.2f,-1);
		
		camera.position.lerp(cameraChase, 0.07f);
		
		// draw shadows
		for(int i = 0; i < entities.size; i++) {
			Entity entity = entities.get(i); 
			if(entity.shadowDecal != null) {
				Decal decal = entity.shadowDecal;
				decalBatch.add(decal);
			}
		}
		decalDrawingStrategy.enableDepthTest = false;
		decalBatch.flush();
		
		// draw static stuff
		for(int i = 0; i < staticEntities.size; i++) {
			Entity entity = staticEntities.get(i); 
			if(entity.decal != null) {
				entity.tick(delta, this);
				Decal decal = entity.decal;
				decalBatch.add(decal);
			}
		}
		
		// draw dynamic stuff
		for(int i = 0; i < entities.size; i++) {
			Entity entity = entities.get(i); 
			if(entity.decal != null) {
				Decal decal = entity.decal;
				//decal.setRotation(cameraLookAt, camera.up);
				decalBatch.add(decal);
			}
		}
		decalDrawingStrategy.enableDepthTest = false;
		decalBatch.flush();
	}
	
	public void tick(float delta) {
		
		if(input.isKeyDown(Keys.RIGHT))
			alien.velocity.x += (alien.isOnGround ? 0.009f : 0.002f) * delta;
		if(input.isKeyDown(Keys.LEFT))
			alien.velocity.x -= (alien.isOnGround ? 0.009f : 0.002f) * delta;
		if(input.isKeyDown(Keys.UP))
			alien.velocity.y += (alien.isOnGround ? 0.009f : 0.002f) * delta;
		if(input.isKeyDown(Keys.DOWN))
			alien.velocity.y -= (alien.isOnGround ? 0.009f : 0.002f) * delta;
		if(input.isKeyDown(Keys.SPACE))
			alien.velocity.z = 0.06f;
		
		// tick entities
		for(int i = 0; i < entities.size; i++) {
			Entity entity = entities.get(i);
			entity.tick(delta, this);
		}
		
		input.tick();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	public Texture makeTexture(String filename) {
		if(textures.containsKey(filename)) return textures.get(filename);
		
		Texture texture = new Texture(Gdx.files.internal(filename));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		textures.put(filename, texture);
		
		return texture;
	}
	
	public TextureRegion makeRegion(String filename) {
		
		if(textureRegions.containsKey(filename)) return textureRegions.get(filename);
		
		Texture texture = makeTexture(filename);
		TextureRegion region = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		
		textureRegions.put(filename, region);
		
		return region;
	}
	
	public boolean isColliding(float x, float y, float z, float size, Entity checking) {
		for(int i = 0; i < entities.size; i++) {
			Entity entity = entities.get(i);
			if(entity.isSolid && entity != checking) {
				if(
						x + size * 0.4f > entity.position.x - entity.size * 0.4f &&
						x - size * 0.4f < entity.position.x + entity.size * 0.4f &&
						y + size * 0.4f > entity.position.y - entity.size * 0.4f &&
						y - size * 0.4f < entity.position.y + entity.size * 0.4f &&
						z - size * checking.ratio * 0.52f < entity.position.z + entity.size * entity.ratio * 0.52f &&
						z + size * checking.ratio * 0.52f > entity.position.z - entity.size * entity.ratio * 0.52f) {
					return true;
				}
			}
		}
		
		return false;
	}
}
