public class two_sum {
    
    public int[] findTwoSum(int[] number, int target) {
        int left = 0;
        int right = number.length - 1;

        while (left < right) { 
            int sum = number[left] + number[right];

            if(sum == target) {
                return new int[]{left + 1, right + 1};
            } else if(sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        two_sum ts = new two_sum();
        int[] hasil = ts.findTwoSum(new int[]{2, 7, 11, 15}, 9);
        System.out.println("Indeks: [" + hasil[0] + ", " + hasil[1] + "]");
    }
}
