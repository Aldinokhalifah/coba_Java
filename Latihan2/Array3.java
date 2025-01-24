public class Array3 {
    public static void main(String[] args) {
        int[] angka = {1, 2, 3, 4, 5, 6};
        
        int[] angka2 = new int[angka.length]; // Buat array baru dengan panjang yang sama
        System.arraycopy(angka, 0, angka2, 0, angka.length); // Salin elemen menggunakan System.arraycopy

        // loop terbalik
        for(int i = angka2.length - 1; i >= 0; i--) {
            System.out.println(angka2[i]);
        }
        

    }
}
