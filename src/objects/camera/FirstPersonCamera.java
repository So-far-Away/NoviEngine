package objects.camera;

import objects.GameObject;
import org.lwjgl.glfw.GLFW;

import kernel.Input;
import maths.Vector3f;

public class FirstPersonCamera extends Camera{

    public FirstPersonCamera(Vector3f position, Vector3f rotation) {
        super(position, rotation);
    }

    public void update() {
        updateMousePos();

        float x = (float) Math.sin(Math.toRadians(rotation.getY())) * moveSpeed;
        float z = (float) Math.cos(Math.toRadians(rotation.getY())) * moveSpeed;

        if (Input.isKeyDown(GLFW.GLFW_KEY_A)) position = Vector3f.add(position, new Vector3f(-z, 0, x));
        if (Input.isKeyDown(GLFW.GLFW_KEY_D)) position = Vector3f.add(position, new Vector3f(z, 0, -x));
        if (Input.isKeyDown(GLFW.GLFW_KEY_W)) position = Vector3f.add(position, new Vector3f(-x, 0, -z));
        if (Input.isKeyDown(GLFW.GLFW_KEY_S)) position = Vector3f.add(position, new Vector3f(x, 0, z));
        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) position = Vector3f.add(position, new Vector3f(0, moveSpeed, 0));
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) position = Vector3f.add(position, new Vector3f(0, -moveSpeed, 0));

        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_1)){
            float dx = (float) (newMouseX - oldMouseX);
            float dy = (float) (newMouseY - oldMouseY);
            rotation = Vector3f.add(rotation, new Vector3f(-dy * mouseSensitivity, -dx * mouseSensitivity, 0));
        }

        swapMousePos();
    }

}