import java.util.ArrayList;

public class SistemPenambahanBuku {
    static class Buku {
        int id;
        String judul;
        String penulis;
        int tahun_terbit;

        public Buku(int id, String judul, String penulis, int tahun_terbit) {
            this.id = id;
            this.judul = judul;
            this.penulis = penulis;
            this.tahun_terbit = tahun_terbit;
        }

        public int getId() {
            return this.id;
        }

        public String getJudul() {
            return this.judul;
        }

        public String getPenulis() {
            return this.penulis;
        }

        public int getTahunTerbit() {
            return this.tahun_terbit;
        }

        public void info() {
            System.out.println("\n===== Detail Buku =====");
            System.out.println("ID: " + this.id);
            System.out.println("Judul: " + this.judul);
            System.out.println("Penulis: " + this.penulis);
            System.out.println("Tahun Terbit: " + this.tahun_terbit);
        }
    }

    static class Penjaga {
        ArrayList<Buku> daftarbuku = new ArrayList<>();

        public void tambahbuku(Buku buku) {
            daftarbuku.add(buku);
            System.out.println("Buku '" + buku.getJudul() + "' berhasil ditambahkan.");
        }

        public void hapusbuku(int id) {
            for(int i = 0; i < daftarbuku.size(); i++) {
                if(daftarbuku.get(i).getId() == id) {
                    daftarbuku.remove(i);
                    System.out.println("Buku dengan ID " + id + " berhasil dihapus.");
                    return;
                }
            }
            System.out.println("Buku dengan ID " + id + " tidak ditemukan.");
        }

        public void tampilkanbuku() {
            System.out.println("\n===== Daftar Buku =====");
            if(daftarbuku.isEmpty()) {
                System.out.println("Tidak ada buku yang tersedia.");
            } else {
                for (Buku buku : daftarbuku) {
                    System.out.println(buku.getId() + ". " + buku.getJudul() + " - " + buku.getPenulis() + " (" + buku.getTahunTerbit() + ")");
                }
            }
        }
    }

    public static void main(String[] args) {
        Penjaga penjaga = new Penjaga();
        
        Buku buku1 = new Buku(1, "Java Programming", "John Doe", 2023);
        Buku buku2 = new Buku(2, "Python Basics", "Jane Smith", 2024);
        
        penjaga.tambahbuku(buku1);
        penjaga.tambahbuku(buku2);
        
        penjaga.tampilkanbuku();
        
        penjaga.hapusbuku(1);
        
        penjaga.tampilkanbuku();
    }
}
