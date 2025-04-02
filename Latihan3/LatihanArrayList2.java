import java.util.ArrayList;
import java.util.Scanner;

public class LatihanArrayList2 {
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

        System.out.print("Apakah Anda ingin menghapus nama dari daftar? (iya/tidak): ");
        String jawab = scanner.nextLine();

        if (jawab.equalsIgnoreCase("iya") || jawab.equalsIgnoreCase("mau")) {
            System.out.print("Sebutkan nama yang ingin dihapus: ");
            String hapus = scanner.nextLine();

            if (namaOrang.contains(hapus)) {
                namaOrang.remove(hapus);
                System.out.println("\nOrang dengan nama \"" + hapus + "\" telah dihapus dari daftar.");
                
                // Menampilkan daftar nama setelah penghapusan
                System.out.println("\n=== Daftar nama setelah penghapusan: ===");
                for (int i = 0; i < namaOrang.size(); i++) {
                    System.out.println((i + 1) + ". " + namaOrang.get(i));
                }
            } else {
                System.out.println("Orang dengan nama \"" + hapus + "\" tidak ditemukan dalam daftar.");
            }
        } else {
            System.out.println("Daftar nama tidak dihapus.");
        }

        scanner.close(); // Menutup Scanner
    }
}