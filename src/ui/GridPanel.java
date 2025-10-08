package ui;

import core.ResourceLoader;
import entity.Skins;
import core.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class GridPanel extends JPanel {

    private List<Skins> skins;
    private final int cols = 3;
    private final int itemSize = 100;
    private final int gap = 10;

    private boolean isBall = true;
    private String currentTab = "BALLS";
    private final InfoPanel infoPanel;
    private final InputHandler input;
    private Skins selected;
    private BufferedImage background;

    private int scrollY = 0;
    private int maxScroll = 0;

    public GridPanel(InputHandler input) {
        this.input = input;
        setLayout(null);
        setBackground(Color.DARK_GRAY);
        setDoubleBuffered(true);

        this.background = ResourceLoader.loadImg("assets/images/shopBg.jpg");
        // Panel hiển thị thông tin skin
        this.infoPanel = new InfoPanel();
        this.infoPanel.setBounds(400, 50, 400, 500);
        this.infoPanel.setVisible(false);
        add(infoPanel);

        initMouse();
    }

    /** Gán danh sách skin và tính lại scroll */
    public void setSkins(List<Skins> skins) {
        this.skins = skins;
        updateSkinBounds();
        calcMaxScroll();
        repaint();
    }

    /** Gán vị trí từng skin (gọi khi load hoặc scroll) */
    private void updateSkinBounds() {
        if (skins == null) return;

        int x0 = gap;
        int y0 = gap - scrollY + 80;
        for (int i = 0; i < skins.size(); i++) {
            int row = i / cols;
            int col = i % cols;
            int x = x0 + col * (itemSize + gap);
            int y = y0 + row * (itemSize + gap);
            skins.get(i).setBounds(new Rectangle(x, y, itemSize, itemSize));
        }
    }

    /** Tính độ dài vùng cuộn */
    private void calcMaxScroll() {
        if (skins == null) {
            maxScroll = 0;
            return;
        }
        int rows = (int) Math.ceil(skins.size() / (double) cols);
        int contentHeight = rows * (itemSize + gap);
        int visibleHeight = getHeight() - 95;
        maxScroll = Math.max(0, contentHeight - visibleHeight);
    }

    /** Khi click vào 1 skin */
    private void handleSkinClick(Skins skin) {
        System.out.println("Clicked: " + skin.getName());
        this.selected = skin;
        infoPanel.setIsBall(isBall);
        infoPanel.setVisible(true);
        infoPanel.showSkinInfo(skin);
        repaint();
    }

    /** Click chuột chọn skin */
    private void handleMouseClick(int mx, int my) {
        if (skins == null) return;
        for (Skins s : skins) {
            if (s.contains(mx, my)) {
                handleSkinClick(s);
                break;
            }
        }
    }

    /** Giới hạn scroll */
    private void clampScroll() {
        if (scrollY < 0) scrollY = 0;
        if (scrollY > maxScroll) scrollY = maxScroll;
    }

    /** Khởi tạo mouse listener */
    private void initMouse() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });

        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            scrollY += notches * 20;
            clampScroll();
            updateSkinBounds();
            repaint();
        });
    }

    /** Vẽ toàn bộ panel */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        if (background != null) {
            g2.drawImage(background, 0, 0, getWidth(), getHeight(), null);
        } else {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // Giới hạn vùng vẽ các ô skin
        int clipTop = 80;
        int clipHeight = getHeight() - 95;
        Shape oldClip = g2.getClip();
        g2.setClip(new Rectangle(0, clipTop, getWidth(), clipHeight));

        // Vẽ danh sách skin
        if (skins != null) {
            for (Skins s : skins) {
                Rectangle b = s.getBounds();
                s.draw(g2, b.x, b.y, b.width, b.height, isBall);
            }
        }

        // Khôi phục clip cũ
        g2.setClip(oldClip);

        // Vẽ thanh cuộn
        drawScrollBar(g2);

        // Vẽ tiêu đề tab (BALLS / PADDLES)
        drawTitle(g2);

        g2.dispose();
    }

    /** Vẽ thanh cuộn */
    private void drawScrollBar(Graphics2D g2) {
        if (maxScroll <= 0) return;

        int barX = gap + cols * (itemSize + gap) + gap;
        int barY = 85;
        int barH = getHeight() - 100;
        int contentHeight = rowsHeight();
        int visibleHeight = getHeight() - 95;

        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barX, barY, 10, barH);

        int thumbH = Math.max(30, barH * visibleHeight / contentHeight);
        int thumbY = barY + (int) ((scrollY / (double) maxScroll) * (barH - thumbH));

        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(barX, thumbY, 10, thumbH);
    }

    /** Vẽ tiêu đề tab */
    private void drawTitle(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, 36));
        g2.drawString(currentTab, 20, 60);
    }

    /** Tính chiều cao tổng số dòng */
    private int rowsHeight() {
        if (skins == null) return 0;
        int rows = (int) Math.ceil(skins.size() / (double) cols);
        return rows * (itemSize + gap);
    }

    // ===== Getter / Setter =====
    public void setIsBall(boolean isBall) {
        this.isBall = isBall;
    }

    public void setCurrentTab(String tab) {
        this.currentTab = tab;
        this.infoPanel.setVisible(false);
        this.selected = null;
    }
}
