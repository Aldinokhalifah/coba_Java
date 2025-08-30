import java.util.Arrays;

public class min_max_Array {
    public static void main(String[] args) {
        int[] arr = {2, 7, 1, 9, 5};
        Arrays.sort(arr);

        int max = arr[arr.length - 1];
        int min = arr[0];

        System.out.println("Max: " +  max);
        System.out.println("Min: " +  min);
    }
}
