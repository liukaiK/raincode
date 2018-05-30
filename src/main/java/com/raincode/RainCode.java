package com.raincode;

import java.awt.*;
import java.util.Random;
import javax.swing.*;

class RainCanvas extends Canvas implements Runnable {

    private int width, height;
    /**
     * 缓冲图片
     */
    private Image offScreen;
    /**
     * 随机字符集合
     */
    private char[][] charset;
    /**
     * 列的起始位置
     */
    private int[] pos;
    /**
     * 列的渐变颜色
     */
    private Color[] colors = new Color[30];

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

    void startRain() {
        new Thread(this).start();
    }

    private void drawRain() {

        if (offScreen == null) {
            return;
        }
        Graphics g = offScreen.getGraphics();
        g.clearRect(0, 0, width, height);
        g.setFont(new Font("Arial", Font.PLAIN, 14));


        for (int i = 0; i < charset.length; i++) {
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
                /*
                可改变睡眠时间以调节速度
                 */
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
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

class RainCode extends JFrame {

    RainCanvas canvas;

    RainCode() {
        super("代码雨");
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        canvas = new RainCanvas(this.getWidth(), this.getHeight());
        getContentPane().add(canvas);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }


}