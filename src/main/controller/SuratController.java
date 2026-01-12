package main.controller;

import main.model.Surat;
import main.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuratController {

    public static List<Surat> getAllSurat() {
        List<Surat> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM surat ORDER BY id DESC")) {

            while (rs.next()) {
                Surat s = new Surat();
                s.setId(rs.getInt("id"));
                s.setNik(rs.getString("nik"));
                s.setNamaPemohon(rs.getString("nama_pemohon"));
                s.setJenisSurat(rs.getString("jenis_surat"));
                s.setTanggalRequest(rs.getDate("tanggal_request"));
                s.setStatus(rs.getString("status"));
                s.setKeperluan(rs.getString("keperluan"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean createSurat(Surat surat) {
        String sql = "INSERT INTO surat (nik, nama_pemohon, jenis_surat, keperluan, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, surat.getNik());
            pstmt.setString(2, surat.getNamaPemohon());
            pstmt.setString(3, surat.getJenisSurat());
            pstmt.setString(4, surat.getKeperluan());
            pstmt.setString(5, "Menunggu Verifikasi");

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateStatus(int id, String newStatus) {
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("UPDATE surat SET status = ? WHERE id = ?")) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Surat getSuratById(int id) {
        Surat s = null;
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM surat WHERE id = ?")) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    s = new Surat();
                    s.setId(rs.getInt("id"));
                    s.setNik(rs.getString("nik"));
                    s.setNamaPemohon(rs.getString("nama_pemohon"));
                    s.setJenisSurat(rs.getString("jenis_surat"));
                    s.setTanggalRequest(rs.getDate("tanggal_request"));
                    s.setStatus(rs.getString("status"));
                    s.setKeperluan(rs.getString("keperluan"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static Map<String, Integer> getStats() {
        Map<String, Integer> stats = new HashMap<>();
        // Initialize 0
        stats.put("Total", 0);
        stats.put("Menunggu", 0);
        stats.put("Selesai", 0);

        try (Connection conn = DatabaseUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT status, COUNT(*) as count FROM surat GROUP BY status")) {

            int total = 0;
            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");
                total += count;

                if (status.contains("Menunggu")) {
                    stats.put("Menunggu", stats.get("Menunggu") + count);
                } else if (status.contains("Selesai") || status.contains("Disetujui")) {
                    stats.put("Selesai", stats.get("Selesai") + count);
                }
            }
            stats.put("Total", total);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }
}
