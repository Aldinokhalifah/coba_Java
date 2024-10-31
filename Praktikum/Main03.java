public class Main03 {
    public static void main(String[] args) {
        int nilai = 85;
        String status = "aktif";

        if (nilai >= 75) {
            if (status.equals("aktif")) {
                System.out.println("Selamat, kamu lulus dan statusmu aktif.");
            } else {
                System.out.println("Kamu lulus, tetapi statusmu tidak aktif.");
            }
        } else {
            System.out.println("Maaf, kamu tidak lulus.");
        }
    }
}
