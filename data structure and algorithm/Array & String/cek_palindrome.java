public class cek_palindrome {
    public static void main(String[] args) {
        String input =  "madam";
        String reversed = "";
        
        for(int  i = 0; i < input.length(); i ++) {
            reversed = input.charAt(i) + reversed;
        }
        
        System.out.println(input.equals(reversed));
    }
}
