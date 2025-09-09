public class Latihan_Enum_Method {
    enum Arah {
        UTARA("Arah ke atas"),
        TIMUR("Arah ke kanan"),
        SELATAN("Arah ke bawah"),
        BARAT("Arah ke kiri");

        private final String deskripsi;

        Arah(String deskripsi) {
            this.deskripsi = deskripsi;
        }

        public String getDeskripsi() {
            return deskripsi;
        }
    }

    public static void main(String[] args) {
        for (Arah a : Arah.values()) {
            System.out.println(a + " -> " + a.getDeskripsi());
        }
    }
}
