public class Multidimensional_array1 {
    public static void main(String[] args) {
        String[][] orang = {
            {"Budi", "Wati", "Joko"},
            {"Reza", "Santi", "Ririn"},
            {"Agus", "Rian", "Rina"}
        };

        // mengambil data dari multidimensional array
        //orang[jumlah baris array][jumlah kolom array]
        System.out.println(orang[0][0]); // Budi
        System.out.println(orang[1][2]); // Ririn

        // loop multidimensional array
        for(int i = 0; i < orang.length; i++) {
            for(int j = 0; j < orang[i].length; j++) {
                System.out.println(orang[i][j]);
            }
        }

        // loop multidimensional array with  foreach loop
        for( String[] i: orang){
            for( String j: i) {
                System.out.print(j + ",");
            }
        }
    }
}
