import java.util.HashSet;

public class remove_duplicate_array {
    
    public void remove_duplicate(int[] arr) {
        HashSet<Integer> seen = new HashSet<>();
        

        for(int s : arr) {
            seen.add(s);
        }
        System.out.println(seen);
    }

    public static void main(String[] args) {
        int[] angka = {1, 1, 2, 2, 3, 4, 4, 5};

        new remove_duplicate_array().remove_duplicate(angka);
    }
}
