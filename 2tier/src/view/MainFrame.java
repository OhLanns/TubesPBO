package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private ProdukPanel produkPanel;
    private RiwayatPesananPanel riwayatPanel;
    private Timer statusTimer;
    private JScrollPane mainScrollPane; // ✅ Tambahkan JScrollPane
    
    public MainFrame() {
        initComponents();
        setupFrame();
        startStatusAnimation();
    }
    
    private void initComponents() {
        // Set look and feel untuk tampilan yang lebih modern
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }
        
        setTitle("PANESYA STUDIO - Sistem Pemesanan Foto");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Main container panel
        JPanel mainContainer = new JPanel(new BorderLayout());
        setContentPane(mainContainer);
        
        // Header dengan gradient dan shadow
        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        
        // Panel utama yang akan di-scroll
        JPanel mainContentPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(245, 245, 245);
                Color color2 = new Color(255, 255, 255);
                GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        // Tabbed pane dengan styling khusus
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(250, 250, 250));
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Create panels
        produkPanel = new ProdukPanel();
        riwayatPanel = new RiwayatPesananPanel();
        
        // Custom tab renderer
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, 
                int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                // No border painting
            }
            
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, 
                int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isSelected) {
                    Color color1 = new Color(41, 128, 185);
                    Color color2 = new Color(52, 152, 219);
                    GradientPaint gradient = new GradientPaint(x, y, color1, x, y + h, color2);
                    g2d.setPaint(gradient);
                    g2d.fillRoundRect(x + 2, y + 2, w - 4, h - 2, 15, 15);
                } else {
                    g2d.setColor(new Color(240, 240, 240));
                    g2d.fillRoundRect(x + 2, y + 2, w - 4, h - 2, 15, 15);
                }
            }
            
            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                // No content border
            }
        });
        
        // Add tabs dengan icon dan tooltip
        tabbedPane.addTab("🛍️ PRODUK", createTabIcon(new Color(46, 204, 113)), produkPanel, "Lihat dan pesan produk studio foto");
        tabbedPane.addTab("📋 RIWAYAT", createTabIcon(new Color(52, 152, 219)), riwayatPanel, "Kelola riwayat pesanan pelanggan");
        
        // Panel untuk tabbed pane dengan border rounded
        JPanel tabContainer = new JPanel(new BorderLayout());
        tabContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        tabContainer.setBackground(Color.WHITE);
        
        // ✅ Bungkus tabbedPane dengan JScrollPane
        JScrollPane tabScrollPane = new JScrollPane(tabbedPane);
        tabScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tabScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tabScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        tabContainer.add(tabScrollPane, BorderLayout.CENTER);
        mainContentPanel.add(tabContainer, BorderLayout.CENTER);
        
        // ✅ Bungkus mainContentPanel dengan JScrollPane utama
        mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Custom scrollbar untuk smooth scrolling
        JScrollBar verticalBar = mainScrollPane.getVerticalScrollBar();
        verticalBar.setUnitIncrement(16);
        verticalBar.setBlockIncrement(64);
        
        JScrollBar horizontalBar = mainScrollPane.getHorizontalScrollBar();
        horizontalBar.setUnitIncrement(16);
        horizontalBar.setBlockIncrement(64);
        
        mainContainer.add(mainScrollPane, BorderLayout.CENTER);
        
        // Footer dengan informasi dan status
        JPanel footerPanel = createFooterPanel();
        mainContainer.add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                Color color1 = new Color(41, 128, 185);
                Color color2 = new Color(52, 152, 219);
                GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), 0, color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Pattern overlay
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
                g2d.setColor(Color.WHITE);
                for (int i = 0; i < getWidth(); i += 20) {
                    g2d.fillOval(i, getHeight() / 2, 4, 4);
                }
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
        };
        
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 120));
        
        // Title dengan icon kamera
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Icon kamera
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel iconLabel = new JLabel("📸");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        titlePanel.add(iconLabel, gbc);
        
        // Title text
        gbc.gridx = 1;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("PANESYA STUDIO");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, gbc);
        
        // Subtitle
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel subtitleLabel = new JLabel("Sistem Pemesanan Foto Profesional");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 230, 255));
        titlePanel.add(subtitleLabel, gbc);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        // User info panel (jika ada login)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        userPanel.setOpaque(false);
        
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Arial", Font.PLAIN, 20));
        
        JLabel userLabel = new JLabel("Admin");
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userLabel.setForeground(Color.WHITE);
        
        userPanel.add(userIcon);
        userPanel.add(userLabel);
        
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        footerPanel.setBackground(new Color(250, 250, 250));
        
        // Left side - Status dengan animation
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        statusPanel.setBackground(new Color(250, 250, 250));
        
        JLabel statusIcon = new JLabel("🟢");
        statusIcon.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel statusLabel = new JLabel("Status: Online");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(new Color(46, 204, 113));
        
        statusPanel.add(statusIcon);
        statusPanel.add(statusLabel);
        
        // Center - Copyright
        JLabel copyrightLabel = new JLabel("© 2025 Panesya Studio - All Rights Reserved", SwingConstants.CENTER);
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        copyrightLabel.setForeground(new Color(150, 150, 150));
        
        // Right side - Version
        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setFont(new Font("Arial", Font.BOLD, 11));
        versionLabel.setForeground(new Color(100, 100, 100));
        
        footerPanel.add(statusPanel, BorderLayout.WEST);
        footerPanel.add(copyrightLabel, BorderLayout.CENTER);
        footerPanel.add(versionLabel, BorderLayout.EAST);
        
        return footerPanel;
    }
    
    private Icon createTabIcon(Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.fillOval(x, y, getIconWidth(), getIconHeight());
            }
            
            @Override
            public int getIconWidth() {
                return 12;
            }
            
            @Override
            public int getIconHeight() {
                return 12;
            }
        };
    }
    
    private void setupFrame() {
        // Set window size and position
        setSize(1300, 850);
        setLocationRelativeTo(null);
        
        // Set window icon
        try {
            // Create a simple camera icon programmatically
            setIconImage(createCameraIcon());
        } catch (Exception e) {
            System.err.println("Error creating icon: " + e.getMessage());
        }
        
        // Add window listener for confirmation on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
            
            @Override
            public void windowOpened(WindowEvent e) {
                // Welcome message
                showWelcomeMessage();
            }
        });
        
        // Enable anti-aliasing for better graphics
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        // Make frame visible
        setVisible(true);
    }
    
    private Image createCameraIcon() {
        // Create a simple camera icon programmatically
        int size = 32;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw camera body
        g2d.setColor(new Color(41, 128, 185));
        g2d.fillRoundRect(4, 8, 24, 18, 5, 5);
        
        // Draw lens
        g2d.setColor(Color.WHITE);
        g2d.fillOval(12, 12, 8, 8);
        
        // Draw flash
        g2d.setColor(new Color(255, 204, 0));
        g2d.fillRect(20, 4, 4, 3);
        
        g2d.dispose();
        return image;
    }
    
    private void startStatusAnimation() {
        // Animate status indicator
        statusTimer = new Timer(1000, e -> {
            Component[] comps = ((JPanel) ((BorderLayout) getContentPane().getLayout()).
                getLayoutComponent(BorderLayout.SOUTH)).getComponents();
            
            for (Component comp : comps) {
                if (comp instanceof JPanel) {
                    Component[] innerComps = ((JPanel) comp).getComponents();
                    for (Component innerComp : innerComps) {
                        if (innerComp instanceof JLabel && innerComp.getName() == null) {
                            JLabel statusIcon = (JLabel) innerComp;
                            if (statusIcon.getText().contains("🟢")) {
                                statusIcon.setText("🟡");
                            } else {
                                statusIcon.setText("🟢");
                            }
                            break;
                        }
                    }
                }
            }
        });
        statusTimer.start();
    }
    
    private void showWelcomeMessage() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>"
                + "<h2 style='color: #2980b9;'>🌟 Selamat Datang di Panesya Studio! 🌟</h2>"
                + "<p>Sistem manajemen pemesanan studio foto siap digunakan.</p>"
                + "<p>Pilih tab <b>PRODUK</b> untuk melihat paket foto,</p>"
                + "<p>atau tab <b>RIWAYAT</b> untuk mengelola pesanan.</p>"
                + "<br><p style='font-size: 11px; color: #95a5a6;'>© 2025 Panesya Studio</p>"
                + "</div></html>",
                "Welcome",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private void confirmExit() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "<html><div style='text-align: center;'>"
            + "<h3 style='color: #e74c3c;'>⚠️ Konfirmasi Keluar</h3>"
            + "<p>Apakah Anda yakin ingin keluar dari aplikasi?</p>"
            + "<p style='font-size: 11px; color: #95a5a6;'>Semua perubahan yang belum disimpan akan hilang.</p>"
            + "</div></html>",
            "Konfirmasi Keluar",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            cleanupResources();
            if (statusTimer != null) {
                statusTimer.stop();
            }
            System.exit(0);
        }
    }
    
    private void cleanupResources() {
        System.out.println("🧹 Membersihkan resources aplikasi...");
        // Add cleanup code here if needed
    }
    
    // Method untuk refresh panels
    public void refreshAllPanels() {
        if (produkPanel != null) {
            produkPanel.refreshProducts();
        }
        
        if (riwayatPanel != null) {
            riwayatPanel.refreshTable();
        }
    }
    
    // ✅ Method untuk scroll ke atas
    public void scrollToTop() {
        if (mainScrollPane != null) {
            JScrollBar vertical = mainScrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMinimum());
        }
    }
    
    // ✅ Method untuk scroll ke bawah
    public void scrollToBottom() {
        if (mainScrollPane != null) {
            JScrollBar vertical = mainScrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        }
    }
    
    // ✅ Method untuk cek apakah bisa di-scroll
    public boolean isScrollable() {
        if (mainScrollPane != null) {
            JViewport viewport = mainScrollPane.getViewport();
            Component view = viewport.getView();
            return view.getPreferredSize().height > viewport.getHeight();
        }
        return false;
    }
    
    // Getter methods
    public ProdukPanel getProdukPanel() {
        return produkPanel;
    }
    
    public RiwayatPesananPanel getRiwayatPanel() {
        return riwayatPanel;
    }
    
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
    
    // ✅ Getter untuk scroll pane
    public JScrollPane getMainScrollPane() {
        return mainScrollPane;
    }
    
    // Toggle fullscreen
    public void toggleFullScreen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        
        if (device.isFullScreenSupported()) {
            if (isUndecorated()) {
                device.setFullScreenWindow(null);
                setUndecorated(false);
            } else {
                setUndecorated(true);
                device.setFullScreenWindow(this);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Fullscreen tidak didukung pada sistem ini.",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                
                // ✅ Debug: Cek apakah scroll pane bekerja
                System.out.println("MainFrame initialized with scroll capability");
                System.out.println("ScrollPane available: " + (frame.getMainScrollPane() != null));
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Error memulai aplikasi: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}