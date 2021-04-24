package kernel;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

    private int width, height;
    private String title;
    private long handle;
    private boolean isRunning, isResized, isFullScreen;
    private GLFWWindowSizeCallback sizeCallback;
    private int[] windowPosX = new int[1], windowPosY = new int[1];

    public Window(int width, int height, String title){
        this.width = width;
        this.height = height;
        this.title = title;
        this.isRunning = true;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        handle = glfwCreateWindow(width, height, title, isFullScreen ? GLFW.glfwGetPrimaryMonitor() : NULL, NULL);
        if (handle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            glfwGetWindowSize(handle, stack.mallocInt(1), stack.mallocInt(1));
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (vidmode != null) {
                glfwSetWindowPos(handle, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
            }
        } // the stack frame is popped automatically
        glfwMakeContextCurrent(handle);// Make the OpenGL context current
        glfwSwapInterval(1); //Enable v-sync
        glfwShowWindow(handle);

        sizeCallback = new GLFWWindowSizeCallback() {
            public void invoke(long window, int w, int h) {
                width = w;
                height = h;
                isResized = true;
            }
        };
        GLFW.glfwSetWindowSizeCallback(handle, sizeCallback);
        GL.createCapabilities();
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);// Set the clear color
    }

    public void close(){
        sizeCallback.free();
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public boolean isRunning(){
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        return isRunning && !glfwWindowShouldClose(handle);
    }

    public void update(){
        if(isResized) {
            GL11.glViewport(0, 0, width, height);
            isResized = false;
        }
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }

    public void swapBuffers(){
        glfwSwapBuffers(handle); // swap the color buffers
    }

    public long getHandle() {
        return handle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void resizeWindow(int width, int height) {
        this.width = width;
        this.height = height;
        //TODO: refresh aspectratio, projectionmatrix and so on
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(handle, title);
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean isFullscreen) {
        this.isFullScreen = isFullscreen;
        isResized = true;
        if (isFullscreen) {
            GLFW.glfwGetWindowPos(handle, windowPosX, windowPosY);
            GLFW.glfwSetWindowMonitor(handle, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
        } else {
            GLFW.glfwSetWindowMonitor(handle, 0, windowPosX[0], windowPosY[0], width, height, 0);
        }
    }
}
