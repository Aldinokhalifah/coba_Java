package Hashmap;

import java.util.HashMap;

public class elemen_pertama_tidak_duplikat {
    public static void main(String[] args) {
        int[] input = {4, 5, 1, 2, 1, 2, 5};

        // Hitung frekuensi tiap elemen
        HashMap<Integer, Integer> freq = new HashMap<>();
        for (int num : input) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }

        // Cari elemen pertama yang frekuensinya 1
        Integer firstNotDuplicate = null;
        for (int num : input) {
            if (freq.get(num) == 1) {
                firstNotDuplicate = num;
                break;
            }
        }

        if (firstNotDuplicate != null) {
            System.out.println("Elemen pertama yang tidak duplikat: " + firstNotDuplicate);
        } else {
            System.out.println("Semua elemen duplikat");
        }
    }
}
