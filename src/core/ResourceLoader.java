package core;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import entity.Skins;
import entity.Rarity;

public class ResourceLoader {
    public static BufferedImage loadImg (String path) {
        BufferedImage img;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Không tìm thấy ảnh, thì phải..PHẢI CHỊU.");
            img = null;
        }
        return img;
    }
    /** Đọc file lấy skins */
    public static List<Skins> loadSkins(String filePath) {
        List<Skins> skins = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",");
                if (parts.length < 6) continue;

                String name = parts[0].trim();
                boolean hasImage = Boolean.parseBoolean(parts[1].trim());
                Rarity rarity = Rarity.valueOf(parts[2].trim());
                int price = Integer.parseInt(parts[3].trim());
                boolean isBought = Boolean.parseBoolean(parts[4].trim());
                String last = parts[5].trim();

                if (hasImage) {
                    // Có ảnh → last là path
                    skins.add(new Skins(name, rarity, price, isBought, last));
                } else {
                    // Không ảnh → last là màu
                    Color color = parseColor(last);
                    if (color == null) color = Color.WHITE; // fallback nếu sai tên
                    skins.add(new Skins(name, rarity, price, isBought, color));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return skins;
    }


    private static Color parseColor(String s) {
        return switch (s.toUpperCase()) {
            case "RED" -> Color.RED;
            case "BLUE" -> Color.BLUE;
            case "GREEN" -> Color.GREEN;
            case "YELLOW" -> Color.YELLOW;
            case "CYAN" -> Color.CYAN;
            default -> null;
        };
    }
}
