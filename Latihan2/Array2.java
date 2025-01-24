public class Array2 {
    public static void main(String[] args) {
        int[] angka = {1, 2, 3, 4, 5};
        int sum = 0;

        for(int i = 0; i < angka.length; i++) {
            sum += angka[i];
        }

        System.out.println("Jumlah: " + sum);
    }
}
