package objects.camera;

import objects.GameObject;
import org.lwjgl.glfw.GLFW;

import kernel.Input;
import maths.Vector3f;

public abstract class Camera {
	public Vector3f position, rotation;
	public float moveSpeed = 0.005f, mouseSensitivity = 0.15f;
	public double oldMouseX = 0, oldMouseY = 0, newMouseX, newMouseY;

	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}

	public void updateMousePos(){
		newMouseX = Input.getMouseX();
		newMouseY = Input.getMouseY();
	}

	public void swapMousePos(){
		oldMouseX = newMouseX;
		oldMouseY = newMouseY;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public abstract void update();
}