public class StackArray {
    int maxSize;   // Kapasitas maksimal
    int[] stack;   // Wadah penyimpanan
    int top;       // Penunjuk posisi teratas

    // Constructor untuk menentukan ukuran stack
    public StackArray(int size) {
        this.maxSize = size;
        this.stack = new int[maxSize];
        this.top = -1; // -1 berarti stack kosong
    }

    // Push: Menambah data ke atas
    public void push(int data) {
        if (top == maxSize - 1) {
            System.out.println("Stack Overflow! (Penuh)");
        } else {
            top++; // Geser top ke index selanjutnya
            stack[top] = data; // Masukkan data
            System.out.println(data + " masuk ke stack.");
        }
    }

    // Pop: Mengambil data dari atas
    public int pop() {
        if (top == -1) {
            System.out.println("Stack Underflow! (Kosong)");
            return -1;
        } else {
            int value = stack[top]; // Ambil datanya
            top--; // Turunkan penunjuk top
            return value;
        }
    }

    // Peek: Intip data paling atas tanpa menghapusnya
    public int peek() {
        if (top == -1) return -1;
        return stack[top];
    }

    public static void main(String[] args) {
        StackArray sa = new StackArray(3); // Kita coba kapasitas kecil dulu

        sa.push(10);
        sa.push(20);
        sa.push(30);
        
        // Coba push ke-4 (Harusnya Overflow)
        sa.push(40);

        System.out.println("Data teratas saat ini (Peek): " + sa.peek());

        System.out.println("Data yang dikeluarkan (Pop): " + sa.pop());
        
        System.out.println("Data teratas setelah pop: " + sa.peek());
    }
}

