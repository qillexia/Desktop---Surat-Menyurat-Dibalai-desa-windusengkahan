package main.view.panel;

import main.util.AppTheme;
import main.util.UiUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LetterVerificationPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private main.model.User currentUser;

    public LetterVerificationPanel(main.model.User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(AppTheme.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppTheme.COLOR_BACKGROUND);

        JLabel titleLabel = new JLabel("Verifikasi Surat");
        titleLabel.setFont(AppTheme.FONT_HEADER_1);
        titleLabel.setForeground(AppTheme.COLOR_TEXT_DARK);

        JLabel subLabel = new JLabel("Validasi dan Persetujuan Surat");
        subLabel.setFont(AppTheme.FONT_BODY);
        subLabel.setForeground(AppTheme.COLOR_TEXT_GRAY);

        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setBackground(AppTheme.COLOR_BACKGROUND);
        titleBox.add(titleLabel);
        titleBox.add(subLabel);

        headerPanel.add(titleBox, BorderLayout.WEST);

        JButton refreshBtn = UiUtil.createFreshButton("Refresh", AppTheme.COLOR_PRIMARY);
        refreshBtn.addActionListener(e -> loadData());
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table List
        String[] columns = { "ID", "Tanggal", "Nama Pemohon", "Jenis Surat", "Status" };
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        UiUtil.styleTable(table);
        table.getColumnModel().getColumn(4).setCellRenderer(UiUtil.getStatusColumnRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(AppTheme.COLOR_BACKGROUND);

        JButton rejectBtn = UiUtil.createFreshButton("Tolak", AppTheme.COLOR_WARNING);
        JButton validateBtn = UiUtil.createFreshButton("Validasi (Staf)", AppTheme.COLOR_ACCENT);
        JButton approveBtn = UiUtil.createFreshButton("Setujui (Kades)", AppTheme.COLOR_SUCCESS);
        JButton printBtn = UiUtil.createFreshButton("Cetak (Staf)", AppTheme.COLOR_PRIMARY);

        // Logic Visibility based on User Role
        String role = user.getRole().toLowerCase();

        if ("staff".equals(role)) {
            // Staff Tasks: READ (Table), UPDATE (Valid), PRINT (Cetak)
            actionPanel.add(rejectBtn); // Staff can reject? Assuming yes for bad data
            actionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            actionPanel.add(validateBtn);
            actionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            actionPanel.add(printBtn);
        } else if ("kades".equals(role)) {
            // Kades Tasks: UPDATE (Disetujui/TTD)
            actionPanel.add(rejectBtn);
            actionPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            actionPanel.add(approveBtn);
        }

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(AppTheme.COLOR_BACKGROUND);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        centerPanel.add(tableContainer, BorderLayout.CENTER);
        centerPanel.add(actionPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // Listeners
        // Listeners
        validateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih surat terlebih dahulu!");
                return;
            }
            String currentStatus = (String) table.getValueAt(row, 4);

            if ("Disetujui".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(this, "Surat sudah disetujui oleh Kades, tidak dapat diubah lagi!",
                        "Akses Dibatasi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!"Menunggu".equalsIgnoreCase(currentStatus) && !"Ditolak".equalsIgnoreCase(currentStatus)) { // Assuming
                                                                                                             // re-validation
                                                                                                             // is
                                                                                                             // allowed
                                                                                                             // only if
                                                                                                             // not
                                                                                                             // already
                                                                                                             // valid/approved
                // For simplicity, let's just allow re-validation unless it's finalized?
                // Let's just go with simple update for now, or maybe restrict if it's already
                // Approved?
                // "Staf Pelayanan: READ & UPDATE (Ubah status jadi Valid)"
            }
            updateStatus("Valid");
        });

        approveBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih surat terlebih dahulu!");
                return;
            }
            String currentStatus = (String) table.getValueAt(row, 4);

            if ("Disetujui".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(this, "Status sudah disetujui oleh kades",
                        "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Kades Requirement: Must be verified by staff (Status == Valid)
            if (!"Valid".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(this,
                        "Surat ini belum diverifikasi oleh Staf Pelayanan!\nStatus saat ini: " + currentStatus,
                        "Akses Ditolak", JOptionPane.WARNING_MESSAGE);
                return;
            }
            updateStatus("Disetujui");
        });

        rejectBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih surat terlebih dahulu!");
                return;
            }
            String currentStatus = (String) table.getValueAt(row, 4);

            if ("Disetujui".equalsIgnoreCase(currentStatus)) {
                if ("kades".equalsIgnoreCase(currentUser.getRole())) {
                    JOptionPane.showMessageDialog(this, "Status sudah disetujui oleh kades",
                            "Info", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Surat sudah disetujui oleh Kades, tidak dapat diubah lagi!",
                            "Akses Dibatasi", JOptionPane.WARNING_MESSAGE);
                }
                return;
            }

            // Kades Requirement: Must be verified by staff (Status == Valid)
            if ("kades".equalsIgnoreCase(currentUser.getRole()) && !"Valid".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(this,
                        "Surat ini belum diverifikasi oleh Staf Pelayanan!\nStatus saat ini: " + currentStatus,
                        "Akses Ditolak", JOptionPane.WARNING_MESSAGE);
                return;
            }

            updateStatus("Ditolak");
        });

        printBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih surat yang ingin dicetak!");
                return;
            }
            String status = (String) table.getValueAt(row, 4);
            if ("Disetujui".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(this, "Mencetak Surat... (Simulasi)\nHarap tunggu printer.", "Print",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Hanya surat yang sudah DISETUJUI yang bisa dicetak!",
                        "Gagal Cetak", JOptionPane.WARNING_MESSAGE);
            }
        });

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        java.util.List<main.model.Surat> list = main.controller.SuratController.getAllSurat();
        for (main.model.Surat s : list) {
            // Only show letters needing verification or recently processed? For now show
            // all or filter
            // Let's show all for transparency, or filter in SQL. Showing all is safer for
            // now.
            model.addRow(new Object[] {
                    s.getId(),
                    s.getTanggalRequest(),
                    s.getNamaPemohon(),
                    s.getJenisSurat(),
                    s.getStatus()
            });
        }
    }

    private void updateStatus(String status) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih surat terlebih dahulu!");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        if (main.controller.SuratController.updateStatus(id, status)) {
            JOptionPane.showMessageDialog(this, "Status berhasil diperbarui!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui status.");
        }
    }
}
