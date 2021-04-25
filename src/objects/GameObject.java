package objects;

import graphics.Material;
import graphics.Mesh;
import maths.Vector3f;

public class GameObject {

	private final Vector3f position;
	private final Vector3f rotation;
	private final Vector3f scale;
	private final Mesh mesh;
	private final Material material;

	public GameObject(Vector3f position, Vector3f rotation, Vector3f scale, Mesh mesh, Material material) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.mesh = mesh;
		this.material = material;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public Material getMaterial() {
		return material;
	}
}