import java.util.Arrays;

public class LatihanArray5 {
    public static void main(String[] args) {
        int[] angka = new int[15];
        int genap = 0;
        int ganjil = 0;

        // Mengisi array dengan angka acak
        for (int i = 0; i < angka.length; i++) {
            angka[i] = (int) (Math.random() * 50 + 1);
        }

        // Menampilkan daftar angka
        System.out.println("=== Daftar Angka ===");
        System.out.println(Arrays.toString(angka));

        // Menentukan jumlah genap dan ganjil
        System.out.println("\nAngka genap:");
        for (int i : angka) {
            if (i % 2 == 0) {
                System.out.print(i + " ");
                genap += i;
            }
        }

        System.out.println("\n\nAngka ganjil:");
        for (int i : angka) {
            if (i % 2 != 0) {
                System.out.print(i + " ");
                ganjil += i;
            }
        }

        // Menampilkan total jumlah bilangan genap dan ganjil
        System.out.println("\n\nTotal jumlah angka genap: " + genap);
        System.out.println("Total jumlah angka ganjil: " + ganjil);
    }
}
