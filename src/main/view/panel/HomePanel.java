package main.view.panel;

import main.util.AppTheme;
import main.util.UiUtil;
import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    private JPanel cardsContainer;

    public HomePanel() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.COLOR_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title / Welcome Banner
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppTheme.COLOR_BACKGROUND);

        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setBackground(AppTheme.COLOR_BACKGROUND);

        JLabel titleLabel = new JLabel("Beranda");
        titleLabel.setFont(AppTheme.FONT_HEADER_1);
        titleLabel.setForeground(AppTheme.COLOR_TEXT_DARK);

        JLabel subtitleLabel = new JLabel("Ringkasan aktivitas pelayanan surat desa hari ini.");
        subtitleLabel.setFont(AppTheme.FONT_BODY);
        subtitleLabel.setForeground(AppTheme.COLOR_TEXT_GRAY);

        titleBox.add(titleLabel);
        titleBox.add(subtitleLabel);

        JButton refreshBtn = UiUtil.createFreshButton("Refresh Data", AppTheme.COLOR_PRIMARY);
        refreshBtn.addActionListener(e -> loadStats());

        headerPanel.add(titleBox, BorderLayout.WEST);
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Content - Summary Cards
        cardsContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
        cardsContainer.setBackground(AppTheme.COLOR_BACKGROUND);

        add(cardsContainer, BorderLayout.CENTER);

        loadStats();
    }

    private void loadStats() {
        cardsContainer.removeAll();

        // Fetch Stats
        java.util.Map<String, Integer> stats = main.controller.SuratController.getStats();

        cardsContainer.add(UiUtil.createCard("Total Permohonan", String.valueOf(stats.getOrDefault("Total", 0)),
                AppTheme.COLOR_ACCENT));
        cardsContainer.add(UiUtil.createCard("Menunggu Verifikasi", String.valueOf(stats.getOrDefault("Menunggu", 0)),
                AppTheme.COLOR_WARNING));
        cardsContainer.add(UiUtil.createCard("Selesai Dicetak", String.valueOf(stats.getOrDefault("Selesai", 0)),
                AppTheme.COLOR_SUCCESS));

        cardsContainer.revalidate();
        cardsContainer.repaint();
    }
}
