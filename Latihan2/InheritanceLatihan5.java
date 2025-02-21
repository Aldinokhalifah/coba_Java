public class InheritanceLatihan5 {
    static class Kendaraan {
        String merek;
        int tahunProduksi;
        double harga;

        public Kendaraan(String merek, int tahunProduksi, double harga) {
            this.merek = merek;
            this.tahunProduksi = tahunProduksi;
            this.harga = harga;
        }

        public void tampilkanInfo() {
            System.out.println("Merek: " + merek);
            System.out.println("Tahun Produksi: " + tahunProduksi);
            System.out.println("Harga: " + harga);
        }
    }

    static class Mobil extends Kendaraan {
        int jumlahPintu;

        public Mobil(String merek, int tahunProduksi, double harga, int jumlahPintu) {
            super(merek, tahunProduksi, harga);
            this.jumlahPintu = jumlahPintu;
        }

        @Override
        public void tampilkanInfo() {
            super.tampilkanInfo();
            System.out.println("Jumlah Pintu: " + jumlahPintu);
        }
    }

    static class Motor extends Kendaraan {
        String jenisMotor;

        public Motor(String merek, int tahunProduksi, double harga, String jenisMotor) {
            super(merek, tahunProduksi, harga);
            this.jenisMotor = jenisMotor;
        }

        @Override
        public void tampilkanInfo() {
            super.tampilkanInfo();
            System.out.println("Jenis Motor: " + jenisMotor);
        }
    }

    public static void main(String[] args) {
        Mobil mobil = new Mobil("Toyota", 2019, 200000000, 4);
        mobil.tampilkanInfo();
        System.out.println();

        Motor motor = new Motor("Honda", 2018, 15000000, "Matic");
        motor.tampilkanInfo();
    }
}
