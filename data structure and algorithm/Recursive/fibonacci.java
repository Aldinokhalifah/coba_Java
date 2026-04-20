package Recursive;

public class fibonacci {
    
    public int fibonacciRecursive(int n) {
        switch (n) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
        }
    }

    public static void main(String[] args) {
        fibonacci fibonacci = new fibonacci();
        System.out.println(fibonacci.fibonacciRecursive(6));
    }
}
