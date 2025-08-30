package Hashmap;
import java.util.HashMap;

public class hitung_frekuensi_karakter {
    public static void main(String[] args) {
        String input = "banana";

        HashMap<Character, Integer> frekuensi_karakter = new HashMap<>();

        // Iterasi melalui setiap karakter dalam string
        for (int i = 0; i < input.length(); i++) {
            char karakter = input.charAt(i);
            
            // Jika karakter sudah ada di HashMap, tambahkan 1 ke count
            if (frekuensi_karakter.containsKey(karakter)) {
                frekuensi_karakter.put(karakter, frekuensi_karakter.get(karakter) + 1);
            } else {
                // Jika karakter belum ada, tambahkan dengan count 1
                frekuensi_karakter.put(karakter, 1);
            }
        }

        // Cetak frekuensi setiap karakter
        System.out.println("Frekuensi karakter dalam string \"" + input + "\":");
        for (Character karakter : frekuensi_karakter.keySet()) {
            System.out.println("'" + karakter + "': " + frekuensi_karakter.get(karakter));
        }
    }
}
