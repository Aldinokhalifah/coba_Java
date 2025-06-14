import  java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class AplikasiCatatan {
    static class Notes {
        int id;
        String judul;
        String konten;
        Date waktuDibuat;


        public Notes(int id, String judul, String konten, Date waktuDibuat) {
            this.id = id;
            this.judul = judul;
            this.konten = konten;
            this.waktuDibuat = waktuDibuat;
        }

        public int getId() {
            return id;
        }

        public String getJudul() {
            return judul;
        }

        public String getKonten() {
            return konten;
        }

        public Date getWaktuDibuat() {
            return waktuDibuat;
        }

        public void info() {
            System.out.println("\n===== Detail Buku =====");
            System.out.println("ID: " + this.id);
            System.out.println("Judul: " + this.judul);
            System.out.println("Konten: " + this.konten);
            System.out.println("Waktu Dibuat: " + this.waktuDibuat);
        }
    } 

    static class notesManager {
        ArrayList<Notes> daftarCatatan = new ArrayList<>();

        public void tambahCatatan(Notes note) {
            daftarCatatan.add(note);
            System.out.println("Berhasil Menambahkan Catatan dengan Judul: " + note.getJudul());
        }

        public void lihatCatatan() {
            if(daftarCatatan.size() < 1) {
                System.out.println("Kamu Belum Punya Catatan!");
            } else {
                System.out.println("=== Daftar Catatan Kamu ===");
                for(Notes note : daftarCatatan) {
                    System.out.println((note.getId()) + " ." + "Judul: " + note.getJudul() + ", Isi: " + note.getKonten() + ", Waktu Dibuat: " + note.getWaktuDibuat());
                }
            }
        }

        public void hapusCatatan(int id) {
            boolean ditemukan = false;
            for (int i = 0; i < daftarCatatan.size(); i++) {
                if (daftarCatatan.get(i).getId() == id) {
                    daftarCatatan.remove(i);
                    System.out.println("Catatan dengan ID " + id + " telah dihapus.");
                    ditemukan = true;
                    break;
                }
            }
            if (!ditemukan) {
                System.out.println("Catatan dengan ID " + id + " tidak ditemukan.");
            }
        }
    }

    public static void main(String[] args) {
        notesManager notesManager = new notesManager();
        Scanner scanner = new Scanner(System.in);
        int pilihan;

        notesManager.tambahCatatan(new Notes( 1, "Catatan Hari Senin", "Harus Belajar Java", new Date()));
        notesManager.tambahCatatan(new Notes( 2, "Catatan Hari Selasa", "Harus Belajar Solidity", new Date()));
        notesManager.tambahCatatan(new Notes( 3, "Catatan Hari Rabu", "Harus Belajar Python", new Date()));

        do { 
            System.out.println("\n=== Aplikasi Manajemen Catatan ===");
            System.out.println("1. Tambah Daftar Catatan");
            System.out.println("2. Lihat Daftar Catatan");
            System.out.println("3. Hapus Catatan");
            System.out.println("4. Keluar");
            System.out.print("Pilih menu: ");
            pilihan = scanner.nextInt();

            switch (pilihan) {
                case 1:
                    scanner.nextLine(); // Clear buffer
                    System.out.print("Masukkan Judul: ");
                    String judul = scanner.nextLine();

                    System.out.print("Masukkan Konten: ");
                    String konten = scanner.nextLine();

                    notesManager.tambahCatatan(new Notes(notesManager.daftarCatatan.size() + 1, judul, konten, new Date()));
                    System.out.println("Catatan Berhasil Ditambahkan!");
                    break;
                case 2:
                    notesManager.lihatCatatan();
                    break;
                case 3:
                    notesManager.lihatCatatan();
                    System.out.println("");

                    System.out.print("Pilih Nomor Catatan untuk Dihapus: ");
                    int hapusId = scanner.nextInt();
                    notesManager.hapusCatatan(hapusId);
                    break;
                case 4:
                    System.out.println("Terima kasih telah menggunakan Aplikasi Manajemen Catatan.");
                    break;
                default:
                    System.out.println("Pilihan Anda Tidak Valid!");
            }

        } while (pilihan != 4);
        scanner.close();
    }
}
