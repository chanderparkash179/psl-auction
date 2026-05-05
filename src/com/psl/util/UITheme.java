package com.psl.util;

import javax.swing.*;
import java.awt.*;

public class UITheme {

    public static Color BG = new Color(245, 247, 250);
    public static Color CARD = Color.WHITE;
    public static Color PRIMARY = new Color(41, 128, 185);
    public static Color SUCCESS = new Color(39, 174, 96);
    public static Color DANGER = new Color(192, 57, 43);
    public static Color TEXT = new Color(44, 62, 80);

    public static Font TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static Font NORMAL = new Font("Segoe UI", Font.PLAIN, 13);

    public static JButton button(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return btn;
    }
}