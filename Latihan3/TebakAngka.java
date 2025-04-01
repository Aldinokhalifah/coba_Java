import java.util.Scanner;

public class TebakAngka {
    public static void main(String[] args) {
        int angkaTebakan = (int) (Math.random() * 100) + 1; 
        int angkaTebak  ;

        System.out.println("Selamat datang di permainan Tebak Angka!");
        System.out.println("Pilih angka antara 1 dan 100. Coba tebak!");

        do { 
            System.out.println("Masukkan tebakan Anda (1-100): ");
            Scanner scanner = new Scanner(System.in);
            angkaTebak = scanner.nextInt();
            if (angkaTebak < 1 || angkaTebak > 100) {
                System.out.println("Tebakan Anda harus antara 1 dan 100. Silakan coba lagi.");
            } else if (angkaTebak < angkaTebakan) {
                System.out.println("Tebakan Anda terlalu rendah. Coba lagi!");
            } else if (angkaTebak > angkaTebakan) {
                System.out.println("Tebakan Anda terlalu tinggi. Coba lagi!");
            } 
        } while (angkaTebak != angkaTebakan);
            System.out.println("Selamat! Anda berhasil menebak angka " + angkaTebakan + " dengan benar.");
            System.out.println("Terima kasih telah bermain!");
    }
}
