package ui;

import utils.Constants;
import core.InputHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ShopScene extends JPanel {
    private final List<Button> buttons = new ArrayList<>();
    private final InputHandler input;
    private final Runnable onBack;

    private String currentTab = "BALLS"; // mặc định mở BALLS
    private final GridPanel gridPanel;

    public ShopScene(InputHandler input, Runnable onBack) {
        this.input = input;
        this.onBack = onBack;

        this.gridPanel = new GridPanel();
        this.gridPanel.setBounds(0, 60, Constants.WIDTH, Constants.HEIGHT - 60);

        initUI();
        initButtons();
        initMouse();
        startRepaintTimer();
    }

    /** Khởi tạo UI cơ bản */
    private void initUI() {
        setBackground(Color.BLACK);
        setLayout(null); // quản lý thủ công
        add(gridPanel);

        // load dữ liệu mặc định
        loadTabData("BALLS");
    }

    /** Tạo các button */
    private void initButtons() {
        String[] texts = {"BACK", "BALLS", "PADDLES", "GACHA"};
        int tabCount = texts.length;
        int tabWidth = Constants.WIDTH / tabCount;  // 800 / 4 = 200
        int tabHeight = 50;

        buttons.clear();
        for (int i = 0; i < tabCount; i++) {
            int x = i * tabWidth;
            int y = 0;
            buttons.add(new Button(texts[i], x, y, tabWidth, tabHeight));
        }
    }

    /** Gắn mouse input */
    private void initMouse() {
        addMouseListener(input.createMouseAdapter());
        addMouseMotionListener(input.createMouseAdapter());
        addMouseWheelListener(input.createMouseAdapter()); // cuộn xuống grid
    }

    /** Khởi động timer repaint */
    private void startRepaintTimer() {
        new javax.swing.Timer(16, e -> repaint()).start();
    }

    /** Cập nhật trạng thái menu */
    private void update() {
        int mx = input.getMouseX();
        int my = input.getMouseY();

        for (Button button : buttons) {
            button.hovered = button.contains(mx, my);
            if (button.hovered && input.consumeClick()) {
                handleButtonClick(button.text);
            }
        }
    }

    /** Xử lý sự kiện click button */
    private void handleButtonClick(String text) {
        System.out.println("Clicked " + text);
        switch (text) {
            case "BACK":
                onBack.run();
                break;
            case "BALLS":
            case "PADDLES":
            case "GACHA":
                currentTab = text;
                loadTabData(text);
                break;
            default:
                break;
        }
    }

    /** Load dữ liệu theo tab */
    private void loadTabData(String tab) {
        if ("BALLS".equals(tab)) {
            gridPanel.setData(30); // tạm thời 30 ball skin
        } else if ("PADDLES".equals(tab)) {
            gridPanel.setData(20); // tạm thời 20 paddle skin
        } else if ("GACHA".equals(tab)) {
            gridPanel.setData(10); // placeholder cho gacha
        }
    }

    /** Vẽ background */
    private void drawBackground(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    /** Vẽ button */
    private void drawButtons(Graphics2D g2) {
        for (Button button : buttons) {
            button.draw(g2);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("Serif", Font.PLAIN, 32));

        update();
        drawBackground(g2);
        drawButtons(g2);
        // gridPanel được add() nên tự vẽ ở dưới
    }
}
