package graphics;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import de.matthiasmann.twl.utils.PNGDecoder;

public class Material {
	private String path;
	private float width, height;
	private int textureID;

	public Material(String path) {
		this.path = path;
	}

	public void create() {
		// load png file
		PNGDecoder decoder;
		try {
			decoder = new PNGDecoder(Material.class.getResourceAsStream(path));

			// create a byte buffer big enough to store RGBA values
			ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

			// decode
			decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);

			// flip the buffer so its ready to read
			buffer.flip();

			// create a texture
			textureID = GL11.glGenTextures();

			// bind the texture
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

			// tell opengl how to unpack bytes
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

			// set the texture parameters, can be GL_LINEAR or GL_NEAREST
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

			// upload texture
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0,
					GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

			// Generate Mip Map
			// glGenerateMipmap(GL11.GL_TEXTURE_2D);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void destroy() {
		GL13.glDeleteTextures(textureID);
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public int getTextureID() {
		return textureID;
	}
}