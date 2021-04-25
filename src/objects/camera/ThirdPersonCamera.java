package objects.camera;

import objects.GameObject;
import org.lwjgl.glfw.GLFW;

import kernel.Input;
import maths.Vector3f;

public class ThirdPersonCamera extends Camera {

    private float distance = 2.0f, horizontalAngle = 0, verticalAngle = 0;
    private GameObject object;

    public ThirdPersonCamera(Vector3f position, Vector3f rotation, GameObject object) {
        super(position, rotation);
        this.object = object;
    }

    public void update() {
        updateMousePos();

        float dx = (float) (newMouseX - oldMouseX);
        float dy = (float) (newMouseY - oldMouseY);

        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            verticalAngle -= dy * mouseSensitivity;
            horizontalAngle += dx * mouseSensitivity;
        }
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            if (distance > 0) {
                distance += dy * mouseSensitivity / 4;
            } else {
                distance = 0.1f;
            }
        }

        float horizontalDistance = (float) (distance * Math.cos(Math.toRadians(verticalAngle)));
        float verticalDistance = (float) (distance * Math.sin(Math.toRadians(verticalAngle)));

        float xOffset = (float) (horizontalDistance * Math.sin(Math.toRadians(-horizontalAngle)));
        float zOffset = (float) (horizontalDistance * Math.cos(Math.toRadians(-horizontalAngle)));

        position.set(object.getPosition().getX() + xOffset, object.getPosition().getY() - verticalDistance, object.getPosition().getZ() + zOffset);

        rotation.set(verticalAngle, -horizontalAngle, 0);

        swapMousePos();
    }

    public GameObject getObject() {
        return object;
    }

    public void setObject(GameObject object) {
        this.object = object;
    }

}