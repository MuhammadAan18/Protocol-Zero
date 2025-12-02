package gui;

import dao.ScoreDAO;
import model.ScoreEntry;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class LeaderBoardDialog {

    public static void show(Main parent) {
        // === SETUP DIALOG ===
        JDialog dialog = new JDialog(parent, "Protocol Zero Leaderboard", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Color bg   = Theme.BACKGROUND;
        Color neon = Theme.NEON_BLUE;
        Color text = Theme.TEXT_COLOR;

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(bg);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        JLabel title = new JLabel("LEADERBOARD", SwingConstants.CENTER);
        title.setForeground(neon);
        title.setFont(Theme.BUTTON_FONT);
        title.setBorder(new EmptyBorder(0, 0, 8, 0));
        root.add(title, BorderLayout.NORTH);

        // ambil current user
        User currentUser = parent.getCurrentUsername();
        int currentUserId = (currentUser != null) ? currentUser.getUserID() : -1;

        // load data leaderboard
        List<ScoreEntry> scores = ScoreDAO.getTopScores(8); //limit 8 data tertinggi

        String[] columns = { "Rank", "Name", "Bomb ID", "Strike", "Time", "Grade" };
        String[][] data = new String[scores.size()][columns.length];

        int rank = 1;
        for (int i = 0; i < scores.size(); i++) {
            ScoreEntry s = scores.get(i);
            data[i][0] = String.valueOf(rank++);
            data[i][1] = s.getUsername();
            data[i][2] = String.valueOf(s.getBombId());
            data[i][3] = String.valueOf(s.getStrikeLeft());
            data[i][4] = formatTime(s.getTimeLeft());
            data[i][5] = s.getGameScore();
        }

        JTable table = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only
            }
        };
        table.setRowHeight(24);
        table.setShowGrid(true);
        table.setGridColor(neon.darker());
        table.setForeground(text);
        table.setBackground(bg.darker());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setBackground(bg.darker());
        header.setForeground(neon);
        header.setFont(Theme.TITLE_FONT);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);

                // reset dasar
                c.setForeground(text);
                c.setBackground(bg.darker());
                setHorizontalAlignment(SwingConstants.CENTER);

                // ambil entry asli berdasarkan row
                if (row >= 0 && row < scores.size()) {
                    ScoreEntry entry = scores.get(row);

                    // 1) Highlight khusus jika ini baris pemain saat ini
                    boolean isCurrentPlayerRow = (entry.getUserId() == currentUserId);

                    // 2) Warna grade
                    String grade = entry.getGameScore();
                    if ("SS".equals(grade)) {
                        c.setForeground(new Color(255, 215, 0)); // emas
                    } else if ("A".equals(grade)) {
                        c.setForeground(Color.GREEN);
                    } else if ("B".equals(grade)) {
                        c.setForeground(Color.CYAN);
                    } else if ("C".equals(grade)) {
                        c.setForeground(Color.ORANGE);
                    } else if ("F".equals(grade)) {
                        c.setForeground(Color.RED);
                    }

                    // 3) Terapkan background spesial untuk current player
                    if (isCurrentPlayerRow) {
                        c.setBackground(new Color(150, 150, 180)); // sedikit lebih terang / kebiruan
                        c.setFont(getFont().deriveFont(Font.BOLD));
                        // optional: tambahkan border di kiri-kanan dengan neon
                        if (c instanceof JComponent jc) {
                            jc.setBorder(new LineBorder(neon));
                        }
                    } else {
                        c.setFont(getFont().deriveFont(Font.PLAIN));
                    }

                    // 4) Jika baris diseleksi dengan mouse, tetap kasih indikasi
                    if (isSelected) {
                        c.setBackground(bg.brighter());
                    }
                }
                return c;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new LineBorder(neon));
        sp.getViewport().setBackground(bg.darker());
        sp.setPreferredSize(new Dimension(520, Math.max(200, table.getRowHeight() * (data.length + 2))));
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(sp);
        centerPanel.setBorder(new EmptyBorder(4, 0, 4, 0));

        root.add(centerPanel, BorderLayout.CENTER);

        // === BOTTOM BUTTONS: PLAY AGAIN & HOME ===
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        bottom.setBorder(new EmptyBorder(10, 0, 0, 0));

        int btnWidth  = 180;
        int btnHeight = 40;

        JButton playAgainBtn = createNeonButton("PLAY AGAIN", neon);
        playAgainBtn.setPreferredSize(new Dimension(btnWidth, btnHeight));
        playAgainBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        playAgainBtn.setMinimumSize(new Dimension(btnWidth, btnHeight));
        playAgainBtn.addActionListener(e -> {
            dialog.dispose();
            parent.showGamePanel();
        });

        JButton homeBtn = createNeonButton("HOME", neon);
        homeBtn.setPreferredSize(new Dimension(btnWidth, btnHeight));
        homeBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        homeBtn.setMinimumSize(new Dimension(btnWidth, btnHeight));
        homeBtn.addActionListener(e -> {
            dialog.dispose();
            parent.showHome();
        });

        bottom.add(Box.createHorizontalGlue());
        bottom.add(playAgainBtn);
        bottom.add(Box.createHorizontalStrut(12));
        bottom.add(homeBtn);
        bottom.add(Box.createHorizontalGlue());

        root.add(bottom, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private static String formatTime(int seconds) {
        if (seconds < 0) seconds = 0;
        int m = seconds / 60;
        int s = seconds % 60;
        return String.format("%02d:%02d", m, s);
    }

    private static JButton createNeonButton(String text, Color neon) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.TITLE_FONT);
        btn.setForeground(neon);
        btn.setBackground(Theme.NEON_DARK);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(neon));
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(Theme.NEON_BLUE);
                btn.setForeground(Theme.COLOR_TITLE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Theme.NEON_DARK);
                btn.setForeground(neon);
            }
        });

        return btn;
    }
}
