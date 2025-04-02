
import java.util.Scanner;

public class cobaMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Apa kamu ingin mencoba tebak angka? (iya/tidak)");
        String jawab = scanner.nextLine();

        if(jawab.equalsIgnoreCase("iya")) {
            TebakAngka tebakAngka = new TebakAngka();
            tebakAngka.main(args);
        } else {
            System.out.println("Baiklah, sampai jumpa lain waktu!");
        }
    }
}
