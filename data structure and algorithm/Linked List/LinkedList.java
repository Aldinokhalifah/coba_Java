public class LinkedList {
    Node head; // Pintu masuk utama kereta

    // Method untuk menambah data di akhir
    public void add(int data) {
        Node newNode = new Node(data);

        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            // Cari gerbong terakhir (yang next-nya masih null)
            while (temp.next != null) {
                temp = temp.next;
            }
            // Sambungkan gerbong baru di ujung
            temp.next = newNode;
        }
    }

    public void delete(int key) {
        // 1. Jika list kosong
        if (head == null) return;

        // 2. Jika yang dihapus adalah HEAD
        if (head.data == key) {
            head = head.next;
            return;
        }

        // 3. Cari node sebelumnya (temp) dari node yang mau dihapus
        Node temp = head;
        while (temp.next != null && temp.next.data != key) {
            temp = temp.next;
        }

        // 4. Jika ketemu, "lompatin" node tersebut
        if (temp.next != null) {
            temp.next = temp.next.next;
        }
    }

    public void reverse() {
        Node prev = null;
        Node current = head;
        Node next = null;

        while (current != null) {
            next = current.next;  // 1. Simpan sisa list agar tidak hilang
            current.next = prev;  // 2. BALIKKAN arah pointer (inti dari reverse)
            
            // 3. Geser kedua pointer (prev dan current) maju satu langkah
            prev = current;
            current = next;
        }
        // 4. Update head ke node terakhir yang kita proses
        head = prev;
    }

    public int findMiddle() {
        if (head == null) return -1;

        Node slow = head;
        Node fast = head;

        // Selama fast belum sampai ujung dan gerbong setelah fast juga bukan null
        while (fast != null && fast.next != null) {
            // Tulis logika perpindahan slow dan fast di sini
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow.data;
    }

    // Method untuk cetak semua isi
    public void display() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " -> ");
            temp = temp.next;
        }
        System.out.println("null");
    }

    public static void main(String[] args) {
        LinkedList list = new LinkedList();
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);

        list.display(); // 10 -> 20 -> 30 -> 40 -> null
        System.out.println("Nilai tengah: " + list.findMiddle());
        list.delete(20);
        list.display(); // Harusnya: 10 -> 30 -> 40 -> null
        list.delete(10);
        list.display(); // Harusnya: 30 -> 40 -> null
        list.reverse();
        list.display(); // Harusnya: 40 -> 30 -> null
    }
}
