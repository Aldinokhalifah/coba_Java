import java.util.Scanner;

public class Praktikum02 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan lantai (1-20): ");
        int floor = scanner.nextInt();

        if (floor > 13) {
            int actualFloor = floor - 1;
            System.out.println("Lantai Sekarang adalah lantai: " + actualFloor);
        } else if(floor == 13) {
            System.out.println("Tidak ada lantai " + floor + ", pilih lantai 14 jika anda ingin menuju lantai 13");
        } else {
            System.out.println("Lantai Sekarang adalah lantai: " + floor);
        }

        System.out.println("Nama: Aldino Khalifah");
        System.out.println("Rombel: SE-01");
        System.out.println("NIM: 0110223089");

        scanner.close(); 
    }
}
