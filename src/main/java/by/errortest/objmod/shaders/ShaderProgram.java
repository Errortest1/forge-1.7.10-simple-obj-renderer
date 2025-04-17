package by.errortest.objmod.shaders;

import by.errortest.objmod.utils.FileUtils;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class ShaderProgram {

    public static final FloatBuffer MATRIX_BUFFER = BufferUtils.createFloatBuffer(16);
    private final Map<String, Integer> uniformLocations = new HashMap<>();
    public final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;

    public ShaderProgram(ResourceLocation vertexShaderLocation, ResourceLocation fragmentShaderLocation) {
        programID = GL20.glCreateProgram();
        this.vertexShaderID = loadShader(vertexShaderLocation, GL20.GL_VERTEX_SHADER);
        this.fragmentShaderID = loadShader(fragmentShaderLocation, GL20.GL_FRAGMENT_SHADER);
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        getAllUniformLocations();
        GL20.glValidateProgram(programID);
    }

    public void delete() {
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    protected abstract void getAllUniformLocations();

    protected void getUniformLocation(String uniformName) {
        if (!uniformLocations.containsKey(uniformName)) {
            int location = GL20.glGetUniformLocation(programID, uniformName);
            uniformLocations.put(uniformName, location);
        } else {
            FMLLog.log("SHADER LOADER", Level.ERROR, "uniform " + uniformName + " already loaded");
        }
    }

    protected void loadMatrix(String variableName, Matrix4f matrix) {
        if (uniformLocations.containsKey(variableName)) {
            matrix.store(MATRIX_BUFFER);
            MATRIX_BUFFER.flip();
            GL20.glUniformMatrix4(uniformLocations.get(variableName), false, MATRIX_BUFFER);
        } else {
            FMLLog.log("RENDER", Level.ERROR, "Could not find uniform: " + variableName);
        }
    }

    protected void loadFloat(String variableName, float value) {
        if (uniformLocations.containsKey(variableName)) {
            GL20.glUniform1f(uniformLocations.get(variableName), value);
        } else {
            FMLLog.log("RENDER", Level.ERROR, "Could not find uniform: " + variableName);
        }
    }

    protected void loadInt(String variableName, int value) {
        if (uniformLocations.containsKey(variableName)) {
            GL20.glUniform1i(uniformLocations.get(variableName), value);
        } else {
            FMLLog.log("RENDER", Level.ERROR, "Could not find uniform: " + variableName);
        }
    }

    protected void loadVector2(String variableName, Vector2f vector) {
        if (uniformLocations.containsKey(variableName)) {
            GL20.glUniform2f(uniformLocations.get(variableName), vector.x, vector.y);
        } else {
            FMLLog.log("RENDER", Level.ERROR, "Could not find uniform: " + variableName);
        }
    }

    protected void loadVector3(String variableName, Vector3f vector) {
        if (uniformLocations.containsKey(variableName)) {
            GL20.glUniform3f(uniformLocations.get(variableName), vector.x, vector.y, vector.z);
        } else {
            FMLLog.log("RENDER", Level.ERROR, "Could not find uniform: " + variableName);
        }
    }

    protected void loadVector4(String variableName, Vector4f vector) {
        if (uniformLocations.containsKey(variableName)) {
            GL20.glUniform4f(uniformLocations.get(variableName), vector.x, vector.y, vector.z, vector.w);
        } else {
            FMLLog.log("RENDER", Level.ERROR, "Could not find uniform: " + variableName);
        }
    }

    protected void loadBoolean(String variableName, boolean value) {
        int toLoad = 0;
        if (value) toLoad = 1;
        loadInt(variableName, toLoad);
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    static int loadShader(ResourceLocation shaderLocation, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.getInputStreamFromResourceLocation(shaderLocation), StandardCharsets.UTF_8));
            String line;
            while((line = reader.readLine())!=null) {
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch(IOException e){
            FMLLog.log("SHADER LOADER", Level.ERROR, "Could not read shader file: " + shaderLocation);
            e.printStackTrace();
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            FMLLog.log("SHADER LOADER", Level.ERROR, "Could not compile shader: " + shaderLocation);
            FMLLog.log("SHADER LOADER", Level.ERROR, GL20.glGetShaderInfoLog(shaderID, 500));
        }
        return shaderID;
    }

}
