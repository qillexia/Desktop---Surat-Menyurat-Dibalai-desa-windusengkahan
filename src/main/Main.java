package main;

import javax.swing.*;
import main.util.AppTheme;
import main.util.DatabaseUtil;
import main.view.LoginView;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Test Database Connection
        System.out.println("----------------------------------------");
        System.out.println("[System] Memulai Aplikasi...");
        Connection conn = DatabaseUtil.getConnection();
        if (conn != null) {
            System.out.println("[System] Koneksi Database Berhasil! (MySQL Connected)");
            DatabaseUtil.initDatabase(conn); // Ensure tables exist & Seed admin
            try {
                conn.close();
            } catch (SQLException e) {
            } // Close test connection
        } else {
            System.err.println("[System] Gagal terhubung ke Database. Cek XAMPP & Konfigurasi.");
        }
        System.out.println("----------------------------------------");

        // Set Look and Feel (System default is usually best for integration, but we
        // will custom style)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}
