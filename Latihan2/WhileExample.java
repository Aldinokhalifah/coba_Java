package Latihan2;
import java.util.Scanner;

public class WhileExample {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Jawab (Iya/Tidak): ");
        String jawab = scanner.nextLine();

        while (jawab.equalsIgnoreCase("Tidak")) {
            System.out.print("Jawab (Iya/Tidak): ");
            jawab = scanner.nextLine();
        }

        if (jawab.equalsIgnoreCase("Iya")) {
            System.out.println("Oke");
        }

        scanner.close();
    }
}
