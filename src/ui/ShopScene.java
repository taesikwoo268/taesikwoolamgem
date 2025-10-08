package ui;

import utils.Constants;
import core.InputHandler;
import core.ResourceLoader;
import entity.Skins;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ShopScene extends JPanel {
    private final List<Button> buttons = new ArrayList<>();
    private final InputHandler input;
    private final Runnable onBack;

    private String currentTab = "BALLS"; // mặc định mở BALLS
    private static BufferedImage iconBack = ResourceLoader.loadImg("assets/images/Xbutton.png");
    private static BufferedImage iconBall = ResourceLoader.loadImg("assets/images/iconBall.png");
    private static BufferedImage iconPaddle = ResourceLoader.loadImg("assets/images/iconPaddle.png");
    private static BufferedImage iconGacha = ResourceLoader.loadImg("assets/images/Xbutton.png");
    private final GridPanel gridPanel;

    private List<Skins> ballSkins;
    private List<Skins> paddleSkins;

    public ShopScene(InputHandler input, Runnable onBack) {
        this.input = input;
        this.onBack = onBack;

        this.gridPanel = new GridPanel(input);
        this.gridPanel.setBounds(0, 55, Constants.WIDTH, Constants.HEIGHT - 60);
        add(gridPanel);
        initUI();
        initButtons();
        initMouse();
        startRepaintTimer();
    }

    /** Khởi tạo UI cơ bản */
    private void initUI() {
        setBackground(Color.GRAY);
        setLayout(null); // quản lý thủ công
        ballSkins = ResourceLoader.loadSkins("assets/images/Balls/balls.txt");
        paddleSkins = new ArrayList<>();

        // Hiển thị luôn tab BALLS
        gridPanel.setSkins(ballSkins);
    }

    /** Tạo các button */
    private void initButtons() {
        buttons.add(new Button("BACK",iconBack,20,0,50,50));
        buttons.add(new Button("BALLS",iconBall,250,0,50,50));
        buttons.add(new Button("PADDLES",iconPaddle,350,0,50,50));
        buttons.add(new Button("GACHA",iconGacha,450,0,50,50));
    }

    /** Gắn mouse input */
    private void initMouse() {
        addMouseListener(input.createMouseAdapter());
        addMouseMotionListener(input.createMouseAdapter());
        addMouseWheelListener(input.createMouseAdapter());
    }

    /** Khởi động timer repaint */
    private void startRepaintTimer() {
        new javax.swing.Timer(16, e -> repaint()).start();
    }

    /** Cập nhật trạng thái shop */
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
                currentTab = "BALLS";
                if (ballSkins.isEmpty()) { // chỉ load lần đầu
                    ballSkins = ResourceLoader.loadSkins("assets/images/ball.txt");
                }
                gridPanel.setSkins(ballSkins);
                gridPanel.setIsBall(true);
                gridPanel.setCurrentTab(currentTab);
                break;
            case "PADDLES":
                currentTab = "PADDLES";
                if (paddleSkins.isEmpty()) {
                    paddleSkins = ResourceLoader.loadSkins("assets/images/Paddles/paddles.txt");
                }
                gridPanel.setSkins(paddleSkins);
                gridPanel.setIsBall(false);
                gridPanel.setCurrentTab(currentTab);
                break;
            case "GACHA":
                currentTab = "GACHA";
                gridPanel.setCurrentTab(currentTab);
                break;
            default:
                break;
        }
    }


    /** Vẽ background */
    private void drawBackground(Graphics2D g2) {
        BufferedImage bar = ResourceLoader.loadImg("assets/images/topBar.png");
        g2.drawImage(bar,0,0,800,50,null);
    }

    /** Vẽ button */
    private void drawButtons(Graphics2D g2) {
        for (Button button : buttons) {
            if (button.text == currentTab) {
                g2.setColor(new Color(255, 255, 0, 100));
                g2.fillRect(button.bound.x,button.bound.y,50,50);
            }
            button.draw(g2);
        }
    }
    /** Vẽ tiền người chơi có */
    private void drawMoney(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Monospaced", Font.BOLD, 24));
        int money = ResourceLoader.getMoney("assets/images/Balls/balls.txt");
        g2.drawString(money + "\uD83D\uDCB0",700,30);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("Serif", Font.PLAIN, 32));

        update();
        drawBackground(g2);
        drawMoney(g2);
        drawButtons(g2);

        //gridPanel.paintComponent(g);
    }
}
