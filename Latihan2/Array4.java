public class Array4 {
    public static void main(String[] args) {
        int[] angka = {62, 32, 98, 22};
        java.util.Arrays.sort(angka);
        
        // ascending loop
        for(int i = 0; i < angka.length; i++) {
            System.out.println(angka[i]);
        }

        // descending loop
        for (int i = angka.length - 1; i >= 0; i--) {
            System.out.println(angka[i]);
        }
    }
}
