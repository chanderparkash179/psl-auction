package com.psl.form;

import com.psl.dao.PlayerDAO;
import com.psl.model.Player;
import com.psl.util.UITheme;
import com.psl.util.Validator;
import com.psl.view.PlayerFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class PlayerFormFrame extends JFrame {

    private final JTextField name;
    private final JTextField nationality;

    // DROPDOWN
    private final JComboBox<String> role;

    private Player player;
    private final PlayerFrame parent;

    public PlayerFormFrame(Player p, PlayerFrame parent) {

        this.player = p;
        this.parent = parent;

        setTitle("Player Form");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));

        panel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        name = new JTextField();

        nationality = new JTextField();

        role = new JComboBox<>(
                new String[]{
                        "Batter",
                        "Bowler",
                        "All-Rounder",
                        "Wicketkeeper"
                }
        );

        panel.add(label("Player Name"));
        panel.add(name);

        panel.add(label("Nationality"));
        panel.add(nationality);

        panel.add(label("Role"));
        panel.add(role);

        JButton btnSave = UITheme.button(
                "Save Player",
                UITheme.SUCCESS
        );

        panel.add(btnSave);

        add(panel);

        if (p != null) {

            name.setText(p.getPlayerName());

            nationality.setText(p.getNationality());

            role.setSelectedItem(p.getPrimaryRole());
        }

        btnSave.addActionListener(e -> save());
    }

    private JLabel label(String text) {

        JLabel lbl = new JLabel(text);

        lbl.setFont(UITheme.NORMAL);

        return lbl;
    }

    private void save() {

        String n = name.getText();

        String nat = nationality.getText();

        String r = Objects.requireNonNull(role.getSelectedItem()).toString();

        if (Validator.isEmpty(n)
                || Validator.isEmpty(nat)
                || Validator.isEmpty(r)) {

            JOptionPane.showMessageDialog(
                    this,
                    "All fields are required!"
            );

            return;
        }

        PlayerDAO dao = new PlayerDAO();

        int id =
                (player == null)
                        ? 0
                        : player.getPlayerId();

        if (dao.existsByName(n, id)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Player already exists!"
            );

            return;
        }

        if (player == null) {
            player = new Player();
        }

        player.setPlayerName(n);

        player.setNationality(nat);

        player.setPrimaryRole(r);

        dao.save(player);

        parent.refresh();

        dispose();
    }
}