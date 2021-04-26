package tests.forest;

import engine.NoviEngine;
import objects.terrain.Terrain;
import objects.camera.FirstPersonCamera;
import org.lwjgl.glfw.GLFW;

import kernel.Input;
import maths.Vector3f;
import objects.camera.Camera;
import objects.GameObject;
import shaders.OBJShader;
import shaders.SkyShader;

import java.util.ArrayList;
import java.util.List;

public class Test extends NoviEngine {

	public OBJShader objShader;
	public SkyShader skyShader;

	public List<GameObject> objects;
	public Camera camera;

	public Test() {
		super(1280, 760, "Test");
	}

	@Override
	public void init() {
		objShader = new OBJShader(loader.loadShader("/shaders/mainVertex.glsl", "/shaders/mainFragment.glsl"));
		skyShader = new SkyShader(loader, loader.loadShader("/shaders/sky/vs.glsl", "/shaders/sky/fs.glsl")) ;

		objects = new ArrayList<>();
		objects.add(new Terrain(new Vector3f(0, 0, 0),
				new Vector3f(0, 0, 0),
				new Vector3f(1, 1, 1),
				loader.loadMaterial("/textures/image.png", false)));

		objects.add(new GameObject(new Vector3f(0, 0, 0),
				new Vector3f(0, 0, 0),
				new Vector3f(1, 1, 1),
				loader.loadModel("res/models/pine/pine.obj"),
				loader.loadMaterial("/models/pine/pine.png", true)));

		camera = new FirstPersonCamera(new Vector3f(0, 0, 1), new Vector3f(0, 0, 0));

		window.mouseState(false);
	}

	@Override
	public void update() {
		camera.update();
	}

	@Override
	public void render() {
		objShader.render(objects, window, camera);
		skyShader.render(window,camera);
	}

	@Override
	public void input() {
		if (Input.isKeyDown(GLFW.GLFW_KEY_F11)) window.setFullscreen(!window.isFullscreen());
	}

	@Override
	public void clean() {
	}

	public static void main(String[] args) {
		new Test().start();
	}
}