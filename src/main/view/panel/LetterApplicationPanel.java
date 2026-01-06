package main.view.panel;

import main.util.AppTheme;
import main.util.UiUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LetterApplicationPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private main.model.User currentUser;

    public LetterApplicationPanel(main.model.User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(AppTheme.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.COLOR_BACKGROUND);

        JLabel titleLabel = new JLabel("Permohonan Surat");
        titleLabel.setFont(AppTheme.FONT_HEADER_1);
        titleLabel.setForeground(AppTheme.COLOR_TEXT_DARK);

        JButton addButton = UiUtil.createFreshButton("+ Buat Permohonan Baru", AppTheme.COLOR_PRIMARY);
        addButton.addActionListener(e -> showAddDialog());

        JButton deleteButton = UiUtil.createFreshButton("Hapus", AppTheme.COLOR_WARNING);

        // Visibility Logic
        String role = user.getRole().toLowerCase();
        addButton.setVisible("warga".equals(role) || "admin".equals(role)); // Warga INSERT
        deleteButton.setVisible("admin".equals(role)); // Admin DELETE

        header.add(titleLabel, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(AppTheme.COLOR_BACKGROUND);

        JButton refreshBtn = UiUtil.createFreshButton("Refresh", AppTheme.COLOR_ACCENT);
        refreshBtn.addActionListener(e -> loadData());

        btnPanel.add(refreshBtn);
        if (addButton.isVisible())
            btnPanel.add(addButton);
        if (deleteButton.isVisible()) {
            btnPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            btnPanel.add(deleteButton);
        }

        header.add(btnPanel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Table List
        String[] columns = { "ID", "Tanggal", "NIK", "Nama Pemohon", "Jenis Surat", "Status", "Keperluan" };
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        UiUtil.styleTable(table);
        table.getColumnModel().getColumn(5).setCellRenderer(UiUtil.getStatusColumnRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        add(tableContainer, BorderLayout.CENTER);

        // Listeners for Delete
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih surat yang ingin dihapus!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Hapus Data",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this,
                        "Simulasi: Data berhasil dihapus (Implementasi Delete SQL menyusul)");
                // TODO: Implement actual SQL delete in Controller
            }
        });

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        java.util.List<main.model.Surat> list = main.controller.SuratController.getAllSurat();
        for (main.model.Surat s : list) {
            model.addRow(new Object[] {
                    s.getId(),
                    s.getTanggalRequest(),
                    s.getNik(),
                    s.getNamaPemohon(),
                    s.getJenisSurat(),
                    s.getStatus()
            });
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Buat Permohonan Baru", true);
        dialog.setSize(450, 600);
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing antar komponen
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // NIK
        JLabel labelNik = new JLabel("NIK Pemohon");
        labelNik.setFont(AppTheme.FONT_BODY_BOLD);
        labelNik.setForeground(AppTheme.COLOR_TEXT_DARK);
        contentPanel.add(labelNik, gbc);

        gbc.gridy++;
        JTextField nikField = new JTextField();
        nikField.setPreferredSize(new Dimension(200, 35));
        contentPanel.add(nikField, gbc);

        // Nama
        gbc.gridy++;
        JLabel labelNama = new JLabel("Nama Pemohon");
        labelNama.setFont(AppTheme.FONT_BODY_BOLD);
        labelNama.setForeground(AppTheme.COLOR_TEXT_DARK);
        contentPanel.add(labelNama, gbc);

        gbc.gridy++;
        JTextField namaField = new JTextField();
        namaField.setPreferredSize(new Dimension(200, 35));
        contentPanel.add(namaField, gbc);

        // Jenis Surat
        gbc.gridy++;
        JLabel labelJenis = new JLabel("Jenis Surat");
        labelJenis.setFont(AppTheme.FONT_BODY_BOLD);
        labelJenis.setForeground(AppTheme.COLOR_TEXT_DARK);
        contentPanel.add(labelJenis, gbc);

        gbc.gridy++;
        String[] types = { "Surat Keterangan Domisili", "Surat Pengantar SKCK", "Surat Keterangan Usaha",
                "Surat Keterangan Tidak Mampu" };
        JComboBox<String> typeCombo = new JComboBox<>(types);
        typeCombo.setPreferredSize(new Dimension(200, 35));
        typeCombo.setBackground(Color.WHITE);
        contentPanel.add(typeCombo, gbc);

        // Keperluan
        gbc.gridy++;
        JLabel labelKet = new JLabel("Keperluan / Keterangan");
        labelKet.setFont(AppTheme.FONT_BODY_BOLD);
        labelKet.setForeground(AppTheme.COLOR_TEXT_DARK);
        contentPanel.add(labelKet, gbc);

        gbc.gridy++;
        JTextArea descArea = new JTextArea(4, 20);
        descArea.setLineWrap(true);
        // Add border to textarea for clarity
        descArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane scrollDesc = new JScrollPane(descArea);
        contentPanel.add(scrollDesc, gbc);

        // Button
        gbc.gridy++;
        gbc.insets = new Insets(20, 5, 5, 5); // Add top margin for button
        JButton saveBtn = new JButton("Simpan Data");
        saveBtn.setFont(AppTheme.FONT_BODY_BOLD);
        saveBtn.setBackground(AppTheme.COLOR_PRIMARY);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setPreferredSize(new Dimension(200, 40));
        saveBtn.setFocusPainted(false);
        saveBtn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        contentPanel.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            main.model.Surat s = new main.model.Surat();
            s.setNik(nikField.getText());
            s.setNamaPemohon(namaField.getText());
            s.setJenisSurat((String) typeCombo.getSelectedItem());
            s.setKeperluan(descArea.getText());

            if (nikField.getText().isEmpty() || namaField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Mohon lengkapi NIK dan Nama.");
                return;
            }

            if (main.controller.SuratController.createSurat(s)) {
                JOptionPane.showMessageDialog(dialog, "Permohonan Berhasil Dibuat!");
                dialog.dispose();
                loadData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Gagal menyimpan data.");
            }
        });

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }
}
