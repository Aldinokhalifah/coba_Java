import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;

public class AplikasiSampahSwing extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextArea laporanArea;
    private ArrayList<String> laporanList;
    private int totalBerat = 0;
    private JLabel lblBankInfo;
    private JTextField txtBerat;
    
    public AplikasiSampahSwing() {
        setTitle("Aplikasi Pengelolaan Sampah");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        
        laporanList = new ArrayList<>();
        
        // Panel Menu - Tombol Navigasi di bagian atas
        JPanel menuPanel = new JPanel();
        JButton btnEdukasi = new JButton("Edukasi");
        JButton btnLapor = new JButton("Laporkan Sampah");
        JButton btnBank = new JButton("Bank Sampah");
        menuPanel.add(btnEdukasi);
        menuPanel.add(btnLapor);
        menuPanel.add(btnBank);
        add(menuPanel, BorderLayout.NORTH);
        
        // Panel utama dengan CardLayout untuk menampung 3 panel konten
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // --- Panel EDUKASI ---
        JPanel panelEdukasi = new JPanel();
        panelEdukasi.setLayout(new BoxLayout(panelEdukasi, BoxLayout.Y_AXIS));
        panelEdukasi.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel lblEdukasiTitle = new JLabel("Edukasi Pengelolaan Sampah");
        JLabel lblTip1 = new JLabel("- Pisahkan sampah organik dan anorganik");
        JLabel lblTip2 = new JLabel("- Kurangi penggunaan plastik sekali pakai");
        JLabel lblTip3 = new JLabel("- Daur ulang barang bekas");
        panelEdukasi.add(lblEdukasiTitle);
        panelEdukasi.add(Box.createVerticalStrut(10));
        panelEdukasi.add(lblTip1);
        panelEdukasi.add(lblTip2);
        panelEdukasi.add(lblTip3);
        
        // --- Panel LAPORAN SAMPAH ---
        JPanel panelLapor = new JPanel(new BorderLayout());
        laporanArea = new JTextArea();
        laporanArea.setEditable(false);
        laporanArea.setLineWrap(true);
        laporanArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(laporanArea);
        JButton btnTambahLaporan = new JButton("Tambah Laporan");
        panelLapor.add(scrollPane, BorderLayout.CENTER);
        panelLapor.add(btnTambahLaporan, BorderLayout.SOUTH);
        
        // --- Panel BANK SAMPAH ---
        JPanel panelBank = new JPanel();
        panelBank.setLayout(new BoxLayout(panelBank, BoxLayout.Y_AXIS));
        panelBank.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel lblBankTitle = new JLabel("Bank Sampah");
        txtBerat = new JTextField();
        txtBerat.setMaximumSize(new Dimension(200, 25));
        txtBerat.setToolTipText("Masukkan berat sampah (KG)");
        JButton btnSetor = new JButton("Setor Sampah");
        lblBankInfo = new JLabel("Total berat: 0 KG | Saldo: Rp0");
        panelBank.add(lblBankTitle);
        panelBank.add(Box.createVerticalStrut(10));
        panelBank.add(new JLabel("Masukkan berat sampah (KG):"));
        panelBank.add(txtBerat);
        panelBank.add(Box.createVerticalStrut(10));
        panelBank.add(btnSetor);
        panelBank.add(Box.createVerticalStrut(10));
        panelBank.add(lblBankInfo);
        
        // Tambahkan semua panel ke mainPanel dengan identifier-nya
        mainPanel.add(panelEdukasi, "edukasi");
        mainPanel.add(panelLapor, "lapor");
        mainPanel.add(panelBank, "bank");
        add(mainPanel, BorderLayout.CENTER);
        
        // Set panel default yang tampil
        cardLayout.show(mainPanel, "edukasi");
        
        // --- Event Handler Tombol Menu ---
        btnEdukasi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "edukasi");
            }
        });
        btnLapor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "lapor");
            }
        });
        btnBank.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "bank");
            }
        });
        
        // --- Event Handler untuk tambah laporan ---
        btnTambahLaporan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String laporan = JOptionPane.showInputDialog(AplikasiSampahSwing.this, "Masukkan laporan sampah:");
                if (laporan != null && !laporan.trim().isEmpty()) {
                    laporanList.add(laporan);
                    laporanArea.append("- " + laporan + "\n");
                }
            }
        });
        
        // --- Event Handler untuk setor sampah ---
        btnSetor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String beratStr = txtBerat.getText();
                try {
                    int berat = Integer.parseInt(beratStr);
                    totalBerat += berat;
                    lblBankInfo.setText("Total berat: " + totalBerat + " KG | Saldo: Rp" + (totalBerat * 500));
                    txtBerat.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(AplikasiSampahSwing.this, "Masukkan angka yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AplikasiSampahSwing().setVisible(true);
            }
        });
    }
}
