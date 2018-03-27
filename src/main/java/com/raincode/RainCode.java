package com.raincode;

import java.awt.*;
import java.util.Random;
import javax.swing.JFrame;

class RainCanvas extends Canvas implements Runnable {

    private int width, height;
    private Image offScreen; // 缓冲图片
    private char[][] charset; // 随机字符集合
    private int[] pos; // 列的起始位置
    private Color[] colors = new Color[30]; // 列的渐变颜色

    RainCanvas(int width, int height) {
        this.width = width;
        this.height = height;


        Random rand = new Random();
        charset = new char[width / 10][height / 10];
        for (int i = 0; i < charset.length; i++) {
            for (int j = 0; j < charset[i].length; j++) {
                charset[i][j] = (char) (rand.nextInt(96) + 48);
            }
        }

        pos = new int[charset.length];
        for (int i = 0; i < pos.length; i++) {
            pos[i] = rand.nextInt(pos.length);
        }

        for (int i = 0; i < colors.length - 1; i++) {
            colors[i] = new Color(0, 255 / colors.length * (i + 1), 0);
        }
        colors[colors.length - 1] = new Color(255, 255, 255);

        setBackground(Color.BLACK);

        setSize(width, height);
        setVisible(true);
    }

    public void startRain() {
        new Thread(this).start();
    }

    private void drawRain() {

        if (offScreen == null) {
            return;
        }
        Random rand = new Random();
        Graphics g = offScreen.getGraphics();
        g.clearRect(0, 0, width, height);
        g.setFont(new Font("Arial", Font.PLAIN, 14));


        for (int i = 0; i < charset.length; i++) {
            int speed = rand.nextInt(3);
            for (int j = 0; j < colors.length; j++) {
                int index = (pos[i] + j) % charset[i].length;
                g.setColor(colors[j]);

                g.drawChars(charset[i], index, 1, i * 10, index * 10);
            }
            pos[i] = (pos[i] + 1) % charset[i].length;
        }
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    public void run() {
        while (true) {
            drawRain();
            repaint();

            try {
                Thread.sleep(50); // 可改变睡眠时间以调节速度
            } catch (InterruptedException e) {
                System.out.println(e);

            }
        }
    }

    @Override
    public void paint(Graphics g) {
        if (offScreen == null) {
            offScreen = createImage(width, height);
        }

        g.drawImage(offScreen, 0, 0, this);
    }
}

public class RainCode extends JFrame {

    public RainCanvas canvas = new RainCanvas(1366, 768);

    public RainCode() {
        super("代码雨");
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        canvas = new RainCanvas(this.getWidth(), this.getHeight());
        getContentPane().add(canvas);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


}