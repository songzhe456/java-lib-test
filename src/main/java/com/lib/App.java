package com.lib;

import com.lib.util.ArgumentParser;
import com.lib.util.loader.TextureLoader;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.*;

import javax.swing.*;
import java.awt.*;

import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * 仅为显示一个可交互窗口
 */
public class App {
    private static final int WINDOW_WIDTH = 854;
    private static final int WINDOW_HEIGHT = 480;
    private static boolean canClearColor = true;
    private static long window;
    private static boolean initialized = false;
    private static TextureLoader textureLoader;
    private static boolean useVulkan = true;
    private static boolean alreadyInitGLFW = false;
    private static VkInstance vkInstance;
    private static boolean noWindowLoop;

    public static void main(String[] args) {
        ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.parseArgs(args);
        TextureLoader.preLoad();


        if(GLFW.glfwInit() && GLFWVulkan.glfwVulkanSupported()) {
            System.out.println("设备支持Vulkan,继续运行...");
            alreadyInitGLFW = true;
            initGLFW();
            loop();
        } else{
            useVulkan = false;
            initGLFW();
            System.out.println("设备不支持Vulkan，用OpenGL代替");
            loop();
        }
    }

    public static void initOpenGL(){
        GL.createCapabilities();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0,WINDOW_WIDTH,0,WINDOW_HEIGHT,-1,1);
    }

    public static boolean initGLFW(){
        if(initialized){return true;}
        try {
            if(!alreadyInitGLFW) {
                if (!GLFW.glfwInit()) {
                    System.err.println("GLFW初始化失败");
                    return false;
                }
            }
            window = GLFW.glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "LWJGL window", NULL, NULL);
            if (window == NULL) {
                System.err.println("窗口初始化失败");
                return false;
            }
            GLFW.glfwMakeContextCurrent(window);
            initOpenGL();
            if (useVulkan) {
                initVulkan();
            }
            if(!ArgumentParser.isRedRequested() && isCanClearColor()) {
                changeColor(0.5f, 0.69f, 0.54f, 1.0f);
            } else if (isCanClearColor()){
                changeColor(1.0f,0.0f,0.0f,1.0f);
            }
        } catch (Exception e) {
            System.err.println("LWJGL初始化时出现错误:" + e);
            return false;
        }
        System.out.println("LWJGL初始化成功");
        initialized = true;
        return true;
    }

    public static boolean initVulkan(){
        if(!useVulkan || initialized || !GLFWVulkan.glfwVulkanSupported()){return false;}
        try(MemoryStack stack = MemoryStack.stackPush()){
            VkInstanceCreateInfo instanceCreateInfo = VkInstanceCreateInfo.calloc(stack);
            PointerBuffer instancePtr = stack.mallocPointer(1);
            VK11.vkCreateInstance(instanceCreateInfo,null,instancePtr);
            long handle = instancePtr.get(0);
            vkInstance = new VkInstance(handle, instanceCreateInfo);
            JFrame frame = new JFrame("Vulkan窗口");
            frame.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
            frame.setVisible(true);
            noWindowLoop = true;
            System.out.println("Vulkan初始化成功！");
        } catch (Exception | UnsatisfiedLinkError e) {
            System.err.println("Vulkan初始化失败");
            e.printStackTrace();
            useVulkan = false;
        }
        return true;
    }

    public static void loop(){
        if(window == NULL || noWindowLoop){cleanup();return;}
        boolean firstTimeToRed = true;
        if(useVulkan){
            System.out.println("注：你正处于Vulkan模式，当前项目的纹理加载器等目前只适配OpenGL，所以你只能看到黑色背景！");
        }
        while(!GLFW.glfwWindowShouldClose(window)){
            if(!useVulkan) {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            }
            else{

            }
            GLFW.glfwPollEvents();
            int[] fbW = new int[1],fbH = new int[1];
            GLFW.glfwGetFramebufferSize(window,fbW,fbH);
            if(!useVulkan) {
                GL11.glViewport(0, 0, fbW[0], fbH[0]);
                loadTextures(fbW, fbH);
            }
            GLFW.glfwSwapBuffers(window);
            if(checkPressed(GLFW.GLFW_KEY_ESCAPE,GLFW.GLFW_PRESS)){
                GLFW.glfwSetWindowShouldClose(window,true);
            } else if (checkPressed(GLFW.GLFW_KEY_R,GLFW.GLFW_PRESS) && firstTimeToRed) {
                changeColor(1.0f,0.0f,0.0f,1.0f);
                System.out.println("已切换至红色背景。注：你无法再切回原来的了！");
                firstTimeToRed = false;
            }
        }
        cleanup();
    }

    public static boolean checkPressed(int key,int status){
        return GLFW.glfwGetKey(window, key) == status;
    }

    public static void cleanup(){
        try {
            GLFW.glfwDestroyWindow(window);
            GLFW.glfwTerminate();
            VK10.vkDestroyInstance(vkInstance,null);
            System.out.println("清理完毕！");
        } catch (Exception e) {
            System.err.println("清理遇到异常:" + e);
        }
    }

    public static void loadTextures(int[] fbW,int[] fbH) {
        TextureLoader loader = new TextureLoader("/assets/textures/grass.png");
        loader.load(fbW[0], fbH[0]);
    }

    public static void changeColor(float red,float green,float blue,float alpha){
        GL11.glClearColor(red,green,blue,alpha);
    }

    public static void setCanClearColor(boolean value) {
        canClearColor = value;
    }

    public static boolean isCanClearColor() {
        return canClearColor;
    }
}
