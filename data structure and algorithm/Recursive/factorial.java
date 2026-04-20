package Recursive;

public class factorial {
    
    public int factorialRecursive(int n) {
        if( n <= 1) {
            return 1;
        } else {
            return n * factorialRecursive(n - 1);
        }
    }

    public static void main(String[] args) {
        factorial factorial = new factorial();
        System.out.println(factorial.factorialRecursive(5));
    }
}

