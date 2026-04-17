public class reverse_words_string {
    
    public void reverse_words(String kalimat) {

        String[] kata = kalimat.split(" ");

        StringBuilder hasil = new StringBuilder();

        for(int i = kata.length - 1; i >= 0; i--) {
            hasil.append(kata[i]);
            hasil.append( " ");
        }
        System.out.println("Kalimat Asli: " + kalimat);
        System.out.println("Kalimat Dibalik: " + hasil.toString().trim());
    }

    public static void main(String[] args) {
        new reverse_words_string().reverse_words("Nurul Fikri Belajar Java");
    }
}
