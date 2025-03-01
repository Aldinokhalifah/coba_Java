public class InheritanceLatihan6 {
    abstract static class Pegawai {
        String nama;
        String id;

        public Pegawai(String nama, String id) {
            this.nama = nama;
            this.id = id;
        }

        public abstract double hitungGaji();

        public void tampilkanInfo() {
            System.out.println("Nama: " + nama);
            System.out.println("ID: " + id);
            System.out.println("Gaji: Rp" + String.format("%,.2f", hitungGaji()));
        }
    }

    static class pegawaiTetap extends Pegawai {
        double gajiPokok;

        public pegawaiTetap(String nama, String id, double gajiPokok) {
            super(nama, id);
            this.gajiPokok = gajiPokok;
        }

        @Override
        public double hitungGaji() {
            return gajiPokok;
        }

    }

    static class PegawaiKontrak extends Pegawai {
        double upahPerJam;
        int jumlahJamKerja;

        public PegawaiKontrak(String nama, String id, double upahPerJam, int jumlahJamKerja) {
            super(nama, id);
            this.upahPerJam = upahPerJam;
            this.jumlahJamKerja = jumlahJamKerja;
        }

        @Override
        public double hitungGaji() {
            return upahPerJam * jumlahJamKerja;
        }

        @Override
        public void tampilkanInfo() {
            super.tampilkanInfo();
            System.out.println("Upah per Jam: Rp" + String.format("%,.2f", upahPerJam));
            System.out.println("Jumlah Jam Kerja: " + jumlahJamKerja);
        }
    }

    public static void main(String[] args) {
        pegawaiTetap pegawaiTetap = new pegawaiTetap("Ahmad", "1234", 5000000);
        pegawaiTetap.tampilkanInfo();
        System.out.println();

        PegawaiKontrak pegawaiKontrak = new PegawaiKontrak("Budi", "5678", 50000, 100);
        pegawaiKontrak.tampilkanInfo();
    }
}
