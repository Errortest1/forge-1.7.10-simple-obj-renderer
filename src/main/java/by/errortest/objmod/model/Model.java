package by.errortest.objmod.model;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Map;
import java.util.Objects;

public class Model {

    private final ResourceLocation modelPath;
    private final Map<String, Mesh> meshMap;

    public Model(ResourceLocation modelPath, Map<String, Mesh> meshMap) {
        this.modelPath = modelPath;
        this.meshMap = meshMap;
    }

    public void renderOnly(String... names) {
        for (String name: names) {
            renderPart(name);
        }
    }

    public void renderAllExcept(String... except) {
        for (Mesh mesh : meshMap.values()) {
            boolean skip = false;
            for (String name: except) {
                if (mesh.getName().equals(name)) {
                    skip = true;
                    break;
                }
            }
            if (skip) continue;
            mesh.render(GL11.GL_TRIANGLES);
        }
    }

    public void renderPart(String name) {
        if (meshMap.containsKey(name)) {
            meshMap.get(name).render(GL11.GL_TRIANGLES);
        }
    }

    public void renderAll() {
        for (Mesh mesh : meshMap.values()) {
            mesh.render(GL11.GL_TRIANGLES);
        }
    }

    public void loadToVAO() {
        for (Mesh mesh : meshMap.values()) {
            mesh.loadToVAO();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Model model = (Model) obj;
        return model.modelPath.equals(modelPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelPath);
    }

}
