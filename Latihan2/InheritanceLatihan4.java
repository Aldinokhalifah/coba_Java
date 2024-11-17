public class InheritanceLatihan4 {
    static class Mahasiswa {
        protected String nama;
        protected int NIM;

        public Mahasiswa(String nama, int NIM) {
            this.nama = nama;
            this.NIM = NIM;
        }

        public void infoMahasiswa() {
            System.out.println("Nama: " + nama);
            System.out.println("NIM: " + NIM);
        }
    }

    static class MahasiswaReguler extends Mahasiswa {
        private int semester;
        private int biayaSemester;

        public MahasiswaReguler(String nama, int NIM, int semester, int biayaSemester) {
            super(nama, NIM);
            this.semester = semester;
            this.biayaSemester = biayaSemester;
        }

        @Override
        public void infoMahasiswa() {
            super.infoMahasiswa();
            System.out.println("Semester: " + semester);
            System.out.println("Biaya per Semester: " + biayaSemester);
        }

        public void biayaKuliah() {
            int totalBiaya = semester * biayaSemester;
            System.out.println("Total biaya kuliah " + nama + " adalah: Rp" + totalBiaya);
        }
    }

    static class MahasiswaTransfer extends Mahasiswa {
        private String asalUniv;

        public MahasiswaTransfer(String nama, int NIM, String asalUniv) {
            super(nama, NIM);
            this.asalUniv = asalUniv;
        }

        @Override
        public void infoMahasiswa() {
            super.infoMahasiswa();
            System.out.println("Asal Universitas: " + asalUniv);
        }
    }

    public static void main(String[] args) {
        MahasiswaReguler reguler1 = new MahasiswaReguler("Joko", 92012, 6, 20000);
        reguler1.infoMahasiswa();
        reguler1.biayaKuliah();
        System.out.println();

        MahasiswaReguler reguler2 = new MahasiswaReguler("Handoyo", 289128, 8, 22000);
        reguler2.infoMahasiswa();
        reguler2.biayaKuliah();
        System.out.println();

        MahasiswaTransfer transfer = new MahasiswaTransfer("Ijat", 12812, "Uniga");
        transfer.infoMahasiswa();
    }
}
