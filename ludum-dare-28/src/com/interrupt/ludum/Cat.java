package com.interrupt.ludum;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class Cat extends Entity {
	
	public float jumpTimer = 0;
	public float runTimer = 0;
	
	public Cat(Vector3 position, TextureRegion region) {
		super(position, region);
		isSolid = true;
	}

	public Cat(Vector3 position, TextureRegion region,
			TextureRegion shadowRegion, Vector3 shadowOffset, float shadowSize) {
		super(position, region, shadowRegion, shadowOffset, shadowSize);
		isSolid = true;
	}

	public void tick(float delta, Alien level) {
		super.tick(delta, level);
		
		jumpTimer += delta;
		runTimer += delta;
		
		if(runTimer > 3f && isOnGround) {
			velocity.x += (level.random.nextFloat() - 0.5f) * 0.07f;
			velocity.y += (level.random.nextFloat() - 0.5f) * 0.07f;
			runTimer = 0f;
		}
		
		if(jumpTimer > 180f && isOnGround) {
			velocity.z = 0.15f;
			jumpTimer = level.random.nextFloat() * 50f;
		}
	}
}
