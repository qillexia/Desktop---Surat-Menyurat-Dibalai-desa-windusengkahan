package main.view;

import main.util.AppTheme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import main.controller.AuthController;
import main.view.DashboardView;

public class LoginView extends JFrame {

    public LoginView() {
        setTitle("Sistem Pelayanan Surat Desa Windusengkahan");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window
        setLayout(new BorderLayout());

        // Left Panel (Branding)
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 1. Gradient Background (Primary -> Accent)
                GradientPaint gp = new GradientPaint(0, 0, AppTheme.COLOR_PRIMARY,
                        getWidth(), getHeight(), AppTheme.COLOR_ACCENT);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // 2. Decorative 2D Shapes (Semi-transparent)
                g2d.setColor(new Color(255, 255, 255, 20)); // Very subtle white
                g2d.fillOval(-50, -50, 250, 250); // Top-left circle

                g2d.setColor(new Color(255, 255, 255, 15));
                g2d.fillOval(getWidth() - 150, getHeight() / 2 - 50, 400, 400); // Right side big circle

                g2d.setColor(new Color(255, 255, 255, 10));
                g2d.fillOval(40, getHeight() - 100, 100, 100); // Bottom small circle
            }
        };
        // leftPanel.setBackground(AppTheme.COLOR_PRIMARY); // Not strictly needed as we
        // paint over, but kept implicitly
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(400, 600));

        JLabel titleLabel = new JLabel(
                "<html><center>Sistem Digitalisasi<br>Pelayanan Surat<br>Desa Windusengkahan</center></html>");
        titleLabel.setFont(AppTheme.FONT_HEADER_1);
        titleLabel.setForeground(AppTheme.COLOR_WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add an icon placeholder or simple graphic
        JLabel iconLabel = new JLabel("🏛️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        iconLabel.setForeground(AppTheme.COLOR_WHITE);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        leftPanel.add(iconLabel, gbc);
        gbc.gridy = 1;
        leftPanel.add(titleLabel, gbc);

        // Right Panel (Login Form)
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(AppTheme.COLOR_BACKGROUND);
        rightPanel.setLayout(new GridBagLayout());

        JPanel loginBox = new JPanel();
        loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
        loginBox.setBackground(AppTheme.COLOR_WHITE);
        loginBox.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Shadow effect simulation (Border)
        loginBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(30, 30, 30, 30)));

        JLabel loginTitle = new JLabel("Login Pengguna");
        loginTitle.setFont(AppTheme.FONT_HEADER_2);
        loginTitle.setForeground(AppTheme.COLOR_TEXT_DARK);
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("Masuk untuk melanjutkan");
        descLabel.setFont(AppTheme.FONT_SMALL);
        descLabel.setForeground(AppTheme.COLOR_TEXT_GRAY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, AppTheme.COLOR_ACCENT),
                new EmptyBorder(5, 5, 5, 5)));

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, AppTheme.COLOR_ACCENT),
                new EmptyBorder(5, 5, 5, 5)));

        JButton loginButton = new JButton("MASUK");
        loginButton.setFont(AppTheme.FONT_BODY_BOLD);
        loginButton.setForeground(AppTheme.COLOR_WHITE);
        loginButton.setBackground(AppTheme.COLOR_PRIMARY);
        loginButton.setBorder(new EmptyBorder(10, 0, 10, 0));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setUI(new javax.swing.plaf.basic.BasicButtonUI()); // Fix for Windows L&F color
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(AppTheme.COLOR_PRIMARY.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(AppTheme.COLOR_PRIMARY);
            }
        });

        loginButton.addActionListener((ActionEvent e) -> {
            String usernameInput = usernameField.getText();
            String passwordInput = new String(passwordField.getPassword());

            main.model.User loggedUser = AuthController.login(usernameInput, passwordInput);

            if (loggedUser != null) {
                JOptionPane.showMessageDialog(this, "Login Berhasil sebagai " + loggedUser.getRole() + "!");
                new DashboardView(loggedUser).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!", "Login Gagal",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        loginBox.add(loginTitle);
        loginBox.add(Box.createRigidArea(new Dimension(0, 5)));
        loginBox.add(descLabel);
        loginBox.add(Box.createRigidArea(new Dimension(0, 30)));
        JLabel userLabel = new JLabel("Username");
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBox.add(userLabel);

        loginBox.add(usernameField);
        loginBox.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel passLabel = new JLabel("Password");
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBox.add(passLabel);

        loginBox.add(passwordField);
        loginBox.add(Box.createRigidArea(new Dimension(0, 30)));
        loginBox.add(loginButton);

        rightPanel.add(loginBox);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }
}
