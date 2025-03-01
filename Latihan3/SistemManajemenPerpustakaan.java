import java.util.ArrayList;
import java.util.Scanner;


public class SistemManajemenPerpustakaan {
    static class Buku {
        int id;
        String judul;
        String penulis;
        int tahun_terbit;
        boolean dipinjam;

        public Buku(int id, String judul, String penulis, int tahun_terbit) {
            this.id = id;
            this.judul = judul;
            this.penulis = penulis;
            this.tahun_terbit = tahun_terbit;
            this.dipinjam = false;
        }

        public int getId() {
            return this.id;
        }

        public String getJudul() {
            return this.judul;
        }

        public boolean isDipinjam() {
            return this.dipinjam;
        }

        public void pinjam() {
            if(!this.dipinjam) {
                this.dipinjam = true;
                System.out.println("Buku '" + judul + "' berhasil dipinjam.");
            } else {
                System.out.println("Buku '" + judul + "' sudah dipinjam.");
            }
        }

        public void kembalikan() {
            if(this.dipinjam) {
                this.dipinjam = false;
                System.out.println("Buku '" + judul + "' berhasil dikembalikan.");
            } else {
                System.out.println("Buku '" + judul + "' sudah dikembalikan.");
            }
        }

        public void info() {
            System.out.println(id + ". " + judul + " - " + penulis + " (" + tahun_terbit + ") [" + (dipinjam ? "Dipinjam" : "Tersedia") + "]");
        }
    }

    static class Perpustakaan {
        ArrayList<Buku> daftarBuku = new ArrayList<>();

        public void tambahBuku(Buku buku) {
            daftarBuku.add(buku);
            System.out.println("Buku '" + buku.getJudul() + "' berhasil ditambahkan.");
        }

        public void tampilkanBuku() {
            System.out.println("\n===== Daftar Buku =====");
            if (daftarBuku.isEmpty()) {
                System.out.println("Daftar buku kosong.");
            } else {
                for (Buku buku : daftarBuku) {
                    buku.info();
                }
            }
        }

        public Buku cariBuku(int id) {
            for(Buku buku : daftarBuku) {
                if(buku.getId() == id) {
                    return buku;
                }
            }
            return null;
        }
    }

    public static void main(String[] args) {
        Perpustakaan perpustakaan = new Perpustakaan();
        Scanner scanner = new Scanner(System.in);
        int pilihan;

        perpustakaan.tambahBuku(new Buku(1, "Java Programming", "James Gosling", 1995));
        perpustakaan.tambahBuku(new Buku(2, "Clean Code", "Robert C. Martin", 2008));
        perpustakaan.tambahBuku(new Buku(3, "Si Pitung", "HJ. Rhoma Irama", 1978));

        do {
            System.out.println("\n=== Sistem Perpustakaan ===");
            System.out.println("1. Lihat Daftar Buku");
            System.out.println("2. Pinjam Buku");
            System.out.println("3. Kembalikan Buku");
            System.out.println("4. Keluar");
            System.out.print("Pilih menu: ");
            pilihan = scanner.nextInt();

            switch (pilihan) {
                case 1 -> perpustakaan.tampilkanBuku();
                case 2 -> {
                    System.out.print("Masukkan ID buku yang ingin dipinjam: ");
                    int idPinjam = scanner.nextInt();
                    Buku bukuPinjam = perpustakaan.cariBuku(idPinjam);
                    if (bukuPinjam != null) {
                        bukuPinjam.pinjam();
                    } else {
                        System.out.println("Buku tidak ditemukan.");
                    }
                }
                case 3 -> {
                    System.out.print("Masukkan ID buku yang ingin dikembalikan: ");
                    int idKembali = scanner.nextInt();
                    Buku bukuKembali = perpustakaan.cariBuku(idKembali);
                    if (bukuKembali != null) {
                        bukuKembali.kembalikan();
                    } else {
                        System.out.println("Buku tidak ditemukan.");
                    }
                }
                case 4 -> System.out.println("Terima kasih telah menggunakan Sistem Perpustakaan.");
                default -> System.out.println("Pilihan tidak valid!");
            }
        } while (pilihan != 4);
        scanner.close();
    }
}
