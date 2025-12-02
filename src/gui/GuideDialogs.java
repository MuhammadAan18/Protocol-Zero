package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;

public class GuideDialogs {

    public static void showCyberpunk(Main parent) {
        JDialog dialog = new JDialog(parent, "Protocol Zero Guide", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Color bg = Theme.BACKGROUND;
        Color neon = Theme.NEON_BLUE;
        Color textCol = Theme.TEXT_COLOR;

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(bg);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("GUIDE MISSION", SwingConstants.CENTER);
        title.setFont(Theme.BUTTON_FONT);
        title.setForeground(neon);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));

        String guideText =
                "🎯 Tujuan Utama\n" +
                "Kamu adalah spesialis penjinak bom. Selesaikan semua modul sebelum waktu habis.\n\n" +

                "⏱ Waktu dan Nyawa\n" +
                "• Bomb memiliki waktu yang berbeda-beda, jika waktu habis maka bom akan meledak.\n" +
                "• Setiap jawaban salah yang kamu, akan mempercepat peluang meledak bom tersebut, dan mengurangi nyawa.\n" +
                "• Jika kamu membuat 3 kesalahan maka bom akan meledak.\n\n" +

                "🔢 Nomor Seri Bom\n" +
                "• Setiap game punya Serial Number acak (AB91, EO04, ZX73, dll).\n" +
                "• Serial bom mengubah aturan beberapa modul.\n\n" +

                "🔧 Modul Bom\n" +
                "Semua bom diatur memiliki 4 modul yaitu: \n" +
                "1. Wire Module : pilih kabel yang benar berdasarkan aturan yang tepat.\n" +
                "2. Button Module : tekan atau tahan tombol sesuai warna & kata.\n" +
                "3. Keypad Module : tekan tombol yang berisikan kata acak dalam urutan yang tepat.\n" +
                "4. Simon Module : ikuti instruksi dari lampu yang menyala, cocokan aturannya.\n\n" +

                "🎮 Cara Menjinakan Bom\n" +
                "• Cek Serial Number yang terdapat pada bom.\n" +
                "• Buka manual dan baca aturan yang sesuai.\n" +
                "• Cocokkan kondisi modul dengan tabel aturan yang terdapat pada manual.\n" +
                "• Ambil keputusan dengan cepat dan tepat.\n\n" +

                "🏆 Kondisi Bom Jinak\n" +
                "• Bom akan non-aktif ketika kamu berhasil menyelesaikan semua modul tersebut.\n\n" +

                "💥 Kondisi Bom Meledak\n" +
                "• Bom akan meledak jika kamu kehabisan waktu,\n" +
                "• atau 3 nyawa kamu sudah habis.\n\n" +

                "Tips: \n" + 
                    "Serial Number adalah kunci.\n" + 
                    "Jangan asal klik.\n" + 
                    "Teliti dalam membaca manual.";

        JTextArea textArea = new JTextArea(guideText);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setForeground(textCol);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(Theme.GUIDE_FONT);

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(new CompoundBorder(
                new LineBorder(neon),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // sembunyikan scrollbar 
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(Theme.TITLE_FONT);
        closeBtn.setForeground(neon);
        closeBtn.setBackground(bg.darker());
        closeBtn.setFocusPainted(false);
        closeBtn.setBorder(null);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dialog.dispose());

        int btnWidth = 220;
        int btnHeight = 40;
        closeBtn.setPreferredSize(new Dimension(btnWidth, btnHeight));
        closeBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        closeBtn.setMinimumSize(new Dimension(btnWidth, btnHeight));
        closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        closeBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                closeBtn.setBackground(Theme.NEON_BLUE);
                closeBtn.setForeground(Theme.COLOR_TITLE);
            }

            public void mouseExited(MouseEvent e) {
                closeBtn.setBackground(Theme.NEON_DARK);
                closeBtn.setForeground(new Color(0, 255, 255));
            }
        });

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(new LineBorder(Theme.NEON_BLUE));
        bottom.add(closeBtn);

        root.add(title, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.setBorder(new EmptyBorder(10, 0, 0, 0));
        root.add(bottom, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setSize(650, 520);
        dialog.setLocationRelativeTo(parent);
        dialog.setUndecorated(true);
        dialog.setVisible(true);
    }

    public static void showManual(Main parrent) {
        JDialog dialog = new JDialog(parrent, "Protocol Zero Manual", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Color bg = Theme.BACKGROUND;
        Color neon = Theme.NEON_BLUE;
        Color textCol = Theme.TEXT_COLOR;

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(bg);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("BOMB DEFUSAL MANUAL", SwingConstants.CENTER);
        title.setFont(Theme.BUTTON_FONT);
        title.setForeground(neon);
        title.setBorder(new EmptyBorder(0, 0, 10, 0));

        // panel isi manual (pakai BoxLayout + ScrollPane)
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        contentPanel.add(createBodyArea(
                "1. WIRE MODULE\n" +
                "• Modul ini hanya memiliki 1 kabel yang benar, berhati-hatilah!.\n" +
                "• Modul ini akan memeriksa Jumlah huruf vokal yang terdapat pada Nomor Serial\n" +
                "• Selain memeriksa huruf vokal, modul ini akan menjumlahkan setiap angka yang terdapat pada serial\n" +
                "• Jumlahkan total huruf vokal yang kamu temukan dengan jumlah setiap angka!\n" +
                "• Kurangi selisih antara total tersebut dengan banyak jumlah kabel!\n" + 
                "   - Jika total kurang dari atau sama dengan jumlah kabel, maka potonglah kabel sesuai total tersebut!\n" +
                "   - Jika total lebih dari jumlah kabel potonglah kabel sesuai hasil dari selisih tersebut!\n" + 
                "• Urutan kabel dimulai dari sebelah kiri! \n",
                textCol));
        contentPanel.add(Box.createVerticalStrut(10));

        contentPanel.add(createBodyArea(
            "2. BUTTON MODULE\n" +
            "• Modul ini memiliki karakteristik warna dan label yang berbeda-beda sesuai dengan Serial Bomnya.\n" +
            "• Terdapat 2 label Umum yang digunakan pada tombol :\n     -HOLD,\n     -PRESS.\n" +
            "• Gunakan tabel berikut sebagai penunjuk penyelesaian:\n",
            textCol));
            
            String[] btnColNames = {"COLOR", "HOLD", "PRESS"};
            String[][] btnData = {
                {"RED",    "TAP",             "HOLD 1 SECONDS"},
                {"YELLOW", "HOLD 4 SECONDS",  "HOLD 1 SECONDS"},
                {"GREEN",  "HOLD 4 SECONDS",  "TAP"}
            };
        contentPanel.add(createTablePanel(btnData, btnColNames, neon, textCol, bg));
        contentPanel.add(Box.createVerticalStrut(6));
        contentPanel.add(createBodyArea(
            "• Sesuaikan karakteristik tombol dengan tabel diatas!\n" +
            "• Jika aksinya berupa TAP maka bernilai 0,\n  jika aksi HOLD 1 SECONDS maka bernilai 1,\n" +
            "  dan HOLD 4 SECONDS akan bernilai 4.\n" +
            "• Pada modul ini cek digit terakhir Serial Number, jika digit terakhir bukan berbentuk angka\n" +
            "  maka simpan nilai 0.\n" +
            "• Jumlahkan nilai serial dengan nilai karakteristik tombol\n" +
            "• Hasil jumlah tersebut dimoduluskan dengan angka 3, lalu cek tabel berikut: \n\n",
            textCol));
            
            String[] modColNames = {"MODULUS % 3", "RESULT ACTION"};
            String[][] modData = {
                {"0", "TAP"},
                {"1", "HOLD 1 SECONDS"},
                {"2", "HOLD 4 SECONDS"}
            };

        contentPanel.add(createTablePanel(modData, modColNames, neon, textCol, bg));
        contentPanel.add(Box.createVerticalStrut(6));
        contentPanel.add(createBodyArea("• Gunakan hasil dari modulus untuk menentukan aksinya.\n",textCol));
        contentPanel.add(Box.createVerticalStrut(10));
                    
        contentPanel.add(createBodyArea(                   
            "3. KEYPAD MODULE\n" +
            "• Modul ini akan menampilkan 4 kata acak pada tombolnya.\n" +
            "• Di bawah ini terdapat 4 kolom yang berisikan kombinasi kata.\n" + 
            "• Cari SATU kolom yang memuat keempat kata yang muncul di modul berikut:\n", 
            textCol));
                    
        String[] colum = {"Kolom 1", "Kolom 2", "Kolom 3", "Kolom 4"};
        String[][] kata = {
                {"BOMB",        "ACCESS",   "HOLD",         "ERROR"},
                {"REBOOTING",   "SYSTEM",   "WIRES",        "DATA"},
                {"MISSION",     "ERROR",    "REBOOTING",    "ACCES"},
                {"ERROR",       "SIGNAL",   "DATA",         "MISSION"},
                {"WIRES",       "CUT",      "MISSION",      "CALM"},
                {"CUT",         "BOMB",     "ACCES",        "CUT"}};

        contentPanel.add(createTablePanel(kata, colum, neon, textCol, bg));
        contentPanel.add(createBodyArea("\n• Setelah Kamu menemukannya, tekan tombol sesuai urutan dari atas ke bawah pada kolom tersebut.\n\n", textCol));
        contentPanel.add(Box.createVerticalStrut(10));

        contentPanel.add(createBodyArea(
                "4. SIMON SAYS\n" +
                "• Modul ini membutuhkan ketelitian yang tajam!\n" +
                "• Modul ini akan memeriksa huruf vokal yang terdapat pada Nomor Serial dan sisa nyawa kamu!\n" +
                "• Modul akan memberikan Lampu yang berkedip dengan warna RED/BLUE/GREEN/YELLOW.\n" +
                "• Kamu harus menekan salah satu lampu untuk memulai.\n" +
                " (⚠️Jangan tekan lampu tersebut sebelum kamu membaca manual ini⚠️)\n" +
                "• Ingatlah warna yang berkedip, jangan sampai lupa.\n" +
                "• Gunakan dua tabel ini sebagai pedoman kamu :\n",
                textCol));
        contentPanel.add(Box.createVerticalStrut(6));

        contentPanel.add(createBodyArea("Jika Serial MENGANDUNG huruf vokal:", textCol));

        String[] simonHeader = {
                "Lampu nyala", "3 Nyawa", "2 Nyawa", "1 Nyawa"
        };
        String[][] simonVokalData = {
                {"RED",    "BLUE",   "YELLOW", "GREEN"},
                {"BLUE",   "RED",    "GREEN",  "BLUE"},
                {"GREEN",  "YELLOW", "BLUE",   "YELLOW"},
                {"YELLOW", "GREEN",  "RED",    "BLUE"}
        };
        contentPanel.add(createTablePanel(simonVokalData, simonHeader, neon, textCol, bg));
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(createBodyArea("Jika Serial TIDAK punya vokal:", textCol));

        String[][] simonNonVokalData = {
                {"RED",    "BLUE",   "RED",    "YELLOW"},
                {"BLUE",   "YELLOW", "BLUE",   "RED"},
                {"GREEN",  "GREEN",  "YELLOW", "BLUE"},
                {"YELLOW", "RED",    "GREEN",  "BLUE"}
        };
        contentPanel.add(createTablePanel(simonNonVokalData, simonHeader, neon, textCol, bg));
        contentPanel.add(Box.createVerticalStrut(8));

        contentPanel.add(createBodyArea(
                "• Pencet warna yang sesuai dengan kondsi lampu yang berkedip, huruf vokal pada serial, dan sisa nyawa.\n" +
                "(⚠️PERIKSALAH DENGAN TELITI⚠️)\n" +
                "(-Hesitation is defeat-)\n",
                textCol));

        // Scroll utama untuk keseluruhan manual
        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(new CompoundBorder(
                new LineBorder(neon),
                new EmptyBorder(10, 10, 10, 10)
        ));

        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(Theme.TITLE_FONT);
        closeBtn.setForeground(neon);
        closeBtn.setBackground(bg.darker());
        closeBtn.setFocusPainted(false);
        closeBtn.setBorder(null);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dialog.dispose());

        int btnWidth = 220;
        int btnHeight = 40;
        closeBtn.setPreferredSize(new Dimension(btnWidth, btnHeight));
        closeBtn.setMaximumSize(new Dimension(btnWidth, btnHeight));
        closeBtn.setMinimumSize(new Dimension(btnWidth, btnHeight));
        closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        closeBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeBtn.setBackground(Theme.NEON_BLUE);
                closeBtn.setForeground(Theme.COLOR_TITLE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeBtn.setBackground(Theme.NEON_DARK);
                closeBtn.setForeground(new Color(0, 255, 255));
            }
        });

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(new LineBorder(Theme.NEON_BLUE));
        bottom.add(closeBtn);

        root.add(title, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.setBorder(new EmptyBorder(10, 0, 0, 0));
        root.add(bottom, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setSize(720, 540);
        dialog.setLocationRelativeTo(parrent);
        dialog.setUndecorated(true);
        dialog.setVisible(true);
    }

    private static JTextArea createBodyArea(String text, Color textCol) {
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setOpaque(false);
        area.setForeground(textCol);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(Theme.TITLE_FONT);
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        return area;
    }

    private static JPanel createTablePanel(String[][] data,
                                           String[] columns,
                                           Color neon,
                                           Color textCol,
                                           Color bg) {
        JTable table = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setFocusable(false);

        table.setBackground(bg.darker());
        table.setForeground(textCol);
        table.setGridColor(neon);
        table.setRowHeight(22);

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setBackground(bg);
        header.setForeground(neon);
        header.setFont(Theme.GUIDE_FONT);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JScrollPane sp = new JScrollPane(table);
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        sp.setBorder(new LineBorder(neon));
        sp.getViewport().setBackground(bg.darker());
        sp.setPreferredSize(new Dimension(520, table.getRowHeight() * (data.length + 2)));

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(sp);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(new EmptyBorder(2, 0, 2, 0));
        return panel;
    }
}