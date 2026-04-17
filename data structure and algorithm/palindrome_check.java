public class palindrome_check {
    
    public void check_palindrome(String kalimat) {

        String dibalik = new StringBuilder(kalimat).reverse().toString().toLowerCase();

        System.out.println(kalimat.equals(dibalik));
    }

    public static void main(String[] args) {
        new palindrome_check().check_palindrome("kasur ini rusak");
    }
}