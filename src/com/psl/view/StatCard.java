package com.psl.view;


import com.psl.util.UITheme;

import javax.swing.*;
import java.awt.*;

public class StatCard extends JPanel {

    public JLabel title;
    public JLabel main;
    public JLabel sub;

    public StatCard(String t) {

        setLayout(new GridLayout(3, 1));
        setBackground(UITheme.CARD);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        title = new JLabel(t);
        title.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        title.setForeground(Color.GRAY);

        main = new JLabel("-");
        main.setFont(new Font("Segoe UI", Font.BOLD, 16));

        sub = new JLabel("-");
        sub.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sub.setForeground(UITheme.PRIMARY);

        add(title);
        add(main);
        add(sub);
    }

    public void setData(String name, String value) {
        main.setText(name);
        sub.setText(value);
    }
}