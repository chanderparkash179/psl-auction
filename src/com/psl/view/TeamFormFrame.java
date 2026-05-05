package com.psl.view;

import com.psl.dao.TeamDAO;
import com.psl.model.Team;
import com.psl.util.UITheme;

import javax.swing.*;
import java.awt.*;

public class TeamFormFrame extends JFrame {

    private final JTextField name;
    private Team team;
    private final TeamFrame parent;

    public TeamFormFrame(Team t, TeamFrame parent) {

        this.team = t;
        this.parent = parent;

        setTitle("Team Form");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        name = new JTextField();

        add(new JLabel("Team Name"));
        add(name);

        JButton btnSave = UITheme.button("Save", UITheme.SUCCESS);
        add(btnSave);

        if (t != null) name.setText(t.getTeamName());

        btnSave.addActionListener(e -> save());
    }

    private void save() {

        if (team == null) team = new Team();

        team.setTeamName(name.getText());

        new TeamDAO().save(team);

        parent.refresh();
        dispose();
    }
}