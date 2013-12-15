package com.interrupt.ludum;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;

public class Input implements InputProcessor {
	
	public IntMap<Boolean> keysDown = new IntMap<Boolean>();
	public IntArray keyEvents = new IntArray();
	
	public void tick() { keyEvents.clear(); }

	@Override
	public boolean keyDown(int keycode) {
		keysDown.put(keycode, true);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		keysDown.put(keycode, false);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		keyEvents.add((int)character);
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean isKeyDown(int keycode) {
		if(!keysDown.containsKey(keycode)) return false;
		return keysDown.get(keycode);
	}

}
