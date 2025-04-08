import java.util.ArrayList;

public class SistemNilaiMahasiswa {
    static class Mahasiswa {
        int nim;
        String nama;
        ArrayList<Integer> nilai = new ArrayList<>();

        public Mahasiswa(int nim, String nama) {
            this.nim = nim;
            this.nama = nama;
        }

        public void GetNim() {
            System.out.println(nim);
        }

        public void GetNama() {
            System.out.println(nama);
        }

        public void TambahNilai(int nilaiBaru) {
            nilai.add(nilaiBaru);
        }

        public double hitungRataRata() {
            int total = 0;
            for (int n : nilai) {
                total += n;
            }
            return !nilai.isEmpty() ? (double) total / nilai.size() : 0;
        }

        public String GetGrade() {
            double rataRata = hitungRataRata();
            if (rataRata >= 85) {
                return "A";
            } else if (rataRata >= 70) {
                return "B";
            } else if (rataRata >= 55) {
                return "C";
            } else if (rataRata >= 40) {
                return "D";
            } else {
                return "E";
            }
        }

        public void info() {
            System.out.println("Nim: " + nim);
            System.out.println("Nama: " + nama);
            System.out.println("Rata-rata: " + hitungRataRata());
            System.out.println("Grade: " + GetGrade());
        }
    }

    public static void main(String[] args) {
        Mahasiswa mhs1 = new Mahasiswa(123456, "Budi");
        mhs1.TambahNilai(80);
        mhs1.TambahNilai(90);
        mhs1.TambahNilai(70);

        Mahasiswa mhs2 = new Mahasiswa(654321, "Siti");
        mhs2.TambahNilai(60);
        mhs2.TambahNilai(75);
        mhs2.TambahNilai(85);

        System.out.println("Informasi Mahasiswa 1:");
        mhs1.info();
        System.out.println("\nInformasi Mahasiswa 2:");
        mhs2.info();
    }
}
