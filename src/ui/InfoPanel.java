package ui;

import core.ResourceLoader;
import entity.Skins;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private Skins selectedSkin;
    private Rectangle buyButtonBounds;
    private boolean hovered = false;
    private boolean isBall;

    public InfoPanel() {
        setOpaque(false);
        buyButtonBounds = new Rectangle(120, 420, 160, 50);

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent e) {
                hovered = buyButtonBounds.contains(e.getPoint());
                repaint();
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (selectedSkin != null && buyButtonBounds.contains(e.getPoint())) {
                    handleAction();
                }
            }
        });
    }

    public void showSkinInfo(Skins skin) {
        this.selectedSkin = skin;
        repaint();
    }

    private void handleAction() {
        if (selectedSkin == null) return;
        if (selectedSkin.isBought()) {
            System.out.println("Equipped " + selectedSkin.getName());
        } else {
            int money = ResourceLoader.getMoney("assets/images/Balls/balls.txt");
            int price = selectedSkin.getPrice();

            if (money >= price) {
                money -= price;
                selectedSkin.setBought(true);
                ResourceLoader.setMoney("assets/images/Balls/balls.txt", money);
                ResourceLoader.updateIsBought("assets/images/Balls/balls.txt", selectedSkin.getId());

                System.out.println("Đã mua " + selectedSkin.getName() + " với giá " + price);
            } else {
                System.out.println("Không đủ tiền để mua " + selectedSkin.getName());
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (selectedSkin == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // tên skin
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, 32));
        FontMetrics fm = g2.getFontMetrics();
        String name = selectedSkin.getName().toUpperCase();
        int nameWidth = fm.stringWidth(name);
        g2.drawString(name, (getWidth() - nameWidth) / 2, 80);

        // bóng hoặc paddle
        if (selectedSkin.getImg() != null) {
            if (isBall) {
                g2.drawImage(selectedSkin.getImg(), getWidth() / 2 - 100, 120, 200, 200, null);
            } else {
                g2.drawImage(selectedSkin.getImg(), getWidth() / 2 - 100, 200, 200, 50, null);
            }
        } else {
            g2.setColor(selectedSkin.getColor());
            if (isBall) {
                g2.fillOval(getWidth() / 2 - 80, 140, 160, 160);
            } else {
                g2.fillRect(getWidth() / 2 - 100, 200, 200, 50);
            }
        }

        // button vàng
        g2.setColor(hovered ? new Color(255, 230, 50) : new Color(255, 210, 0));
        g2.fillRoundRect(buyButtonBounds.x, buyButtonBounds.y, buyButtonBounds.width, buyButtonBounds.height, 25, 25);

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(buyButtonBounds.x, buyButtonBounds.y, buyButtonBounds.width, buyButtonBounds.height, 25, 25);

        // text BUY/EQUIP
        g2.setFont(new Font("Serif", Font.BOLD, 24));
        String text = selectedSkin.isBought() ? "EQUIP" : "BUY";
        int textWidth = g2.getFontMetrics().stringWidth(text);
        int textX = buyButtonBounds.x + (buyButtonBounds.width - textWidth) / 2;
        int textY = buyButtonBounds.y + buyButtonBounds.height / 2 + 8;
        g2.drawString(text, textX, textY);
    }

    public void setIsBall(boolean isBall) {
        this.isBall = isBall;
    }
}
