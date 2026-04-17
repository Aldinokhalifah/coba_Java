
import java.util.Arrays;

public class min_max_finder {
    
    public void min_max(int[] arr) {
        Arrays.sort(arr);

        int max = arr[arr.length -1];
        int min = arr[0];

        System.out.println("Terbesar: " + max + " | Terkecil: " + min);
    }

    public static void main(String[] args) {
        int[] angka = {1,52,152,37,854,21,7,87,21,6,8,41,4,6,8,4};

        new min_max_finder().min_max(angka);
    }
}
