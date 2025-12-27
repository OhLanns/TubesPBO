package view;

import src.model.Pesanan;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PesananDialog extends JDialog {
    private Pesanan pesanan;
    private boolean confirmed = false;
    
    private JTextField txtProduk, txtPemesan, txtTelepon, txtEmail, txtTotal;
    private JTextArea txtKeterangan;
    private JComboBox<String> comboStatus;
    
    public PesananDialog(Frame parent, Pesanan existingPesanan) {
        super(parent, existingPesanan == null ? "Tambah Pesanan Baru" : "Edit Pesanan", true);
        this.pesanan = existingPesanan != null ? existingPesanan : new Pesanan();
        initComponents();
        setupDialog();
    }
    
    private void initComponents() {
        // Use GridBagLayout instead of MigLayout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        String title = pesanan.getId() == null ? "Tambah Pesanan Baru" : "Edit Pesanan #" + pesanan.getId();
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, gbc);
        
        // Form fields
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(new JLabel("Nama Produk*:"), gbc);
        
        gbc.gridx = 1;
        txtProduk = new JTextField(20);
        add(txtProduk, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("Nama Pemesan*:"), gbc);
        
        gbc.gridx = 1;
        txtPemesan = new JTextField(20);
        add(txtPemesan, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("Telepon:"), gbc);
        
        gbc.gridx = 1;
        txtTelepon = new JTextField(15);
        add(txtTelepon, gbc);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        txtEmail = new JTextField(20);
        add(txtEmail, gbc);
        
        gbc.gridy = 5;
        gbc.gridx = 0;
        add(new JLabel("Total Harga*:"), gbc);
        
        gbc.gridx = 1;
        txtTotal = new JTextField(15);
        add(txtTotal, gbc);
        
        gbc.gridy = 6;
        gbc.gridx = 0;
        add(new JLabel("Status:"), gbc);
        
        gbc.gridx = 1;
        String[] statusOptions = {"PROSES", "SUKSES", "BATAL", "MENUNGGU"};
        comboStatus = new JComboBox<>(statusOptions);
        add(comboStatus, gbc);
        
        gbc.gridy = 7;
        gbc.gridx = 0;
        add(new JLabel("Keterangan:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        txtKeterangan = new JTextArea(4, 20);
        JScrollPane scrollPane = new JScrollPane(txtKeterangan);
        add(scrollPane, gbc);
        
        // Buttons
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton btnSimpan = createButton("💾 SIMPAN", new Color(46, 204, 113));
        JButton btnBatal = createButton("BATAL", new Color(231, 76, 60));
        
        btnSimpan.addActionListener(e -> simpanPesanan());
        btnBatal.addActionListener(e -> dispose());
        
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);
        add(buttonPanel, gbc);
        
        // Load data if editing
        if (pesanan.getId() != null) {
            loadData();
        } else {
            comboStatus.setSelectedItem("PROSES");
        }
    }
    
    // TAMBAHKAN METHOD setupDialog() YANG HILANG
    private void setupDialog() {
        pack();
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setMinimumSize(new Dimension(500, 600));
    }
    
    // TAMBAHKAN METHOD simpanPesanan() YANG HILANG
    private void simpanPesanan() {
        // Validasi input
        if (!validateInput()) {
            return;
        }
        
        try {
            // Ambil data dari form
            String produk = txtProduk.getText().trim();
            String pemesan = txtPemesan.getText().trim();
            String telepon = txtTelepon.getText().trim();
            String email = txtEmail.getText().trim();
            BigDecimal total = new BigDecimal(txtTotal.getText().trim().replace(",", "."));
            String status = (String) comboStatus.getSelectedItem();
            String keterangan = txtKeterangan.getText().trim();
            
            // Set data ke objek pesanan
            pesanan.setProduk(produk);
            pesanan.setPemesan(pemesan);
            pesanan.setTelepon(telepon);
            pesanan.setEmail(email);
            pesanan.setTotal(total);
            pesanan.setStatus(status);
            pesanan.setKeterangan(keterangan);
            
            // Jika pesanan baru, set tanggal
            if (pesanan.getId() == null) {
                pesanan.setTanggalPesan(LocalDateTime.now());
            } else {
                pesanan.setTanggalUpdate(LocalDateTime.now());
            }
            
            // Tandai sebagai confirmed
            confirmed = true;
            
            JOptionPane.showMessageDialog(this,
                "Pesanan berhasil disimpan!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Format total harga tidak valid! Gunakan angka (contoh: 150000 atau 150000.00)",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Terjadi kesalahan: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // TAMBAHKAN METHOD loadData() YANG HILANG
    private void loadData() {
        if (pesanan == null) {
            return;
        }
        
        txtProduk.setText(pesanan.getProduk() != null ? pesanan.getProduk() : "");
        txtPemesan.setText(pesanan.getPemesan() != null ? pesanan.getPemesan() : "");
        txtTelepon.setText(pesanan.getTelepon() != null ? pesanan.getTelepon() : "");
        txtEmail.setText(pesanan.getEmail() != null ? pesanan.getEmail() : "");
        txtTotal.setText(pesanan.getTotal() != null ? pesanan.getTotal().toString() : "0");
        txtKeterangan.setText(pesanan.getKeterangan() != null ? pesanan.getKeterangan() : "");
        
        // Set status
        if (pesanan.getStatus() != null) {
            comboStatus.setSelectedItem(pesanan.getStatus());
        }
    }
    
    // Method untuk validasi input
    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();
        
        if (txtProduk.getText().trim().isEmpty()) {
            errors.append("• Nama Produk harus diisi\n");
        }
        
        if (txtPemesan.getText().trim().isEmpty()) {
            errors.append("• Nama Pemesan harus diisi\n");
        }
        
        if (txtTotal.getText().trim().isEmpty()) {
            errors.append("• Total Harga harus diisi\n");
        } else {
            try {
                BigDecimal total = new BigDecimal(txtTotal.getText().trim().replace(",", "."));
                if (total.compareTo(BigDecimal.ZERO) <= 0) {
                    errors.append("• Total Harga harus lebih dari 0\n");
                }
            } catch (NumberFormatException e) {
                errors.append("• Format Total Harga tidak valid\n");
            }
        }
        
        // Validasi email jika diisi
        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !email.contains("@")) {
            errors.append("• Format email tidak valid\n");
        }
        
        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this,
                "Harap perbaiki kesalahan berikut:\n\n" + errors.toString(),
                "Validasi Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 40));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    // Getter untuk mendapatkan pesanan yang telah disimpan
    public Pesanan getPesanan() {
        return confirmed ? pesanan : null;
    }
    
    // Getter untuk status confirmed
    public boolean isConfirmed() {
        return confirmed;
    }
    
    // Method untuk membuka dialog dan mendapatkan hasil
    public static Pesanan showDialog(Frame parent, Pesanan existingPesanan) {
        PesananDialog dialog = new PesananDialog(parent, existingPesanan);
        dialog.setVisible(true);
        return dialog.getPesanan();
    }
}