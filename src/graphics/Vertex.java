package graphics;

import maths.Vector2f;
import maths.Vector3f;

public class Vertex {

	private final Vector3f position;
	private final Vector3f normal;
	private final Vector2f uv;
	
	public Vertex(Vector3f position, Vector3f normal, Vector2f uv) {
		this.position = position;
		this.normal = normal;
		this.uv = uv;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public Vector3f getNormal() {
		return normal;
	}

	public Vector2f getUV() {
		return uv;
	}
}