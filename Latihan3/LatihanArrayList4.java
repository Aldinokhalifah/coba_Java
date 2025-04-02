import java.util.ArrayList;
import java.util.Collections;

public class LatihanArrayList4 {
    public static void main(String[] args) {
        ArrayList<Integer> angkaAcak = new ArrayList<>();

        // Mengisi ArrayList dengan angka acak antara 1 - 100
        for (int i = 0; i < 10; i++) {
            angkaAcak.add((int) (Math.random() * 100 + 1));
        }

        // Menampilkan daftar angka sebelum diurutkan
        System.out.println("=== Daftar angka sebelum diurutkan ===");
        System.out.println(angkaAcak); 

        // Mengurutkan daftar angka
        Collections.sort(angkaAcak);

        // Menampilkan daftar angka setelah diurutkan
        System.out.println("\n=== Daftar angka setelah diurutkan ===");
        for (int angka : angkaAcak) {
            System.out.println(angka);
        }
    }
}
