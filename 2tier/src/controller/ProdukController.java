package src.controller;

import src.model.Produk;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProdukController {
    
    private List<Produk> produkList;
    private List<CartItem> cartItems;
    
    private class CartItem {
        Produk produk;
        int quantity;
        
        CartItem(Produk produk, int quantity) {
            this.produk = produk;
            this.quantity = quantity;
        }
    }
    
    public ProdukController() {
        initProdukList();
        this.cartItems = new ArrayList<>();
        System.out.println("🎮 Controller Produk siap!");
    }
    
    private void initProdukList() {
        produkList = new ArrayList<>();
        
        // Data produk sesuai desain Figma
        produkList.add(new Produk(1, "Paket Hemat", 
            "• Makeup biasa + pakaian + foto (HANYA FILE MENTAHAN SAJA)\n" +
            "• Foto Keluarga Perorang (+Rp50.000.00)",
            new BigDecimal("160000.00"),
            "PAKET"));
        
        produkList.add(new Produk(2, "Paket Sedang",
            "• Makeup biasa + pakaian + foto (SUDAH DI CETAK)\n" +
            "• Foto Keluarga Perorang (+Rp50.000.00)",
            new BigDecimal("270000.00"),
            "PAKET"));
        
        produkList.add(new Produk(3, "Paket Combo",
            "• Makeup biasa + pakaian + foto + bingkai (SUDAH DI CETAK)\n" +
            "• Foto Keluarga Perorang (+Rp50.000.00)",
            new BigDecimal("470000.00"),
            "PAKET"));
        
        produkList.add(new Produk(4, "Sewa Pakaian",
            "• Penyewaan pakaian perkostum Rp 35.000.00\n" +
            "• Pakaian bayi sampai dewasa",
            new BigDecimal("35000.00"),
            "SEWA"));
        
        produkList.add(new Produk(5, "WeedingGo",
            "• 1 Roll (40 foto) + Cetak album ukuran 4R\n" +
            "• Model Book (+Rp1.000.000.00)",
            new BigDecimal("400000.00"),
            "WEEDING"));
        
        System.out.println("📦 Loaded " + produkList.size() + " produk");
    }
    
    public List<Produk> getAllProduk() {
        return new ArrayList<>(produkList);
    }
    
    public List<Produk> getProdukByJenis(String jenis) {
        List<Produk> result = new ArrayList<>();
        for (Produk p : produkList) {
            if (jenis.equals(p.getJenis())) {
                result.add(p);
            }
        }
        return result;
    }
    
    public void tambahKeKeranjang(Produk produk, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity harus lebih dari 0");
        }
        
        // Cek apakah produk sudah ada di keranjang
        for (CartItem item : cartItems) {
            if (item.produk.getNama().equals(produk.getNama())) {
                item.quantity += quantity;
                System.out.println("🛒 Update keranjang: " + produk.getNama() + " (+" + quantity + ")");
                return;
            }
        }
        
        // Jika belum ada, tambah baru
        cartItems.add(new CartItem(produk, quantity));
        System.out.println("🛒 Tambah ke keranjang: " + produk.getNama() + " x" + quantity);
    }
    
    public BigDecimal getTotalKeranjang() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            total = total.add(item.produk.getHarga()
                    .multiply(new BigDecimal(item.quantity)));
        }
        return total;
    }
    
    public List<String> getCartSummary() {
        List<String> summary = new ArrayList<>();
        for (CartItem item : cartItems) {
            summary.add(item.quantity + "x " + item.produk.getNama());
        }
        return summary;
    }
    
    public void clearCart() {
        cartItems.clear();
        System.out.println("🛒 Keranjang dikosongkan");
    }
    
    public int getCartItemCount() {
        return cartItems.size();
    }
    
    // Method baru untuk mendapatkan daftar keranjang
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }
}