package by.errortest.objmod;

import by.errortest.objmod.shaders.ModelShader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class ExampleRenderer {

    private ModelShader worldShader = new ModelShader();
    private final ResourceLocation texture = new ResourceLocation(Main.MODID, "texture.png");
    private final ResourceLocation modelLocation = new ResourceLocation(Main.MODID, "model.obj");
    private final Minecraft mc = Minecraft.getMinecraft();
    private int timer;

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
            worldShader.delete();
            worldShader = new ModelShader();
        }
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glColor4f(1.0F, 0.0F, 1.0F, 1.0F);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        worldShader.start();
        worldShader.setupLightMapCords(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        worldShader.setupLightPosition((float) Math.cos(Math.toRadians(timer)), 0.0F, (float) Math.sin(Math.toRadians(timer)));
        worldShader.loadData();

        GL11.glPushMatrix();
        GL11.glTranslatef(3.0F, -1.5F, 0.0F);
        Main.OBJ_LOADER.getModel(modelLocation).renderAll();
        GL11.glPopMatrix();

        worldShader.stop();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.START && e.type == TickEvent.Type.CLIENT) {
            timer+=20;
            if (timer > 360) {
                timer = 0;
            }
        }
    }

}
