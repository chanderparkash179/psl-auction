package com.psl.view;

import com.psl.dao.AuctionDAO;
import com.psl.dao.DashboardDAO;
import com.psl.config.DBConnection;
import com.psl.util.UITheme;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;

public class DashboardFrame extends JFrame {

    private final JTable table;
    private JTextField search;
    private final JComboBox<String> filter;
    private final JComboBox<String> teamFilter;
    private final JComboBox<String> roleFilter;
    private final JComboBox<String> categoryFilter;

    private StatCard intl, local, highest, team, count;

    public DashboardFrame() {

        setTitle("PSL Dashboard");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG);
        setContentPane(root);

        // ================= TOP CARDS =================
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

        // ================= FILTER BAR =================
        JPanel topBar = new JPanel();

        search = new JTextField(15);
        filter = new JComboBox<>(new String[]{"All", "Sold", "Retained"});

        // -------- NEW DROPDOWNS --------
        teamFilter = new JComboBox<>();
        roleFilter = new JComboBox<>();
        categoryFilter = new JComboBox<>();

        JButton btnSearch = UITheme.button("Search", UITheme.PRIMARY);

        topBar.add(search);
        topBar.add(filter);

        topBar.add(teamFilter);
        topBar.add(roleFilter);
        topBar.add(categoryFilter);

        topBar.add(btnSearch);

        center.add(topBar, BorderLayout.NORTH);

        // ================= TABLE =================
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

        // reload on filter change
        filter.addActionListener(e -> loadTable());
        teamFilter.addActionListener(e -> loadTable());
        roleFilter.addActionListener(e -> loadTable());
        categoryFilter.addActionListener(e -> loadTable());

        loadFilters();
        loadStats();
        loadTable();
    }

    // ================= LOAD FILTER DATA =================
    private void loadFilters() {

        try {
            Connection con = DBConnection.getConnection();

            // ================= TEAMS =================
            ResultSet rs1 = con.createStatement()
                    .executeQuery("SELECT Team_Name FROM Team WHERE IsDeleted=0");
            teamFilter.addItem("All Teams");
            while (rs1.next()) {
                teamFilter.addItem(rs1.getString(1));
            }

            // ================= ROLES =================
            String[] roles = {
                    "All Roles",
                    "Batter",
                    "Bowler",
                    "All-Rounder",
                    "Wicketkeeper"
            };

            for (String r : roles) {
                roleFilter.addItem(r);
            }

            // ================= CATEGORY =================
            ResultSet rs2 = con.createStatement()
                    .executeQuery("SELECT Category_Name FROM Category WHERE IsDeleted=0");

            categoryFilter.addItem("All Categories");

            while (rs2.next()) {
                categoryFilter.addItem(rs2.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LOAD TABLE =================
    private void loadTable() {

        String base = "SELECT a.Auction_ID, p.Player_Name, p.Primary_Role, " +
                "t.Team_Name, c.Category_Name, a.Final_Price, a.Status " +
                "FROM Auction a " +
                "JOIN Player p ON a.Player_ID = p.Player_ID " +
                "JOIN Team t ON a.Team_ID = t.Team_ID " +
                "JOIN Category c ON a.Category_ID = c.Category_ID " +
                "WHERE a.IsDeleted = 0 ";

        // ================= SAFE FETCH =================
        String status = (filter.getSelectedItem() != null)
                ? filter.getSelectedItem().toString()
                : "All";

        String team = (teamFilter.getSelectedItem() != null)
                ? teamFilter.getSelectedItem().toString()
                : "All Teams";

        String role = (roleFilter.getSelectedItem() != null)
                ? roleFilter.getSelectedItem().toString()
                : "All Roles";

        String cat = (categoryFilter.getSelectedItem() != null)
                ? categoryFilter.getSelectedItem().toString()
                : "All Categories";

        // ================= APPLY FILTERS =================

        if (!status.equals("All")) {
            base += " AND a.Status = '" + status + "'";
        }

        if (!team.equals("All Teams")) {
            base += " AND t.Team_Name = '" + team + "'";
        }

        if (!role.equals("All Roles")) {
            base += " AND p.Primary_Role = '" + role + "'";
        }

        if (!cat.equals("All Categories")) {
            base += " AND c.Category_Name = '" + cat + "'";
        }

        table.setModel(new AuctionDAO().getAuctionTableCustom(base));
    }

    // ================= STATS =================
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

    // ================= DELETE =================
    private void deleteAuction() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
            new AuctionDAO().softDelete(id);
            loadTable();
        }
    }
}