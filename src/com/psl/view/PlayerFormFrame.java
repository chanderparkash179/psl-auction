package com.psl.view;

import com.psl.dao.PlayerDAO;
import com.psl.model.Player;
import com.psl.util.UITheme;

import javax.swing.*;
import java.awt.*;

public class PlayerFormFrame extends JFrame {

    private final JTextField name;
    private final JTextField nationality;
    private final JTextField role;
    private Player player;
    private final PlayerFrame parent;

    public PlayerFormFrame(Player p, PlayerFrame parent) {

        this.player = p;
        this.parent = parent;

        setTitle("Player Form");
        setSize(400, 300);
        setLayout(new GridLayout(4, 1, 10, 10));

        name = new JTextField();
        nationality = new JTextField();
        role = new JTextField();

        add(new JLabel("Name"));
        add(name);
        add(new JLabel("Nationality"));
        add(nationality);
        add(new JLabel("Role"));
        add(role);

        JButton btnSave = UITheme.button("Save", UITheme.SUCCESS);
        add(btnSave);

        if (p != null) {
            name.setText(p.getPlayerName());
            nationality.setText(p.getNationality());
            role.setText(p.getPrimaryRole());
        }

        btnSave.addActionListener(e -> save());
    }

    private void save() {

        if (player == null) player = new Player();

        player.setPlayerName(name.getText());
        player.setNationality(nationality.getText());
        player.setPrimaryRole(role.getText());

        new PlayerDAO().save(player);

        parent.refresh();
        dispose();
    }
}