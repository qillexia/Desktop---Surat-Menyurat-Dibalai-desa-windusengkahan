package main.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UiUtil {

    public static void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(AppTheme.FONT_BODY);
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(AppTheme.COLOR_TEXT_DARK);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(230, 230, 230));

        // Header Styling
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                l.setFont(AppTheme.FONT_BODY_BOLD);
                l.setBackground(AppTheme.COLOR_PRIMARY);
                l.setForeground(Color.WHITE);
                l.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                        new EmptyBorder(10, 10, 10, 10)));
                return l;
            }
        });
        header.setPreferredSize(new Dimension(header.getWidth(), 45));

        // Left padding for all cells (Default)
        DefaultTableCellRenderer leftPaddingRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return this;
            }
        };
        table.setDefaultRenderer(Object.class, leftPaddingRenderer);
    }

    // Custom Renderer for Status Columns
    public static DefaultTableCellRenderer getStatusColumnRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String status = (value != null) ? value.toString() : "";

                // Style logic based on status text
                if (status.contains("Selesai") || status.contains("Aktif") || status.contains("Disetujui")) {
                    setForeground(AppTheme.COLOR_SUCCESS);
                    setFont(AppTheme.FONT_BODY_BOLD);
                } else if (status.contains("Menunggu") || status.contains("Perlu")) {
                    setForeground(AppTheme.COLOR_WARNING);
                    setFont(AppTheme.FONT_BODY_BOLD);
                } else if (status.contains("Diproses")) {
                    setForeground(AppTheme.COLOR_ACCENT);
                    setFont(AppTheme.FONT_BODY_BOLD);
                } else if (status.contains("Tolak") || status.contains("Nonaktif")) {
                    setForeground(new Color(192, 57, 43)); // Red
                    setFont(AppTheme.FONT_BODY_BOLD);
                } else {
                    setForeground(isSelected ? AppTheme.COLOR_TEXT_DARK : AppTheme.COLOR_TEXT_DARK);
                    setFont(AppTheme.FONT_BODY);
                }

                setBorder(new EmptyBorder(0, 10, 0, 10));
                return this;
            }
        };
    }

    public static JPanel createCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(300, 140));
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());

        // Shadow/Border effect
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, accentColor), // Color strip on left
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                        new EmptyBorder(20, 25, 20, 25))));

        JLabel countLabel = new JLabel(value);
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        countLabel.setForeground(AppTheme.COLOR_TEXT_DARK);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.FONT_BODY);
        titleLabel.setForeground(AppTheme.COLOR_TEXT_GRAY);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(countLabel);
        textPanel.add(titleLabel);

        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    public static JButton createFreshButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(AppTheme.FONT_BODY_BOLD);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI()); // Essential for solid colors on Windows
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }
}
