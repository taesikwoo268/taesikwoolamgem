package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import core.ResourceLoader;

public class Skins {
    private int id;
    private String name;
    private Rarity rarity;
    private int price;
    private boolean isBought = false;
    private Color color;
    private Rectangle bounds;
    private boolean equipped;

    private static final BufferedImage common = ResourceLoader.loadImg("assets/images/CommonBg.jpg");
    private static final BufferedImage rare = ResourceLoader.loadImg("assets/images/RareBg.jpg");
    private static final BufferedImage epic = ResourceLoader.loadImg("assets/images/EpicBg.jpg");
    private static final BufferedImage legendary = ResourceLoader.loadImg("assets/images/LegendaryBg.jpg");
    private BufferedImage img;
    /** skin phai them anh */
    public Skins(int id, String name,Rarity rarity,int price,boolean isBought,String path) {
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.price = price;
        this.isBought = isBought;
        this.color = Color.WHITE;
        this.img = ResourceLoader.loadImg(path);
        this.bounds = null;
        this.equipped = false;
    }
    /** skin binh thuong*/
    public Skins(int id, String name,Rarity rarity,int price,boolean isBought,Color color) {
        this.name = name;
        this.rarity = rarity;
        this.price = price;
        this.isBought = isBought;
        this.color = color;
        this.img = null;
        this.bounds = null;
        this.equipped = false;
    }
    /** ve ô skin */
    public void draw (Graphics2D g2,int x,int y,int w,int h, boolean isBall) {
        // ve nen truoc
        switch (rarity) {
            case COMMON -> g2.drawImage(common,x,y,w,h,null);
            case RARE -> g2.drawImage(rare,x,y,w,h,null);
            case EPIC -> g2.drawImage(epic,x,y,w,h,null);
            case LEGENDARY -> g2.drawImage(legendary,x,y,w,h,null);
        }
        //g2.fillRect(x,y,w,h);
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
        if (!isBought()) {
            g2.setColor(new Color(0, 0, 0, 150)); // 150 = độ mờ
            g2.fillRect(x, y, w, h);
            // ve price
            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Serif", Font.BOLD, 20));
            g2.drawString(price + " \uD83D\uDCB0", x + w/3, y + h - 10);
        } else {

        }
        if (isEquipped()) {
            int size = 20;
            g2.setColor(new Color(50, 205, 50)); // xanh lá
            g2.fillOval(w - size - 5, 5, size, size);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("✓", w - size, 21);
            g2.setStroke(new BasicStroke(3f));
            g2.setColor(new Color(0, 255, 0, 180));
            g2.drawRect(1, 1, w - 2, h - 2);
        }
    }

    public boolean contains(int mx, int my) {
        return bounds != null && bounds.contains(mx, my);
    }

    // setter, getter
    public int getId() { return id; }
    public String getName() { return name; }
    public boolean isBought() { return isBought; }
    public BufferedImage getImg() { return img; }
    public Color getColor() { return color; }
    public int getPrice() { return price; }
    public Rarity getRarity() { return rarity; }
    public Rectangle getBounds() { return bounds;}
    public boolean isEquipped() { return equipped; }
    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }
    public void setBounds(Rectangle bounds) { this.bounds = bounds;}
    public void setBought(boolean bought) {
        this.isBought = bought;
    }
}
