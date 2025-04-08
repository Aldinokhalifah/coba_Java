import java.util.ArrayList;
import java.util.Scanner;

public class SistemPembelianBarang {
    static class Barang {
        String nama;
        int harga;

        public Barang(String nama, int harga) {
            this.nama = nama;
            this.harga = harga;
        }

        public void infoBarang() {
            System.out.println("Nama Barang: " + nama);
            System.out.println("Harga Barang: " + harga);
        }
    }

    static class Keranjang {
        ArrayList<Barang> daftarBelanja = new ArrayList<>();

        public void tambahBarang(Barang barang) {
            daftarBelanja.add(barang);
            System.out.println(barang.nama + " berhasil ditambahkan ke keranjang.");
        }

        public void tampilkanBelanja() {
            System.out.println("\nDaftar Keranjang Belanja:");
            for (Barang barang : daftarBelanja) {
                barang.infoBarang();
                System.out.println("==========");
            }
        }

        public int hitungTotal() {
            int totalHarga = 0;
            for (Barang barang : daftarBelanja) {
                totalHarga += barang.harga;
            }
            System.out.println("Total harga barang di keranjang: " + totalHarga);
            return totalHarga;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Daftar barang yang tersedia
        ArrayList<Barang> daftarBarang = new ArrayList<>();
        daftarBarang.add(new Barang("Buku", 2000));
        daftarBarang.add(new Barang("Pensil", 1000));
        daftarBarang.add(new Barang("Pulpen", 1500));

        Keranjang keranjang = new Keranjang();

        String lanjut;
        do {
            System.out.println("\nDaftar Barang Tersedia:");
            for (int i = 0; i < daftarBarang.size(); i++) {
                System.out.println((i + 1) + ". " + daftarBarang.get(i).nama + " - Rp" + daftarBarang.get(i).harga);
            }

            System.out.print("Masukkan nama barang yang ingin dibeli: ");
            String inputNama = scanner.nextLine();

            boolean ditemukan = false;
            for (Barang b : daftarBarang) {
                if (b.nama.equalsIgnoreCase(inputNama)) {
                    keranjang.tambahBarang(b);
                    ditemukan = true;
                    break;
                }
            }

            if (!ditemukan) {
                System.out.println("Barang tidak ditemukan.");
            }

            System.out.print("Apakah ingin menambah barang lagi? (iya/tidak): ");
            lanjut = scanner.nextLine();

        } while (lanjut.equalsIgnoreCase("iya"));

        keranjang.tampilkanBelanja();
        keranjang.hitungTotal();

        scanner.close();
    }
}
