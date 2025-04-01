
import java.util.Scanner;

public class LatihanArray3 {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int[] angka = new int[10]; // Initialize array with size 10

            for (int i = 0; i < angka.length; i++) {
            angka[i] = (int)(Math.random() * 20 + 1); // Mengisi array dengan angka acak antara 1 dan 20
        }

        System.out.print("Pilih angka yang mau dicari: ");
        int angkaDicari = scanner.nextInt();

        for(int a = 0; a < angka.length; a++) {
            if(angka[a] == angkaDicari) {
                System.out.println("Angka " + angkaDicari + " ditemukan pada index ke-" + a);
                break; // Keluar dari loop setelah menemukan angka
            } else if (a == angka.length - 1) {
                System.out.println("Angka " + angkaDicari + " tidak ditemukan dalam array.");
            }
        }
    }
    }
}
