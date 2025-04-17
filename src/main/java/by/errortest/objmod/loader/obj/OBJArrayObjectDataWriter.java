package by.errortest.objmod.loader.obj;

import by.errortest.objmod.loader.ArrayObjectDataWriter;
import by.errortest.objmod.model.MeshData;

public class OBJArrayObjectDataWriter extends ArrayObjectDataWriter {

    public OBJArrayObjectDataWriter() {
        super(3);
    }

    protected void writeData(MeshData meshData) {
        storeDataInAttributeList(0, 3, meshData.getVerticesBuffer());
        storeDataInAttributeList(1, 2, meshData.getTexturesBuffer());
        storeDataInAttributeList(2, 3, meshData.getNormalsBuffer());
    }

}
