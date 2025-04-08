import java.util.ArrayList;
import java.util.Scanner;

public class SistemPendaftaranKursus {
    static class Peserta {
        String nama;
        String email;

        public Peserta(String nama, String email) {
            this.nama = nama;
            this.email = email;
        }

        public void info() {
            System.out.println("Nama " + nama);
            System.out.println("Email " + email);
        }
    }

    static class Kursus{
        String namaKursus;
        ArrayList<Peserta> daftarPeserta = new ArrayList<>();

        public Kursus(String namaKursus) {
            this.namaKursus = namaKursus;
        }

        public void kursus() {
            System.out.println(namaKursus);
        }

        public void daftarPeserta(Peserta peserta) {
            daftarPeserta.add(peserta);
            System.out.println("Nama peserta " + peserta.nama + " berhasil ditambahkan di kursus " + namaKursus);
        }

        public void tampilkanPeserta() {
            System.out.println("\nDaftar Peserta di Kursus " + namaKursus + ":");
            for (Peserta i : daftarPeserta) {
                i.info();
                System.out.println("==========");
            }
        }

        public void cariPeserta(String nama) {
            boolean ditemukan = false;
            for (Peserta p : daftarPeserta) {
                if (p.nama.equalsIgnoreCase(nama)) {
                    System.out.println("Nama " + nama + " ditemukan:");
                    p.info();
                    ditemukan = true;
                    break;
                }
            }
            if (!ditemukan) {
                System.out.println("Peserta dengan nama '" + nama + "' tidak ditemukan.");
            }
        }        
    }

    static class Main {
        public static void main(String[] args) {
            Kursus javaKursus = new Kursus("Pemrograman Java");
            Scanner scanner = new Scanner(System.in);

            // Daftar 2 peserta
            javaKursus.daftarPeserta(new Peserta("Ali", "ali@example.com"));
            javaKursus.daftarPeserta(new Peserta("Sinta", "sinta@example.com"));

            // Tampilkan semua peserta
            javaKursus.tampilkanPeserta();

            // Cari peserta
            System.out.print("Masukkan nama peserta yang ingin dicari: ");
            String namaCari = scanner.nextLine();
            javaKursus.cariPeserta(namaCari);
            
            scanner.close();
        }
    }
}
