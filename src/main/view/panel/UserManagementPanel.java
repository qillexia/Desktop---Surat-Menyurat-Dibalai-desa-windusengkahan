package main.view.panel;

import main.util.AppTheme;
import main.util.UiUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserManagementPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public UserManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.COLOR_BACKGROUND);

        JLabel titleLabel = new JLabel("Manajemen User");
        titleLabel.setFont(AppTheme.FONT_HEADER_1);
        titleLabel.setForeground(AppTheme.COLOR_TEXT_DARK);

        JButton addUserBtn = UiUtil.createFreshButton("+ Tambah User", AppTheme.COLOR_PRIMARY);
        addUserBtn.addActionListener(e -> showAddUserDialog());

        header.add(titleLabel, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(AppTheme.COLOR_BACKGROUND);

        JButton refreshBtn = UiUtil.createFreshButton("Refresh", AppTheme.COLOR_ACCENT);
        refreshBtn.addActionListener(e -> loadData());

        btnPanel.add(refreshBtn);
        btnPanel.add(addUserBtn);

        header.add(btnPanel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Table List
        String[] columns = { "ID", "Username", "Role", "Status" };
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        UiUtil.styleTable(table);
        table.getColumnModel().getColumn(3).setCellRenderer(UiUtil.getStatusColumnRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        // Delete Button
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(AppTheme.COLOR_BACKGROUND);
        JButton deleteBtn = UiUtil.createFreshButton("Hapus User", AppTheme.COLOR_WARNING);
        deleteBtn.addActionListener(e -> deleteSelectedUser());
        actionPanel.add(deleteBtn);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(AppTheme.COLOR_BACKGROUND);
        centerPanel.add(tableContainer, BorderLayout.CENTER);
        centerPanel.add(actionPanel, BorderLayout.SOUTH);

        add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        java.util.List<main.model.User> list = main.controller.UserController.getAllUsers();
        for (main.model.User u : list) {
            model.addRow(new Object[] {
                    u.getId(),
                    u.getUsername(),
                    u.getRole(),
                    "Aktif" // Hardcoded active for now
            });
        }
    }

    private void deleteSelectedUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih user yang akan dihapus!");
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus user ID " + id + "?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (main.controller.UserController.deleteUser(id)) {
                JOptionPane.showMessageDialog(this, "User berhasil dihapus.");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus user.");
            }
        }
    }

    private void showAddUserDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tambah User Baru", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Username
        JLabel labelUser = new JLabel("Username");
        labelUser.setFont(AppTheme.FONT_BODY_BOLD);
        labelUser.setForeground(AppTheme.COLOR_TEXT_DARK);
        contentPanel.add(labelUser, gbc);

        gbc.gridy++;
        JTextField userField = new JTextField();
        userField.setPreferredSize(new Dimension(200, 35));
        contentPanel.add(userField, gbc);

        // Password
        gbc.gridy++;
        JLabel labelPass = new JLabel("Password");
        labelPass.setFont(AppTheme.FONT_BODY_BOLD);
        labelPass.setForeground(AppTheme.COLOR_TEXT_DARK);
        contentPanel.add(labelPass, gbc);

        gbc.gridy++;
        JPasswordField passField = new JPasswordField();
        passField.setPreferredSize(new Dimension(200, 35));
        contentPanel.add(passField, gbc);

        // Role
        gbc.gridy++;
        JLabel labelRole = new JLabel("Role");
        labelRole.setFont(AppTheme.FONT_BODY_BOLD);
        labelRole.setForeground(AppTheme.COLOR_TEXT_DARK);
        contentPanel.add(labelRole, gbc);

        gbc.gridy++;
        String[] roles = { "admin", "staff", "kades", "warga" };
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        roleCombo.setPreferredSize(new Dimension(200, 35));
        roleCombo.setBackground(Color.WHITE);
        contentPanel.add(roleCombo, gbc);

        // Button
        gbc.gridy++;
        gbc.insets = new Insets(20, 5, 5, 5);
        JButton saveBtn = new JButton("Simpan");
        saveBtn.setFont(AppTheme.FONT_BODY_BOLD);
        saveBtn.setBackground(AppTheme.COLOR_PRIMARY);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setPreferredSize(new Dimension(200, 40));
        saveBtn.setFocusPainted(false);
        saveBtn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        contentPanel.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            String u = userField.getText();
            String p = new String(passField.getPassword());
            String r = (String) roleCombo.getSelectedItem();

            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Username/Password tidak boleh kosong.");
                return;
            }

            if (main.controller.UserController.addUser(u, p, r)) {
                JOptionPane.showMessageDialog(dialog, "User Berhasil Ditambah!");
                dialog.dispose();
                loadData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Gagal menambah user (Mungkin username kembar?)");
            }
        });

        dialog.add(contentPanel);
        dialog.setVisible(true);
    }
}
