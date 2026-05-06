package com.psl.view;

import com.psl.dao.DashboardDAO;
import com.psl.dao.AuctionDAO;
import com.psl.form.AuctionEditDialog;
import com.psl.util.UITheme;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private JTable table;
    private JTextField search;
    private JComboBox<String> filter;

    private StatCard intl, local, highest, team, count;

    public DashboardFrame() {

        setTitle("PSL Dashboard");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG);
        setContentPane(root);

        // ================= CARDS =================
        JPanel cards = new JPanel(new GridLayout(1, 5, 15, 15));
        cards.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        intl = new StatCard("Int. High Paid");
        local = new StatCard("National High Paid");
        highest = new StatCard("Highest Paid");
        team = new StatCard("Top Spending Team");
        count = new StatCard("Max Players Team");

        cards.add(intl);
        cards.add(local);
        cards.add(highest);
        cards.add(team);
        cards.add(count);

        root.add(cards, BorderLayout.NORTH);

        // ================= CENTER =================
        JPanel center = new JPanel(new BorderLayout());

        // 🔍 SEARCH BAR BACK
        JPanel topBar = new JPanel();

        search = new JTextField(20);
        filter = new JComboBox<>(new String[]{"All", "Sold", "Retained"});

        JButton btnSearch = UITheme.button("Search", UITheme.PRIMARY);

        topBar.add(search);
        topBar.add(filter);
        topBar.add(btnSearch);

        center.add(topBar, BorderLayout.NORTH);

        // TABLE
        table = new JTable();
        table.setRowHeight(28);

        center.add(new JScrollPane(table), BorderLayout.CENTER);

        root.add(center, BorderLayout.CENTER);

        // ================= ACTIONS =================
        JPanel actions = new JPanel();

        JButton btnTeams = UITheme.button("Teams", UITheme.PRIMARY);
        JButton btnPlayers = UITheme.button("Players", UITheme.PRIMARY);
        JButton btnEdit = UITheme.button("Edit Auction", UITheme.SUCCESS);
        JButton btnDelete = UITheme.button("Delete Auction", UITheme.DANGER);

        actions.add(btnTeams);
        actions.add(btnPlayers);
        actions.add(btnEdit);
        actions.add(btnDelete);

        root.add(actions, BorderLayout.SOUTH);

        // ================= EVENTS =================
        btnSearch.addActionListener(e -> loadTable());
        btnTeams.addActionListener(e -> new TeamFrame().setVisible(true));
        btnPlayers.addActionListener(e -> new PlayerFrame().setVisible(true));
        btnDelete.addActionListener(e -> deleteAuction());
        btnEdit.addActionListener(e -> editAuction());

        loadStats();
        loadTable();
    }

    private void editAuction() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a record first!");
            return;
        }

        int auctionId = Integer.parseInt(table.getValueAt(row, 0).toString());

        new AuctionEditDialog(this, auctionId, this).setVisible(true);
    }

    private void loadStats() {

        DashboardDAO dao = new DashboardDAO();

        String[] i = dao.topInternational();
        intl.setData(i[0], i[1] + " Cr");

        String[] l = dao.topNational();
        local.setData(l[0], l[1] + " Cr");

        String[] h = dao.highestPaid();
        highest.setData(h[0], h[1] + " Cr");

        String[] t = dao.topSpendingTeam();
        team.setData(t[0], t[1] + " Cr");

        String[] c = dao.maxPlayersTeam();
        count.setData(c[0], c[1] + " Players");
    }

    private void loadTable() {
        table.setModel(
                new AuctionDAO().getAuctionTable(
                        search.getText(),
                        filter.getSelectedItem().toString()
                )
        );
    }

    private void deleteAuction() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
            new AuctionDAO().softDelete(id);
            loadTable();
        }
    }
}