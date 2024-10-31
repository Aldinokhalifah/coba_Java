public class IntefaceClass2 {
    interface Kendaraan {
        void bergerak();
        int kapasitasPenumpang();
    }

    static class Mobil implements Kendaraan {
        @Override
        public void bergerak() {
            System.out.println("Mobil: Bergerak menggunakan mesin");
        }

        @Override
        public int kapasitasPenumpang() {
            return 4;
        }
    }

    static class Sepeda implements Kendaraan {
        @Override
        public void bergerak() {
            System.out.println("Sepeda: Bergerak dengan dikayuh");
        }

        @Override
        public int kapasitasPenumpang() {
            return 1;
        }
    }

    public static void main(String[] args) {
        Kendaraan mobil = new Mobil();
        Kendaraan sepeda = new Sepeda();

        mobil.bergerak();
        System.out.println("Kapasitas penumpang mobil: " + mobil.kapasitasPenumpang());

        sepeda.bergerak();
        System.out.println("Kapasitas penumpang sepeda: " + sepeda.kapasitasPenumpang());
    }
}
