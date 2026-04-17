
public class min_max_finder_manual {
    
    public void min_max(int[] arr) {
        int max = arr[0];
        int min = arr[0];

        for(int i = 1; i < arr.length; i++) {
            if(arr[i] > max) {
                max = arr[i];
            } else if(arr[i] < min) {
                min = arr[i];
            }
        }

        System.out.println("Terbesar: " + max + " | Terkecil: " + min);
    }

    public static void main(String[] args) {
        int[] angka = {1,52,152,37,854,21,7,87,21,6,8,41,4,6,8,4};

        new min_max_finder_manual().min_max(angka);
    }
}
