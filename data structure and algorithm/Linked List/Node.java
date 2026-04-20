public class Node {
    int data;
    Node next;

    public Node(int data) {
        this.data = data;
        this.next = null;
    }

    public static void main(String[] args) {
        // 1. Buat 3 Node terpisah
        Node node1 = new Node(10);
        Node node2 = new Node(20);
        Node node3 = new Node(30);

        // 2. Sambungkan! (Manual Linking)
        node1.next = node2; // node1 menunjuk ke node2
        node2.next = node3; // node2 menunjuk ke node3

        Node current = node1; // Kita mulai dari awal (Head)

        // 3. Menelusuri Linked List (Traversal)
        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next; // Geser pointer ke node selanjutnya
        }
        System.out.println("null");
    }
}
