package src.view;

import src.controller.PesananController;
import src.model.Pesanan;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class RiwayatPesananPanel extends JPanel {
    private PesananController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnTambah, btnEdit, btnHapus, btnSelesai, btnBatalkan, btnRefresh;
    private JComboBox<String> comboFilter;
    private JPanel pnlTotal, pnlProses, pnlSukses, pnlBatal; // Diubah dari JLabel ke JPanel
    
    // Warna untuk status pesanan
    private final Color COLOR_PROGRESS = new Color(52, 152, 219, 30);     // Biru muda (Proses)
    private final Color COLOR_SUCCESS = new Color(46, 204, 113, 30);      // Hijau muda (Sukses)
    private final Color COLOR_CANCEL = new Color(231, 76, 60, 30);        // Merah muda (Batal)
    private final Color COLOR_WAITING = new Color(243, 156, 18, 30);      // Oranye muda (Menunggu)
    
    public RiwayatPesananPanel() {
        controller = new PesananController();
        controller.setView(this);
        setLayout(new BorderLayout());
        initComponents();
        loadData();
        updateStatistics();
        System.out.println("📋 RiwayatPesananPanel siap!");
    }
    
    private void initComponents() {
        // North panel for title and controls
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(new Color(245, 245, 245));
        
        // Title dengan gradient
        JLabel titleLabel = new JLabel("📋 RIWAYAT PESANAN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(250, 250, 250));
        northPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlPanel.setBackground(new Color(245, 245, 245));
        
        // Buttons dengan styling lebih baik
        btnTambah = createStyledButton("➕ Tambah", new Color(46, 204, 113));
        btnEdit = createStyledButton("✏️ Edit", new Color(52, 152, 219));
        btnHapus = createStyledButton("🗑️ Hapus", new Color(231, 76, 60));
        btnSelesai = createStyledButton("✅ Selesai", new Color(39, 174, 96));
        btnBatalkan = createStyledButton("❌ Batalkan", new Color(230, 126, 34));
        btnRefresh = createStyledButton("🔄 Refresh", new Color(149, 165, 166));
        
        controlPanel.add(btnTambah);
        controlPanel.add(btnEdit);
        controlPanel.add(btnHapus);
        controlPanel.add(btnSelesai);
        controlPanel.add(btnBatalkan);
        controlPanel.add(btnRefresh);
        
        // Filter dengan styling
        controlPanel.add(new JLabel("Filter Status:"));
        String[] filterOptions = {"SEMUA", "PROSES", "SUKSES", "BATAL", "MENUNGGU"};
        comboFilter = new JComboBox<>(filterOptions);
        comboFilter.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        comboFilter.setBackground(Color.WHITE);
        comboFilter.setPreferredSize(new Dimension(120, 32));
        comboFilter.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        controlPanel.add(comboFilter);
        
        northPanel.add(controlPanel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);
        
        // Center panel for table
        String[] columnNames = {"ID", "Produk", "Pemesan", "Tanggal", "Total", "Status", "Keterangan"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                
                // Warna baris berdasarkan status
                if (!isRowSelected(row)) {
                    Object statusValue = getValueAt(row, 5);
                    if (statusValue != null) {
                        String status = statusValue.toString().toUpperCase();
                        
                        if (status.contains("SUKSES")) {
                            c.setBackground(COLOR_SUCCESS);
                        } else if (status.contains("BATAL")) {
                            c.setBackground(COLOR_CANCEL);
                        } else if (status.contains("PROSES") || status.contains("SEDANG")) {
                            c.setBackground(COLOR_PROGRESS);
                        } else if (status.contains("MENUNGGU") || status.contains("PENDING")) {
                            c.setBackground(COLOR_WAITING);
                        } else {
                            c.setBackground(row % 2 == 0 ? new Color(250, 250, 250) : Color.WHITE);
                        }
                    } else {
                        c.setBackground(row % 2 == 0 ? new Color(250, 250, 250) : Color.WHITE);
                    }
                }
                
                return c;
            }
        };
        
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        // Custom renderer untuk kolom tertentu
        table.getColumnModel().getColumn(4).setCellRenderer(new CurrencyCellRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(200);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        add(scrollPane, BorderLayout.CENTER);
        
        // South panel for statistics
        JPanel southPanel = createStatisticsPanel();
        add(southPanel, BorderLayout.SOUTH);
        
        // Event listeners
        setupEventListeners();
    }
    
    private JPanel createStatisticsPanel() {
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        southPanel.setBackground(new Color(245, 245, 245));
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Inisialisasi stat cards sebagai JPanel
        pnlTotal = createStatCard("📦 Total Pesanan", "0", new Color(52, 152, 219));
        pnlProses = createStatCard("🔄 Proses", "0", new Color(41, 128, 185));
        pnlSukses = createStatCard("✅ Sukses", "0", new Color(39, 174, 96));
        pnlBatal = createStatCard("❌ Batal", "0", new Color(231, 76, 60));
        
        southPanel.add(pnlTotal);
        southPanel.add(pnlProses);
        southPanel.add(pnlSukses);
        southPanel.add(pnlBatal);
        
        return southPanel;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker()),
            BorderFactory.createEmptyBorder(6, 15, 6, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 32));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(150, 60));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        titleLabel.setForeground(new Color(100, 100, 100));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(color);
        valueLabel.setName("valueLabel"); // Beri nama untuk identifikasi
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void setupEventListeners() {
        btnTambah.addActionListener(e -> tambahPesanan());
        btnEdit.addActionListener(e -> editPesanan());
        btnHapus.addActionListener(e -> hapusPesanan());
        btnSelesai.addActionListener(e -> selesaikanPesanan());
        btnBatalkan.addActionListener(e -> batalkanPesanan());
        btnRefresh.addActionListener(e -> {
            loadData();
            updateStatistics();
            showMessage("Data berhasil direfresh!");
        });
        comboFilter.addActionListener(e -> filterData());
    }
    
    // Custom cell renderer untuk mata uang
    private class CurrencyCellRenderer extends DefaultTableCellRenderer {
        private NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(getIndonesianLocale());
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof BigDecimal) {
                setText(rupiahFormat.format(value));
                setHorizontalAlignment(RIGHT);
                setFont(new Font("Segoe UI", Font.BOLD, 11));
                setForeground(new Color(39, 174, 96));
            } else if (value instanceof String) {
                String strValue = value.toString();
                if (!strValue.isEmpty() && !strValue.equals("Rp 0")) {
                    try {
                        // Coba parse angka dari string
                        String cleanStr = strValue.replaceAll("[^\\d.,]", "");
                        if (!cleanStr.isEmpty()) {
                            BigDecimal amount = new BigDecimal(cleanStr.replace(",", "."));
                            setText(rupiahFormat.format(amount));
                            setHorizontalAlignment(RIGHT);
                            setFont(new Font("Segoe UI", Font.BOLD, 11));
                            setForeground(new Color(39, 174, 96));
                        }
                    } catch (Exception e) {
                        setText(strValue);
                    }
                } else {
                    setText(strValue);
                }
            }
            
            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }
            
            return c;
        }
    }
    
    // Custom cell renderer untuk status
    private class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value != null) {
                String status = value.toString().toUpperCase();
                
                // Set warna dan ikon berdasarkan status
                if (status.contains("SUKSES") || status.contains("SELESAI")) {
                    label.setForeground(new Color(39, 174, 96));
                    label.setText("✅ " + value.toString());
                } else if (status.contains("BATAL")) {
                    label.setForeground(new Color(231, 76, 60));
                    label.setText("❌ " + value.toString());
                } else if (status.contains("PROSES") || status.contains("SEDANG")) {
                    label.setForeground(new Color(41, 128, 185));
                    label.setText("🔄 " + value.toString());
                } else if (status.contains("MENUNGGU") || status.contains("PENDING")) {
                    label.setForeground(new Color(230, 126, 34));
                    label.setText("⏳ " + value.toString());
                } else {
                    label.setForeground(Color.BLACK);
                    label.setText(value.toString());
                }
                
                label.setHorizontalAlignment(CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 11));
            }
            
            return label;
        }
    }
    
    private void tambahPesanan() {
        PesananDialog dialog = new PesananDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Pesanan newPesanan = dialog.getPesanan();
            boolean success = controller.tambahPesanan(newPesanan);
            
            if (success) {
                showMessage("Pesanan berhasil ditambahkan!");
                loadData();
                updateStatistics();
            } else {
                showErrorMessage("Gagal menambahkan pesanan!");
            }
        }
    }
    
    private void editPesanan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showErrorMessage("Pilih pesanan yang akan diedit!");
            return;
        }
        
        Object idObj = tableModel.getValueAt(selectedRow, 0);
        if (idObj == null) {
            showErrorMessage("Data pesanan tidak valid!");
            return;
        }
        
        int id = Integer.parseInt(idObj.toString());
        Pesanan selectedPesanan = controller.getPesananById(id);
        
        if (selectedPesanan == null) {
            showErrorMessage("Pesanan tidak ditemukan!");
            return;
        }
        
        PesananDialog dialog = new PesananDialog((Frame) SwingUtilities.getWindowAncestor(this), selectedPesanan);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            boolean success = controller.updatePesanan(dialog.getPesanan());
            if (success) {
                showMessage("Pesanan berhasil diupdate!");
                loadData();
                updateStatistics();
            } else {
                showErrorMessage("Gagal mengupdate pesanan!");
            }
        }
    }
    
    private void hapusPesanan() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showErrorMessage("Pilih pesanan yang akan dihapus!");
            return;
        }
        
        Object idObj = tableModel.getValueAt(selectedRow, 0);
        if (idObj == null) {
            showErrorMessage("Data pesanan tidak valid!");
            return;
        }
        
        int id = Integer.parseInt(idObj.toString());
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus pesanan ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = controller.hapusPesanan(id);
            if (success) {
                showMessage("Pesanan berhasil dihapus!");
                loadData();
                updateStatistics();
            } else {
                showErrorMessage("Gagal menghapus pesanan!");
            }
        }
    }
    
    private void selesaikanPesanan() {
        updatePesananStatus("SUKSES", "selesai");
    }
    
    private void batalkanPesanan() {
        updatePesananStatus("BATAL", "dibatalkan");
    }
    
    private void updatePesananStatus(String newStatus, String actionName) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            showErrorMessage("Pilih pesanan yang akan di" + actionName + "!");
            return;
        }
        
        Object idObj = tableModel.getValueAt(selectedRow, 0);
        if (idObj == null) {
            showErrorMessage("Data pesanan tidak valid!");
            return;
        }
        
        int id = Integer.parseInt(idObj.toString());
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menandai pesanan ini sebagai " + newStatus + "?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = controller.ubahStatusPesanan(id, newStatus);
            
            if (success) {
                showMessage("Pesanan berhasil ditandai sebagai " + newStatus + "!");
                loadData();
                updateStatistics();
            } else {
                showErrorMessage("Gagal mengupdate status pesanan!");
            }
        }
    }
    
    public void loadData() {
        tableModel.setRowCount(0);
        List<Pesanan> pesananList = controller.getAllPesanan();
        
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(getIndonesianLocale());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        
        for (Pesanan pesanan : pesananList) {
            String statusDisplay = pesanan.getStatus();
            if (statusDisplay != null) {
                String upperStatus = statusDisplay.toUpperCase();
                if (upperStatus.contains("SUKSES")) {
                    statusDisplay = "✅ " + statusDisplay;
                } else if (upperStatus.contains("BATAL")) {
                    statusDisplay = "❌ " + statusDisplay;
                } else if (upperStatus.contains("PROSES") || upperStatus.contains("SEDANG")) {
                    statusDisplay = "🔄 " + statusDisplay;
                } else if (upperStatus.contains("MENUNGGU") || upperStatus.contains("PENDING")) {
                    statusDisplay = "⏳ " + statusDisplay;
                }
            }
            
            Object[] row = {
                pesanan.getId(),
                pesanan.getProduk() != null ? pesanan.getProduk() : "-",
                pesanan.getPemesan() != null ? pesanan.getPemesan() : "-",
                pesanan.getTanggalPesan() != null ? pesanan.getTanggalPesan().format(dateFormat) : "-",
                pesanan.getTotal() != null ? rupiahFormat.format(pesanan.getTotal()) : "Rp 0",
                statusDisplay != null ? statusDisplay : "🔄 PROSES",
                pesanan.getKeterangan() != null ? pesanan.getKeterangan() : "-"
            };
            tableModel.addRow(row);
        }
        
        System.out.println("📊 Data loaded: " + pesananList.size() + " pesanan");
    }
    
    private Locale getIndonesianLocale() {
        return Locale.forLanguageTag("id-ID");
    }
    
    private void filterData() {
        String filter = (String) comboFilter.getSelectedItem();
        if (filter == null || filter.equals("SEMUA")) {
            loadData();
            return;
        }
        
        List<Pesanan> filteredList = controller.getPesananByStatus(filter);
        tableModel.setRowCount(0);
        
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(getIndonesianLocale());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        
        for (Pesanan pesanan : filteredList) {
            String statusDisplay = pesanan.getStatus();
            if (statusDisplay != null) {
                String upperStatus = statusDisplay.toUpperCase();
                if (upperStatus.contains("SUKSES")) {
                    statusDisplay = "✅ " + statusDisplay;
                } else if (upperStatus.contains("BATAL")) {
                    statusDisplay = "❌ " + statusDisplay;
                } else if (upperStatus.contains("PROSES") || upperStatus.contains("SEDANG")) {
                    statusDisplay = "🔄 " + statusDisplay;
                } else if (upperStatus.contains("MENUNGGU") || upperStatus.contains("PENDING")) {
                    statusDisplay = "⏳ " + statusDisplay;
                }
            }
            
            Object[] row = {
                pesanan.getId(),
                pesanan.getProduk() != null ? pesanan.getProduk() : "-",
                pesanan.getPemesan() != null ? pesanan.getPemesan() : "-",
                pesanan.getTanggalPesan() != null ? pesanan.getTanggalPesan().format(dateFormat) : "-",
                pesanan.getTotal() != null ? rupiahFormat.format(pesanan.getTotal()) : "Rp 0",
                statusDisplay != null ? statusDisplay : "🔄 PROSES",
                pesanan.getKeterangan() != null ? pesanan.getKeterangan() : "-"
            };
            tableModel.addRow(row);
        }
    }
    
    private void updateStatistics() {
        List<Pesanan> allPesanan = controller.getAllPesanan();
        int total = allPesanan.size();
        int proses = 0;
        int sukses = 0;
        int batal = 0;
        
        for (Pesanan p : allPesanan) {
            String status = p.getStatus().toUpperCase();
            if (status.contains("PROSES") || status.contains("SEDANG")) {
                proses++;
            } else if (status.contains("SUKSES")) {
                sukses++;
            } else if (status.contains("BATAL")) {
                batal++;
            }
        }
        
        // Update stat cards - PERBAIKAN DI SINI
        updateStatCardValue(pnlTotal, total);
        updateStatCardValue(pnlProses, proses);
        updateStatCardValue(pnlSukses, sukses);
        updateStatCardValue(pnlBatal, batal);
    }
    
    private void updateStatCardValue(JPanel card, int value) {
        if (card != null) {
            // Cari label value di dalam card berdasarkan nama
            for (Component comp : card.getComponents()) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    // Cek berdasarkan nama
                    if ("valueLabel".equals(label.getName())) {
                        label.setText(String.valueOf(value));
                        break;
                    }
                }
            }
        }
    }
    
    public void refreshTable() {
        loadData();
        updateStatistics();
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void showWarningMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }
    
    public JTable getTable() {
        return table;
    }
    
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}