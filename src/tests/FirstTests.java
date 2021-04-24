package tests;

import kernel.Engine;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;

/**
 * available classes to change are:
 * Thread.java -> game
 * Window.java -> window
 * Timer.java -> timer
 * Input.java -> input
 */
public class FirstTests extends Engine {

    public FirstTests() {
        super(300, 300, "NoviLinux", true, false);
    }

    public static void main(String[] args) {
        new FirstTests();
    }

    public void init() {
    }

    public void input() {
        if (input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) System.out.println("X: " + input.getMouseX() + ", Y: " + input.getMouseY());
        if (input.isKeyDown(GLFW.GLFW_KEY_F11)) {
            window.setFullScreen(!window.isFullScreen());
        }
    }

    public void update() {
        window.setTitle("NoviEngine --> " + timer.getFPS() + " FPS");
    }

    public void render() {
    }

    public void close() {
    }
}
