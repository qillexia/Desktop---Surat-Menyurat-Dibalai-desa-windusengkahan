package main.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    // 1. Ganti konfigurasi ini sesuai database komputer Anda
    private static final String URL = "jdbc:mysql://localhost:3306/db_windusengkahan";
    private static final String USER = "root"; // Default user XAMPP
    private static final String PASSWORD = ""; // Default password XAMPP (biasanya kosong)

    /**
     * Mencoba membuat koneksi ke database.
     * Pastikan library MySQL JDBC Driver sudah ditambahkan ke project!
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // 2. Load driver JDBC (Penting untuk aplikasi non-Maven/Gradle terkadang)
            // Pastikan Anda sudah menambahkan file .jar mysql-connector ke 'Referenced
            // Libraries'
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 3. Buat koneksi
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            // System.out.println("DEBUG: Koneksi database berhasil!");

        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: Driver JDBC MySQL tidak ditemukan!");
            System.err.println("Tips: Download mysql-connector-j-8.x.jar dan tambahkan ke library project.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("ERROR: Gagal terhubung ke database!");
            System.err.println("Cek: Apakah XAMPP/MySQL sudah jalan? Apakah nama database benar?");
            e.printStackTrace();
        }
        return connection;
    }

    // Test koneksi sederhana (bisa dijalankan langsung klik kanan -> Run)
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("SUKSES: Berhasil terhubung ke database " + URL);
            initDatabase(conn); // Initialize tables for testing
        } else {
            System.out.println("GAGAL: Tidak bisa terhubung.");
        }
    }

    public static void initDatabase(Connection conn) {
        try {
            java.sql.Statement stmt = conn.createStatement();

            // 1. Create Users Table
            String createUsers = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) NOT NULL UNIQUE, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "role VARCHAR(20) NOT NULL" +
                    ")";
            stmt.execute(createUsers);

            // 1.5 Create Surat Table
            String createSurat = "CREATE TABLE IF NOT EXISTS surat (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nik VARCHAR(20) NOT NULL, " +
                    "nama_pemohon VARCHAR(100) NOT NULL, " +
                    "jenis_surat VARCHAR(100) NOT NULL, " +
                    "tanggal_request DATE DEFAULT (CURRENT_DATE), " +
                    "status VARCHAR(50) DEFAULT 'Menunggu Verifikasi', " +
                    "keperluan TEXT" +
                    ")";
            stmt.execute(createSurat);

            // Fix for existing tables with small column size
            try {
                stmt.execute("ALTER TABLE surat MODIFY status VARCHAR(50)");
            } catch (SQLException ignored) {
                // Ignore if fails (e.g. table doesn't exist yet or other unimportant error)
            }

            // 2. Check if admin exists
            java.sql.PreparedStatement checkAdmin = conn
                    .prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?");
            checkAdmin.setString(1, "admin");
            java.sql.ResultSet rs = checkAdmin.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                // 3. Insert default admin
                String insertAdmin = "INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin')";
                stmt.execute(insertAdmin);
                System.out.println("[DatabaseUtil] Default user 'admin' created.");
            }

            System.out.println("[DatabaseUtil] Tables checked/initialized.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
