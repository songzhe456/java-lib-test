package com.lib;

import com.lib.util.loader.TextureLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

/**
 * 给LWJGL程序写的测试
 */
public class AppTest {
    @Test
    public void testTexture(){
        TextureLoader loader = new TextureLoader("/assets/textures/grass.png");
        BufferedImage image = loader.load();
        System.out.println(image);
        Assertions.assertNotNull(image);
    }
}
