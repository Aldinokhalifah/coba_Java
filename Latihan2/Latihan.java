import java.util.Scanner;


public class Latihan {
    
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Masukkan angka: ");
        int angka = scan.nextInt();

        if(angka % 2 == 0) {
            System.out.println("Genap");
        } else{
            System.out.println("Ganjil");
        }
    }
}
