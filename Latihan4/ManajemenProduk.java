import java.util.ArrayList;

public class ManajemenProduk {

    static class Produk {
        public int id;
        public String name;
        public int price;

        public Produk(int id, String name, int price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }

    static class ProdukService {
        ArrayList<Produk> daftarProduk = new ArrayList<>();

        public void addProduct(Produk product) {
            if (product.name == null || product.name.trim().isEmpty()) {
                System.err.println("Name tidak boleh kosong");
                return;
            }
            if (product.price <= 0) {
                System.err.println("Price harus lebih dari 0");
                return;
            }

            daftarProduk.add(product);
            System.out.println("Produk " + product.name + " telah ditambahkan");
        }

        public void getProducts() {
            if (daftarProduk.isEmpty()) {
                System.out.println("Tidak ada produk");
                return;
            }

            for (Produk p : daftarProduk) {
                System.out.println("ID: " + p.id + " | Name: " + p.name + " | Price: " + p.price);
            }
        }

        public void updateProduct(int id, Produk newData) {
            for (int i = 0; i < daftarProduk.size(); i++) {
                if (daftarProduk.get(i).id == id) {
                    daftarProduk.set(i, newData);
                    System.out.println("Produk ID " + id + " berhasil diupdate");
                    return;
                }
            }
            System.err.println("Produk dengan ID " + id + " tidak ditemukan");
        }

        public void deleteProduct(int id) {
            for (int i = 0; i < daftarProduk.size(); i++) {
                if (daftarProduk.get(i).id == id) {
                    daftarProduk.remove(i);
                    System.out.println("Produk ID " + id + " berhasil dihapus");
                    return;
                }
            }
            System.err.println("Produk dengan ID " + id + " tidak ditemukan");
        }

        public void searchProduct(String keyword) {
            String lower = keyword.toLowerCase();
            boolean found = false;

            for (Produk p : daftarProduk) {
                if (p.name.toLowerCase().contains(lower)) {
                    System.out.println("ID: " + p.id + " | Name: " + p.name + " | Price: " + p.price);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("Tidak ada produk yang ditemukan.");
            }
        }
    }
}
