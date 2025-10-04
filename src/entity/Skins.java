package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import core.ResourceLoader;

enum Rarity {
    COMMON, RARE, EPIC, LEGENDARY;
}
public class Skins {
    private Rarity rarity;
    private String name;
    private Color color;
    private boolean isBought = false;
    private int price;
    private BufferedImage img;
    /** skin phai them anh */
    public Skins(String name,Rarity rarity,int price,boolean isBought,String path) {
        this.name = name;
        this.rarity = rarity;
        this.price = price;
        this.isBought = isBought;
        this.color = Color.WHITE;
        this.img = ResourceLoader.loadImg(path);
    }
    /** skin binh thuong*/
    public Skins(String name,Rarity rarity,int price,boolean isBought,Color color) {
        this.name = name;
        this.rarity = rarity;
        this.price = price;
        this.isBought = isBought;
        this.color = color;
        this.img = null;
    }
    public void draw (Graphics2D g2,int x,int y,int w,int h) {
        // ve nen truoc
        switch (rarity) {
            case COMMON -> g2.setColor(Color.LIGHT_GRAY);
            case RARE -> g2.setColor(Color.BLUE);
            case EPIC -> g2.setColor(Color.MAGENTA);
            case LEGENDARY -> g2.setColor(Color.ORANGE);
        }
        g2.fillRect(x,y,w,h);
        // ve skin
        if (img == null) {
            if (Color.RED.equals(color)) {
                g2.setColor(Color.RED);
            } else if (Color.GREEN.equals(color)) {
                g2.setColor(Color.GREEN);
            } else if (Color.BLUE.equals(color)) {
                g2.setColor(Color.BLUE);
            } else if (Color.YELLOW.equals(color)) {
                g2.setColor(Color.YELLOW);
            } else if (Color.CYAN.equals(color)) {
                g2.setColor(Color.CYAN);
            } else {
                g2.setColor(Color.WHITE);
            }

            g2.fillOval(x + w/4,y + h/4, w/2, h/2);
        }
    }
}
