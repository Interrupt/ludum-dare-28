package com.interrupt.ludum;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Entity {
	public Vector3 position = new Vector3();
	public Vector3 velocity = new Vector3();
	
	public Decal decal;
	public float size = 1f;
	public float ratio;
	
	public Decal shadowDecal;
	public float shadowRatio = 1f;
	public Vector3 shadowOffset = new Vector3();
	public float shadowSize = 1f;
	
	public boolean isSolid = false;
	
	public boolean isOnGround = false;
	
	public Entity(Vector3 position, TextureRegion region) {
		this.position.set(position);
		ratio = (float)region.getRegionHeight() / (float)region.getRegionWidth();
		decal = Decal.newDecal(1f, 1f * ratio, region, true);
	}
	
	public Entity(Vector3 position, TextureRegion region, TextureRegion shadowRegion, Vector3 shadowOffset, float shadowSize) {
		this.position.set(position);
		ratio = (float)region.getRegionHeight() / (float)region.getRegionWidth();
		decal = Decal.newDecal(1f, 1f * ratio, region, true);
		
		shadowRatio = (float)shadowRegion.getRegionHeight() / (float)shadowRegion.getRegionWidth();
		shadowDecal = Decal.newDecal(1f, 1f * ratio, shadowRegion, true);
		
		if(shadowOffset != null) this.shadowOffset.set(shadowOffset);
		this.shadowSize = shadowSize;
	}

	public void tick(float delta, Alien level) {
		
		boolean onEntity = false;
		
		// gravity
		if(position.z > 0) {
			velocity.z -= 0.009f * delta;
		}
		
		if(isSolid && (Math.abs(velocity.x) > 0 || Math.abs(velocity.y) > 0 || Math.abs(velocity.z) > 0)) {
			if(!level.isColliding(position.x + velocity.x * delta, position.y, position.z, size, this))
				position.x += velocity.x * delta;
			else
				velocity.x = 0;
			
			if(!level.isColliding(position.x, position.y + velocity.y * delta, position.z, size, this))
				position.y += velocity.y * delta;
			else
				velocity.y = 0;
			
			if(!level.isColliding(position.x, position.y, position.z + velocity.z * delta, size, this)) {
				position.z += velocity.z * delta;
			}
			else {
				velocity.z = 0;
				onEntity = true;
			}
		}
		else {
			position.x += velocity.x * delta;
			position.y += velocity.y * delta;
			position.z += velocity.z * delta;
		}
		
		
		if(onEntity) {
			// entity friction
			velocity.x *= 0.9f * delta;
			velocity.y *= 0.9f * delta;
			velocity.z = 0;
			
			isOnGround = true;
		}
		else {
			// ground plane collision
			if(position.z <= 0) {
				position.z = 0;
	
				// ground friction
				velocity.x *= 0.9f * delta;
				velocity.y *= 0.9f * delta;
				velocity.z = 0;
				
				isOnGround = true;
			}
			else {
				// air friction
				velocity.x *= 0.995f * delta;
				velocity.y *= 0.995f * delta;
				
				isOnGround = false;
			}
		}
		
		if(decal != null) {
			decal.setWidth(size);
			decal.setHeight(size * ratio);
			decal.setPosition(position.x, position.y, position.z + (size * ratio) * 0.5f);
			
			if(shadowDecal != null) {
				shadowDecal.setPosition(position.x + (shadowOffset.x * size), position.y + shadowOffset.y, shadowOffset.z);
				shadowDecal.setWidth(shadowSize * size);
				shadowDecal.setHeight(shadowSize * size * shadowRatio);
			}
			
			decal.setRotation(level.cameraLookAt, level.camera.up);
			decal.rotateZ(velocity.x * -120f);
			decal.rotateX(velocity.y * -90f);
		}
	}
}
