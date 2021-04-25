package graphics;

public class Material {

	private Texture texture;
	private boolean transparent; /** Take alpha channel **/

	public Material(Texture texture, boolean transparent) {
		this.texture = texture;
		this.transparent = transparent;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}
}