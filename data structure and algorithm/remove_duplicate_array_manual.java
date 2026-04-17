
public class remove_duplicate_array_manual {

    public void remove_duplicate_manual(int[] arr) {
        if (arr.length == 0) return;

        // uniqueIndex menjaga posisi di mana angka unik terakhir diletakkan
        int uniqueIndex = 0; 

        for (int i = 1; i < arr.length; i++) {
            // Jika angka sekarang beda dengan angka di posisi uniqueIndex
            if (arr[i] != arr[uniqueIndex]) {
                uniqueIndex++; // Geser gawang ke depan
                arr[uniqueIndex] = arr[i]; // Masukkan angka unik baru ke gawang
            }
        }

        // Cetak hanya sampai indeks unik terakhir
        System.out.print("Hasil Unik: ");
        for (int k = 0; k <= uniqueIndex; k++) {
            System.out.print(arr[k] + (k < uniqueIndex ? ", " : ""));
        }
    }

    public static void main(String[] args) {
        int[] angka = {1, 1, 2, 2, 3, 4, 4, 5};

        new remove_duplicate_array_manual().remove_duplicate_manual(angka);
    }
}
