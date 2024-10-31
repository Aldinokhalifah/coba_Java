public class Praktikum01 {
    public static String nama = "Aldino Khalifah";
    public static long  nim = 110223089;


    public static void main(String[] args) {
        double a = 10;
        double b = 5;
        double y = 10;

        double c = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2) - 2 * a * b * Math.cos(Math.toRadians(y)));

        System.out.println("Nama: " + Praktikum01.nama);
        System.out.println("NIM: " + Praktikum01.nim);
        System.out.println("Hasilnya adalah: "  + String.format("%.4f", c));
    }
}
