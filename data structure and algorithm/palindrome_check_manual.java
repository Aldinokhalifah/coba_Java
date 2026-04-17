public class palindrome_check_manual {
    
    public boolean check_palindrome(String kalimat) {

        String clean = kalimat.toLowerCase();

        int left = 0;
        int right = clean.length() - 1;

        while(left < right) {
            if(clean.charAt(left) != clean.charAt(right)) {
                return false;
            }

            left++;
            right--;
        }
        return true;
    }

    public static void main(String[] args) {
        palindrome_check_manual pm =new palindrome_check_manual();
        System.out.println(pm.check_palindrome("kasur ini rusak"));
    }
}
