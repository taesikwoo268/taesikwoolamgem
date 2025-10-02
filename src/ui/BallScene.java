package ui;

import core.InputHandler;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BallScene extends JPanel {
    private final InputHandler input;
    private final Runnable onBack;

    private List<String> ballItems = new ArrayList<>();
    private int scrollOffset = 0;

    private final int itemHeight = 100;   // chiều cao mỗi ô bóng
    private final int padding = 20;       // khoảng cách giữa các item

    public BallScene(InputHandler input, Runnable onBack) {
        this.input = input;
        this.onBack = onBack;
        setBackground(Color.BLACK);

        // Ví dụ danh sách bóng
        for (int i = 1; i <= 20; i++) {
            ballItems.add("Ball Skin " + i);
        }

        addMouseListener(input.createMouseAdapter());
        addMouseMotionListener(input.createMouseAdapter());
        addMouseWheelListener(input.createMouseAdapter());

        // Timer render
        new Timer(16, e -> repaint()).start();
    }

    private void update() {
        // Scroll bằng chuột
        int scroll = input.consumeScroll();
        if (scroll != 0) {
            scrollOffset += scroll * 30; // mỗi nấc cuộn = 30px
            // Giữ không cuộn vượt ngoài danh sách
            int maxOffset = Math.max(0, ballItems.size() * (itemHeight + padding) - Constants.HEIGHT);
            if (scrollOffset < 0) scrollOffset = 0;
            if (scrollOffset > maxOffset) scrollOffset = maxOffset;
        }

        // Click back nếu cần
        if (input.consumeClick()) {
            int mx = input.getMouseX();
            int my = input.getMouseY();
            if (mx >= 20 && mx <= 120 && my >= 20 && my <= 60) {
                onBack.run();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        update();

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Serif", Font.PLAIN, 28));

        // Vẽ nút Back
        g2.setColor(Color.RED);
        g2.fillRect(20, 20, 100, 40);
        g2.setColor(Color.WHITE);
        g2.drawString("BACK", 40, 50);

        // Vẽ danh sách bóng
        int startY = 100 - scrollOffset;
        for (int i = 0; i < ballItems.size(); i++) {
            int y = startY + i * (itemHeight + padding);

            if (y + itemHeight < 80) continue; // bỏ qua item ngoài màn hình trên
            if (y > Constants.HEIGHT) break;   // bỏ qua item ngoài màn hình dưới

            g2.setColor(new Color(50, 50, 50));
            g2.fillRoundRect(200, y, 400, itemHeight, 20, 20);
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(200, y, 400, itemHeight, 20, 20);

            g2.drawString(ballItems.get(i), 220, y + 60);
        }
    }
}
