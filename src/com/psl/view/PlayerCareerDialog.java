package com.psl.view;

import com.psl.dao.PlayerCareerDAO;
import com.psl.model.PlayerCareer;
import com.psl.util.UITheme;

import javax.swing.*;
import java.awt.*;

public class PlayerCareerDialog extends JDialog {

    private final int playerId;

    private JLabel lblName, lblTeam;

    private JTextField txtMatches, txtRuns, txtAvg, txtSR, txtWickets, txtEco;

    public PlayerCareerDialog(JFrame parent, int playerId, String playerName) {
        super(parent, "Player Career", true);

        this.playerId = playerId;

        setSize(420, 500);
        setLocationRelativeTo(parent);

        initUI(playerName);
        loadData();
    }

    private void initUI(String playerName) {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG);
        setContentPane(root);

        // ===== HEADER =====
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        lblName = new JLabel(playerName);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));

        lblTeam = new JLabel("-");
        lblTeam.setForeground(Color.GRAY);

        header.add(lblName);
        header.add(lblTeam);

        root.add(header, BorderLayout.NORTH);

        // ===== FORM =====
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        txtMatches = field(form, "Matches");
        txtRuns = field(form, "Runs");
        txtAvg = field(form, "Average");
        txtSR = field(form, "Strike Rate");
        txtWickets = field(form, "Wickets");
        txtEco = field(form, "Economy");

        root.add(form, BorderLayout.CENTER);

        // ===== BUTTON =====
        JButton btnSave = UITheme.button("Save Changes", UITheme.SUCCESS);

        JPanel bottom = new JPanel();
        bottom.add(btnSave);

        root.add(bottom, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> saveData());
    }

    private JTextField field(JPanel panel, String label) {
        panel.add(new JLabel(label));
        JTextField tf = new JTextField();
        panel.add(tf);
        return tf;
    }

    private void loadData() {

        PlayerCareerDAO dao = new PlayerCareerDAO();

        PlayerCareer pc = dao.getByPlayerId(playerId);

        String team = dao.getCurrentTeam(playerId);
        lblTeam.setText("Team: " + team);

        if (pc != null) {
            txtMatches.setText(String.valueOf(pc.getPslMatches()));
            txtRuns.setText(String.valueOf(pc.getTotalRuns()));
            txtAvg.setText(String.valueOf(pc.getBattingAvg()));
            txtSR.setText(String.valueOf(pc.getStrikeRate()));
            txtWickets.setText(String.valueOf(pc.getWickets()));
            txtEco.setText(String.valueOf(pc.getEconomy()));
        }
    }

    private void saveData() {

        try {

            PlayerCareer pc = new PlayerCareer();

            pc.setPlayerId(playerId);
            pc.setPslMatches(Integer.parseInt(txtMatches.getText()));
            pc.setTotalRuns(Integer.parseInt(txtRuns.getText()));
            pc.setBattingAvg(Double.parseDouble(txtAvg.getText()));
            pc.setStrikeRate(Double.parseDouble(txtSR.getText()));
            pc.setWickets(Integer.parseInt(txtWickets.getText()));
            pc.setEconomy(Double.parseDouble(txtEco.getText()));

            new PlayerCareerDAO().update(pc);

            JOptionPane.showMessageDialog(this, "Updated Successfully");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}