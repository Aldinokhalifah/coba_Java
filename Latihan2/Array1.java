public class Array1 {
    public static void main(String[] args) {
        int[] angka = {1, 2, 3, 4, 5};

        String[] nama = new String[3];
        nama[0] = "Budi";
        nama[1] = "Andi";
        nama[2] = "Joko";

        //  for loop
        for(int x = 0; x < angka.length; x++) {
            System.out.println(angka[x]);
        }

        //  for each
        for( String i: nama) {
            System.out.println(i);
        }
    }
}