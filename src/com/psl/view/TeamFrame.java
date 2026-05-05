package com.psl.view;

import com.psl.dao.TeamDAO;
import com.psl.model.Team;
import com.psl.util.UITheme;

import javax.swing.*;
import java.awt.*;

public class TeamFrame extends JFrame {

    private final JTable table;
    private final JTextField search;

    public TeamFrame() {

        setTitle("Teams");
        setSize(900, 600);
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

        actions.add(add);
        actions.add(edit);
        actions.add(del);

        root.add(actions, BorderLayout.SOUTH);

        btnSearch.addActionListener(e -> load());
        add.addActionListener(e -> new TeamFormFrame(null, this).setVisible(true));
        edit.addActionListener(e -> edit());
        del.addActionListener(e -> delete());

        load();
    }

    private void load() {
        table.setModel(new TeamDAO().getTeams(search.getText()));
    }

    private void edit() {
        int r = table.getSelectedRow();
        if (r != -1) {
            int id = Integer.parseInt(table.getValueAt(r, 0).toString());
            Team t = new TeamDAO().getById(id);
            new TeamFormFrame(t, this).setVisible(true);
        }
    }

    private void delete() {
        int r = table.getSelectedRow();
        if (r != -1) {
            int id = Integer.parseInt(table.getValueAt(r, 0).toString());
            new TeamDAO().softDelete(id);
            load();
        }
    }

    public void refresh() {
        load();
    }
}