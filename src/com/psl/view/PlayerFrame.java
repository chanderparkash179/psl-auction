package com.psl.view;

import com.psl.dao.PlayerDAO;
import com.psl.form.PlayerFormFrame;
import com.psl.model.Player;
import com.psl.util.UITheme;

import javax.swing.*;
import java.awt.*;

public class PlayerFrame extends JFrame {

    private final JTable table;
    private final JTextField search;

    public PlayerFrame() {

        setTitle("Players");
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        setContentPane(root);

        JPanel top = new JPanel();
        search = new JTextField(20);

        JButton btnSearch = UITheme.button("Search", UITheme.PRIMARY);

        top.add(search);
        top.add(btnSearch);

        root.add(top, BorderLayout.NORTH);

        table = new JTable();
        table.setRowHeight(28);
        root.add(new JScrollPane(table), BorderLayout.CENTER);


        JPanel actions = new JPanel();


        JButton add = UITheme.button("Add", UITheme.SUCCESS);
        JButton edit = UITheme.button("Edit", UITheme.PRIMARY);
        JButton del = UITheme.button("Delete", UITheme.DANGER);
        JButton view = UITheme.button("View", UITheme.PRIMARY);
        this.viewBtnStyle(view);

        actions.add(view);
        actions.add(add);
        actions.add(edit);
        actions.add(del);

        view.setEnabled(false);

        root.add(actions, BorderLayout.SOUTH);

        btnSearch.addActionListener(e -> load());
        add.addActionListener(e -> new PlayerFormFrame(null, this).setVisible(true));
        edit.addActionListener(e -> edit());
        del.addActionListener(e -> delete());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                view.setEnabled(table.getSelectedRow() != -1);
            }
        });
        view.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a player first!");
                return;
            }

            int playerId = Integer.parseInt(table.getValueAt(row, 0).toString());
            String playerName = table.getValueAt(row, 1).toString();

            new PlayerCareerDialog(this, playerId, playerName).setVisible(true);
        });


        load();
    }

    private void load() {
        table.setModel(new PlayerDAO().getPlayers(search.getText(), "All"));
    }

    private void edit() {
        int r = table.getSelectedRow();
        if (r != -1) {
            int id = Integer.parseInt(table.getValueAt(r, 0).toString());
            Player p = new PlayerDAO().getById(id);
            new PlayerFormFrame(p, this).setVisible(true);
        }
    }

    private void delete() {
        int r = table.getSelectedRow();
        if (r != -1) {
            int id = Integer.parseInt(table.getValueAt(r, 0).toString());
            new PlayerDAO().softDelete(id);
            load();
        }
    }

    public void refresh() {
        load();
    }

    public void viewBtnStyle(JButton view) {
        view.setFont(new Font("Segoe UI", Font.BOLD, 12));
        view.setBackground(new Color(52, 152, 219));
        view.setForeground(Color.WHITE);
    }
}