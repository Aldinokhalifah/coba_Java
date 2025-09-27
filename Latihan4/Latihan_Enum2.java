public class Latihan_Enum2 {
    enum StatusOrder {
        PENDING("Pesanan menunggu konfirmasi"),
        PROSES("Pesanan sedang diproses"),
        DIKIRIM("Pesanan sedang dikirim"),
        SELESAI("Pesanan sudah selesai"),
        DIBATALKAN("Pesanan dibatalkan");

        private final String status;

        StatusOrder(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    public static void main(String[] args) {
        System.out.println("Contoh satu status:");
        System.out.println("Status Order: " + StatusOrder.DIKIRIM.getStatus());

        System.out.println("\nDaftar semua status:");
        for (StatusOrder s : StatusOrder.values()) {
            System.out.println(s + " -> " + s.getStatus());
        }
    }
}
