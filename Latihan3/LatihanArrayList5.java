
import java.util.ArrayList;
import java.util.Scanner;

public class LatihanArrayList5 {
    public static void main(String[] args) {
        ArrayList<Integer> angka = new ArrayList<>();

        int jumlah = 0;
    
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Masukkan angka untuk dimasukkan ke dalam array: ");
            int input = scanner.nextInt();
            scanner.nextLine(); // consume newline
            angka.add(input);

            while (true) {
                System.out.print("Apakah Anda ingin menambah angka lain? (iya/tidak): ");
                String jawab = scanner.nextLine();
                
                if (!jawab.equalsIgnoreCase("iya")) {
                    System.out.println("Daftar angka tidak ditambah.");
                    break;
                }
                
                System.out.print("Masukkan angka untuk dimasukkan ke dalam array: ");
                int inputBaru = scanner.nextInt();
                scanner.nextLine(); // consume newline
                angka.add(inputBaru);
            }

        System.out.println("=== Daftar angka yang telah dimasukkan: ===");
        for (int i : angka) {
            System.out.println("" + i);
        }

        for (int i : angka) {
            jumlah += i ;
        }
        System.out.println("Jumlah angka yang telah dimasukkan: " + jumlah);

    } finally {
        scanner.close();
    }
}
}   
