package Latihan2;

public class InterfaceLatihan {
    interface Hewan {
        void suara();
        String habitat();
    }

    static class Kucing implements Hewan {
        @Override
        public void suara() {
            System.out.println("Suara kucing: Meow");
        }

        @Override
        public String habitat() {
            return "Daratan";
        }
    }

    static class Ikan implements Hewan {
        @Override
        public void suara() {
            System.out.println("Suara ikan: Tidak ada suara");
        }

        @Override
        public String habitat() {
            return "Air";
        }
    }

    public static void main(String[] args) {
        Hewan kucing = new Kucing();
        Hewan ikan = new Ikan();

        kucing.suara();
        System.out.println("Habitat kucing: " + kucing.habitat());

        ikan.suara();
        System.out.println("Habitat ikan: " + ikan.habitat());
    }
}
