import java.util.ArrayList;
import java.util.Scanner;

public class LatihanArrayList1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> namaOrang = new ArrayList<>();
        
        String jawab;
        
        do {
            System.out.print("Masukkan nama: ");
            String nama = scanner.nextLine();
            namaOrang.add(nama);
            
            System.out.print("Apakah Anda ingin menambah nama yang lain? (iya/tidak): ");
            jawab = scanner.nextLine();
            
        } while (jawab.equalsIgnoreCase("iya")); // Loop berhenti jika pengguna menjawab "tidak"

        System.out.println("\n=== Daftar nama yang telah dimasukkan: ===");
        for (String i : namaOrang) {
            System.out.println(i);
        }

        scanner.close(); 
    }
}
