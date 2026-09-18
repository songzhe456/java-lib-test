package com.lib.util.loader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class TextureLoader {
    private BufferedImage texture;
    private String path;
    public TextureLoader(String path){
        this.path = path;
    }
    public BufferedImage load() {
        try{
            texture = ImageIO.read(Objects.requireNonNull(TextureLoader.class.getResource(path)));
        } catch (IOException e) {
            System.err.println("纹理读取异常" + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return texture;
    }
}
