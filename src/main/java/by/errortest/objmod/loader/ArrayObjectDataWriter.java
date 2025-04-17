package by.errortest.objmod.loader;

import by.errortest.objmod.model.MeshData;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public abstract class ArrayObjectDataWriter {

    private final int attributesCount;

    public ArrayObjectDataWriter(int attributesCount) {
        this.attributesCount = attributesCount;
    }

    public int loadToVAO(MeshData meshData) {
        int vaoID = bindVAO();
        bindIndicesBuffer(meshData.getIndicesBuffer());
        writeData(meshData);
        unbindVAO();
        return vaoID;
    }

    protected abstract void writeData(MeshData meshData);

    private int bindVAO() {
        int vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    protected void storeDataInAttributeList(int attributeNumber, int dataSize, FloatBuffer buffer) {
        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, dataSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(IntBuffer buffer) {
        int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    public int getAttributesCount() {
        return attributesCount;
    }
}
