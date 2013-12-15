package com.interrupt.ludum;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "ludum-dare-28";
		cfg.useGL20 = true;
		cfg.width = 480;
		cfg.height = 320;
		cfg.vSyncEnabled = true;
		
		new LwjglApplication(new Alien(), cfg);
	}
}
