package Latihan2;

public class InterfaceLatihan2 {
    interface BangunDatar {
        double luas();
        double keliling();
    }

    static class Persegi implements BangunDatar {
        double sisi;

        public Persegi(double sisi) {
            this.sisi = sisi;
        }

        @Override
        public double luas() {
            return sisi * sisi;
        }

        @Override
        public double keliling() {
            return 4 * sisi;
        }
    }

    static class Lingkaran implements BangunDatar {
        double jariJari;

        public Lingkaran(double jariJari) {
            this.jariJari = jariJari;
        }

        @Override
        public double luas() {
            return Math.PI * jariJari * jariJari;
        }

        @Override
        public double keliling() {
            return 2 * Math.PI * jariJari;
        }
    }

    public static void main(String[] args) {
        BangunDatar persegi = new Persegi(4);
        BangunDatar lingkaran = new Lingkaran(7);

        System.out.println("Persegi:");
        System.out.println("Luas = " + String.format("%.2f", persegi.luas()));
        System.out.println("Keliling = " + String.format("%.2f", persegi.keliling()));

        System.out.println("\nLingkaran:");
        System.out.println("Luas = " + String.format("%.2f", lingkaran.keliling()));
        System.out.println("Keliling = " + String.format("%.2f", lingkaran.keliling()));
    }
}
