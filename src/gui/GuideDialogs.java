package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
public class GuideDialogs {

    public static void showCyberpunk(Main parent) {
        JDialog dialog = new JDialog(parent, "Protocol Zero – Panduan", true);
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

        String html = """
            <html>
                <body style='background-color:rgb(5,10,20); color:rgb(220,240,255); font-family:Sans-Serif; font-size:12px;'>
                    <b>🎯 Tujuan Utama</b><br/>
                    Kamu adalah <b>EOD Specialist</b>. Selesaikan semua modul sebelum waktu habis.<br/><br/>

                    <b>⏱ Waktu & Strike</b><br/>
                    • Timer mundur: waktu habis = bom meledak.<br/>
                    • Setiap jawaban salah = <b>1 Strike</b>.<br/>
                    • <b>3 Strike</b> = bom meledak.<br/><br/>

                    <b>🔢 Nomor Seri Bom</b><br/>
                    • Setiap game punya <i>Serial Number</i> acak (AB91, EO04, ZX73, dll).<br/>
                    • Serial Number mengubah aturan beberapa modul.<br/><br/>

                    <b>🔧 Modul Bom</b><br/>
                    1. <u>Wire Module</u>: pilih kabel yang benar berdasarkan warna & aturan.<br/>
                    2. <u>Keypad Module</u>: tekan tombol yang berisikan kata acak dalam urutan yang tepat.<br/>
                    3. <u>Button Module</u>: tekan atau tahan tombol sesuai warna & dan kata.<br/>
                    4. <u>Simon Module</u>: ikuti instruksi dari lampu yang menyala.<br/><br/>

                    <b>🎮 Cara Main</b><br/>
                    • Cek Serial Number di casing bom.<br/>
                    • Buka manual dan baca aturan yang sesuai.<br/>
                    • Cocokkan kondisi modul dengan tabel aturan.<br/>
                    • Ambil keputusan dengan cepat dan tepat.<br/><br/>

                    <b>🏆 Menang</b><br/>
                    • Semua modul berstatus <i>SOLVED</i> sebelum waktu habis & Strike &lt; 3.<br/><br/>

                    <b>💥 Kalah</b><br/>
                    • Timer habis, atau Strike mencapai 3.<br/><br/>

                    <i>Tips: Serial Number adalah kunci. Jangan asal klik.</i>
                </body>
            </html>
        """;

        JEditorPane editor = new JEditorPane("text/html", html);
        editor.setEditable(false);
        editor.setOpaque(false);
        editor.setForeground(textCol);

        JScrollPane scroll = new JScrollPane(editor);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(new CompoundBorder(
            new LineBorder(neon),
            new EmptyBorder(10, 10, 10, 10)
        ));

        //hide scrollbarnya
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
                closeBtn.setForeground(new Color(0,255,255));
            }
        });

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(new LineBorder(Theme.NEON_BLUE));
        bottom.add(closeBtn);

        root.add(title, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.setBorder(new EmptyBorder(10,0,0,0));
        root.add(bottom, BorderLayout.SOUTH);

        dialog.setContentPane(root);
        dialog.setSize(650, 520);
        dialog.setLocationRelativeTo(parent);
        dialog.setUndecorated(true); // menghilangkan header dialog
        dialog.setVisible(true);
    }

    public static void showManual(Main parrent) {
        JDialog dialog = new JDialog(parrent, "Protocol Zero – Manual", true);
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

        String html = """
            <html>
              <body style='background-color:rgb(5,10,20); color:rgb(220,240,255); font-family:Sans-Serif; font-size:11px;'>
                <b>📚 PROTOCOL ZERO – MANUAL SINGKAT</b><br/>
                <i>Gunakan manual ini untuk membantu menyelesaikan tiap modul.</i><br/><br/>

                <b>🛜 1. WIRE MODULE – Aturan Serial Sum</b><br/>
                Tujuan: hanya <b>1 kabel</b> yang benar.<br/>
                Langkah:<br/>
                • Hitung jumlah kabel (N).<br/>
                • Lihat <i>Serial Number</i> di casing bom.<br/>
                • Jumlahkan semua digit angka di serial.<br/>
                • Hitung berapa banyak huruf vokal (A, I, U, E, O) pada serial.<br/>
                • Total = (jumlah digit) + (jumlah vokal).<br/>
                • Hitung sisa: <code>sisa = Total % N</code>.<br/>
                • Kabel yang harus dipotong = kabel ke-(sisa + 1) dari atas.<br/>
                Contoh: Total = 17, N = 5 → 17 % 5 = 2 → potong kabel ke-3.<br/><br/>

                <b>⌨ 2. KEYPAD MODULE – Kolom Kata</b><br/>
                • Modul menampilkan 4 kata acak (BOMB, ACCESS, SYSTEM, dst).<br/>
                • Di bawah ini ada 4 kolom kata. Cari SATU kolom yang memuat keempat kata yang muncul di modul.<br/>
                • Setelah ketemu, tekan tombol sesuai urutan dari atas ke bawah pada kolom tersebut.<br/><br/>

                <u>Kolom 1</u>: BOMB → REBOOTING → MISSION → ERROR → WIRES → CUT<br/>
                <u>Kolom 2</u>: ACCESS → SYSTEM → ERROR → SIGNAL → CUT → BOMB<br/>
                <u>Kolom 3</u>: HOLD → WIRES → REBOOTING → DATA → MISSION → ACCESS<br/>
                <u>Kolom 4</u>: ERROR → DATA → ACCESS → MISSION → CALM → CUT<br/><br/>

                <b>🔴 3. BUTTON MODULE – Tekan atau Tahan?</b><br/>
                • Setiap tombol punya <i>tipe dasar</i>: TAP (tekan-lepas) atau HOLD (tahan).<br/>
                • Lihat digit terakhir Serial Number, sebut D (jika bukan angka → 0).<br/>
                • Hitung <code>d = D % 3</code>.<br/>
                • Gunakan tabel berikut:<br/><br/>

                <table border='1' cellpadding='3' cellspacing='0'>
                  <tr><th>d = COLOR</th><th>PRESS</th><th>HOLD</th></tr>
                  <tr><td align='center'>0</td><td>TAP (tekan &amp; lepas cepat)</td><td>HOLD &amp; lepas di ~1 detik</td></tr>
                  <tr><td align='center'>1</td><td>HOLD &amp; lepas di ~1 detik</td><td>HOLD &amp; lepas di ~4 detik</td></tr>
                  <tr><td align='center'>2</td><td>HOLD &amp; lepas di ~4 detik</td><td>TAP (tekan &amp; lepas cepat)</td></tr>
                </table>
                <br/>
                Salah memilih aksi → Strike.<br/><br/>

                <b>🎵 4. SIMON SAYS – Peta Warna</b><br/>
                • Lampu akan berkedip dengan warna RED/BLUE/GREEN/YELLOW.<br/>
                • Kamu harus menekan warna lain sesuai tabel.<br/>
                • Aturan tergantung: apakah Serial punya vokal (A, E, I, O, U) dan jumlah Strike (0, 1, atau 2+).<br/><br/>

                <u>Jika Serial MENGANDUNG vokal</u>:<br/>
                <table border='1' cellpadding='3' cellspacing='0'>
                  <tr><th>Lampu nyala</th><th>0 Strike → tekan</th><th>1 Strike → tekan</th><th>2+ Strike → tekan</th></tr>
                  <tr><td>RED</td><td>BLUE</td><td>YELLOW</td><td>GREEN</td></tr>
                  <tr><td>BLUE</td><td>RED</td><td>GREEN</td><td>BLUE</td></tr>
                  <tr><td>GREEN</td><td>YELLOW</td><td>BLUE</td><td>YELLOW</td></tr>
                  <tr><td>YELLOW</td><td>GREEN</td><td>RED</td><td>BLUE</td></tr>
                </table><br/>

                <u>Jika Serial TIDAK punya vokal</u>:<br/>
                <table border='1' cellpadding='3' cellspacing='0'>
                  <tr><th>Lampu nyala</th><th>0 Strike → tekan</th><th>1 Strike → tekan</th><th>2+ Strike → tekan</th></tr>
                  <tr><td>RED</td><td>BLUE</td><td>RED</td><td>YELLOW</td></tr>
                  <tr><td>BLUE</td><td>YELLOW</td><td>BLUE</td><td>RED</td></tr>
                  <tr><td>GREEN</td><td>GREEN</td><td>YELLOW</td><td>BLUE</td></tr>
                  <tr><td>YELLOW</td><td>RED</td><td>GREEN</td><td>BLUE</td></tr>
                </table><br/>

                Tekan urutan lengkap dari awal sampai akhir setiap kali sequence bertambah.<br/><br/>
                </body>
            </html>
            """;

        JEditorPane editor = new JEditorPane("text/html", html);
        editor.setEditable(false);
        editor.setOpaque(false);
        editor.setForeground(textCol);

        JScrollPane scroll = new JScrollPane(editor);
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
}