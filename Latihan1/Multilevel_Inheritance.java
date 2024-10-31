public class Multilevel_Inheritance {
    static class Kendaraan{
        public String merk;
        public int kecepatan;

        public Kendaraan(String merk, int kecepatan) {
            this.merk = merk;
            this.kecepatan = kecepatan;
        }

        public void nyalakan() {
            System.out.println("Kendaraan Dinyalakan");
        }
    }

    static class Mobil extends Kendaraan{
        public int jumlahPintu;
        public String jenisBbm;

        public Mobil(String merk, int kecepatan, int jumlahPintu,  String jenisBbm) {
            super(merk, kecepatan);
            this.jumlahPintu = jumlahPintu;
            this.jenisBbm = jenisBbm;
        }

        public void percepat() {
            System.out.println("Mobil Dipercepat");
        }
    }

    static class MobilListrik extends Mobil{
        public int  kapasitasBaterai;
        public int waktuPengisian;
        
        public MobilListrik(String merk, int kecepatan, int jumlahPintu,  String jenisBbm, int kapasitasBaterai, int waktuPengisian) {
            super(merk, kecepatan, jumlahPintu, jenisBbm);
            this.kapasitasBaterai = kapasitasBaterai;
            this.waktuPengisian = waktuPengisian;
        }

        public void isiDaya() {
            System.out.println("Mobil sedang mengisi daya");
        }
    }

    public static void main(String[] args) {
        MobilListrik tesla = new MobilListrik("Tesla", 100, 4, "Listrik", 100000, 120);
        tesla.nyalakan();
        tesla.percepat();
        tesla.isiDaya();
    }
}
