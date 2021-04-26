package shaders;

import graphics.Material;
import graphics.Mesh;
import graphics.Vertex;
import kernel.Loader;
import kernel.Window;
import maths.Matrix4f;
import maths.Vector3f;
import objects.GameObject;
import objects.camera.Camera;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.util.List;

public class SkyShader extends Shader {

    GameObject sky;

    public SkyShader(Loader loader, int programId) {
        super(programId);
        sky = new GameObject(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(100,100,100),
            loader.loadModel("res/models/sky/sky.obj"), null);
    }

    public void render(Window window, Camera camera) {
        bindVertexArray(sky.getMesh().getVAO());
        enableVertexBuffer(0, 1, 2);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, sky.getMesh().getIBO());

        bind();
        setUniform("model", Matrix4f.transform(sky.getPosition(), sky.getRotation(),sky.getScale()));
        setUniform("view", Matrix4f.view(camera.getPosition(), camera.getRotation()));
        setUniform("projection", window.getProjectionMatrix());

        GL11.glDrawElements(GL11.GL_TRIANGLES, sky.getMesh().getIndices().length, GL11.GL_UNSIGNED_INT, 0);
        unbind();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        disableVertexBuffer(0, 1, 2);
        unbindVertexArray();
    }

}