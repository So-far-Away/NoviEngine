package objects.terrain;

import graphics.Material;
import graphics.Mesh;
import graphics.Vertex;
import maths.Vector2f;
import maths.Vector3f;
import objects.GameObject;

public class Terrain extends GameObject {

    public static Mesh create(){
        int VERTEX_COUNT = 2, SIZE = 100;
        int count = VERTEX_COUNT * VERTEX_COUNT;
        Vertex[] vertices = new Vertex[count];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer] = new Vertex(
                        new Vector3f((float)j/((float)VERTEX_COUNT - 1) * SIZE,
                                0,(float)i/((float)VERTEX_COUNT - 1) * SIZE),
                        new Vector3f(0,1,0),
                        new Vector2f((float)j/((float)VERTEX_COUNT - 1), (float)i/((float)VERTEX_COUNT - 1))
                );
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return new Mesh(vertices, indices);
    }

    public Terrain(Vector3f position, Vector3f rotation, Vector3f scale, Material material) {
        super(position, rotation, scale, create(), material);
    }
}
