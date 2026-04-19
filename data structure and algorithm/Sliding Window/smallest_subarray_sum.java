public class smallest_subarray_sum {

    public double  findSmallestSubarray(int[] arr, int s) {
        int window_start = 0;
        int window_sum = 0;
        double minLength = Double.POSITIVE_INFINITY;

        for(int window_end = 0; window_end < arr.length; window_end++) {
            window_sum += arr[window_end];

            while (window_sum >= s) { 
                int current_window_size = window_end - window_start + 1;
    
                minLength = Math.min(minLength, current_window_size);
    
                window_sum -= arr[window_start];
                window_start += 1;
            }
        }
        return minLength == Double.POSITIVE_INFINITY ? 0 : minLength;
    }

    public static void main(String[] args) {
        smallest_subarray_sum ssm = new smallest_subarray_sum();
        System.out.println(ssm.findSmallestSubarray(new int[]{2, 1, 5, 2, 8}, 7));
    }
}