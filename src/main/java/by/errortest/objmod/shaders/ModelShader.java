package by.errortest.objmod.shaders;

import by.errortest.objmod.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class ModelShader extends ShaderProgram {

    private static final ResourceLocation MODEL_VERTEX_SHADER_LOCATION = new ResourceLocation(Main.MODID, "world.vsh");
    private static final ResourceLocation MODEL_FRAGMENT_SHADER_LOCATION = new ResourceLocation(Main.MODID, "world.fsh");
    private Vector2f lightMapCords = new Vector2f();
    private Vector3f lightSourcePosition = new Vector3f();

    public ModelShader() {
        super(MODEL_VERTEX_SHADER_LOCATION, MODEL_FRAGMENT_SHADER_LOCATION);
    }

    public void loadData() {
        loadInt("diffuseMap", 0);
        loadInt("lightMap", 1);
        loadBoolean("texturingEnabled", GL11.glGetBoolean(GL11.GL_TEXTURE_2D));
        loadBoolean("lightingEnabled", GL11.glGetBoolean(GL11.GL_LIGHTING));
        loadVector2("lightMapCords", lightMapCords);
        loadVector3("lightSourcePosition", lightSourcePosition);
    }

    public void setupLightPosition(float x, float y, float z) {
        lightSourcePosition.set(x, y, z);
    }

    public void setupLightMapCords(double x, double y, double z) {
        int i = Minecraft.getMinecraft().theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z), 0);
        int j = i % 65536;
        int k = i / 65536;
        lightMapCords.set((j + 8.0F) / 256.0F, (k + 8.0F) / 256.0F);
    }

    @Override
    protected void getAllUniformLocations() {
        getUniformLocation("diffuseMap");
        getUniformLocation("lightMap");
        getUniformLocation("texturingEnabled");
        getUniformLocation("lightingEnabled");
        getUniformLocation("lightMapCords");
        getUniformLocation("lightSourcePosition");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "in_position");
        bindAttribute(1, "in_textureCords");
        bindAttribute(2, "in_normal");
    }

}
