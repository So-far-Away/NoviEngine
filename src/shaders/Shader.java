package shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import graphics.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import maths.Matrix4f;
import maths.Vector2f;
import maths.Vector3f;

public abstract class Shader {

	private final int programID;
	
	public Shader(int id) {
		programID = id;
	}

	public void bindVertexArray(int vao){
		GL30.glBindVertexArray(vao);
	}

	public void unbindVertexArray(){
		GL30.glBindVertexArray(0);
	}

	public void enableVertexBuffer(int... vbs){
		for (int vbo : vbs)
			GL30.glEnableVertexAttribArray(vbo);
	}

	public void disableVertexBuffer(int... vbs){
		for (int vbo : vbs)
			GL30.glDisableVertexAttribArray(vbo);
	}

	public void activeTexture(int i){
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
	}

	public void bindTexture(Texture texture){
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
	}

	public void bind() {
		GL20.glUseProgram(programID);
	}

	public void unbind() {
		GL20.glUseProgram(0);
	}

	public int getUniformLocation(String name) {
		return GL20.glGetUniformLocation(programID, name);
	}
	
	public void setUniform(String name, float value) {
		GL20.glUniform1f(getUniformLocation(name), value);
	}
	
	public void setUniform(String name, int value) {
		GL20.glUniform1i(getUniformLocation(name), value);
	}
	
	public void setUniform(String name, boolean value) {
		GL20.glUniform1i(getUniformLocation(name), value ? 1 : 0);
	}
	
	public void setUniform(String name, Vector2f value) {
		GL20.glUniform2f(getUniformLocation(name), value.getX(), value.getY());
	}
	
	public void setUniform(String name, Vector3f value) {
		GL20.glUniform3f(getUniformLocation(name), value.getX(), value.getY(), value.getZ());
	}
	
	public void setUniform(String name, Matrix4f value) {
		FloatBuffer matrix = MemoryUtil.memAllocFloat(Matrix4f.SIZE * Matrix4f.SIZE);
		matrix.put(value.getAll()).flip();
		GL20.glUniformMatrix4fv(getUniformLocation(name), true, matrix);
	}

}