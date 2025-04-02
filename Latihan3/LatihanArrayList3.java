import java.util.ArrayList;
import java.util.Scanner;

public class LatihanArrayList3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> namaOrang = new ArrayList<>();
        namaOrang.add("John");
        namaOrang.add("Emma");
        namaOrang.add("Michael");
        namaOrang.add("Sophie");
        namaOrang.add("David");

        System.out.println("=== Daftar nama yang telah dimasukkan: ===");
        for (int i = 0; i < namaOrang.size(); i++) {
            System.out.println((i + 1) + ". " + namaOrang.get(i));
        }

        System.out.print("Apakah Anda ingin mencari nama dari daftar? (iya/tidak): ");
        String jawab = scanner.nextLine();

        if (jawab.equalsIgnoreCase("iya") || jawab.equalsIgnoreCase("mau")) {
            System.out.print("Siapa nama yang ingin Anda cari: ");
            String namaDicari = scanner.nextLine();

            if (namaOrang.contains(namaDicari)) {
                int index = namaOrang.indexOf(namaDicari) + 1; // Menghitung posisi manusia (1-based)
                System.out.println("Nama \"" + namaDicari + "\" ada di dalam daftar pada posisi ke-" + index + ".");
            } else {
                System.out.println("Nama \"" + namaDicari + "\" tidak ada di dalam daftar.");
            }
        } else {
            System.out.println("Pencarian nama dibatalkan.");
        }

        scanner.close(); // Menutup scanner
    }
}
