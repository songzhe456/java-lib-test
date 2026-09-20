package com.lib.util.loader;

import com.lib.App;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public class TextureLoader {
    private BufferedImage texture;
    private final String path;
    public TextureLoader(String path){
        this.path = path;
    }
    public BufferedImage load(int width,int height) {
        try{
            texture = ImageIO.read(Objects.requireNonNull(TextureLoader.class.getResource(path)));
            drawPixels(texture,width,height);
        } catch (IOException e) {
            System.err.println("纹理读取异常" + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return texture;
    }

    public void drawPixels(BufferedImage texture,int windowWidth,int windowHeight){
        int width = texture.getWidth();
        int height = texture.getHeight();
        int[] argb = new int[width * height];
        texture.getRGB(0, 0, width, height, argb, 0, width);
        ByteBuffer buf = MemoryUtil.memAlloc(width * height * 4);//4为每个像素的字节数
        try{
            for (int i = 0; i < argb.length; i++) {
                int pixel = argb[i];
                buf.put((byte) ((pixel >> 16) & 0xFF));
                buf.put((byte) ((pixel >> 8) & 0xFF));
                buf.put((byte) (pixel & 0xFF));
                buf.put((byte) ((pixel >> 24) & 0xFF));
            }
            buf.flip();
            GL11.glRasterPos2i(0,0);
            GL11.glPixelZoom(windowWidth / 1728f,windowHeight / 1080f);
            GL11.glDrawPixels(width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
            GL11.glPixelZoom(1f,1f);
        }
        finally {
            MemoryUtil.memFree(buf);
        }
    }
    public static void preLoad(){
        App.setCanClearColor(false);
        System.out.println("纹理加载器已预加载");
    }
}
