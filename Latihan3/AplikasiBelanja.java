import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class AplikasiBelanja {

    static class Keranjang {
        private static int nextId = 1;

        private int id;
        private String namaBarang;
        private double hargaBarang;
        private int jumlahBarang;

        public Keranjang(String namaBarang, double hargaBarang, int jumlahBarang) {
            this.id = nextId++;
            this.namaBarang = namaBarang;
            this.hargaBarang = hargaBarang;
            this.jumlahBarang = jumlahBarang;
        }

        public int getId() {
            return id;
        }

        public String getNamaBarang() {
            return namaBarang;
        }

        public double getHargaBarang() {
            return hargaBarang;
        }

        public int getJumlahBarang() {
            return jumlahBarang;
        }

        public void info() {
            DecimalFormat formatter = new DecimalFormat("#,###.00");
            System.out.println("=== Detail Keranjang ===");
            System.out.println("ID: " + this.id);
            System.out.println("Nama Barang: " + this.namaBarang);
            System.out.println("Harga Barang: Rp " + formatter.format(this.hargaBarang));
            System.out.println("Jumlah Barang: " + this.jumlahBarang);
        }
    }

    static class KeranjangManager {
        ArrayList<Keranjang> daftarKeranjang = new ArrayList<>();

        public void tambahKeranjang(Keranjang keranjang) {
            daftarKeranjang.add(keranjang);
            System.out.println("✅ Barang telah ditambahkan: " + keranjang.getNamaBarang());
        }

        public void lihatKeranjang() {
            if (daftarKeranjang.isEmpty()) {
                System.out.println("⚠️ Kamu belum punya daftar belanja.");
            } else {
                System.out.println("=== Daftar Keranjang ===");
                DecimalFormat formatter = new DecimalFormat("#,###.00");
                for (Keranjang kj : daftarKeranjang) {
                    System.out.println("ID: " + kj.getId());
                    System.out.println("Nama Barang: " + kj.getNamaBarang());
                    System.out.println("Harga Barang: Rp " + formatter.format(kj.getHargaBarang()));
                    System.out.println("Jumlah Barang: " + kj.getJumlahBarang());
                    System.out.println();
                }
            }
        }

        public void hapusKeranjang(int id) {
            boolean ditemukan = false;
            for (int i = 0; i < daftarKeranjang.size(); i++) {
                if (daftarKeranjang.get(i).getId() == id) {
                    daftarKeranjang.remove(i);
                    System.out.println("🗑️ Barang dengan ID " + id + " telah dihapus.");
                    ditemukan = true;
                    break;
                }
            }

            if (!ditemukan) {
                System.out.println("❌ Barang dengan ID " + id + " tidak ditemukan.");
            }
        }
    }

    public static void main(String[] args) {
        KeranjangManager keranjangManager = new KeranjangManager();
        Scanner scanner = new Scanner(System.in);
        int pilihan;

        do {
            System.out.println("\n=== Aplikasi Belanja ===");
            System.out.println("1. Tambah Daftar Belanja");
            System.out.println("2. Lihat Daftar Belanja");
            System.out.println("3. Hapus Daftar Belanja");
            System.out.println("4. Keluar");
            System.out.print("Pilih menu: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Masukkan angka (1-4): ");
                scanner.next(); // Clear invalid input
            }
            pilihan = scanner.nextInt();

            switch (pilihan) {
                case 1:
                    scanner.nextLine(); // clear buffer
                    System.out.print("Masukkan Nama Barang: ");
                    String namaBarang = scanner.nextLine();

                    System.out.print("Masukkan Harga Barang: ");
                    while (!scanner.hasNextDouble()) {
                        System.out.print("Masukkan harga berupa angka: ");
                        scanner.next();
                    }
                    double hargaBarang = scanner.nextDouble();

                    System.out.print("Masukkan Jumlah Barang: ");
                    while (!scanner.hasNextInt()) {
                        System.out.print("Masukkan jumlah berupa angka: ");
                        scanner.next();
                    }
                    int jumlahBarang = scanner.nextInt();

                    keranjangManager.tambahKeranjang(
                        new Keranjang(namaBarang, hargaBarang, jumlahBarang)
                    );
                    break;

                case 2:
                    keranjangManager.lihatKeranjang();
                    break;

                case 3:
                    keranjangManager.lihatKeranjang();
                    System.out.print("Masukkan ID Barang yang Ingin Dihapus: ");
                    while (!scanner.hasNextInt()) {
                        System.out.print("Masukkan angka ID: ");
                        scanner.next();
                    }
                    int idHapus = scanner.nextInt();
                    keranjangManager.hapusKeranjang(idHapus);
                    break;

                case 4:
                    System.out.println("🛒 Terima kasih telah menggunakan Aplikasi Belanja!");
                    break;

                default:
                    System.out.println("❌ Pilihan tidak valid. Silakan pilih antara 1 - 4.");
            }
        } while (pilihan != 4);

        scanner.close();
    }
}
