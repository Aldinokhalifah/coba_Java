import java.util.Scanner;

public class Switch {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Masukkan angka (1-3): ");
        int angka = scanner.nextInt();

        switch (angka) {
            case 1:
                System.out.println("Anda memilih angka satu.");
                break;
            case 2:
                System.out.println("Anda memilih angka dua.");
                break;
            case 3:
                System.out.println("Anda memilih angka tiga.");
                break;
            default:
                System.out.println("Angka yang Anda masukkan tidak valid.");
                break;
        }
        scanner.close();
    }
}

