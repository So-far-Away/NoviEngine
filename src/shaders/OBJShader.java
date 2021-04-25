package shaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import kernel.Window;
import maths.Matrix4f;
import objects.camera.Camera;
import objects.GameObject;

import java.util.List;

public class OBJShader extends Shader {

	public OBJShader(int programId) {
		super(programId);
	}

	public void render(List<GameObject> objects, Window window, Camera camera) {
		bind();
		setUniform("view", Matrix4f.view(camera.getPosition(), camera.getRotation()));
		setUniform("projection", window.getProjectionMatrix());

		for (GameObject object : objects) {
			bindVertexArray(object.getMesh().getVAO());
			enableVertexBuffer(0, 1, 2);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, object.getMesh().getIBO());

			activeTexture(0);
			bindTexture(object.getMaterial().getTexture());

			setUniform("model", Matrix4f.transform(object.getPosition(), object.getRotation(), object.getScale()));

			GL11.glDrawElements(GL11.GL_TRIANGLES, object.getMesh().getIndices().length, GL11.GL_UNSIGNED_INT, 0);
		}

		unbind();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		disableVertexBuffer(0, 1, 2);
		unbindVertexArray();
	}
	
	public void render(GameObject object, Window window, Camera camera) {
		bindVertexArray(object.getMesh().getVAO());
		enableVertexBuffer(0,1,2);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, object.getMesh().getIBO());

		activeTexture(0);
		bindTexture(object.getMaterial().getTexture());
		bind();

		setUniform("model", Matrix4f.transform(object.getPosition(), object.getRotation(), object.getScale()));
		setUniform("view", Matrix4f.view(camera.getPosition(), camera.getRotation()));
		setUniform("projection", window.getProjectionMatrix());

		GL11.glDrawElements(GL11.GL_TRIANGLES, object.getMesh().getIndices().length, GL11.GL_UNSIGNED_INT, 0);
		unbind();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		disableVertexBuffer(0,1,2);
		unbindVertexArray();
	}

}