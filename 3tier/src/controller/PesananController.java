package controller;

import src.model.Produk;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PesananController {

    private List<CartItem> cartItems = new ArrayList<>();

    private class CartItem {
        Produk produk;
        int quantity;

        CartItem(Produk produk, int quantity) {
            this.produk = produk;
            this.quantity = quantity;
        }
    }

    public void tambahKeKeranjang(Produk produk, int qty) {
        for (CartItem item : cartItems) {
            if (item.produk.getId() == produk.getId()) {
                item.quantity += qty;
                return;
            }
        }
        cartItems.add(new CartItem(produk, qty));
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            total = total.add(
                item.produk.getHarga()
                    .multiply(new BigDecimal(item.quantity))
            );
        }
        return total;
    }

    public List<String> getRingkasanPesanan() {
        List<String> list = new ArrayList<>();
        for (CartItem item : cartItems) {
            list.add(item.quantity + "x " + item.produk.getNama());
        }
        return list;
    }

    public void clear() {
        cartItems.clear();
    }
}
