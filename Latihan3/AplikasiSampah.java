import java.util.ArrayList;
import java.util.Scanner;

public class AplikasiSampah {
    static class Edukasi {
        public void tampilkanEdukasi() {
            System.out.println("\n=== Edukasi Pengelolaan Sampah ===");
            System.out.println("- Pisahkan sampah organik dan anorganik.");
            System.out.println("- Kurangi penggunaan plastik sekali pakai.");
            System.out.println("- Manfaatkan barang bekas untuk didaur ulang.");
        }
    }

    static class LaporanSampah {
        ArrayList<String> laporan = new ArrayList<>();

        public void tambahLaporan(String isilaporan) {
            laporan.add(isilaporan);
            System.out.println("Laporan berhasil ditambahkan!");
        }

        public void tampilkanLaporan() {
            System.out.println("\n === Laporan Sampah ===");
            for (int i = 0; i < laporan.size(); i++) {
                System.out.println((i + 1) + ". " + laporan.get(i));
            }
        }
    }

    static class BankSampah {
        int totalBerat;
        Scanner scanner = new Scanner(System.in);

        public BankSampah(int totalBerat) {
            this.totalBerat = totalBerat;
        }

        public void setorSampah() {
            String tanya;
            do { 
                System.out.print("Masukkan berat sampah(KG): ");
                int beratSampah = scanner.nextInt();
                totalBerat += beratSampah;

                System.out.print("Apakah anda mau menambah sampah lagi? (iya / tidak) ");
                scanner.nextLine(); // consume the leftover newline
                tanya = scanner.nextLine();
            } while (tanya.equalsIgnoreCase("iya"));
            System.out.println("Sampah berhasil disetor, Total berat sekarang: " + totalBerat);
        }

        public void tampilkanSaldo() {
            System.out.println("\n === Informasi Bank Sampah");
            System.out.println("Total Berat sampah: " + totalBerat + " KG");
            System.out.println("Estimasi Saldo: RP" + totalBerat * 500);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Edukasi edukasi = new Edukasi();
        LaporanSampah laporanSampah = new LaporanSampah();
        BankSampah bankSampah = new BankSampah(0);
        int pilihan;
        

        do { 
            System.out.println("\n=== Aplikasi Pengelolaan Sampah ===");
            System.out.println("1. Edukasi");
            System.out.println("2. Laporkan Sampah");
            System.out.println("3. Bank Sampah");
            System.out.println("4. Keluar");
            System.out.print("Pilih menu: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    edukasi.tampilkanEdukasi();
                    break;
                case 2:
                    System.out.print("Tulis laporan sampah: ");
                    String laporan = scanner.nextLine();
                    laporanSampah.tambahLaporan(laporan);
                    break;
                case 3:
                    // System.out.print("Masukkan berat sampah yang ingin disetor (kg): ");
                    // int berat = scanner.nextInt();
                    bankSampah.setorSampah();
                    bankSampah.tampilkanSaldo();
                    break;
                case 4:
                    System.out.println("Terima kasih telah menggunakan aplikasi ini!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");;
            }
        } while (pilihan != 4);

        scanner.close();
    }
}
