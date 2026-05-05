package com.psl.view;

import com.psl.config.DBConnection;
import com.psl.dao.AuctionDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuctionFrame extends JFrame {

    private JTable table;
    private JComboBox<String> playerBox, teamBox, categoryBox, statusBox, roundBox;
    private JTextField priceField;

    public AuctionFrame() {
        setTitle("Auction Management");
        setSize(1100, 650);
        setLayout(null);

        initUI();
        loadData();
        loadTable();
    }

    private void initUI() {

        // ================= DROPDOWNS =================
        playerBox = new JComboBox<>();
        teamBox = new JComboBox<>();
        categoryBox = new JComboBox<>();
        statusBox = new JComboBox<>(new String[]{"Sold", "Retained", "Unsold"});
        roundBox = new JComboBox<>(new String[]{"Pre-Auction", "Round 1", "Round 2"});

        playerBox.setBounds(20, 20, 200, 30);
        teamBox.setBounds(230, 20, 200, 30);
        categoryBox.setBounds(440, 20, 150, 30);
        statusBox.setBounds(600, 20, 120, 30);
        roundBox.setBounds(730, 20, 150, 30);

        add(playerBox);
        add(teamBox);
        add(categoryBox);
        add(statusBox);
        add(roundBox);

        priceField = new JTextField();
        priceField.setBounds(890, 20, 150, 30);
        add(priceField);

        JButton btnSave = new JButton("Finalize Auction");
        btnSave.setBounds(20, 70, 200, 30);
        add(btnSave);

        btnSave.addActionListener(e -> saveAuction());

        // ================= TABLE =================
        table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 120, 1020, 450);
        add(sp);
    }

    // ================= LOAD DROPDOWNS =================
    private void loadData() {
        try {
            Connection con = DBConnection.getConnection();

            ResultSet rs1 = con.createStatement()
                    .executeQuery("SELECT Player_Name FROM Player WHERE IsDeleted=0");
            while (rs1.next()) playerBox.addItem(rs1.getString(1));

            ResultSet rs2 = con.createStatement()
                    .executeQuery("SELECT Team_Name FROM Team WHERE IsDeleted=0");
            while (rs2.next()) teamBox.addItem(rs2.getString(1));

            ResultSet rs3 = con.createStatement()
                    .executeQuery("SELECT Category_Name FROM Category WHERE IsDeleted=0");
            while (rs3.next()) categoryBox.addItem(rs3.getString(1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= SAVE AUCTION =================
    private void saveAuction() {

        try {
            Connection con = DBConnection.getConnection();

            String sql =
                    "INSERT INTO Auction (Player_ID, Team_ID, Category_ID, Final_Price, Auction_Round, Acquisition_Type, Status, IsDeleted) " +
                            "VALUES (?,?,?,?,?,?,?,0)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, getId("Player", playerBox.getSelectedItem().toString()));
            ps.setInt(2, getId("Team", teamBox.getSelectedItem().toString()));
            ps.setInt(3, getId("Category", categoryBox.getSelectedItem().toString()));
            ps.setDouble(4, Double.parseDouble(priceField.getText()));
            ps.setString(5, roundBox.getSelectedItem().toString());
            ps.setString(6, "Manual");
            ps.setString(7, statusBox.getSelectedItem().toString());

            ps.executeUpdate();

            loadTable();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getId(String table, String name) {
        try {
            Connection con = DBConnection.getConnection();
            ResultSet rs = con.createStatement()
                    .executeQuery("SELECT * FROM " + table + " WHERE " +
                            table + "_Name='" + name + "'");

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ================= TABLE =================
    private void loadTable() {
        AuctionDAO dao = new AuctionDAO();
        table.setModel(dao.getAuctionTable(null, "All"));
    }
}