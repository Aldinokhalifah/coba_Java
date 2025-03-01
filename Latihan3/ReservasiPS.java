import java.util.Scanner;

public class ReservasiPS {
    static class PS {
        String nama;
        String tgl_sewa;
        String jam_sewa;
        String jenis_ps;
        int harga;
        int lama_sewa;
        int total_harga;

        public PS(String nama, String tgl_sewa, String jam_sewa, String jenis_ps, int harga, int lama_sewa) {
            this.nama = nama;
            this.tgl_sewa = tgl_sewa;
            this.jam_sewa = jam_sewa;
            this.jenis_ps = jenis_ps;
            this.harga = harga;
            this.lama_sewa = lama_sewa;
            this.total_harga = harga * lama_sewa;
        }

        public void info() {
            System.out.println("\n===== Detail Reservasi =====");
            System.out.println("Nama Penyewa   : " + this.nama);
            System.out.println("Tanggal Sewa   : " + this.tgl_sewa);
            System.out.println("Jam Sewa       : " + this.jam_sewa + " WIB");
            System.out.println("Jenis PS       : " + this.jenis_ps);
            System.out.println("Harga Sewa     : Rp " + String.format("%,d", this.harga).replace(",", ".") + " /jam");
            System.out.println("Lama Sewa      : " + this.lama_sewa + " jam");
            System.out.println("Total Harga    : Rp " + String.format("%,d", this.total_harga).replace(",", "."));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Selamat datang di PS Store!");
        System.out.print("Apakah Anda ingin menyewa PS? (iya/tidak): ");
        String jawab = scanner.nextLine();

        if (jawab.equalsIgnoreCase("mau") || jawab.equalsIgnoreCase("iya")) {
            System.out.print("Masukkan nama Anda: ");
            String nama = scanner.nextLine();

            System.out.print("Masukkan tanggal sewa (DD-MM-YYYY): ");
            String tgl_sewa = scanner.nextLine();

            System.out.print("Masukkan jam sewa (HH:MM): ");
            String jam_sewa = scanner.nextLine();

            System.out.print("Masukkan jenis PS (PS2, PS3, PS4, PS5): ");
            String jenis_ps = scanner.nextLine();

            int harga = 0;
            switch (jenis_ps.toLowerCase()) {
                case "ps2", "2" -> harga = 5000;
                case "ps3", "3" -> harga = 10000;
                case "ps4", "4" -> harga = 15000;
                case "ps5", "5" -> harga = 20000;
                default -> {
                    System.out.println("Jenis PS tidak tersedia. Reservasi dibatalkan.");
                    scanner.close();
                    return;
                }
            }

            System.out.println("Harga sewa " + jenis_ps.toUpperCase() + ": Rp " + String.format("%,d", harga).replace(",", ".") + " /jam");

            System.out.print("Masukkan lama sewa (jam): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Harap masukkan angka!");
                scanner.next(); 
            }
            int lama_sewa = scanner.nextInt();
            scanner.nextLine(); 

            PS ps1 = new PS(nama, tgl_sewa, jam_sewa, jenis_ps.toUpperCase(), harga, lama_sewa);
            ps1.info();
        } else {
            System.out.println("Terima kasih sudah datang di PS Store.");
        }

        scanner.close();
    }
}
