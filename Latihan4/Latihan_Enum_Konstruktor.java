public class Latihan_Enum_Konstruktor {
    enum Planet {
        MERKURIUS(3.303e+23, 2.4397e6),
        BUMI(5.976e+24, 6.37814e6),
        MARS(6.421e+23, 3.3972e6);

        private final double massa;
        private final double radius;

        Planet(double massa, double radius) {
            this.massa = massa;
            this.radius = radius;
        }

        public double getMassa() {
            return massa;
        }

        public double getRadius() {
            return radius;
        }
    }
    public static void main(String[] args) {

        for( Planet p : Planet.values()) {
            System.out.println("==========");
            System.out.println("Planet: " + p);
            System.out.println("Massa: " + p.getMassa());
            System.out.println("Radius: " + p.getRadius());
        }
    }
}
