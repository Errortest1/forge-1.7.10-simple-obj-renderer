package by.errortest.objmod.loader;

import by.errortest.objmod.model.Model;
import net.minecraft.util.ResourceLocation;

public interface IModelLoader {

    Model loadModel(ResourceLocation resourceLocation);

    String getExtension();

}
