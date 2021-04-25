package engine;

import kernel.Input;
import kernel.Loader;
import kernel.Window;
import org.lwjgl.glfw.GLFW;

public abstract class NoviEngine extends Thread implements Runnable {

    public Window window;
    public Loader loader;

    public NoviEngine(int width, int height, String title) {
        super( "NoviEngine");
        window = new Window(width, height, title);
        loader = new Loader();
    }

    public abstract void init();

    public abstract void update();

    public abstract void render();

    public abstract void input();

    public abstract void clean();

    @Override
    public void run() {
        window.create();
        init();
        while (!window.shouldClose() && !Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            window.update();
            update();
            render();
            window.swapBuffers();
            input();
        }
        loader.clean();
        window.clean();
        clean();
    }

}
