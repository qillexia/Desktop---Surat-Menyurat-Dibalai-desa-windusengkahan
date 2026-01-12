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
                // Fetch full details
                int id = (int) table.getValueAt(row, 0);
                main.model.Surat surat = main.controller.SuratController.getSuratById(id);

                if (surat == null) {
                    JOptionPane.showMessageDialog(this, "Data surat tidak ditemukan!");
                    return;
                }

                // Create Official Letter View
                JTextPane printPane = new JTextPane();
                printPane.setContentType("text/html");
                printPane.setText("<html>" +
                        "<head><style>" +
                        "body { font-family: 'Times New Roman', serif; padding: 30px; font-size: 12pt; }" +
                        ".header-container { text-align: center; width: 100%; }" +
                        ".header-main { font-weight: bold; font-size: 16pt; margin: 0; }" +
                        ".header-sub { font-weight: bold; font-size: 14pt; margin: 0; }" +
                        ".address { font-size: 11pt; margin-top: 5px; font-style: italic; }" +
                        ".line { border-bottom: 3px double black; margin-top: 5px; margin-bottom: 10px; }" +
                        ".title { text-align: center; text-decoration: underline; font-weight: bold; font-size: 14pt; margin-bottom: 2px; }"
                        +
                        ".nomor { text-align: center; font-size: 12pt; margin-bottom: 15px; }" +
                        ".content { margin-left: 20px; margin-right: 20px; }" +
                        "p { margin-bottom: 4px; line-height: 1.1; text-align: justify; }" +
                        "table { width: 100%; margin-top: 5px; margin-bottom: 5px; border-collapse: collapse; }" +
                        "td { vertical-align: top; padding: 1px; }" +
                        "</style></head>" +
                        "<body>" +

                        "<div class='header-container'>" +
                        "<div class='header-main'>PEMERINTAH KABUPATEN KUNINGAN</div>" +
                        "<div class='header-sub'>KECAMATAN KUNINGAN</div>" +
                        "<div class='header-sub'>KELURAHAN WINDUSENGKAHAN</div>" +
                        "<div class='address'>Jl. Subur Kode Pos 45515</div>" +
                        "</div>" +
                        "<div class='line'></div>" +

                        "<div class='title'>SURAT KETERANGAN " + surat.getJenisSurat().toUpperCase() + "</div>" +
                        "<div class='nomor'>Nomor: 470 / " + surat.getId() + " / DS / "
                        + java.time.Year.now().getValue() + "</div>" +

                        "<p style='margin-top: 10px;'>Yang bertanda tangan di bawah ini Kepala Desa Windusengkahan, Kecamatan Kuningan, Kabupaten Kuningan, menerangkan bahwa:</p>"
                        +

                        "<div class='content'>" +
                        "<table>" +
                        "<tr><td width='130'>Nama</td><td width='10'>:</td><td><b>" + surat.getNamaPemohon()
                        + "</b></td></tr>" +
                        "<tr><td>Tempat/Tgl Lahir</td><td>:</td><td>Kuningan, 01 Januari 1990</td></tr>" +
                        "<tr><td>NIK</td><td>:</td><td>" + surat.getNik() + "</td></tr>" +
                        "<tr><td>Jenis Kelamin</td><td>:</td><td>Laki-laki</td></tr>" +
                        "<tr><td>Agama</td><td>:</td><td>Islam</td></tr>" +
                        "<tr><td>Pekerjaan</td><td>:</td><td>Wiraswasta</td></tr>" +
                        "<tr><td>Alamat</td><td>:</td><td>Desa Windusengkahan RT 01 RW 01</td></tr>" +
                        "</table>" +
                        "</div>" +

                        "<p>Bahwa nama yang tercantum di atas adalah benar-benar berdomisili di Desa Windusengkahan, Kecamatan Kuningan. Sepanjang pengamatan kami dan sesuai data yang ada dalam catatan kependudukan, orang tersebut adalah warga berkelakuan baik.</p>"
                        +

                        "<p>Surat keterangan ini diberikan untuk keperluan: <b>" + surat.getKeperluan() + "</b>.</p>" +

                        "<p>Demikian surat keterangan ini dibuat dengan sebenarnya untuk dapat dipergunakan sebagaimana mestinya.</p>"
                        +

                        "<table style='width: 100%; margin-top: 20px; page-break-inside: avoid;'>" +
                        "<tr>" +
                        "<td width='50%'></td>" +
                        "<td width='50%' align='center'>" +
                        "Windusengkahan, "
                        + java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy", new java.util.Locale("id", "ID"))
                                .format(java.time.LocalDate.now())
                        + "<br>" +
                        "Kepala Desa<br><br><br><br><br>" +
                        "<b><u>DIDI SUPARDI</u></b>" +
                        "</td>" +
                        "</tr>" +
                        "</table>" +

                        "</body></html>");

                try {
                    // Set Print Attributes (A4 Size & Metric Margins)
                    javax.print.attribute.PrintRequestAttributeSet attr = new javax.print.attribute.HashPrintRequestAttributeSet();
                    attr.add(javax.print.attribute.standard.MediaSizeName.ISO_A4);
                    // Margins: 15mm all sides
                    attr.add(new javax.print.attribute.standard.MediaPrintableArea(15, 15, 180, 267,
                            javax.print.attribute.standard.MediaPrintableArea.MM));

                    printPane.print(null, null, true, null, attr, true);
                } catch (java.awt.print.PrinterException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Gagal mencetak: " + ex.getMessage());
                }
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
