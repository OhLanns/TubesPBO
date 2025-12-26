package src.view;

import src.controller.ProdukController;
import src.model.Produk;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProdukPanel extends JPanel {

    private ProdukController controller;
    private JTabbedPane categoryTabs;
    private JLabel lblTotalHarga;
    private BigDecimal totalHarga = BigDecimal.ZERO;

    // ✅ WARNA YANG SAMA DENGAN VERSI BAWAH
    private final Color BORDER_PAKET = new Color(0, 122, 255);     // Biru terang
    private final Color BORDER_SEWA = new Color(142, 68, 173);     // Ungu terang
    private final Color BORDER_WEEDING = new Color(230, 126, 34);  // Oranye terang

    public ProdukPanel() {
        controller = new ProdukController();
        setLayout(new BorderLayout());

        // 🔥 PENTING UNTUK SCROLL
        setPreferredSize(new Dimension(1100, 1400));

        initComponents();
    }

    private void initComponents() {

        categoryTabs = new JTabbedPane(JTabbedPane.TOP);
        categoryTabs.setFont(new Font("Arial", Font.BOLD, 14));

        categoryTabs.addTab("📦 PAKET FOTO", wrapScrollable(createCategoryPanel("PAKET")));
        categoryTabs.addTab("👗 SEWA PAKAIAN", wrapScrollable(createCategoryPanel("SEWA")));
        categoryTabs.addTab("💍 PAKET WEEDING", wrapScrollable(createCategoryPanel("WEEDING")));

        add(categoryTabs, BorderLayout.CENTER);

        // ===== BOTTOM TOTAL =====
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bottomPanel.setBackground(new Color(240, 240, 240));

        JLabel lblTotal = new JLabel("Total Belanja:");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setForeground(Color.BLACK); // ✅ Warna hitam

        lblTotalHarga = new JLabel("Rp 0");
        lblTotalHarga.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalHarga.setForeground(new Color(46, 204, 113));

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.setBackground(new Color(240, 240, 240));
        totalPanel.add(lblTotal);
        totalPanel.add(lblTotalHarga);

        bottomPanel.add(totalPanel, BorderLayout.WEST);

        JButton btnCheckout = new JButton("🚀 CHECKOUT");
        btnCheckout.setFont(new Font("Arial", Font.BOLD, 14));
        btnCheckout.setBackground(new Color(46, 204, 113));
        btnCheckout.setForeground(Color.WHITE);

        // 🔥 FIX BUTTON WINDOWS
        btnCheckout.setOpaque(true);
        btnCheckout.setContentAreaFilled(true);
        btnCheckout.setBorderPainted(false);
        btnCheckout.setFocusPainted(false);

        btnCheckout.setPreferredSize(new Dimension(160, 40));
        btnCheckout.addActionListener(e -> checkout());

        bottomPanel.add(btnCheckout, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        loadProducts();
    }

    // ================= SCROLL HELPER =================
    private JScrollPane wrapScrollable(JPanel panel) {
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(25);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        return scroll;
    }

    private JPanel createCategoryPanel(String category) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel(category + " FOTO", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(getCategoryColor(category));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(15));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        return panel;
    }

    private void loadProducts() {
        String[] categories = {"PAKET", "SEWA", "WEEDING"};

        for (String cat : categories) {
            List<Produk> list = controller.getProdukByJenis(cat);
            JPanel panel = getCategoryPanel(cat);

            for (Produk p : list) {
                panel.add(createProductCard(p));
                panel.add(Box.createVerticalStrut(15));
            }
            panel.add(Box.createVerticalGlue());
        }
    }

    private JPanel getCategoryPanel(String category) {
        JScrollPane scroll;
        switch (category) {
            case "PAKET":
                scroll = (JScrollPane) categoryTabs.getComponentAt(0);
                break;
            case "SEWA":
                scroll = (JScrollPane) categoryTabs.getComponentAt(1);
                break;
            case "WEEDING":
                scroll = (JScrollPane) categoryTabs.getComponentAt(2);
                break;
            default:
                scroll = (JScrollPane) categoryTabs.getComponentAt(0);
        }
        return (JPanel) scroll.getViewport().getView();
    }

    private JPanel createProductCard(Produk produk) {

        Color borderColor = getCategoryColor(produk.getJenis());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(420, 340));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ✅ BORDER DENGAN WARNA YANG SAMA DENGAN VERSI BAWAH
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2),
                BorderFactory.createLineBorder(new Color(240, 240, 240), 3)
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblNama = new JLabel(produk.getNama());
        lblNama.setFont(new Font("Arial", Font.BOLD, 17));
        lblNama.setForeground(borderColor.darker()); // ✅ Sedikit lebih gelap dari border
        lblNama.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblNama);
        card.add(Box.createVerticalStrut(5));
        
        // ✅ BOOKING INFO DENGAN WARNA ABU-ABU
        JLabel lblBooking = new JLabel("📅 Booking: yy-xx-zz");
        lblBooking.setFont(new Font("Arial", Font.PLAIN, 12));
        lblBooking.setForeground(new Color(100, 100, 100));
        lblBooking.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblBooking);
        
        card.add(Box.createVerticalStrut(5));

        NumberFormat rupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));
        JLabel lblHarga = new JLabel("💰 Price: " + rupiah.format(produk.getHarga()));
        lblHarga.setFont(new Font("Arial", Font.BOLD, 14));
        lblHarga.setForeground(new Color(231, 76, 60)); // ✅ Merah untuk harga
        lblHarga.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblHarga);
        card.add(Box.createVerticalStrut(15));

        // ✅ DESCRIPTION PANEL DENGAN BACKGROUND ABU-ABU
        JPanel descPanel = new JPanel();
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
        descPanel.setBackground(new Color(242, 242, 242));
        descPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        descPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descPanel.setMaximumSize(new Dimension(380, 100));
        
        String[] lines = produk.getDeskripsi().split("\n");
        for (String line : lines) {
            JLabel lblDesc = new JLabel("• " + line);
            lblDesc.setFont(new Font("Arial", Font.PLAIN, 11));
            lblDesc.setForeground(new Color(60, 60, 60)); // ✅ Warna gelap untuk kontras
            lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
            descPanel.add(lblDesc);
        }
        
        card.add(descPanel);
        card.add(Box.createVerticalStrut(15));

        // ✅ QUANTITY SELECTOR YANG LEBIH BAIK
        JPanel qtyPanel = new JPanel(new FlowLayout());
        qtyPanel.setBackground(Color.WHITE);
        
        JLabel lblQty = new JLabel("Jumlah:");
        lblQty.setFont(new Font("Arial", Font.BOLD, 13));
        lblQty.setForeground(Color.BLACK); // ✅ Warna hitam
        
        JLabel lblJumlah = new JLabel("0");
        lblJumlah.setFont(new Font("Arial", Font.BOLD, 16));
        lblJumlah.setForeground(Color.BLACK); // ✅ Warna hitam

        // ✅ PANEL UNTUK QUANTITY DENGAN BORDER
        JPanel quantityContainer = new JPanel(new BorderLayout());
        quantityContainer.setBackground(Color.WHITE);
        quantityContainer.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        quantityContainer.setPreferredSize(new Dimension(60, 35));
        quantityContainer.add(lblJumlah, BorderLayout.CENTER);

        // ✅ TOMBOL MINUS DENGAN WARNA MERAH
        JButton btnMinus = new JButton("−");
        btnMinus.setFont(new Font("Arial", Font.BOLD, 16));
        btnMinus.setBackground(new Color(220, 50, 50));
        btnMinus.setForeground(Color.WHITE);
        btnMinus.setFocusPainted(false);
        btnMinus.setBorder(BorderFactory.createRaisedBevelBorder());
        btnMinus.setPreferredSize(new Dimension(40, 35));
        btnMinus.setOpaque(true);
        btnMinus.setContentAreaFilled(true);
        btnMinus.setBorderPainted(false);

        // ✅ TOMBOL PLUS DENGAN WARNA HIJAU
        JButton btnPlus = new JButton("+");
        btnPlus.setFont(new Font("Arial", Font.BOLD, 16));
        btnPlus.setBackground(new Color(50, 180, 50));
        btnPlus.setForeground(Color.WHITE);
        btnPlus.setFocusPainted(false);
        btnPlus.setBorder(BorderFactory.createRaisedBevelBorder());
        btnPlus.setPreferredSize(new Dimension(40, 35));
        btnPlus.setOpaque(true);
        btnPlus.setContentAreaFilled(true);
        btnPlus.setBorderPainted(false);

        btnPlus.addActionListener(e -> {
            int q = Integer.parseInt(lblJumlah.getText()) + 1;
            lblJumlah.setText(String.valueOf(q));
            updateTotalHarga(produk.getHarga(), 1);
        });

        btnMinus.addActionListener(e -> {
            int q = Integer.parseInt(lblJumlah.getText());
            if (q > 0) {
                lblJumlah.setText(String.valueOf(q - 1));
                updateTotalHarga(produk.getHarga(), -1);
            }
        });

        qtyPanel.add(lblQty);
        qtyPanel.add(btnMinus);
        qtyPanel.add(quantityContainer);
        qtyPanel.add(btnPlus);

        card.add(qtyPanel);
        card.add(Box.createVerticalStrut(15));

        // ✅ BUY NOW BUTTON DENGAN WARNA TERANG
        JButton btnBuy = new JButton("🛒 BUY NOW");
        btnBuy.setFont(new Font("Arial", Font.BOLD, 14));
        
        // ✅ WARNA YANG SAMA DENGAN VERSI BAWAH
        Color btnBuyColor = borderColor;
        
        btnBuy.setBackground(btnBuyColor);
        btnBuy.setForeground(Color.WHITE);

        // 🔥 FIX BUTTON
        btnBuy.setOpaque(true);
        btnBuy.setContentAreaFilled(true);
        btnBuy.setBorderPainted(false);
        btnBuy.setFocusPainted(false);

        // ✅ BORDER UNTUK KONTRASTING
        btnBuy.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(btnBuyColor.darker(), 3),
            BorderFactory.createEmptyBorder(12, 35, 12, 35)
        ));

        btnBuy.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBuy.setPreferredSize(new Dimension(180, 45));

        // ✅ EFEK HOVER (sama dengan versi bawah)
        btnBuy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuy.setBackground(btnBuyColor.brighter());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuy.setBackground(btnBuyColor);
            }
        });

        btnBuy.addActionListener(e -> {
            int qty = Integer.parseInt(lblJumlah.getText());
            if (qty > 0) {
                controller.tambahKeKeranjang(produk, qty);
                JOptionPane.showMessageDialog(this,
                        "✅ " + qty + "x " + produk.getNama() + " ditambahkan!",
                        "Berhasil",
                        JOptionPane.INFORMATION_MESSAGE);
                lblJumlah.setText("0");
                updateCartDisplay();
            }
        });

        card.add(btnBuy);

        return card;
    }

    private Color getCategoryColor(String kategori) {
        switch (kategori) {
            case "PAKET": return BORDER_PAKET;
            case "SEWA": return BORDER_SEWA;
            case "WEEDING": return BORDER_WEEDING;
        }
        return BORDER_PAKET;
    }

    private void updateTotalHarga(BigDecimal harga, int qty) {
        totalHarga = totalHarga.add(harga.multiply(BigDecimal.valueOf(qty)));
        lblTotalHarga.setText(
                NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
                        .format(totalHarga)
        );
    }

    private void updateCartDisplay() {
        totalHarga = controller.getTotalKeranjang();
        lblTotalHarga.setText(
                NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
                        .format(totalHarga)
        );
    }

    private void checkout() {
        if (totalHarga.compareTo(BigDecimal.ZERO) <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Keranjang masih kosong!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ✅ CHECKOUT DIALOG YANG SAMA DENGAN VERSI BAWAH
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Nama Pemesan:"));
        JTextField txtNama = new JTextField();
        panel.add(txtNama);
        
        panel.add(new JLabel("Telepon:"));
        JTextField txtTelepon = new JTextField();
        panel.add(txtTelepon);
        
        panel.add(new JLabel("Email:"));
        JTextField txtEmail = new JTextField();
        panel.add(txtEmail);
        
        panel.add(new JLabel("Jadwal (yy-xx-zz):"));
        JTextField txtJadwal = new JTextField("yy-xx-zz");
        panel.add(txtJadwal);
        
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"));
        panel.add(new JLabel("Total:"));
        panel.add(new JLabel(rupiahFormat.format(totalHarga)));
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Checkout", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            if (txtNama.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(this,
                "✅ Checkout berhasil!\nTotal: " + rupiahFormat.format(totalHarga),
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
            
            controller.clearCart();
            totalHarga = BigDecimal.ZERO;
            lblTotalHarga.setText("Rp 0");
        }
    }

    public void refreshProducts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'refreshProducts'");
    }
}