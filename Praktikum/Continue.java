public class Continue {
    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                continue; // Lewati iterasi jika i adalah angka genap
            }
            System.out.println("Angka ganjil: " + i);
        }
    }
}