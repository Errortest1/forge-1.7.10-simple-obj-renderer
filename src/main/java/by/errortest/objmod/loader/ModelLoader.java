package by.errortest.objmod.loader;

import by.errortest.objmod.Main;
import by.errortest.objmod.model.Model;
import by.errortest.objmod.utils.FileUtils;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class ModelLoader {

    private static final Model EMPTY_MODEL = new Model(new ResourceLocation(""), new HashMap<>());
    private final ModelLoadingThread modelLoadingThread = new ModelLoadingThread(this);
    private final HashMap<ResourceLocation, Model> modelMap = new HashMap<>();
    private final IModelLoader loader;

    public ModelLoader(IModelLoader modelLoader) {
        this.loader = modelLoader;
    }

    public Model getModel(ResourceLocation resourceLocation) {
        if (modelMap.containsKey(resourceLocation)) {
            return modelMap.get(resourceLocation);
        } else {
            Model model = loader.loadModel(resourceLocation);
            if (model != null) {
                model.loadToVAO();
                modelMap.put(resourceLocation, model);
                return model;
            } else {
                modelMap.put(resourceLocation, EMPTY_MODEL);
                return EMPTY_MODEL;
            }
        }
    }

    public void loadModels() {
        modelLoadingThread.start();
    }

    public void loadModelsToVao() {
        try {
            modelLoadingThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (Model model : modelMap.values()) {
            if (model != null) {
                model.loadToVAO();
            }
        }
    }

    private static final class ModelLoadingThread extends Thread {

        private final ModelLoader modelLoader;

        public ModelLoadingThread(ModelLoader modelLoader) {
            this.modelLoader = modelLoader;
            setName("Model Loading");
            setDaemon(true);
        }

        public void run() {
            String assetsDirectory = Main.ASSETS_DIRECTORY;
            List<File> modelFiles = FileUtils.getAllFiles(assetsDirectory, "." + modelLoader.loader.getExtension());
            for (File file : modelFiles) {
                ResourceLocation modelLocation = new ResourceLocation(Main.MODID, file.getPath().replace("\\", "/").replace(assetsDirectory + Main.MODID + "/", ""));
                Model model = modelLoader.loader.loadModel(modelLocation);
                modelLoader.modelMap.put(modelLocation, model);
            }
        }

    }

}
