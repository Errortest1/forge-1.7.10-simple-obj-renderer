package by.errortest.objmod.utils;

import by.errortest.objmod.Main;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static List<File> getAllFiles(String path, String extension) {
        List<File> list = new ArrayList<>();
        try {
            Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.toString().endsWith(extension)) {
                        list.add(file.toFile());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static InputStream getInputStreamFromResourceLocation(ResourceLocation resourceLocation) {
        try {
            return Files.newInputStream(Paths.get(Main.ASSETS_DIRECTORY + resourceLocation.getResourceDomain() + "/" + resourceLocation.getResourcePath()));
        } catch (IOException e) {
            FMLLog.log("File Manager", Level.ERROR, "Exception while loading file: " + e);
            return null;
        }
    }

}
