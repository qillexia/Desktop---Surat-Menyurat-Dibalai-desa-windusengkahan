package main.util;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ReportManager {

        /**
         * Menampilkan laporan surat menggunakan JasperReports.
         */
        public static void printLaporanSurat() {
                try (Connection conn = DatabaseUtil.getConnection()) {
                        if (conn == null) {
                                JOptionPane.showMessageDialog(null, "Gagal terhubung ke database!");
                                return;
                        }

                        // 1. Load file JRXML
                        // Pastikan file LaporanSurat.jrxml ada di package main.report atau di folder
                        // yang sesuai
                        InputStream reportStream = ReportManager.class
                                        .getResourceAsStream("/main/report/LaporanSurat.jrxml");

                        if (reportStream == null) {
                                JOptionPane.showMessageDialog(null,
                                                "File laporan tidak ditemukan!\nPastikan LaporanSurat.jrxml ada di /src/main/report/");
                                return;
                        }

                        // 2. Compile Report
                        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

                        // 3. Fill Report (isi data)
                        Map<String, Object> parameters = new HashMap<>(); // Parameter kosong jika tidak ada filter

                        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);

                        // 4. Tampilkan di Viewer
                        JasperViewer viewer = new JasperViewer(jasperPrint, false); // false = jangan exit aplikasi saat
                                                                                    // viewer ditutup
                        viewer.setTitle("Laporan Pengajuan Surat");
                        viewer.setVisible(true);

                } catch (JRException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error JasperReports:\n" + e.getMessage());
                } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error tidak diketahui:\n" + e.getMessage());
                }
        }
}
