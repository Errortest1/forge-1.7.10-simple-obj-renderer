package by.errortest.objmod.loader.obj;

import by.errortest.objmod.loader.IModelLoader;
import by.errortest.objmod.model.Mesh;
import by.errortest.objmod.model.MeshData;
import by.errortest.objmod.model.Model;
import by.errortest.objmod.model.obj.OBJMesh;
import by.errortest.objmod.utils.FileUtils;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OBJModelLoader implements IModelLoader {

    private String currentObjectName;
    private final List<Vector3f> vertices = new ArrayList<>();
    private final List<Vector2f> textures = new ArrayList<>();
    private final List<Vector3f> normals = new ArrayList<>();
    private final List<Integer> vertexIndices = new ArrayList<>();
    private final List<Integer> textureCordIndices = new ArrayList<>();
    private final List<Integer> normalIndices = new ArrayList<>();
    private final Map<String, Mesh> meshMap = new HashMap<>();
    private int prevVertexCount;
    private int vertexCount;
    private int prevTextureCordsCount;
    private int textureCordsCount;
    private int prevNormalCount;
    private int normalCount;

    @Override
    public Model loadModel(ResourceLocation resourceLocation) {
        InputStream inputStream = FileUtils.getInputStreamFromResourceLocation(resourceLocation);
        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                load(reader);
                FMLLog.log("MODEL LOADER", Level.INFO, "Model loaded: " + resourceLocation);
                return new Model(resourceLocation, new HashMap<>(meshMap));
            } catch (IOException e) {
                FMLLog.log("MODEL LOADER", Level.ERROR, "Error occurred during load model: " + e);
                return null;
            }
        } else {
            return null;
        }
    }

    private void load(BufferedReader reader) throws IOException {
        vertices.clear();
        textures.clear();
        normals.clear();

        vertexIndices.clear();
        textureCordIndices.clear();
        normalIndices.clear();
        prevNormalCount = 0;
        normalCount = 0;
        prevTextureCordsCount = 0;
        textureCordsCount = 0;
        prevVertexCount = 0;
        vertexCount = 0;
        currentObjectName = null;
        meshMap.clear();
        String line;
        while ((line = reader.readLine()) != null) {
            assembleLine(line);
        }
        assembleObjectLine(null);
    }

    private void assembleLine(String line) {
        String[] currentLine = line.split(" ");
        if (line.startsWith("v ")) {
            Vector3f vertex = parseVector3f(currentLine);
            vertices.add(vertex);
            vertexCount++;
        } else if (line.startsWith("vt ")) {
            Vector2f texture = parseVector2f(currentLine);
            textures.add(texture);
            textureCordsCount++;
        } else if (line.startsWith("vn ")) {
            Vector3f normal = parseVector3f(currentLine);
            normals.add(normal);
            normalCount++;
        } else if (line.startsWith("f ")) {
            assembleFaceLine(currentLine);
        } else if (line.startsWith("o ") || line.startsWith("g ")) {
            prevVertexCount = vertexCount;
            prevNormalCount = normalCount;
            prevTextureCordsCount = textureCordsCount;
            assembleObjectLine(currentLine);
        }
    }

    private void assembleFaceLine(String[] currentLine) {
        for (int i = 1; i < 4; i++) {
            String[] vertexLine = currentLine[i].split("/");
            int decrement = meshMap.isEmpty() ? 1 : 1 + prevVertexCount;
            int decrement2 = meshMap.isEmpty() ? 1 : 1 + prevTextureCordsCount;
            int decrement3 = meshMap.isEmpty() ? 1 : 1 + prevNormalCount;
            vertexIndices.add(Integer.parseInt(vertexLine[0]) - decrement);
            textureCordIndices.add(Integer.parseInt(vertexLine[1]) - decrement2);
            normalIndices.add(Integer.parseInt(vertexLine[2]) - decrement3);
        }
    }

    private void assembleObjectLine(String[] currentLine) {
        if (currentObjectName != null) {
            MeshData meshData = getMeshData(vertices, textures, normals, vertexIndices, textureCordIndices, normalIndices);
            meshMap.put(currentObjectName, new OBJMesh(currentObjectName, meshData));

            vertices.clear();
            textures.clear();
            normals.clear();
            vertexIndices.clear();
            textureCordIndices.clear();
            normalIndices.clear();
        }
        if (currentLine != null) {
            currentObjectName = currentLine[1];
        }
    }

    private MeshData getMeshData(List<Vector3f> vertices, List<Vector2f> textures, List<Vector3f> normals, List<Integer> vertexIndices, List<Integer> textureCordIndices, List<Integer> normalIndices) {
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.size() * 3);
        FloatBuffer texturesBuffer = BufferUtils.createFloatBuffer(vertices.size() * 2);
        FloatBuffer normalsBuffer = BufferUtils.createFloatBuffer(vertices.size() * 3);
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(vertexIndices.size());

        for (int i = 0; i < vertexIndices.size(); i++) {
            int vertexIndex = vertexIndices.get(i);
            Vector3f vertex = vertices.get(vertexIndex);
            Vector2f textureCord = textures.get(textureCordIndices.get(i));
            Vector3f normal = normals.get(normalIndices.get(i));

            verticesBuffer.put(vertexIndex * 3, vertex.x);
            verticesBuffer.put(vertexIndex * 3 + 1, vertex.y);
            verticesBuffer.put(vertexIndex * 3 + 2, vertex.z);

            texturesBuffer.put(vertexIndex * 2, textureCord.x);
            texturesBuffer.put(vertexIndex * 2 + 1, 1 - textureCord.y);

            normalsBuffer.put(vertexIndex * 3, normal.x);
            normalsBuffer.put(vertexIndex * 3 + 1, normal.y);
            normalsBuffer.put(vertexIndex * 3 + 2, normal.z);

            indicesBuffer.put(vertexIndex);
        }
        return new MeshData(verticesBuffer, texturesBuffer, normalsBuffer, indicesBuffer);
    }

    private Vector3f parseVector3f(String[] currentLine) {
        return new Vector3f(
                Float.parseFloat(currentLine[1]),
                Float.parseFloat(currentLine[2]),
                Float.parseFloat(currentLine[3]));
    }

    private Vector2f parseVector2f(String[] currentLine) {
        return new Vector2f(
                Float.parseFloat(currentLine[1]),
                Float.parseFloat(currentLine[2]));
    }

    @Override
    public String getExtension() {
        return "obj";
    }

}
