
import java.util.ArrayList;
import java.util.Collections;

public class LatihanArray4 {
    public static void main(String[] args) {
        ArrayList<Integer> angka = new ArrayList<>();

        for ( int i = 0; i < 10; i++) {
            angka.add((int)(Math.random() * 100 + 1));
        }

        System.out.println("=== Daftar angka ===");
        System.out.println(angka);

        System.out.println("Nilai tertinggi: " + Collections.max(angka));
        System.out.println("Nilai terendah: " + Collections.min(angka));
    }
}
