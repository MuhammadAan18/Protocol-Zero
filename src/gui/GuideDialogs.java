package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
public class GuideDialogs {

    public static void showCyberpunk(Main parent) {
        JDialog dialog = new JDialog(parent, "Protocol Zero – Panduan", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Color bg      = Theme.BACKGROUND;
        Color neon    = Theme.NEON_BLUE;
        Color textCol = Theme.TEXT_COLOR;
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(bg);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));
        JLabel title = new JLabel("PANDUAN PERMAINAN", SwingConstants.CENTER);
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
                    • Setiap game punya <i>Serial Number</i> acak (A-91, E-04, Z-73, dll).<br/>
                    • Serial Number mengubah aturan beberapa modul.<br/><br/>

                    <b>🔧 Modul Bom</b><br/>
                    1. <u>Wire Module</u>: pilih kabel yang benar berdasarkan warna & aturan.<br/>
                    2. <u>Keypad Module</u>: tekan simbol dalam urutan yang tepat.<br/>
                    3. <u>Button Module</u>: tekan / tahan tombol sesuai warna & teks.<br/>
                    4. <u>Password Module</u>: susun huruf jadi kata sandi yang benar.<br/><br/>

                    <b>🎮 Cara Main</b><br/>
                    • Cek Serial Number di casing bom.<br/>
                    • Buka manual dan baca aturan yang sesuai.<br/>
                    • Cocokkan kondisi modul dengan tabel aturan.<br/>
                    • Ambil keputusan dengan cepat dan tepat.<br/><br/>

                    <b>🏆 Menang</b><br/>
                    • Semua modul berstatus <i>SOLVED</i> sebelum waktu habis & Strike &lt; 3.<br/><br/>

                    <b>💥 Kalah</b><br/>
                    • Timer habis, atau Strike mencapai 3.<br/><br/>

                    <i>Tip: Serial Number adalah kunci. Jangan asal klik.</i>
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
}
