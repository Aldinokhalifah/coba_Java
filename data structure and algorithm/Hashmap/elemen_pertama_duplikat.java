package Hashmap;

import java.util.HashSet;

public class elemen_pertama_duplikat {
    public static void main(String[] args) {
        int[] input = {2, 5, 3, 5, 2, 6};

        HashSet<Integer> seen = new HashSet<>();
        Integer firstDuplicate = null;

        for (int num : input) {
            if (seen.contains(num)) {
                firstDuplicate = num;
                break;
            }
            seen.add(num);
        }

        if (firstDuplicate != null) {
            System.out.println("Elemen duplikat pertama: " + firstDuplicate);
        } else {
            System.out.println("Tidak ada duplikat");
        }
    }
}
