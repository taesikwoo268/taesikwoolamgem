package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import core.ResourceLoader;

public class Skins {
    private Rarity rarity;
    private String name;
    private Color color;
    private boolean isBought = false;
    private int price;
    private Rectangle bounds;


    private BufferedImage img;
    /** skin phai them anh */
    public Skins(String name,Rarity rarity,int price,boolean isBought,String path) {
        this.name = name;
        this.rarity = rarity;
        this.price = price;
        this.isBought = isBought;
        this.color = Color.WHITE;
        this.img = ResourceLoader.loadImg(path);
        this.bounds = null;
    }
    /** skin binh thuong*/
    public Skins(String name,Rarity rarity,int price,boolean isBought,Color color) {
        this.name = name;
        this.rarity = rarity;
        this.price = price;
        this.isBought = isBought;
        this.color = color;
        this.img = null;
        this.bounds = null;
    }
    /** ve o skin */
    public void draw (Graphics2D g2,int x,int y,int w,int h, boolean isBall) {
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
            g2.setColor(color != null ? color : Color.WHITE);
            if (isBall) {
                g2.fillOval(x + w/4,y + h/4 - 10, w/2, h/2);
            } else {
                g2.fillRect(x+w/2-30, y+h/2-10, 60, 20);
            }
        } else {
            if (isBall) {
                g2.drawImage(img, x + w/4,y + h/4 - 10, w/2, h/2, null);
            } else {
                g2.drawImage(img, x+ 10, y + h/2 - 10, w - 20, 20, null);
            }
        }
        // ve price
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Serif", Font.BOLD, 20));
        g2.drawString(price + " \uD83D\uDCB0", x + w/3, y + h - 10);
    }

    public boolean contains(int mx, int my) {
        return bounds != null && bounds.contains(mx, my);
    }

    // setter, getter

    public String getName() { return name; }
    public boolean isBought() { return isBought; }
    public BufferedImage getImg() { return img; }
    public Color getColor() { return color; }
    public int getPrice() { return price; }
    public Rarity getRarity() { return rarity; }
    public Rectangle getBounds() { return bounds;}

    public void setBounds(Rectangle bounds) { this.bounds = bounds;}
    public void setBought(boolean bought) {
        this.isBought = bought;
    }
}
