public class InheritanceLatihan3 {
    static class Karyawan {
        private String nama;
        private double gajiPokok;

        public Karyawan(String nama, double gajiPokok) {
            this.nama = nama;
            this.gajiPokok = gajiPokok;
        }

        public String getNama() {
            return nama;
        }

        public double getGajiPokok() {
            return gajiPokok;
        }

        public void infoKaryawan() {
            System.out.println("Nama: " + nama);
            System.out.println("Gaji Pokok: " + gajiPokok);
        }
    }

    static class KaryawanTetap extends Karyawan {
        private double tunjangan;

        public KaryawanTetap(String nama, double gajiPokok, double tunjangan) {
            super(nama, gajiPokok);
            this.tunjangan = tunjangan;
        }

        @Override
        public void infoKaryawan() {
            super.infoKaryawan();
            System.out.println("Tunjangan: " + tunjangan);
            System.out.println("Total Gaji: " + (getGajiPokok() + tunjangan));
        }
    }

    static class KaryawanKontrak extends Karyawan {
        private int lamaKontrak;

        public KaryawanKontrak(String nama, double gajiPokok, int lamaKontrak) {
            super(nama, gajiPokok);
            this.lamaKontrak = lamaKontrak;
        }

        @Override
        public void infoKaryawan() {
            super.infoKaryawan();
            System.out.println("Lama Kontrak: " + lamaKontrak + " bulan");
        }
    }

    public static void main(String[] args) {
        Karyawan karyawan = new Karyawan("Rojak", 12000);
        karyawan.infoKaryawan();
        System.out.println();

        KaryawanTetap karyawanTetap = new KaryawanTetap("Deden", 10000, 5000);
        karyawanTetap.infoKaryawan();
        System.out.println();

        KaryawanKontrak karyawanKontrak = new KaryawanKontrak("Jajang", 5000, 10);
        karyawanKontrak.infoKaryawan();
    }
}
