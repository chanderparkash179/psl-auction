package com.psl.test;

import com.psl.config.DBConnection;

public class TestConnection {
    public static void main(String[] args) {
        DBConnection.getConnection();
    }
}