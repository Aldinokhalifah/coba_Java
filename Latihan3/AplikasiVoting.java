
import java.util.ArrayList;
import java.util.Scanner;

public class AplikasiVoting {
    static class Terpilih {
        static int nextId = 1;
        int id;
        String namaTerpilih;
        int jumlahPemilih;

        public Terpilih(int id, String namaTerpilih, int jumlahPemilih) {
            this.id = nextId++;
            this.namaTerpilih = namaTerpilih;
            this.jumlahPemilih = 0;
        }

        public int getId() {
            return id;
        }

        public String getNamaTerpilih() {
            return namaTerpilih;
        }

        public int getJumlahPemilih() {
            return jumlahPemilih;
        }

        public void info() {
            System.out.println("=== Daftar Nama Terpilih ===");
            System.out.println("No. " + this.id);
            System.out.println("Nama: " + this.namaTerpilih);
            System.out.println("Jumlah Pemilih: " + this.jumlahPemilih);
        }
    }

    static class SistemVoting {
        ArrayList<Terpilih> daftarTerpilih = new ArrayList<>();

        public void tambahTerpilih(Terpilih terpilih) {
            daftarTerpilih.add(terpilih);
            System.out.println("Nama "+ terpilih.getNamaTerpilih() + " Telah Ditambahkan!");
        }

        public void lihatTerpilih() {
            if(daftarTerpilih.isEmpty()) {
                System.out.println("Belum ada Daftar Terpilih!");
            } else {
                for (Terpilih pilih : daftarTerpilih) {
                    pilih.info();
                    System.out.println();
                }
            }
        }

        public void votingTerpilih(int id) {
            boolean ditemukan = false;
            for (Terpilih pilih : daftarTerpilih) {
                if (pilih.getId() == id) {
                    pilih.jumlahPemilih++;
                    System.out.println("Voting untuk " + pilih.getNamaTerpilih() + " berhasil!");
                    ditemukan = true;
                    break;
                }
            }
            if (!ditemukan) {
                System.out.println("ID tidak ditemukan!");
            }
        }

        public void hapusTerpilih(int id) {
            boolean ditemukan = false;
            for (int i = 0; i < daftarTerpilih.size(); i++) {
                if(daftarTerpilih.get(i).getId() == id) {
                    daftarTerpilih.remove(i);
                    System.out.println("Nama " + daftarTerpilih.get(i).getNamaTerpilih() + " Berhasil Dihapus1");
                    ditemukan = true;
                    break;
                } 
            }

            if(!ditemukan) {
                System.out.println("Nama Terpilih Tidak Valid!");
            }
        }
    }

    public static void main(String[] args) {
        SistemVoting sistemVoting = new SistemVoting();
        Scanner scanner = new Scanner(System.in);
        int pilihan;

        do { 
            System.out.println("\n=== Aplikasi Voting ===");
            System.out.println("1. Tambah Nama Terpllih");
            System.out.println("2. Lihat Nama Terpllih");
            System.out.println("3. Hapus Nama Terpllih");
            System.out.println("4. Voting Nama Terpllih");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Masukkan angka (1-5): ");
                scanner.next(); // Clear invalid input
            }
            pilihan = scanner.nextInt();

            switch (pilihan) {
                case 1:
                    scanner.nextLine(); 
                    System.out.print("Masukkan Nama Terpilih: ");
                    String nama = scanner.nextLine();
                    sistemVoting.tambahTerpilih(new Terpilih(1, nama, 0));
                    break;
                case 2:
                    scanner.nextLine(); 
                    sistemVoting.lihatTerpilih();
                    break;
                case 3:
                    scanner.nextLine();
                    sistemVoting.lihatTerpilih();
                    System.out.print("Masukkan ID Yang Ingin Dihapus: ");
                    int pilihNo = scanner.nextInt();

                    while(pilihNo > sistemVoting.daftarTerpilih.size()) {
                        System.out.println("ID Yang Anda Masukkan Tidak Valid!");
                        System.out.print("Masukkan ID Yang Ingin Dihapus: ");
                        int pilihNoLagi = scanner.nextInt();

                        if(pilihNoLagi <= sistemVoting.daftarTerpilih.size()) {
                            sistemVoting.hapusTerpilih(pilihNoLagi);
                            break;
                        }
                    }
                    
                    if(pilihNo <= sistemVoting.daftarTerpilih.size()) {
                        sistemVoting.hapusTerpilih(pilihNo);
                        break;
                    }
                case 4:
                    sistemVoting.lihatTerpilih();
                    System.out.print("Masukkan ID Yang Ingin Divote: ");
                    int pilihVote = scanner.nextInt();

                    while(pilihVote > sistemVoting.daftarTerpilih.size()) {
                        System.out.println("ID Yang Anda Masukkan Tidak Valid!");
                        System.out.print("Masukkan ID Yang Ingin Divote: ");
                        int pilihVoteLagi = scanner.nextInt();

                        if(pilihVoteLagi <= sistemVoting.daftarTerpilih.size()) {
                            sistemVoting.votingTerpilih(pilihVoteLagi);
                            break;
                        }
                    }
                    
                    if(pilihVote <= sistemVoting.daftarTerpilih.size()) {
                        sistemVoting.votingTerpilih(pilihVote);
                        break;
                    }
                default:
                    System.out.println("Terimakasih Telah Melakukan Vote");
            }
        } while (pilihan != 5);

        scanner.close();
    }
}