package by.errortest.objmod.model.obj;

import by.errortest.objmod.loader.obj.OBJArrayObjectDataWriter;
import by.errortest.objmod.model.MeshData;
import by.errortest.objmod.model.Mesh;

public class OBJMesh extends Mesh {

    private static final OBJArrayObjectDataWriter ARRAY_OBJECT_DATA_WRITER = new OBJArrayObjectDataWriter();

    public OBJMesh(String name, MeshData meshData) {
        super(ARRAY_OBJECT_DATA_WRITER, name, meshData);
    }

}
