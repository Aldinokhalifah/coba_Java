public class reverse_string {
    public static void main(String[] args) {
        String input =  "Hello";
        String reversed = "";
        
        for(int  i = 0; i < input.length(); i ++) {
            reversed = input.charAt(i) + reversed;
        }
        System.out.println("Reversed String: " + reversed);

        // cara lain
        String input2 = "Hello";
        String reversed2 = new StringBuilder(input2).reverse().toString();
        System.out.println("Reversed String 2: " + reversed2);


    }
}