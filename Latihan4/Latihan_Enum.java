public class Latihan_Enum{
    public static void main(String[] args) {
        enum Hari{
            SENIN, SELASA, RABU, KAMIS, JUMAT, SABTU, MINGGU
        };

        System.out.println("Hari Ini Adalah Hari: " + Hari.SELASA);
    }
}