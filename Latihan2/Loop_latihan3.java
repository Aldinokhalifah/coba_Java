public class Loop_latihan3 {
    public static void main(String[] args) {
        int angka = 5;

        for (int i = 0; i <= angka; i++) {
            for (int j = 0; j < i; j++) {
                System.out.print("*"); // Use print instead of println to stay on the same line
            }
            System.out.println(); // Move to the next line after printing the asterisks
        }
    }
}