package com.lib;

import com.lib.util.ArgumentParser;
import com.lib.util.loader.TextureLoader;
import org.junit.jupiter.api.Test;

/**
 * 给LWJGL程序写的测试
 */
public class AppTest {
    @Test
    public void testTexture(){
        App.setCanClearColor(false);
        App.initGLFW();
        TextureLoader loader = new TextureLoader("/assets/textures/grass.png");
        loader.drawPixels(loader.load(0,0),0,0);
        App.loop();
    }
    @Test
    public void testParseArgs(){
        App.initGLFW();
        Runnable testParsingRunnable = () -> {
            System.out.println("循环前输出");
            App.loop();
            System.out.println("循环后输出");
        };
        Runnable testCrashRunnable = () -> {
            ArgumentParser parser = new ArgumentParser();
            String[] args = new String[]{"--need-me","--R","--crash"};
            parser.parseArgs(args);
        };
        Thread testParsingThread = new Thread(testParsingRunnable);
        Thread testCrashThread = new Thread(testCrashRunnable);
        testParsingThread.start();
        testCrashThread.start();
    }

    @Test
    public void thisTestIsTheProblemSelves(){
        App.cleanup();
        App.loop();
        App.initGLFW();
        //App.loadTextures();
    }
}
