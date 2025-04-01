public class LatihanArray1 {
    public static void main(String[] args) {
        int [] angka = new int[5];
        angka[0] = 10;
        angka[1] = 45;
        angka[2] = 22;
        angka[3] = 78;
        angka[4] = 99;

        int jumlah = 0;

        System.out.println("Jumlah angka pada array adalah: ");
        for (int i: angka) {
            System.out.print(i + " ");
            jumlah +=i;
        }
        System.out.println("\nJumlah angka dalam array: " + jumlah);
    }
}
