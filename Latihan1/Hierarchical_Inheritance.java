public class Hierarchical_Inheritance {
    static class Kendaraan{
        public String merk;
        public int tahun;

        public Kendaraan(String merk, int tahun) {
            this.merk = merk;
            this.tahun = tahun;
        }

        public void tampilInfo() {
            System.out.println("Merk Kendaraan: " + this.merk);
            System.out.println("Tahun Kendaraan: " + this.tahun);
        }
    }

    static class Mobil extends Kendaraan{
        public int jumlahPintu;

        public Mobil(String merk, int tahun, int jumlahPintu) {
            super(merk, tahun);
            this.jumlahPintu = jumlahPintu;
        }

        public void tampilInfoMobil() {
            super.tampilInfo();
            System.out.println("Jumlah pintu mobil: " + this.jumlahPintu);
        }
    }

    static class Motor extends Kendaraan{
        public String tipe;

        public Motor(String merk, int tahun, String tipe) {
            super(merk, tahun);
            this.tipe = tipe;
        }

        public void tampilInfoMotor() {
            super.tampilInfo();
            System.out.println("Tipe motor: " + this.tipe);
        }
    }

    public static void main(String[] args) {
    Mobil avanza = new Mobil("Avanza", 2021, 4);
    Motor bebek = new Motor("Honda", 2015,"Bebek");

    avanza.tampilInfoMobil();

    bebek.tampilInfoMotor();
    }
}
