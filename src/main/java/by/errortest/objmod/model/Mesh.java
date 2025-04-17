package by.errortest.objmod.model;

import by.errortest.objmod.loader.ArrayObjectDataWriter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Mesh {

    private final ArrayObjectDataWriter arrayObjectDataWriter;
    private final String name;
    private final MeshData meshData;
    private int vaoID;

    public Mesh(ArrayObjectDataWriter arrayObjectDataWriter, String name, MeshData meshData) {
        this.arrayObjectDataWriter = arrayObjectDataWriter;
        this.name = name;
        this.meshData = meshData;
    }

    public void render(int mode) {
        GL30.glBindVertexArray(vaoID);
        for (int i = 0; i < arrayObjectDataWriter.getAttributesCount(); i++) {
            GL20.glEnableVertexAttribArray(i);
        }
        GL11.glDrawElements(mode, meshData.getIndicesBuffer().capacity(), GL11.GL_UNSIGNED_INT, 0);
        for (int i = 0; i < arrayObjectDataWriter.getAttributesCount(); i++) {
            GL20.glDisableVertexAttribArray(i);
        }
        GL30.glBindVertexArray(0);
    }

    public void loadToVAO() {
        vaoID = arrayObjectDataWriter.loadToVAO(meshData);
    }

    public String getName() {
        return name;
    }

}
