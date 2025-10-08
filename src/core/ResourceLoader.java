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
                if (parts.length < 7) continue;

                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                boolean hasImage = Boolean.parseBoolean(parts[2].trim());
                Rarity rarity = Rarity.valueOf(parts[3].trim());
                int price = Integer.parseInt(parts[4].trim());
                boolean isBought = Boolean.parseBoolean(parts[5].trim());
                String last = parts[6].trim();

                if (hasImage) {
                    skins.add(new Skins(id, name, rarity, price, isBought, last));
                } else {
                    Color color = parseColor(last);
                    if (color == null) color = Color.WHITE;
                    skins.add(new Skins(id, name, rarity, price, isBought, color));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return skins;
    }

    /** ghi file skins */
    public static void updateIsBought(String filePath, int id) {
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.startsWith("#") || trimmed.isEmpty()) {
                        lines.add(line);
                        continue;
                    }

                    String[] parts = trimmed.split(",");
                    if (parts.length < 7) {
                        lines.add(line);
                        continue;
                    }

                    int currentId = Integer.parseInt(parts[0].trim());
                    if (currentId == id) {
                        parts[5] = "true";
                        line = String.join(", ", parts);
                    }
                    lines.add(line);
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            }

            System.out.println("Đã cập nhật isBought cho id = " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Lấy số tiền hiện tại từ file */ // tien dat ngay dau file balls.txt
    public static int getMoney(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#")) {
                    line = line.replace("#", "").trim();
                    return Integer.parseInt(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** Ghi lại số tiền mới vào file */
    public static void setMoney(String filePath, int newMoney) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String line;
            boolean moneyUpdated = false;

            while ((line = br.readLine()) != null) {
                if (!moneyUpdated && line.trim().startsWith("#")) {
                    // kiểm tra xem dòng có phải chứa chỉ số tiền không
                    // (chỉ chứa 1 số sau #, không có dấu phẩy)
                    if (!line.contains(",")) {
                        line = "# " + newMoney;
                        moneyUpdated = true;
                    }
                }
                lines.add(line);
            }

            try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
                for (String l : lines) pw.println(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Color parseColor(String s) {
        return switch (s.toUpperCase()) {
            case "RED" -> Color.RED;
            case "BLUE" -> Color.BLUE;
            case "GREEN" -> Color.GREEN;
            case "YELLOW" -> Color.YELLOW;
            case "CYAN" -> Color.CYAN;
            case "MAGENTA" -> Color.MAGENTA;
            case "PINK" -> Color.PINK;
            case "BLACK" -> Color.BLACK;
            case "ORANGE" -> Color.ORANGE;
            default -> null;
        };
    }
}
