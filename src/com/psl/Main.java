package com.psl;

import com.psl.view.DashboardFrame;

public class Main {

    public static void main(String[] args) {

        // Swing UI thread safe launch
        javax.swing.SwingUtilities.invokeLater(() -> {
            new DashboardFrame().setVisible(true);
        });

    }
}