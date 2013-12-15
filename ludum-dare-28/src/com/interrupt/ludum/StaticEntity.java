package com.interrupt.ludum;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class StaticEntity extends Entity {
	
	public StaticEntity(Vector3 position, TextureRegion region) {
		super(position, region);
	}
	
	public StaticEntity(Vector3 position, TextureRegion region, TextureRegion shadowRegion, Vector3 shadowOffset, float shadowSize) {
		super(position, region, shadowRegion, shadowOffset, shadowSize);
	}
	
	public void tick(float delta, Alien level) {
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
		}
	}
}
