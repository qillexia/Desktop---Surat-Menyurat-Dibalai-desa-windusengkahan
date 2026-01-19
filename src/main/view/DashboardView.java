package main.view;

import main.util.AppTheme;
import main.view.panel.HomePanel;
import main.view.panel.LetterApplicationPanel;
import main.view.panel.LetterVerificationPanel;
import main.view.panel.UserManagementPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardView extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private main.model.User currentUser;

    public DashboardView(main.model.User user) {
        this.currentUser = user;
        setTitle("Dashboard - Sistem Pelayanan Surat Desa Windusengkahan");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Sidebar (Navigation) ---
        JPanel sidebar = new JPanel();
        sidebar.setBackground(AppTheme.COLOR_PRIMARY);
        sidebar.setPreferredSize(new Dimension(250, 800));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Sidebar Title / Logo
        JLabel appTitle = new JLabel("<html><center>Sistem<br>Desa Windusengkahan</center></html>");
        appTitle.setFont(AppTheme.FONT_HEADER_2);
        appTitle.setForeground(AppTheme.COLOR_WHITE);
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        appTitle.setHorizontalAlignment(SwingConstants.CENTER);

        sidebar.add(appTitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        // --- Main Content Area (CardLayout) ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(AppTheme.COLOR_BACKGROUND);

        // Add Panels
        contentPanel.add(new HomePanel(), "Beranda");

        // Filter Panels Based on Role
        String role = user.getRole().toLowerCase();

        // Permohonan Surat: Warga (Create), Staff (Read), Admin (Delete)
        // Note: Kades doesn't need to see this raw list, just Verification.
        if ("admin".equals(role) || "warga".equals(role) || "staff".equals(role)) {
            contentPanel.add(new LetterApplicationPanel(user), "Permohonan Surat");
        }

        // Verifikasi Surat: Staff (Validate/Print), Kades (ACC), Admin (Maybe?)
        // Request says: "Staf Pelayanan: READ & UPDATE" and "Kades: UPDATE".
        // Let's hide from Warga. Admin maybe for debug? Let's say Admin, Staff, Kades.
        if ("admin".equals(role) || "kades".equals(role) || "staff".equals(role)) {
            contentPanel.add(new LetterVerificationPanel(user), "Verifikasi Surat");
        }

        if ("admin".equals(role)) {
            contentPanel.add(new UserManagementPanel(), "Manajemen User");
        }

        // Menu Buttons - Using Safe Unicode Characters to prevent "Square Boxes"
        addMenuButton(sidebar, "Beranda", "\u2302", "Beranda"); // HOUSE

        if ("admin".equals(role) || "warga".equals(role) || "staff".equals(role)) {
            addMenuButton(sidebar, "Permohonan Surat", "\u270D", "Permohonan Surat"); // WRITING HAND
        }

        if ("admin".equals(role) || "kades".equals(role) || "staff".equals(role)) {
            addMenuButton(sidebar, "Verifikasi Surat", "\u2714", "Verifikasi Surat"); // CHECK MARK
        }

        if ("admin".equals(role)) {
            addMenuButton(sidebar, "Manajemen User", "\u265F", "Manajemen User"); // PAWN (User-like)
        }

        // --- ADDED PRINT BUTTON ---
        if ("admin".equals(role) || "kades".equals(role) || "staff".equals(role)) {
            JButton printBtn = createMenuButton("Cetak Laporan", "\uD83D\uDDA8"); // PRINTER
            printBtn.addActionListener(e -> main.util.ReportManager.printLaporanSurat());
            sidebar.add(printBtn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        sidebar.add(Box.createVerticalGlue()); // Push Logout to bottom

        JButton logoutBtn = createMenuButton("Keluar", "\u2716"); // HEAVY X
        logoutBtn.setBackground(new Color(192, 57, 43)); // Red for logout
        logoutBtn.addActionListener(e -> {
            new LoginView().setVisible(true);
            this.dispose();
        });
        sidebar.add(logoutBtn);

        add(sidebar, BorderLayout.WEST);

        // --- Header (Top Bar) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(1030, 70));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(10, 30, 10, 30)));

        // LEFT: Current Date
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                .ofPattern("EEEE, dd MMMM yyyy", java.util.Locale.forLanguageTag("id-ID"));
        JLabel dateLabel = new JLabel(java.time.LocalDate.now().format(formatter));
        dateLabel.setFont(AppTheme.FONT_BODY);
        dateLabel.setForeground(AppTheme.COLOR_TEXT_GRAY);
        headerPanel.add(dateLabel, BorderLayout.WEST);

        // RIGHT: User Profile Section
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setBackground(Color.WHITE);

        // Dynamic User Text
        String roleDisplay = user.getRole().substring(0, 1).toUpperCase() + user.getRole().substring(1);
        JLabel userText = new JLabel("<html><div style='text-align: right;'>" +
                "<span style='color: #7f8c8d; font-size: 11px;'>Selamat Datang,</span><br>" +
                "<span style='color: #2c3e50; font-weight: bold; font-size: 14px;'>" + user.getUsername() + " ("
                + roleDisplay + ")</span>" +
                "</div></html>");

        // Avatar Icon Based on Role
        String avatarUnicode = "\uD83D\uDC64"; // Default Bust
        if ("kades".equals(role)) {
            avatarUnicode = "\uD83D\uDC54"; // Necktie for Kades
        } else if ("staff".equals(role)) {
            avatarUnicode = "\uD83D\uDCBC"; // Briefcase for Staff
        }

        JLabel avatarIcon = new JLabel(avatarUnicode);
        avatarIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        avatarIcon.setForeground(AppTheme.COLOR_PRIMARY);
        avatarIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        userPanel.add(userText);
        userPanel.add(avatarIcon);

        headerPanel.add(userPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void addMenuButton(JPanel panel, String text, String icon, String targetCard) {
        JButton btn = createMenuButton(text, icon);
        btn.addActionListener(e -> cardLayout.show(contentPanel, targetCard));
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private JButton createMenuButton(String text, String icon) {
        // Use HTML Table with NOBR to absolutely prevent wrapping
        // Increased font size for Icon column
        String html = "<html><table cellpadding='0' cellspacing='0' width='100%'><tr>" +
                "<td width='35' align='center'><span style='font-size: 20px'>" // Font fallback automatic
                + icon + "</span></td>" +
                "<td style='padding-left: 5px'><nobr>" + text + "</nobr></td>" +
                "</tr></table></html>";

        JButton btn = new JButton(html);
        btn.setFont(AppTheme.FONT_BODY);
        btn.setForeground(AppTheme.COLOR_TEXT_DARK);
        btn.setBackground(Color.WHITE);
        btn.setBorder(new EmptyBorder(8, 10, 8, 10)); // Reduced padding slightly to give more space
        btn.setMaximumSize(new Dimension(245, 50)); // INCREASED WIDTH to fit "Permohonan Surat"
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover Effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!btn.getText().contains("Keluar")) {
                    btn.setBackground(AppTheme.COLOR_ACCENT);
                    btn.setForeground(AppTheme.COLOR_WHITE);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.getText().contains("Keluar")) {
                    btn.setBackground(Color.WHITE);
                    btn.setForeground(AppTheme.COLOR_TEXT_DARK);
                }
            }
        });

        return btn;
    }
}
