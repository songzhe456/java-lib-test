package com.lib;

import com.lib.util.ArgumentParser;
import com.lib.util.loader.TextureLoader;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * 仅为显示一个可交互窗口
 */
public class App {
    private static final int WINDOW_WIDTH = 2048;
    private static final int WINDOW_HEIGHT = 560;
    private static long window;

    public static void main(String[] args) {
        ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.parseArgs(args);
        if(init()) {
            loop();
        }
    }

    public static boolean init(){
        try {
            if (!GLFW.glfwInit()) {
                System.err.println("GLFW初始化失败");
                return false;
            }
            window = GLFW.glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "LWJGL window", NULL, NULL);
            if (window == NULL) {
                System.err.println("窗口初始化失败");
                return false;
            }
            GLFW.glfwMakeContextCurrent(window);
            GL.createCapabilities();
            GL11.glClearColor(0.5f, 0.69f, 0.54f, 1.0f);
        } catch (Exception e) {
            System.err.println("LWJGL初始化时出现错误:" + e);
            return false;
        }
        System.out.println("LWJGL初始化成功");
        return true;
    }

    public static void loop(){
        if(window == NULL){return;}
        loadTextures();
        boolean firstTimeToRed = true;
        while(!GLFW.glfwWindowShouldClose(window)){
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            GLFW.glfwPollEvents();
            GLFW.glfwSwapBuffers(window);
            if(checkPressed(GLFW.GLFW_KEY_ESCAPE,GLFW.GLFW_PRESS)){
                GLFW.glfwSetWindowShouldClose(window,true);
            } else if (checkPressed(GLFW.GLFW_KEY_R,GLFW.GLFW_PRESS) && firstTimeToRed) {
                GL11.glClearColor(1.0f,0.0f,0.0f,1.0f);
                System.out.println("已切换至红色背景。注：你无法再切回原来的了！");
                firstTimeToRed = false;
            }
        }
        cleanup();
    }

    public static boolean checkPressed(int key,int staus){
        return GLFW.glfwGetKey(window, key) == staus;
    }

    public static void cleanup(){
        try {
            GLFW.glfwDestroyWindow(window);
            GLFW.glfwTerminate();
            System.out.println("清理完毕！");
        } catch (Exception e) {
            System.err.println("清理遇到异常:" + e);
        }
    }

    public static void loadTextures() {
        TextureLoader loader = new TextureLoader( "./assets/textures/grass.png");
        System.out.println(loader.load());
    }
}
