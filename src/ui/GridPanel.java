package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GridPanel extends JPanel {
    private int cols = 3;
    private int itemSize = 100; // kich thuoc 1 o skin
    private int gap = 10; // khoang cach giua cac o
    private int itemCount = 40; // so luong skin hien thi

    private int scrollY = 0;       // vị trí cuộn hiện tại
    private int maxScroll = 0;     // cuộn tối đa

    public GridPanel() {
        setBackground(Color.BLACK);

        // bắt sự kiện cuộn chuột
        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            scrollY += notches * 20; // cuộn 20px mỗi nấc
            clampScroll();
            repaint();
        });
    }

    public void setData(int count) {
        this.itemCount = count;
        int rows = (int) Math.ceil(itemCount / (double) cols);
        int contentHeight = rows * (itemSize + gap);

        int visibleHeight = getHeight() - 15 - 80; // khung hiển thị
        maxScroll = Math.max(0, contentHeight - visibleHeight);

        scrollY = 0;
        repaint();
    }


    private void clampScroll() {
        if (scrollY < 0) scrollY = 0;
        if (scrollY > maxScroll) scrollY = maxScroll;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Vùng hiển thị: từ barY đến đáy panel
        int clipTop = 80;
        int clipBottom = getHeight() - 15;
        int clipHeight = clipBottom - clipTop;

        // Chỉ cho phép vẽ trong vùng này
        Shape oldClip = g2.getClip();
        g2.setClip(new Rectangle(0, clipTop, getWidth(), clipHeight));

        int x0 = gap, y0 = gap - scrollY + 80;
        for (int i = 0; i < itemCount; i++) {
            int row = i / cols;
            int col = i % cols;

            int x = x0 + col * (itemSize + gap);
            int y = y0 + row * (itemSize + gap);

            // vẽ skin placeholder
            g2.setColor(Color.GRAY);
            g2.fillRect(x, y, itemSize, itemSize); // can sua sang skin
            g2.setColor(Color.WHITE);
            g2.drawRect(x, y, itemSize, itemSize);

            g2.drawString("Skin " + (i + 1), x + 10, y + itemSize / 2);
        }

        // Khôi phục clip cũ để vẽ scrollbar bình thường
        g2.setClip(oldClip);

        // Vẽ thanh cuộn
        if (maxScroll > 0) {
            int barX = x0 + cols * (itemSize + gap) + gap;
            int barY = 85;
            int barH = getHeight() -100;
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(barX, barY, 10, barH);

            int thumbH = Math.max(30, barH * (getHeight() / (rowsHeight() + 1)));
            int thumbY = barY + (int) ((scrollY / (double) maxScroll) * (barH - thumbH));

            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(barX, thumbY, 10, thumbH);
        }
    }



    private int rowsHeight() {
        int rows = (int) Math.ceil(itemCount / (double) cols);
        return rows * (itemSize + gap);
    }
}
