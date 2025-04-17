package by.errortest.objmod;

import by.errortest.objmod.loader.ModelLoader;
import by.errortest.objmod.loader.obj.OBJModelLoader;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;

@Mod(
        name = "OBJ Mod",
        modid = Main.MODID,
        version = "1.0",
        acceptedMinecraftVersions = "[1.7.10]"
)
public class Main {

    public static final String ASSETS_FOLDER = "modassets";
    public static final String ASSETS_DIRECTORY = ASSETS_FOLDER + "/assets/";
    public static final String MODID = "objmod";
    public static final ModelLoader OBJ_LOADER = new ModelLoader(new OBJModelLoader());

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(manager -> {
            if (manager instanceof SimpleReloadableResourceManager) {
                FolderResourcePack pack = new FolderResourcePack(new File( Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), ASSETS_FOLDER));
                ((SimpleReloadableResourceManager) manager).reloadResourcePack(pack);
            }
        });
        ExampleRenderer exampleRenderer = new ExampleRenderer();
        MinecraftForge.EVENT_BUS.register(exampleRenderer);
        FMLCommonHandler.instance().bus().register(exampleRenderer);
        OBJ_LOADER.loadModels();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        OBJ_LOADER.loadModelsToVao();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {

    }

}
