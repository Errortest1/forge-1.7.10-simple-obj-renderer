package by.errortest.objmod.model;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class MeshData {

    private final FloatBuffer verticesBuffer;
    private final FloatBuffer texturesBuffer;
    private final FloatBuffer normalsBuffer;
    private final IntBuffer indicesBuffer;

    public MeshData(FloatBuffer verticesBuffer, FloatBuffer texturesBuffer, FloatBuffer normalsBuffer, IntBuffer indicesBuffer) {
        this.verticesBuffer = verticesBuffer;
        this.texturesBuffer = texturesBuffer;
        this.normalsBuffer = normalsBuffer;
        this.indicesBuffer = indicesBuffer;

        this.verticesBuffer.rewind();
        this.texturesBuffer.rewind();
        this.normalsBuffer.rewind();
        this.indicesBuffer.rewind();
    }

    public FloatBuffer getVerticesBuffer() {
        return verticesBuffer;
    }

    public FloatBuffer getTexturesBuffer() {
        return texturesBuffer;
    }

    public FloatBuffer getNormalsBuffer() {
        return normalsBuffer;
    }

    public IntBuffer getIndicesBuffer() {
        return indicesBuffer;
    }

}
