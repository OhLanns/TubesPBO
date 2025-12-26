package src.controller;

import src.model.Pesanan;
import src.service.PesananService;
import src.service.PesananServiceDefault;
import src.view.RiwayatPesananPanel;
import java.util.List;
import java.util.ArrayList;

public class PesananController {
    
    private final PesananService pesananService;
    private RiwayatPesananPanel view;
    
    public PesananController() {
        this.pesananService = new PesananServiceDefault();
        System.out.println("🎮 Controller Pesanan siap!");
    }
    
    public void setView(RiwayatPesananPanel view) {
        this.view = view;
    }
    
    // ========== CREATE ==========
    public boolean tambahPesanan(Pesanan pesanan) {
        try {
            System.out.println("🎮 Controller: Memproses tambah pesanan...");
            boolean success = pesananService.tambahPesanan(pesanan);
            
            if (success && view != null) {
                view.refreshTable();
                showMessage("✅ Pesanan berhasil ditambahkan!");
            }
            
            return success;
            
        } catch (Exception e) {
            String errorMsg = "❌ Gagal tambah pesanan: " + e.getMessage();
            System.err.println(errorMsg);
            
            if (view != null) {
                view.showErrorMessage(errorMsg);
            }
            
            return false;
        }
    }
    
    // ========== READ ==========
    public List<Pesanan> getAllPesanan() {
        try {
            System.out.println("🎮 Controller: Mengambil semua pesanan...");
            return pesananService.getAllPesanan();
            
        } catch (Exception e) {
            String errorMsg = "❌ Gagal mengambil data: " + e.getMessage();
            System.err.println(errorMsg);
            
            if (view != null) {
                view.showErrorMessage(errorMsg);
            }
            
            return new ArrayList<>(); // Return empty list
        }
    }
    
    public Pesanan getPesananById(int id) {
        try {
            System.out.println("🎮 Controller: Mencari pesanan ID " + id);
            return pesananService.getPesananById(id);
            
        } catch (Exception e) {
            String errorMsg = "❌ Pesanan tidak ditemukan: " + e.getMessage();
            System.err.println(errorMsg);
            
            if (view != null) {
                view.showErrorMessage(errorMsg);
            }
            
            return null;
        }
    }
    
    // ========== UPDATE ==========
    public boolean updatePesanan(Pesanan pesanan) {
        try {
            System.out.println("🎮 Controller: Memproses update pesanan ID " + pesanan.getId());
            boolean success = pesananService.updatePesanan(pesanan);
            
            if (success && view != null) {
                view.refreshTable();
                showMessage("✅ Pesanan berhasil diupdate!");
            }
            
            return success;
            
        } catch (Exception e) {
            String errorMsg = "❌ Gagal update pesanan: " + e.getMessage();
            System.err.println(errorMsg);
            
            if (view != null) {
                view.showErrorMessage(errorMsg);
            }
            
            return false;
        }
    }
    
    public boolean ubahStatusPesanan(int id, String status) {
        try {
            System.out.println("🎮 Controller: Mengubah status pesanan ID " + id + " -> " + status);
            boolean success = pesananService.ubahStatusPesanan(id, status);
            
            if (success && view != null) {
                view.refreshTable();
                showMessage("✅ Status berhasil diubah!");
            }
            
            return success;
            
        } catch (Exception e) {
            String errorMsg = "❌ Gagal ubah status: " + e.getMessage();
            System.err.println(errorMsg);
            
            if (view != null) {
                view.showErrorMessage(errorMsg);
            }
            
            return false;
        }
    }
    
    // ========== DELETE ==========
    public boolean hapusPesanan(int id) {
        try {
            System.out.println("🎮 Controller: Memproses hapus pesanan ID " + id);
            boolean success = pesananService.hapusPesanan(id);
            
            if (success && view != null) {
                view.refreshTable();
                showMessage("✅ Pesanan berhasil dihapus!");
            }
            
            return success;
            
        } catch (Exception e) {
            String errorMsg = "❌ Gagal hapus pesanan: " + e.getMessage();
            System.err.println(errorMsg);
            
            if (view != null) {
                view.showErrorMessage(errorMsg);
            }
            
            return false;
        }
    }
    
    // ========== BUSINESS METHODS ==========
    public List<Pesanan> getPesananByStatus(String status) {
        try {
            System.out.println("🎮 Controller: Filter pesanan by status: " + status);
            return pesananService.getPesananByStatus(status);
            
        } catch (Exception e) {
            String errorMsg = "❌ Gagal filter pesanan: " + e.getMessage();
            System.err.println(errorMsg);
            
            if (view != null) {
                view.showErrorMessage(errorMsg);
            }
            
            return new ArrayList<>();
        }
    }
    
    public double getTotalPendapatan() {
        try {
            return pesananService.getTotalPendapatan();
        } catch (Exception e) {
            System.err.println("❌ Gagal menghitung pendapatan: " + e.getMessage());
            return 0.0;
        }
    }
    
    public int getJumlahPesananByStatus(String status) {
        try {
            return pesananService.getJumlahPesananByStatus(status);
        } catch (Exception e) {
            System.err.println("❌ Gagal menghitung jumlah pesanan: " + e.getMessage());
            return 0;
        }
    }
    
    // ========== HELPER METHODS ==========
    private void showMessage(String message) {
        if (view != null) {
            view.showMessage(message);
        }
    }
    
    // ========== UTILITY ==========
    public void testConnection() {
        try {
            List<Pesanan> pesanan = getAllPesanan();
            System.out.println("✅ Koneksi database berhasil!");
            System.out.println("   Total pesanan: " + pesanan.size());
        } catch (Exception e) {
            System.err.println("❌ Koneksi database gagal: " + e.getMessage());
        }
    }
}