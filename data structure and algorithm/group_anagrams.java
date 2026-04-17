import java.util.*;

public class group_anagrams {
    
    public void group_anagrams(String[] arr) {
        // Gunakan List<String> sebagai value agar bisa menampung banyak kata
        HashMap<String, List<String>> res = new HashMap<>();

        for(String word : arr) {
            char[] chars = word.toCharArray();
            java.util.Arrays.sort(chars);
            String sorted = new String(chars);
            
            // Jika key belum ada, buat list baru
            if (!res.containsKey(sorted)) {
                res.put(sorted, new ArrayList<>());
            }
            // Tambahkan kata ke dalam list yang sesuai dengan key-nya
            res.get(sorted).add(word);
        }
        
        // Cetak nilainya saja (kumpulan grupnya)
        System.out.println(res.values());
    }

    public static void main(String[] args) {
        String[] words = {"eat", "tea", "tan", "ate", "nat", "bat"};
        new group_anagrams().group_anagrams(words);
    }
}