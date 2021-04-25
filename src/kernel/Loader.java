package kernel;

import de.matthiasmann.twl.utils.PNGDecoder;
import graphics.*;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import maths.Vector2f;
import maths.Vector3f;
import org.lwjgl.opengl.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private final List<Mesh> meshes = new ArrayList<>();
    private final List<Integer> textures = new ArrayList<>();

    private final List<Integer> vertexIDs = new ArrayList<>();
    private final List<Integer> fragmentIDs = new ArrayList<>();
    private final List<Integer> programIDs = new ArrayList<>();

    public Mesh loadModel(String filePath) {
        AIScene scene = Assimp.aiImportFile(filePath,
                Assimp.aiProcess_JoinIdenticalVertices |
                        Assimp.aiProcess_Triangulate |
                        Assimp.aiProcess_FlipUVs);

        if (scene == null) System.err.println("Couldn't load model at " + filePath);

        AIMesh mesh = AIMesh.create(scene.mMeshes().get(0));
        int vertexCount = mesh.mNumVertices();

        AIVector3D.Buffer vertices = mesh.mVertices();
        AIVector3D.Buffer normals = mesh.mNormals();

        Vertex[] vertexList = new Vertex[vertexCount];

        for (int i = 0; i < vertexCount; i++) {
            AIVector3D vertex = vertices.get(i);
            Vector3f meshVertex = new Vector3f(vertex.x(), vertex.y(), vertex.z());

            AIVector3D normal = normals.get(i);
            Vector3f meshNormal = new Vector3f(normal.x(), normal.y(), normal.z());

            Vector2f meshTextureCoord = new Vector2f(0, 0);
            if (mesh.mNumUVComponents().get(0) != 0) {
                AIVector3D texture = mesh.mTextureCoords(0).get(i);
                meshTextureCoord.setX(texture.x());
                meshTextureCoord.setY(texture.y());
            }

            vertexList[i] = new Vertex(meshVertex, meshNormal, meshTextureCoord);
        }

        int faceCount = mesh.mNumFaces();
        AIFace.Buffer indices = mesh.mFaces();
        int[] indicesList = new int[faceCount * 3];

        for (int i = 0; i < faceCount; i++) {
            AIFace face = indices.get(i);
            indicesList[i * 3 + 0] = face.mIndices().get(0);
            indicesList[i * 3 + 1] = face.mIndices().get(1);
            indicesList[i * 3 + 2] = face.mIndices().get(2);
        }
        Mesh mesh1 = new Mesh(vertexList, indicesList);
        meshes.add(mesh1);
        return mesh1;
    }

    public Texture loadTexture(String path) {
        try {
            int id, width, height;
            PNGDecoder decoder = new PNGDecoder(graphics.Material.class.getResourceAsStream(path));
            width = decoder.getWidth();
            height = decoder.getHeight();
            ByteBuffer buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            id = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            textures.add(id);
            return new Texture(id, width, height);
        } catch (IOException e) {
            System.err.println("[IOException: " + path);
            System.exit(0);
            return null;
        }
    }

    public Material loadMaterial(String path, boolean transparent) {
        return new Material(loadTexture(path), transparent);
    }

    public int loadShader(String vertexPath, String fragmentPath) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Loader.class.getResourceAsStream(vertexPath)))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Couldn't find the file at " + vertexPath);
        }
        vertexPath = result.toString();

        result.delete(0, result.length());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Loader.class.getResourceAsStream(fragmentPath)))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Couldn't find the file at " + fragmentPath);
        }
        fragmentPath = result.toString();
        int programID = GL20.glCreateProgram();
        programIDs.add(programID);
        int vertexID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        vertexIDs.add(vertexID);
        GL20.glShaderSource(vertexID, vertexPath);
        GL20.glCompileShader(vertexID);
        if (GL20.glGetShaderi(vertexID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Vertex Shader: " + GL20.glGetShaderInfoLog(vertexID));
        }
        int fragmentID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        fragmentIDs.add(fragmentID);
        GL20.glShaderSource(fragmentID, fragmentPath);
        GL20.glCompileShader(fragmentID);
        if (GL20.glGetShaderi(fragmentID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Fragment Shader: " + GL20.glGetShaderInfoLog(fragmentID));
        }
        GL20.glAttachShader(programID, vertexID);
        GL20.glAttachShader(programID, fragmentID);
        GL20.glLinkProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Linking: " + GL20.glGetProgramInfoLog(programID));
        }
        GL20.glValidateProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Validation: " + GL20.glGetProgramInfoLog(programID));
        }
        return programID;
    }

    public void clean() {
        for (Mesh m : meshes) {
            GL15.glDeleteBuffers(m.getPBO());
            GL15.glDeleteBuffers(m.getIBO());
            GL15.glDeleteBuffers(m.getTBO());
            GL30.glDeleteVertexArrays(m.getVAO());
        }

        for (int id : textures)
            GL13.glDeleteTextures(id);

        for (int i = 0; i < programIDs.size(); ++i){
            GL20.glDetachShader(programIDs.get(i), vertexIDs.get(i));
            GL20.glDetachShader(programIDs.get(i), fragmentIDs.get(i));
            GL20.glDeleteShader(vertexIDs.get(i));
            GL20.glDeleteShader(fragmentIDs.get(i));
            GL20.glDeleteProgram(programIDs.get(i));
        }
    }
}