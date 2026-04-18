import java.util.HashMap;
import java.util.Map;

public class two_sum_map {
    
    public int[] solveTwoSum(int[] nums, int target) {
        // 1. Inisialisasi HashMap
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            // 2. Hitung complement
            int complement = target - nums[i];

            if(map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            } else {
                map.put(nums[i], i);
            }
            
        }
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        two_sum_map tsm = new two_sum_map();
        int[] hasil = tsm.solveTwoSum(new int[]{3,2, 4}, 6);
        System.out.println("Indeks: [" + hasil[0] + ", " + hasil[1] + "]");
    }
}
