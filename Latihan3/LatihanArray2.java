import java.util.Arrays;

public class LatihanArray2 {
    public static void main(String[] args) {
        int[] angka = {5, 2, 8, 5, 12, 3, 7, 8, 15, 2, 9, 5, 6, 3, 8, 12, 4, 7, 15, 9};
        
        Arrays.sort(angka); // Mengurutkan array untuk mempermudah perhitungan
        
        int count = 1;
        System.out.println("Frekuensi kemunculan angka:");
        
        for(int i = 1; i < angka.length; i++) {
            if(angka[i] == angka[i-1]) {
                count++;
            } else {
                System.out.println("Angka " + angka[i-1] + " muncul " + count + " kali");
                count = 1;
            }
            
            // Untuk menangani elemen terakhir
            if(i == angka.length-1) {
                System.out.println("Angka " + angka[i] + " muncul " + count + " kali");
            }
        }
    }
}