package ui;

import entity.Skins;
import core.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GridPanel extends JPanel {
    private List<Skins> skins = new ArrayList<>();
    private int cols = 3;
    private int itemSize = 100; // kích thước 1 ô skin
    private int gap = 10;       // khoảng cách giữa các ô

    private boolean isBall = true;
    private String currentTab = "BALLS";
    private InfoPanel infoPanel;
    private final InputHandler input;
    private Skins selected;

    private int scrollY = 0;    // vị trí cuộn hiện tại
    private int maxScroll = 0;  // cuộn tối đa

    public GridPanel(InputHandler input) {
        this.input = input;
        setBackground(Color.DARK_GRAY);
        this.infoPanel = new InfoPanel();
        this.infoPanel.setBounds(400, 50, 400, 500); // vùng bên phải
        add(infoPanel);
        // bắt sự kiện cuộn chuột
        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            scrollY += notches * 20; // cuộn 20px mỗi nấc
            clampScroll();
            repaint();
        });
    }

    /** Gán danh sách skin và tính lại scroll */
    public void setSkins(List<Skins> skins) {
        this.skins = skins;

        int rows = (int) Math.ceil(skins.size() / (double) cols);
        int contentHeight = rows * (itemSize + gap);

        int visibleHeight = getHeight() - 95; // khung hiển thị (80 -> height-15)
        maxScroll = Math.max(0, contentHeight - visibleHeight);

        scrollY = 0;
        repaint();
    }

    private void update() {
        int mx = input.getMouseX();
        int my = input.getMouseY();

        for (Skins i : skins) {
            boolean hovered = i.contains(mx,my);
            if(hovered && input.consumeClick()) {
                handleSkinClick(i);
            }
        }
    }
    private void handleSkinClick(Skins skin) {
        System.out.println("Clicked: " + skin.getName());
        this.selected = skin;
        infoPanel.setVisible(true);
        infoPanel.showSkinInfo(skin);
    }

    private void clampScroll() {
        if (scrollY < 0) scrollY = 0;
        if (scrollY > maxScroll) scrollY = maxScroll;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // vùng hiển thị từ 80 đến height - 15
        int clipTop = 80;
        int clipHeight = getHeight() - 95;
        Shape oldClip = g2.getClip();
        g2.setClip(new Rectangle(0, clipTop, getWidth(), clipHeight));

        // vẽ skins
        int x0 = gap, y0 = gap - scrollY + clipTop;
        for (int i = 0; i < skins.size(); i++) {
            int row = i / cols;
            int col = i % cols;

            int x = x0 + col * (itemSize + gap);
            int y = y0 + row * (itemSize + gap);

            Skins skin = skins.get(i);
            skin.setBounds(new Rectangle(x, y, itemSize, itemSize));
            skin.draw(g2, x, y, itemSize, itemSize, isBall);
        }

        g2.setClip(oldClip);

        update();

        // vẽ thanh cuộn
        if (maxScroll > 0) {
            int barX = x0 + cols * (itemSize + gap) + gap;
            int barY = 85;
            int barH = getHeight() - 100;

            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(barX, barY, 10, barH);

            int contentHeight = rowsHeight();
            int visibleHeight = getHeight() - 95;
            int thumbH = Math.max(30, barH * visibleHeight / contentHeight);
            int thumbY = barY + (int) ((scrollY / (double) maxScroll) * (barH - thumbH));

            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(barX, thumbY, 10, thumbH);
        }
        // vẽ title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, 36));
        g2.drawString(currentTab, 20, 60);
    }

    /** Chiều cao toàn bộ grid (tính theo số skin) */
    private int rowsHeight() {
        int rows = (int) Math.ceil(skins.size() / (double) cols);
        return rows * (itemSize + gap);
    }

    public void setIsBall(boolean isBall) {
        this.isBall = isBall;
    }
    public void setCurrentTab(String tab) {
        this.currentTab = tab;
    }
}
